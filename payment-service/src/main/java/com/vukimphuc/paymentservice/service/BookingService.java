package com.vukimphuc.paymentservice.service;

import com.phucvukimcore.base.Result;
import com.vukimphuc.paymentservice.dto.request.TransactionDTO;

public interface BookingService {
    Result createBooking(TransactionDTO dto);
}
