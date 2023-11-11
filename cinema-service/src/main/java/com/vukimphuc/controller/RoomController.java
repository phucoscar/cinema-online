package com.vukimphuc.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.RoomDto;
import com.vukimphuc.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/create-room")
    public Result createRoom(@RequestHeader(value = "Auth") String token,
                             @RequestBody RoomDto roomDto) {
        return roomService.createRoom(roomDto);
    }

    @PostMapping("/{id}")
    public Result findRoomById(@PathVariable Integer id) {
        return roomService.findRoom(id);
    }

    @PostMapping("/room-in-cinema")
    public Result findRoomByCinema(@RequestParam Integer cinemaId) {
        return roomService.findAllRoomInCinema(cinemaId);
    }
}
