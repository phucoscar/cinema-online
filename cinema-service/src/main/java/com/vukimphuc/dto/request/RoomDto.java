package com.vukimphuc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Integer id;
    private String name;
    private Integer horizontalSeats;
    private Integer verticalSeats;
    private Integer cinemaId;
}
