package org.pollub.user.service.utils.exporter;

import lombok.extern.slf4j.Slf4j;
import org.pollub.user.model.User;
import org.pollub.user.repository.IUserRepository;

//Lab5 : DIP 2 Start
@Slf4j
public abstract class AbstractProfileExporter implements IProfileExporter {
    
    private final IUserRepository userRepository;

    protected AbstractProfileExporter(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public byte[] exportProfile(Long userId) {
        log.info("Rozpoczęcie procesu eksportu danych dla użytkownika: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));
        
        return serializeProfile(user);
    }

    protected abstract byte[] serializeProfile(User user);
}
//Lab5 : DIP 2 End
