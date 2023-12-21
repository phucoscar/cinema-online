package com.vukimphuc.service;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.ProfileDto;
import com.vukimphuc.dto.request.RegisterDto;
import com.vukimphuc.entity.User;
import com.vukimphuc.dto.response.LoginResponse;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    Result editProfile(ProfileDto dto);

    LoginResponse convertToLoginResp(User user, String token);

    Result loginByGoogle(RegisterDto dto);

    Result register(RegisterDto dto, boolean isAdmin);

    Result verifyToken(String token);

    Result changePassword(Integer userId, String oldPassword, String newPassword);

    Result changeUserStatus(Integer id);

    Result unBlockUser(Integer id);

    Result findAllCustomerAccount();

    Result findAllAdminAccount();

    Result findAllAdminAccountWithoutCinema();
}
