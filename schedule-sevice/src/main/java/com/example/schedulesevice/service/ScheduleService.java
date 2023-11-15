package com.example.schedulesevice.service;

import com.example.schedulesevice.dto.request.ScheduleDto;
import com.phucvukimcore.base.Result;

public interface ScheduleService {
    Result scheduleShow(ScheduleDto dto);
}
