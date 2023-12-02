package com.vukimphuc.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.service.UserService;
import com.vukimphuc.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
