package com.example.schedulesevice.controller;

import com.example.schedulesevice.dto.request.BookingDto;
import com.example.schedulesevice.service.BookingService;
import com.phucvukimcore.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    @PostMapping("/check-available-seats")
    public Result checkSeatsBooking(@RequestBody BookingDto bookingDto) {
        return bookingService.checkAvailableSeats(bookingDto);
    }

    @GetMapping("/history")
    public Result getListHistoryBooking(@RequestHeader(value = "Auth") String token) {
        return bookingService.getListHistoryBooking(token);
    }

}
