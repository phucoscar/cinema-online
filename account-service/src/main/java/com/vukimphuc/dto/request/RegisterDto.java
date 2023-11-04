package com.vukimphuc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDto {
    private String username;
    private String password;
    private String fullname;
    private String dateOfBirth;
    private String address;
    private String email;
    private String phone;
}
