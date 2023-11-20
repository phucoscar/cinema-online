package com.example.schedulesevice.dto.response;

import com.example.schedulesevice.entity.Film;
import com.example.schedulesevice.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowDetailsResponse {
    private Film film;
    private String startTime;
    private String endTime;
    private Room room;
    private List<Seat> seatsBooked;
}
