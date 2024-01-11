package com.example.schedulesevice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListScheduleResponseByPage {
    private List<ScheduleResponse> scheduleResponseList;
    private PageInfo pageInfo;
}
