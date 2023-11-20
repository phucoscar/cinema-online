package com.example.schedulesevice.dto.request;

import com.example.schedulesevice.dto.response.Seat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Integer scheduleId;
    private List<Seat> seats;
}
