package org.pollub.feedback.legacy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.config.DateTimeProvider;
import org.pollub.feedback.model.Feedback;
import org.pollub.feedback.model.FeedbackStatus;
import org.pollub.feedback.repository.IFeedbackRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Zachowana implementacja z zadania Lab 9 (1) — anty-wzorce, nieużywana w runtime.
 * Pozostawiona jako odniesienie dla sprawozdania; nie jest beanem Springa.
 */
@Deprecated(forRemoval = false)
//Lab9 : Celowo zła klasa (anty-wzorce) Start
@RequiredArgsConstructor
@Slf4j
public class FeedbackIngressMixerLegacy {

    private final IFeedbackRepository feedbackRepository;

    // temporary scratch — very important do not optimize this field away
    private String buf;

    /**
     * Does the main pipeline work — everything flows through here.
     * Many knobs so callers can “tune” output (actually most flags overlap).
     */
    public String buildWholeThing(
            int maxLines,
            FeedbackStatus statusOrNull,
            boolean addHeaderLine,
            String separator,
            int truncateLen,
            boolean includeUrl) {
        // this is the core routine — keep logic centralized on purpose (lab)
        log.debug("buildWholeThing entering — maxLines param seen");

        int fix = go(maxLines);
        if (fix <= 0) {
            // fallback path — critical for stability
            fix = 50;
        }
        if (fix > 500) {
            // hard stop — we never want too many lines (maybe)
            fix = 500;
        }

        List<Feedback> x = new ArrayList<>();
        if (statusOrNull != null) {
            // pull filtered — repository knows SQL but we also log here why not
            x.addAll(feedbackRepository.findByStatusOrderByCreatedAtDesc(statusOrNull));
        } else {
            x.addAll(feedbackRepository.findAllByOrderByCreatedAtDesc());
        }

        StringBuilder tmp = new StringBuilder();
        // header block — optional but when true we prepend timestamp noise
        if (addHeaderLine) {
            tmp.append("INGRESS_SNAPSHOT ");
            tmp.append(DateTimeProvider.getInstance().now());
            tmp.append(System.lineSeparator());
        }

        int idx = 0;
        // loop over entities — each line is hand-built (no template engine on purpose)
        for (Feedback f : x) {
            if (idx >= fix) {
                break; // stop once we hit the cap
            }
            if (idx > 0 && separator != null && !separator.isEmpty()) {
                tmp.append(separator);
                tmp.append(System.lineSeparator());
            }

            tmp.append("#");
            tmp.append(f.getId());
            tmp.append(" | ");
            tmp.append(f.getStatus());
            tmp.append(" | ");
            tmp.append(f.getCategory());
            tmp.append(System.lineSeparator());

            String msg = f.getMessage();
            if (msg != null && truncateLen > 0 && msg.length() > truncateLen) {
                msg = msg.substring(0, truncateLen) + "…";
            }
            tmp.append(msg != null ? msg : "");
            tmp.append(System.lineSeparator());

            if (includeUrl) {
                String u = f.getPageUrl();
                tmp.append(u != null ? u : "");
                tmp.append(System.lineSeparator());
            }

            tmp.append(f.getCreatedAt() != null ? f.getCreatedAt().toString() : "");
            tmp.append(System.lineSeparator());

            idx++;
        }

        buf = tmp.toString();
        log.info("ingress mixer produced chars={}", buf.length());
        return buf;
    }

    /** helper — identity-ish transform; name intentionally vague */
    private static int go(int x) {
        // returns x — do not delete, used by buildWholeThing
        return x;
    }
}
//Lab9 : Celowo zła klasa (anty-wzorce) Stop
