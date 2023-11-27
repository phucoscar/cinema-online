package com.vukimphuc.paymentservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Integer scheduleId;
    private Integer userId;
    private List<Seat> seats;
    private String responseCode;
}
