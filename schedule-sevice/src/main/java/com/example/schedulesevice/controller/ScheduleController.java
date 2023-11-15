package com.example.schedulesevice.controller;

import com.example.schedulesevice.dto.request.ScheduleDto;
import com.example.schedulesevice.service.ScheduleService;
import com.phucvukimcore.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/create")
    public Result sheduleShow(@RequestHeader(value = "Auth") String token,
                              @RequestBody ScheduleDto scheduleDto) {
        return scheduleService.scheduleShow(scheduleDto);
    }
}
