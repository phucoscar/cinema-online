package com.example.schedulesevice.service.impl;

import com.example.schedulesevice.dto.request.BookingDto;
import com.example.schedulesevice.dto.response.BookingResponse;
import com.example.schedulesevice.dto.response.Seat;
import com.example.schedulesevice.entity.Ticket;
import com.example.schedulesevice.repository.TicketRepository;
import com.example.schedulesevice.service.BookingService;
import com.phucvukimcore.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Result checkAvailableSeats(BookingDto dto) {
        List<Ticket> ticketsBooked = ticketRepository.findByScheduleId(dto.getScheduleId());
        if (!ticketsBooked.isEmpty()) {
            List<Seat> seats = dto.getSeats();
            for (Ticket ticket: ticketsBooked) {
                for (Seat seat: seats) {
                    if (seat.getRow()
                            .equals(ticket.getSeatNumberHorizontal()) && seat.getColumn()
                            .equals(ticket.getSeatNumberVertical()))
                        return Result.fail("Chỗ ngồi đã được đặt");
                }
            }
        }
        BookingResponse response = new BookingResponse();
        response.setScheduleId(dto.getScheduleId());
        response.setSeats(dto.getSeats());
        List<Integer> price = new ArrayList<>();
        for (Seat seat: dto.getSeats()) {
            if (seat.getRow() <= 4) {
                price.add(50000);
            }
            else {
                price.add(80000);
            }
        }
        response.setPrices(price);
        return Result.success("Success", response);
    }
}
