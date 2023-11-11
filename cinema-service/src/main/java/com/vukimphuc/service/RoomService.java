package com.vukimphuc.service;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.RoomDto;

public interface RoomService {
    Result createRoom(RoomDto dto);

    Result findRoom(Integer id);

    Result findAllRoomInCinema(Integer cinemaId);
}
