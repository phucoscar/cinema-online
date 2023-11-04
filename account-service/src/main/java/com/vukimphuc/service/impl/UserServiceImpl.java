package com.vukimphuc.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucvukimcore.base.Result;
import com.phucvukimcore.enums.RoleEnums;
import com.vukimphuc.dto.request.RegisterDto;
import com.vukimphuc.entity.Role;
import com.vukimphuc.entity.User;
import com.vukimphuc.dto.response.LoginResponse;
import com.vukimphuc.repository.RoleRepository;
import com.vukimphuc.repository.UserRepository;
import com.vukimphuc.service.UserService;
import com.vukimphuc.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public User findByUsername(String username) {
        Optional<User> op = userRepository.findByUsername(username);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    @Override
    public LoginResponse convertToLoginResp(User user, String token) {
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPassword(user.getPassword());
        response.setFullname(user.getFullname());
        response.setDateOfBirth(user.getDateOfBirth());
        response.setAddress(user.getAddress());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setBlocked(user.isBlocked());
        response.setRole(user.getRole());
        response.setToken(token);
        response.setRefreshToken(null);
        return response;
    }

    @Override
    public Result register(RegisterDto dto, boolean isAdmin) {
        boolean isValid = validateUsername(dto.getUsername());
        if (!isValid)
            return Result.fail("Username is already use");
        isValid = validateEmail(dto.getEmail());
        if (!isValid)
            return Result.fail("Email is already use");
        User user = mapper.convertValue(dto, User.class);
        Role role = isAdmin? roleRepository.findById(RoleEnums.ADMIN.getCode()).get() : roleRepository.findById(RoleEnums.CUSTOMER.getCode()).get();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        return new Result(200, "Success", user);
    }

    @Override
    public Result blockUser(Integer id) {
        Optional<User> op = userRepository.findById(id);
        if (!op.isPresent())
            return Result.fail("User not found");
        else {
            User user = op.get();
            user.setBlocked(true);
            userRepository.save(user);
            return new Result(200, "Success", user);
        }
    }

    public boolean validateUsername(String username) {
        Optional<User> op = userRepository.findByUsername(username);
        if (op.isPresent())
            return false;
        return true;
    }

    public boolean validateEmail(String email) {
        Optional<User> op = userRepository.findByEmail(email);
        if (op.isPresent())
            return false;
        return true;
    }

    @Override
    public Result changePassword(String token, String oldPassword, String newPassword) {
        String username = jwtUtils.getUsernameFromJwtToken(token);
        Optional<User> op = userRepository.findByUsername(username);
        if (op.isPresent()) {
            User user = op.get();
            if (!passwordEncoder.matches(oldPassword, user.getPassword()))
                return Result.fail("Old password is not correct!");
            else {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return Result.success();
            }
        }
        return Result.fail("User not found!");
    }

}
