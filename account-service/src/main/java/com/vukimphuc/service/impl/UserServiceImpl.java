package com.vukimphuc.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucvukimcore.base.Result;
import com.phucvukimcore.enums.RoleEnums;
import com.vukimphuc.dto.request.RegisterDto;
import com.vukimphuc.dto.response.AdminAccountResponse;
import com.vukimphuc.entity.Cinema;
import com.vukimphuc.entity.Role;
import com.vukimphuc.entity.User;
import com.vukimphuc.dto.response.LoginResponse;
import com.vukimphuc.repository.CinemaRepository;
import com.vukimphuc.repository.RoleRepository;
import com.vukimphuc.repository.UserRepository;
import com.vukimphuc.service.UserService;
import com.vukimphuc.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailSenderService emailSenderService;

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
            return Result.fail("Tên đăng nhập đã đã được sử dụng");
        isValid = validateEmail(dto.getEmail());
        if (!isValid)
            return Result.fail("Email đã được sử dụng");
        User user = mapper.convertValue(dto, User.class);
        Role role = isAdmin? roleRepository.findById(RoleEnums.ADMIN.getCode()).get() : roleRepository.findById(RoleEnums.CUSTOMER.getCode()).get();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        //emailSenderService.sendMailRegister(dto.getEmail(), dto.getUsername(), dto.getPassword());
        return new Result(200, "Success", user);
    }

    @Override
    public Result verifyToken(String token) {

        String username = jwtUtils.getUsernameFromJwtToken(token);
        if (username == null)
            return Result.fail("Invalid token");

        Optional<User> op = userRepository.findByUsername(username);
        if (!op.isPresent())
            return Result.fail("Không tồn tại người dùng với username " + username);

        User user = op.get();
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
        return Result.success("Success", response);
    }

    @Override
    public Result blockUser(Integer id) {
        Optional<User> op = userRepository.findById(id);
        if (!op.isPresent())
            return Result.fail("Người dùng không tồn tại!");
        else {
            User user = op.get();
            user.setBlocked(true);
            userRepository.save(user);
            return new Result(200, "Success", user);
        }
    }

    @Override
    public Result unBlockUser(Integer id) {
        Optional<User> op = userRepository.findById(id);
        if (!op.isPresent())
            return Result.fail("Người dùng không tồn tại!");
        else {
            User user = op.get();
            user.setBlocked(false);
            userRepository.save(user);
            return new Result(200, "Success", user);
        }
    }

    @Override
    public Result findAllCustomerAccount() {
        List<User> users = userRepository.findAllByRoleId(RoleEnums.CUSTOMER.getCode());
        return Result.success("Success", users);
    }

    @Override
    public Result findAllAdminAccount() {

        List<User> users = userRepository.findAllByRoleId(RoleEnums.ADMIN.getCode());
        List<AdminAccountResponse> responses = new ArrayList<>();

        for (User user: users) {
            AdminAccountResponse response = new AdminAccountResponse();
            response.setUser(user);
            Optional<Cinema> optional = cinemaRepository.findByAdmin(user);
            optional.ifPresent(response::setCinema);
            responses.add(response);
        }

        return Result.success("Success", responses);
    }

    @Override
    public Result findAllAdminAccountWithoutCinema() {
        List<User> users = userRepository.findAllByRoleId(RoleEnums.ADMIN.getCode());
        List<User> response = new ArrayList<>();
        for (User user: users) {
            if (user.getManagedCinema() == null)
                response.add(user);
        }
        return Result.success("Success", response);
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
                return Result.fail("Mật khẩu cũ không chính xác!");
            else {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return Result.success();
            }
        }
        return Result.fail("Người dùng không tồn tại!");
    }

}
