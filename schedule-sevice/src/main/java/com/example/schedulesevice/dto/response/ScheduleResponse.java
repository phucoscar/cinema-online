package com.example.schedulesevice.dto.response;

import com.example.schedulesevice.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private Schedule schedule;
    private Integer totalSeats;
    private Integer availables;
}
