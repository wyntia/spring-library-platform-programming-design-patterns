package org.pollub.user.service.utils.exporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pollub.user.model.User;
import org.pollub.user.repository.IUserRepository;
import org.springframework.stereotype.Component;

//Lab5 : DIP 2 Start
@Slf4j
@Component
public class JsonProfileExporter extends AbstractProfileExporter {
    
    private final ObjectMapper objectMapper;

    public JsonProfileExporter(IUserRepository userRepository, ObjectMapper objectMapper) {
        super(userRepository);
        this.objectMapper = objectMapper;
    }

    @Override
    protected byte[] serializeProfile(User user) {
        try {
            log.info("Serializowanie profilu użytkownika do formatu JSON");
            String json = objectMapper.writeValueAsString(user);
            return json.getBytes();
        } catch (JsonProcessingException e) {
            log.error("Błąd podczas eksportu profilu do JSON: {}", e.getMessage());
            throw new RuntimeException("Błąd eksportu JSON", e);
        }
    }
}
//Lab5 : DIP 2 End
