package org.pollub.feedback.service.moderator;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

//Lab5 : DIP 3 Start
@Slf4j
@Component
public class BannedWordsModerator extends AbstractFeedbackModerator {
    
    private final List<String> bannedWords = List.of("nienawidzę", "głupi", "zły");

    @Override
    protected boolean runSpecificModeration(String content) {
        String lowerContent = content.toLowerCase();
        for (String word : bannedWords) {
            if (lowerContent.contains(word)) {
                log.warn("Komentarz odrzucony - znaleziono zakazane słowo: {}", word);
                return false;
            }
        }
        return true;
    }
}
//Lab5 : DIP 3 End
