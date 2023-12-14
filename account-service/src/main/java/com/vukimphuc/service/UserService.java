package com.vukimphuc.service;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.RegisterDto;
import com.vukimphuc.entity.User;
import com.vukimphuc.dto.response.LoginResponse;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    LoginResponse convertToLoginResp(User user, String token);

    Result loginByGoogle(RegisterDto dto);

    Result register(RegisterDto dto, boolean isAdmin);

    Result verifyToken(String token);

    Result changePassword(String token, String oldPassword, String newPassword);

    Result blockUser(Integer id);

    Result unBlockUser(Integer id);

    Result findAllCustomerAccount();

    Result findAllAdminAccount();

    Result findAllAdminAccountWithoutCinema();
}
