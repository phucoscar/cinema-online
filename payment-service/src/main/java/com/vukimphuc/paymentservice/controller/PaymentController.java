package com.vukimphuc.paymentservice.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.paymentservice.dto.request.OrderRequestDTO;
import com.vukimphuc.paymentservice.dto.request.TransactionDTO;
import com.vukimphuc.paymentservice.service.BookingService;
import com.vukimphuc.paymentservice.service.VNPayService;
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

    @GetMapping("/create-payment")
    public Result createPayment(HttpServletRequest request, @RequestBody OrderRequestDTO orderRequestDTO) throws UnsupportedEncodingException {

        Map<String, Object> result = this.vnPayService.createOrder(request, orderRequestDTO);

        return Result.success("Success", result);
    }

    /*
    * TODO: xử lý bất đồng bộ: Khi 2 người cùng đặt cùng vị trí chỗ ngồi
    * */
    @PostMapping("/result-info")
    public Result transactionInfor(@RequestBody TransactionDTO transactionDTO){
        return bookingService.createBooking(transactionDTO);
    }
}
