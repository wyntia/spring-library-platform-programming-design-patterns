package org.pollub.auth.util.strategy;

import java.util.Random;

//L6 Strategy Design Pattern - Default password generation strategy
/**
 * Default strategy generating a strong password with at least 12 characters,
 * containing uppercase, lowercase, digits, and special characters.
 */
public class DefaultPasswordStrategy implements PasswordGenerationStrategy {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";
    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL;

    private static final Random random = new Random();
    private static final int PASSWORD_LENGTH = 12;

    @Override
    public String generate() {
        StringBuilder password = new StringBuilder();

        // Ensure at least one of each type
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Fill the rest randomly
        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        // Shuffle the password
        return password.toString().chars()
                .boxed()
                .map(c -> (char) c.intValue())
                .collect(StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append)
                .toString();
    }
}
