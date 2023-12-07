package com.example.schedulesevice.service;

import com.example.schedulesevice.dto.request.ScheduleDto;
import com.phucvukimcore.base.Result;

public interface ScheduleService {
    Result scheduleShow(ScheduleDto dto);

    Result editSchedule(ScheduleDto dto);

    Result deleteSchedule(Integer scheduleId);

    Result findAllCurrentScheduleInCinema(Integer cinemaId);

    Result findAllHistoryScheduleInCinema(Integer cinemaId);

    Result findAllScheduleInCinemaByDay(Integer cinemaId, String date);
}
