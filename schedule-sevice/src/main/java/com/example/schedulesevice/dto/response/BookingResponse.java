package com.example.schedulesevice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Integer scheduleId;
    private List<Seat> seats;
    private List<Integer> prices;
}
