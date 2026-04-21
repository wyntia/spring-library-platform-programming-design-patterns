package org.pollub.rental.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pollub.common.function.RentalReminderBatchReporter;
import org.pollub.common.mediator.Mediator;
import org.pollub.rental.mediator.request.SendOverdueReminderNotification;
import org.pollub.rental.model.RentalHistory;
import org.pollub.rental.model.RentalStatus;
import org.pollub.rental.repository.IRentalHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for sending rental reminder emails to users
 * whose rentals expire in 3 days.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RentalReminderService {

    private static final int DAYS_BEFORE_DUE = 3;

    // Lab5 Mediator Start
    private final IRentalHistoryRepository rentalHistoryRepository;
    private final Mediator mediator;

    /**
     * Find all rentals expiring in 3 days and send reminder notifications via
     * Mediator.
     */
    public void sendReminders() {
        log.info("Starting rental reminder job...");

        LocalDate targetDate = LocalDate.now().plusDays(DAYS_BEFORE_DUE);
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

        List<RentalHistory> expiringRentals = rentalHistoryRepository
                .findByDueDateBetweenAndStatus(startOfDay, endOfDay, RentalStatus.RENTED);

        log.info("Found {} rentals expiring in {} days", expiringRentals.size(), DAYS_BEFORE_DUE);

        // Lab7 : Programowanie funkcyjne — strumienie na kolekcjach 2/3 (rental) Start

        Map<Boolean, List<RentalHistory>> partitioned = expiringRentals.stream()
                .filter(rental -> rental.getUserId() != null)
                .collect(Collectors.partitioningBy(rental -> {
                    try {
                        mediator.send(new SendOverdueReminderNotification(rental.getId()));
                        return true;
                    } catch (Exception e) {
                        log.error("Error processing reminder for rental {}: {}", rental.getId(), e.getMessage());
                        return false;
                    }
                }));

        int successCount = partitioned.get(true).size();
        int failCount = partitioned.get(false).size();

        List<Long> notifiedUserIds = partitioned.get(true).stream()
                .map(RentalHistory::getUserId)
                .sorted()
                .collect(Collectors.toList());
        log.debug("Notified user IDs (sorted): {}", notifiedUserIds);
        // Lab7 : Programowanie funkcyjne — strumienie na kolekcjach 2/3 (rental) End

        // Lab7 : Interfejsy funkcyjne i lambda — użycie 2/3 (rental) Start
        reportBatch(
                successCount,
                failCount,
                (ok, fail) -> log.info("Rental reminder job completed. Sent: {}, Failed: {}", ok, fail));
        // Lab7 : Interfejsy funkcyjne i lambda — użycie 2/3 (rental) End
    }

    private void reportBatch(int successCount, int failureCount, RentalReminderBatchReporter reporter) {
        reporter.report(successCount, failureCount);
    }
    // Lab5 Mediator End
}
