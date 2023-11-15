package com.example.schedulesevice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private int id;
    private String startTime;
    private String endTime;
    private Integer filmId;
    private Integer roomId;
}
