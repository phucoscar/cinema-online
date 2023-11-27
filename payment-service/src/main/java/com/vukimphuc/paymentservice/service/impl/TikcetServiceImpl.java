package com.vukimphuc.paymentservice.service.impl;

import com.phucvukimcore.enums.TicketClassEnums;
import com.vukimphuc.paymentservice.dto.request.Seat;
import com.vukimphuc.paymentservice.entity.Booking;
import com.vukimphuc.paymentservice.entity.Schedule;
import com.vukimphuc.paymentservice.entity.Ticket;
import com.vukimphuc.paymentservice.repository.ScheduleRepository;
import com.vukimphuc.paymentservice.repository.TicketRepository;
import com.vukimphuc.paymentservice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TikcetServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public List<Ticket> createTicket(Booking booking, int scheduleId, List<Seat> seats) {
        Optional<Schedule> op = scheduleRepository.findById(scheduleId);
        if (!op.isPresent())
            return null;
        List<Ticket> tickets = new ArrayList<>();
        for (Seat seat: seats) {
           Ticket ticket = new Ticket();
           if (seat.getRow() <= 4) {
               ticket.setPrice(50000L);
               ticket.setTicketClass(TicketClassEnums.REGULAR.getCode());
           }
           else {
               ticket.setPrice(80000L);
               ticket.setTicketClass(TicketClassEnums.VIP.getCode());
           }
           ticket.setSchedule(op.get());
           ticket.setBooking(booking);
           ticket.setSeatNumberHorizontal(seat.getRow());
           ticket.setSeatNumberVertical(seat.getColumn());
           ticket = ticketRepository.save(ticket);
           tickets.add(ticket);
       }
       return tickets;
    }
}
