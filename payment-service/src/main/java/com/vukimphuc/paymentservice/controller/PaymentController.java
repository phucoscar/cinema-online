package com.vukimphuc.paymentservice.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.paymentservice.dto.request.OrderRequestDTO;
import com.vukimphuc.paymentservice.dto.request.TransactionDTO;
import com.vukimphuc.paymentservice.entity.User;
import com.vukimphuc.paymentservice.repository.UserRepository;
import com.vukimphuc.paymentservice.service.BookingService;
import com.vukimphuc.paymentservice.service.VNPayService;
import com.vukimphuc.paymentservice.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/create-payment")
    public Result createPayment(HttpServletRequest request,
                                @RequestHeader(value = "Auth") String token,
                                @RequestParam Long amount) throws UnsupportedEncodingException {
        String username = jwtUtils.getUsernameFromJwtToken(token);
        Optional<User> op = userRepository.findByUsername(username);
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setAmount(amount);
        orderRequestDTO.setOrderInfor("Thanh toan ve xem phim cua: " + op.get().getEmail());
        Map<String, Object> result = this.vnPayService.createOrder(request, orderRequestDTO);

        return Result.success("Success", result);
    }

    /*
    * TODO: xử lý bất đồng bộ: Khi 2 người cùng đặt cùng vị trí chỗ ngồi
    *  sử dụng queue
    * */
    @PostMapping("/result-info")
    public Result transactionInfor(@RequestBody TransactionDTO transactionDTO){
        return bookingService.createBooking(transactionDTO);
    }
}
