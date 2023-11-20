package com.example.schedulesevice.dto.response;

import com.example.schedulesevice.entity.Film;
import com.example.schedulesevice.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowResponse {
    private Film film;
    private List<Schedule> schedules = new ArrayList<>();
}
