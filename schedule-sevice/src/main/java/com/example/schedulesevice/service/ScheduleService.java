package com.example.schedulesevice.service;

import com.example.schedulesevice.dto.request.ScheduleDto;
import com.phucvukimcore.base.Result;

public interface ScheduleService {

    Result scheduleShow(ScheduleDto dto);

    Result getScheduleById(Integer scheduleId);

    Result editSchedule(ScheduleDto dto);

    Result deleteSchedule(Integer scheduleId);

    Result findAllCurrentScheduleInCinema(Integer cinemaId);

    Result findAllCurrentScheduleInCinemaByPage(Integer cinemaId, Integer page, Integer perPage);

    Result findAllHistoryScheduleInCinema(Integer cinemaId);

    Result findAllHistoryScheduleInCinemaByPage(Integer cinemaId, Integer page, Integer perPage);

    Result findAllScheduleInCinemaByDay(Integer cinemaId, String date);

    Result findAllOrdered(Integer scheduleId);

    Result getRevenueStatistic(Integer cinemaId, String startDate, String endDate);

    Result getAllBookedSeats(Integer scheduleId);
}
