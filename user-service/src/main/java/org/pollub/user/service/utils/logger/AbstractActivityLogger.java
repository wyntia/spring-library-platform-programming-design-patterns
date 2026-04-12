package org.pollub.user.service.utils.logger;

import java.time.LocalDateTime;

//Lab5 : DIP 1 Start
public abstract class AbstractActivityLogger implements IUserActivityLogger {

    @Override
    public void logActivity(Long userId, String action) {
        String timestamp = LocalDateTime.now().toString();
        String formattedMessage = String.format("[%s] UŻYTKOWNIK [%d]: %s", timestamp, userId, action);
        writeLog(formattedMessage);
    }
    
    protected abstract void writeLog(String formattedMessage);
}
//Lab5 : DIP 1 End
