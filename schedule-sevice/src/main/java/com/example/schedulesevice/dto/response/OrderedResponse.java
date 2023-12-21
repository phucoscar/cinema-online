package com.example.schedulesevice.dto.response;

import com.example.schedulesevice.entity.Booking;
import com.example.schedulesevice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedResponse {
    private String fullname;
    private String email;
    private LocalDateTime bookedTime;
    private Integer numberOfTicket;
    private Integer vips;
    private Integer regulars;
    private String seats;
    private Long totalPaid;
}
