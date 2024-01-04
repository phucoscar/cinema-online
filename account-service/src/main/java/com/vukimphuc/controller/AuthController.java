package com.vukimphuc.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.RegisterDto;
import com.vukimphuc.entity.User;
import com.phucvukimcore.util.JsonUtil;
import com.vukimphuc.dto.response.LoginResponse;
import com.vukimphuc.service.UserService;
import com.vukimphuc.service.impl.CustomUserDetails;
import com.vukimphuc.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public Result login(@RequestParam(name = "username") String username,
                        @RequestParam(name = "password") String password) {
        try {
            String token = null;
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username, password
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(customUserDetails);
            token = jwtCookie.getValue();
            User user = userService.findByUsername(username);
            if (user.isBlocked()) {
                return Result.fail("Tài khoản của bạn đã bị khóa, vui lòng liên hệ quản trị viên để biết thêm chi tiết.");
            }
            LoginResponse response = userService.convertToLoginResp(user, token);
            return new Result(200, "Success", response);
        } catch (Exception e) {
            return Result.fail("Tên đăng nhập hoặc mật khẩu không chính xác");
        }
    }

    @GetMapping("/logout")
    public Result logoutUser(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return Result.success();
    }

    @PostMapping("/signup")
    public Result signup(@RequestBody RegisterDto registerDto) {
        Result result = userService.register(registerDto, false);
        return result ;
    }

    @GetMapping("/verify-token")
    public Result refreshRequest(@RequestHeader(value = "Auth") String token) {
        System.out.println(token);
        return userService.verifyToken(token);
    }

    @GetMapping("/change-password")
    public Result signup(@RequestParam Integer userId,
                         @RequestParam String oldPassword,
                         @RequestParam String newPassword) {
        Result result = userService.changePassword(userId, oldPassword, newPassword);
        return result ;
    }
}
