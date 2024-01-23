package com.example.schedulesevice.service.impl;

import com.example.schedulesevice.dto.request.BookingDto;
import com.example.schedulesevice.dto.response.BookingResponse;
import com.example.schedulesevice.dto.response.HistoryBookingResponse;
import com.example.schedulesevice.dto.response.RatingDtoRepsonse;
import com.example.schedulesevice.dto.response.Seat;
import com.example.schedulesevice.entity.*;
import com.example.schedulesevice.repository.BookingRepository;
import com.example.schedulesevice.repository.TicketRepository;
import com.example.schedulesevice.repository.UserRepository;
import com.example.schedulesevice.service.BookingService;
import com.example.schedulesevice.utils.JwtUtils;
import com.phucvukimcore.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestTemplate restTemplate;

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

    @Override
    public Result getListHistoryBooking(String token) {
        String username = jwtUtils.getUsernameFromJwtToken(token);
        Optional<User> op = userRepository.findByUsername(username);
        if (!op.isPresent()) {
            return Result.fail("Người dùng không tồn tại");
        }
        User user = op.get();
        List<Booking> bookings = bookingRepository.findAllByUserId(user.getId());
        Collections.sort(bookings, Comparator.comparing(Booking::getBookingTime, Comparator.reverseOrder()));
        List<HistoryBookingResponse> responses = new ArrayList<>();
        for (Booking booking: bookings) {
            HistoryBookingResponse response = new HistoryBookingResponse();
            response.setTimeBooking(booking.getBookingTime());
            response.setId(booking.getId());
            List<Ticket> tickets = booking.getTickets();
            response.setTickets(tickets);
            Film film = tickets.get(0).getSchedule().getFilm();
            response.setFilm(film);
            response.setCinema(tickets.get(0).getSchedule().getRoom().getCinema());

            Long totalPaid = tickets.stream().mapToLong(Ticket::getPrice).sum();
            response.setTotalPrice(totalPaid);

            String apiUrl = "http://localhost:8087/api/v1/rating/check-rating";
            RatingDtoRepsonse ratingDtoRepsonse = restTemplate.getForObject(apiUrl + "?userId=" + user.getId() + "&filmId=" + film.getId(), RatingDtoRepsonse.class);
            response.setRated(ratingDtoRepsonse != null);
            response.setRatingDtoRepsonse(ratingDtoRepsonse);
            responses.add(response);
        }
        return Result.success("Success", responses);
    }
}
