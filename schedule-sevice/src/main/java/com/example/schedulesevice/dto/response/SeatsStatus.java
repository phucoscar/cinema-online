package com.example.schedulesevice.dto.response;

import com.example.schedulesevice.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatsStatus {
    private Schedule schedule;
    private Integer row;
    private Integer column;
    private List<String> bookedSeats;
}
