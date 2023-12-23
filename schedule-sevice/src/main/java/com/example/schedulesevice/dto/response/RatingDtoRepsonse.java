package com.example.schedulesevice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDtoRepsonse {
    private Integer star;
    private String comment;
}
