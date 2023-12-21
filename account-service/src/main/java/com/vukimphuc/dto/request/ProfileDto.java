package com.vukimphuc.dto.request;

import lombok.Data;

@Data
public class ProfileDto {
    private int id;
    private String username;
    private String password;
    private String fullname;
    private String dateOfBirth;
    private String address;
    private String email;
    private String phone;
}
