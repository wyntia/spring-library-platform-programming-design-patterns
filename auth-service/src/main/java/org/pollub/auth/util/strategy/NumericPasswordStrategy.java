package org.pollub.auth.util.strategy;

import java.util.Random;

//L3 Strategy Design Pattern - NumericPasswordStrategy implementation
/**
 * Numeric strategy generating a simple numeric PIN-like password.
 */
public class NumericPasswordStrategy implements PasswordGenerationStrategy {

    private static final String DIGITS = "0123456789";
    private static final Random random = new Random();
    private static final int PASSWORD_LENGTH = 6; // Default to 6-digit PIN

    @Override
    public String generate() {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }

        return password.toString();
    }
}
