package org.pollub.user.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String username;
    private String email;
    private String name;
    private String surname;
    private String phone;
    private Long favouriteBranchId;
}
