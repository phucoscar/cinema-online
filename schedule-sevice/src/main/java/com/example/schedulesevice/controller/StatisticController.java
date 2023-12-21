package com.example.schedulesevice.controller;

import com.example.schedulesevice.service.ScheduleService;
import com.phucvukimcore.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/statistic")
public class StatisticController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/view-revenue-statistic")
    public Result getRevenueStatistic(@RequestParam Integer cinemaId,
                                      @RequestParam String startDate,
                                      @RequestParam(required = false) String endDate) {
        return scheduleService.getRevenueStatistic(cinemaId, startDate, endDate);
    }
}
