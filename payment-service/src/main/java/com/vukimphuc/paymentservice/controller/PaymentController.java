package com.vukimphuc.paymentservice.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.paymentservice.dto.OrderRequestDTO;
import com.vukimphuc.paymentservice.service.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/create-payment")
    public Result createPayment(HttpServletRequest request, @RequestBody OrderRequestDTO orderRequestDTO) throws UnsupportedEncodingException {

        Map<String, Object> result = this.vnPayService.createOrder(request, orderRequestDTO);

        return Result.success("Success", result);
    }
}
