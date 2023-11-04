package com.vukimphuc.dto.response;

import com.vukimphuc.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private String dateOfBirth;
    private String address;
    private String email;
    private String phone;
    private boolean isBlocked;
    private Role role;
    private String token;
    private String refreshToken;
}
