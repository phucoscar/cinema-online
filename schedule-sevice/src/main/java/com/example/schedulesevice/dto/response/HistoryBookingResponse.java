package com.example.schedulesevice.dto.response;

import com.example.schedulesevice.entity.Cinema;
import com.example.schedulesevice.entity.Film;
import com.example.schedulesevice.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryBookingResponse {
    private Integer id;
    private Cinema cinema;
    private Film film;
    private LocalDateTime timeBooking;
    private List<Ticket> tickets;
    private Long totalPrice;
    private boolean isRated;
    private RatingDtoRepsonse ratingDtoRepsonse;
}
