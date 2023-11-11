package com.vukimphuc.service.impl;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.RoomDto;
import com.vukimphuc.entity.Cinema;
import com.vukimphuc.entity.Room;
import com.vukimphuc.repository.CinemaRepository;
import com.vukimphuc.repository.RoomRepository;
import com.vukimphuc.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Result createRoom(RoomDto dto) {
        Room room = new Room();
        room.setName(dto.getName());
        room.setHorizontalSeats(dto.getHorizontalSeats());
        room.setVerticalSeats(dto.getVerticalSeats());
        Optional<Cinema> op = cinemaRepository.findById(dto.getCinemaId());
        if (!op.isPresent())
            return Result.fail("Cinema not found");
        Cinema cinema = op.get();
        room.setCinema(cinema);
        roomRepository.save(room);
        List<Room> rooms = cinema.getRooms();
        if (rooms == null)
            rooms = new ArrayList<>();
        rooms.add(room);
        cinema.setRooms(rooms);
        cinemaRepository.save(cinema);
        return Result.success("Success", room);
    }

    @Override
    public Result findRoom(Integer id) {
        Optional<Room> op = roomRepository.findById(id);
        if (op.isPresent())
            return Result.success("Success", op.get());
        return Result.fail("Room not found");
    }

    @Override
    public Result findAllRoomInCinema(Integer cinemaId) {
        Optional<Cinema> op = cinemaRepository.findById(cinemaId);
        if (!op.isPresent())
            return Result.fail("Cinema not found");
        Cinema cinema = op.get();
        List<Room> rooms = roomRepository.findByCinema(cinema);
        return Result.success("Success", rooms);
    }
}
