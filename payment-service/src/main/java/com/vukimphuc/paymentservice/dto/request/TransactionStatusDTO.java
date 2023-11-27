package com.vukimphuc.paymentservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatusDTO implements Serializable {
    private String status;
    private String message;
    private String data;
}
