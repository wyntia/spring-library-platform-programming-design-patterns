package org.pollub.feedback.service.moderator;

import lombok.extern.slf4j.Slf4j;

//Lab5 : DIP 3 Start
@Slf4j
public abstract class AbstractFeedbackModerator implements IFeedbackModerator {

    //Lab6 : Magiczne liczby Start
    private static final int MIN_CONTENT_LENGTH_CHARS = 5;
    //Lab6 : Magiczne liczby Stop

    @Override
    public boolean isAppropriate(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        if (content.length() < MIN_CONTENT_LENGTH_CHARS) {
            log.warn("Komentarz odrzucony - zbyt krótki");
            return false;
        }
        
        return runSpecificModeration(content);
    }
    
    protected abstract boolean runSpecificModeration(String content);
}
//Lab5 : DIP 3 End
