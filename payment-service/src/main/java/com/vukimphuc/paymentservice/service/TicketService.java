package com.vukimphuc.paymentservice.service;

import com.vukimphuc.paymentservice.dto.request.Seat;
import com.vukimphuc.paymentservice.entity.Booking;
import com.vukimphuc.paymentservice.entity.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> createTicket(Booking booking, int scheduleId, List<Seat> seats);
}
