package com.example.schedulesevice.dto.response;

import com.example.schedulesevice.entity.Film;
import com.example.schedulesevice.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRevenueStatistic {
    private LocalDateTime showDate;
    private Film film;
    private Room room;
    private Integer ticketsSold;
    private Long revenue;
}
