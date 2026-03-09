package org.pollub.common.dto;

import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String title;
    private boolean available;

    public boolean isAvailable() {
        return available;
    }
}
