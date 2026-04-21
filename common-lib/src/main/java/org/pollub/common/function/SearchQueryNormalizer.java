package org.pollub.common.function;

//Lab7 : Interfejsy funkcyjne i lambda — definicja 1/3
/**
 * Normalizes raw search query text before passing it to persistence (e.g. trim, blank handling).
 */
@FunctionalInterface
public interface SearchQueryNormalizer {

    /**
     * @param rawQuery user-provided query; may be null or blank
     * @return value to use in repository call, or null if search should ignore this field
     */
    String normalize(String rawQuery);
}
