package org.pollub.auth.util;

import org.pollub.auth.util.strategy.DefaultPasswordStrategy;
import org.pollub.auth.util.strategy.PasswordGenerationStrategy;
import org.pollub.common.adapter.IPasswordGenerator;
import org.springframework.stereotype.Component;

import java.util.Random;

//L6 Strategy Design Pattern - PasswordGenerator as Context for different password generation strategies
/**
 * Utility class for generating random passwords.
 * Refactored to act as a Context for the PasswordGenerationStrategy (Strategy Pattern).
 * Implements the IPasswordGenerator adapter interface from common library.
 */
@Component
public class PasswordGenerator implements IPasswordGenerator {

    private PasswordGenerationStrategy strategy;

    /**
     * Initializes Context with a default strong password strategy.
     */
    public PasswordGenerator() {
        this.strategy = new DefaultPasswordStrategy();
    }

    /**
     * Allows dynamic change of the password generation algorithm.
     *
     * @param strategy The new strategy to use
     */
    public void setStrategy(PasswordGenerationStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Generates a random password using the current strategy.
     */
    @Override
    public String generate() {
        return strategy.generate();
    }
}
