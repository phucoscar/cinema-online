package com.vukimphuc.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.RegisterDto;
import com.vukimphuc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/sp-admin")
public class SPAdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/block")
    public Result blockUser(@RequestParam(name = "id") Integer id) {
        return userService.blockUser(id);
    }

    @PostMapping("/un-block")
    public Result unBlockUser(@RequestParam(name = "id") Integer id) {
        return userService.blockUser(id);
    }

    @PostMapping("/create-admin-account")
    public Result createAccount(@RequestBody RegisterDto registerDto) {
        Result result = userService.register(registerDto, true);
        return result ;
    }
}
