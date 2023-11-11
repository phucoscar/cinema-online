package com.vukimphuc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CinemaDto {
    private Integer id;
    private String name;
    private String address;
    private Integer adminId;
}
