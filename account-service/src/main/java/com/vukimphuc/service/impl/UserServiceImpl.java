package com.vukimphuc.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucvukimcore.base.Result;
import com.phucvukimcore.enums.RoleEnums;
import com.vukimphuc.dto.request.ProfileDto;
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
import com.vukimphuc.util.UsernameGenerator;
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
    public User findByEmail(String email) {
        Optional<User> op = userRepository.findByUsername(email);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    @Override
    public Result editProfile(ProfileDto dto) {
        Optional<User> op = userRepository.findById(dto.getId());
        if (!op.isPresent()) {
            return Result.fail("Không tồn tại người dùng");
        }

//        String error = errorUsername(dto.getUsername());
//        if (error != null)
//            return Result.fail(error);

        User user = op.get();
        user.setUsername(dto.getUsername());
        user.setFullname(dto.getFullname());
        user.setAddress(dto.getAddress());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setPhone(dto.getPhone());
        LoginResponse response = convertToLoginResp(user, "");
        return Result.success("Success", response);
    }

    @Override
    public LoginResponse convertToLoginResp(User user, String token) {
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPassword(user.getPassword() != null? user.getPassword() : null);
        response.setFullname(user.getFullname() != null? user.getFullname() : null);
        response.setDateOfBirth(user.getDateOfBirth() != null? user.getDateOfBirth() : null);
        response.setAddress(user.getAddress() != null? user.getAddress() : null);
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone() != null? user.getPhone() : null);
        response.setBlocked(user.isBlocked());
        response.setRole(user.getRole());
        response.setToken(token);
        response.setRefreshToken(null);
        return response;
    }

    @Override
    public Result register(RegisterDto dto, boolean isAdmin) {
        String error = errorUsername(dto.getUsername());
        if (error != null)
            return Result.fail(error);
        boolean isValid = validateEmail(dto.getEmail());
        if (!isValid)
            return Result.fail("Email đã được sử dụng");
        User user = mapper.convertValue(dto, User.class);
        Role role = isAdmin? roleRepository.findById(RoleEnums.ADMIN.getCode()).get() : roleRepository.findById(RoleEnums.CUSTOMER.getCode()).get();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        emailSenderService.sendMailRegister(dto.getEmail(), dto.getUsername(), dto.getPassword());
        return new Result(200, "Success", user);
    }

    @Override
    public Result loginByGoogle(RegisterDto dto) {
        String username = UsernameGenerator.generateUsernameFromEmail(dto.getEmail());
        while (userRepository.existsByUsername(username)) {
            username = UsernameGenerator.generateUsernameFromEmail(dto.getEmail());
        }
        User user = mapper.convertValue(dto, User.class);
        user.setUsername(username);
        Role role = roleRepository.findById(RoleEnums.CUSTOMER.getCode()).get();
        user.setRole(role);
        user = userRepository.save(user);
        String token = jwtUtils.generateTokenFromUsername(username);
        LoginResponse response = convertToLoginResp(user, token);
        //emailSenderService.sendMailRegister(dto.getEmail(), dto.getUsername(), dto.getPassword());
        return new Result(200, "Success", response);
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
        response.setPassword(user.getPassword() != null? user.getPassword() : null);
        response.setFullname(user.getFullname() != null? user.getFullname() : null);
        response.setDateOfBirth(user.getDateOfBirth() != null? user.getDateOfBirth() : null);
        response.setAddress(user.getAddress() != null? user.getAddress() : null);
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone() != null? user.getPhone() : null);
        response.setBlocked(user.isBlocked());
        response.setRole(user.getRole());
        response.setToken(token);
        response.setRefreshToken(null);
        return Result.success("Success", response);
    }

    @Override
    public Result changeUserStatus(Integer id) {
        Optional<User> op = userRepository.findById(id);
        if (!op.isPresent())
            return Result.fail("Người dùng không tồn tại!");
        else {
            User user = op.get();
            boolean status = user.isBlocked();
            user.setBlocked(!status);
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

    @Override
    public Result forgotPassword(String email) {
        Optional<User> op = userRepository.findByEmail(email);

        if (!op.isPresent()) {
            return Result.fail("Email không tồn tại trong hệ thống");
        }
        return Result.success("Success", "");
    }

    public String errorUsername(String username) {
        if (isContainsSpecialCharacter(username)) {
            return "Tên đăng nhập không được chứa ký tự đặc biệt";
        }

        if (!Character.isLetter(username.charAt(0))) {
            return "Tên đăng nhập không được bắt đầu bằng chữ số";
        }

        Optional<User> op = userRepository.findByUsername(username);
        if (op.isPresent())
            return "Tên đăng nhập đã được sử dụng";

        return null;
    }

    private boolean isContainsSpecialCharacter(String data) {
        String pattern = ".*[^a-zA-Z0-9].*";
        return data.matches(pattern);
    }

    public boolean validateEmail(String email) {
        Optional<User> op = userRepository.findByEmail(email);
        if (op.isPresent())
            return false;
        return true;
    }

    @Override
    public Result changePassword(Integer userId, String oldPassword, String newPassword) {
        Optional<User> op = userRepository.findById(userId);
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
