package org.pollub.user.service.utils.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

//Lab5 : DIP 1 Start
@Slf4j
@Component
public class ConsoleActivityLogger extends AbstractActivityLogger {

    @Override
    protected void writeLog(String formattedMessage) {
        log.info("{}", formattedMessage);
    }
}
//Lab5 : DIP 1 End
