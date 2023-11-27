package com.vukimphuc.paymentservice.service.impl;

import com.phucvukimcore.base.Result;
import com.vukimphuc.paymentservice.dto.request.Seat;
import com.vukimphuc.paymentservice.dto.request.TransactionDTO;
import com.vukimphuc.paymentservice.entity.Booking;
import com.vukimphuc.paymentservice.entity.Ticket;
import com.vukimphuc.paymentservice.entity.User;
import com.vukimphuc.paymentservice.repository.BookingRepository;
import com.vukimphuc.paymentservice.repository.UserRepository;
import com.vukimphuc.paymentservice.service.BookingService;
import com.vukimphuc.paymentservice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketService ticketService;

    @Transactional
    @Override
    public Result createBooking(TransactionDTO dto) {

        String errorTransactionResponse = errorTransactionResponseCode(dto.getResponseCode());
        if (errorTransactionResponse != null)
            return Result.fail(errorTransactionResponse);

        Booking booking = new Booking();
        Optional<User> op = userRepository.findById(dto.getUserId());
        if (!op.isPresent())
            return Result.fail("Không tìm thấy người dùng");
        List<User> users = booking.getUser();
        if (users == null || users.isEmpty())
            users = new ArrayList<>();
        users.add(op.get());
        booking.setUser(users);
        booking.setBookingTime(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        List<Ticket> tickets = ticketService.createTicket(booking, dto.getScheduleId(), dto.getSeats());
        booking.setTickets(tickets);
        return Result.success("Success", bookingRepository.save(booking));
    }

    private String errorTransactionResponseCode(String responseCode) {
        String error = null;
        switch (responseCode) {
            case "07":
                error = "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).";
                break;
            case "09":
                error = "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.";
                break;
            case "10":
                error = "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
                break;
            case "11":
                error = "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
                break;
            case "12":
                error = "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
                break;
            case "13":
                error = "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.";
                break;
            case "24":
                error = "Giao dịch không thành công do: Khách hàng hủy giao dịch";
                break;
            case "51":
                error = "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
                break;
            case "65":
                error = "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
                break;
            case "75":
                error = "Ngân hàng thanh toán đang bảo trì.";
                break;
            case "79":
                error = "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch";
                break;
        }
        return error;
    }
}
