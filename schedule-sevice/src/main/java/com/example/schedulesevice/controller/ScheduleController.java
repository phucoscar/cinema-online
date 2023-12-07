package com.example.schedulesevice.controller;

import com.example.schedulesevice.dto.request.ScheduleDto;
import com.example.schedulesevice.entity.Schedule;
import com.example.schedulesevice.repository.TicketRepository;
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

    @PostMapping("/schedule-by-cinema") // lay schedule theo rap
    public Result getCurrentScheduleInCinema(@RequestParam Integer cinemaId) {
        return scheduleService.findAllCurrentScheduleInCinema(cinemaId);
    }

    @PostMapping("/schedule-history-by-cinema")
    public Result getHistoryScheduleInCinema(@RequestParam Integer cinemaId) {
        return scheduleService.findAllHistoryScheduleInCinema(cinemaId);
    }

    @PostMapping("/schedule-by-film") // lay tat ca schedule theo film
    public Result getCurrentScheduleByFilm(@RequestParam Integer filmId) {
        return Result.success();
    }

    @PostMapping("/cinema-by-day")
    public Result listScheduleInCinemaByDay(@RequestParam Integer cinemaId,
                                             @RequestParam String date) { // lay tat ca film theo schedule
        return scheduleService.findAllScheduleInCinemaByDay(cinemaId, date);
    }

    @PostMapping("/details/{scheduleId}")
    public Result showScheduleDetails(@PathVariable Integer scheduleId) {
        return Result.success();
    }

    @PostMapping("/edit")
    public Result editSchedule(@RequestBody ScheduleDto scheduleDto) {
        return scheduleService.editSchedule(scheduleDto);
    }

    @PostMapping("/delete")
    public Result deleteSchedule(@RequestParam Integer scheduleId) {
        return scheduleService.deleteSchedule(scheduleId);
    }


}
