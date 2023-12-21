package com.vukimphuc.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.ProfileDto;
import com.vukimphuc.dto.request.RegisterDto;
import com.vukimphuc.service.UserService;
import com.vukimphuc.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/customers")
    public Result findAllCustomerAccount() {
        return userService.findAllCustomerAccount();
    }

    @GetMapping("/admins")
    public Result findAllAdminAccount() {
        return userService.findAllAdminAccount();
    }

    @GetMapping("/available-admins")
    public Result findAllAdminAccountWithoutCinema() {
        return userService.findAllAdminAccountWithoutCinema();
    }

    @PostMapping("/edit-profile")
    public Result editProfile(@RequestBody ProfileDto dto) {
        return userService.editProfile(dto);
    }
}
