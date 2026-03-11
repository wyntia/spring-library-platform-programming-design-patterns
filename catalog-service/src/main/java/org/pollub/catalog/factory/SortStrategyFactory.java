package org.pollub.catalog.factory;

import org.springframework.data.domain.Sort;

//Lab1 - Simple Factory 2 Start
public class SortStrategyFactory {

    private SortStrategyFactory() {
    }

    /**
     * Creates a Sort object based on the given sort parameter string.
     *
     * @param sortParam the sort parameter (e.g., "TITLE_ASC", "AUTHOR_DESC")
     * @return the corresponding Sort object
     */
    public static Sort createSort(String sortParam) {
        if (sortParam == null) {
            return Sort.by("id").ascending();
        }

        return switch (sortParam) {
            case "TITLE_ASC" -> Sort.by("title").ascending();
            case "TITLE_DESC" -> Sort.by("title").descending();
            case "AUTHOR_ASC" -> Sort.by("author").ascending();
            case "AUTHOR_DESC" -> Sort.by("author").descending();
            default -> Sort.by("id").ascending();
        };
    }
}
// End Simple 2 Factory
