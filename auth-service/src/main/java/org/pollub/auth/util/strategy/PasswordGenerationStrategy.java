package org.pollub.auth.util.strategy;

//start L6 Strategy Design Pattern - Strategy interface for password generation
/**
 * Strategy interface for password generation.
 */
public interface PasswordGenerationStrategy {

    /**
     * Generates a password according to the specific implementation strategy.
     *
     * @return A generated password string.
     */
    String generate();
}