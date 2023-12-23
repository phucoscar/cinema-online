package com.example.schedulesevice.service;

import com.example.schedulesevice.dto.request.BookingDto;
import com.phucvukimcore.base.Result;

public interface BookingService {
    Result checkAvailableSeats(BookingDto dto);

    Result getListHistoryBooking(String token);
}
