package com.example.schedulesevice.service.impl;

import com.example.schedulesevice.dto.request.ScheduleDto;
import com.example.schedulesevice.entity.Film;
import com.example.schedulesevice.entity.Room;
import com.example.schedulesevice.entity.Schedule;
import com.example.schedulesevice.repository.FilmRepository;
import com.example.schedulesevice.repository.RoomRepository;
import com.example.schedulesevice.repository.ScheduleRepository;
import com.example.schedulesevice.service.ScheduleService;
import com.phucvukimcore.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public Result scheduleShow(ScheduleDto dto) {
        LocalDateTime startTime = convertToLocalDateTimeFromString(dto.getStartTime());
        String error = checkTime(startTime);
        if (error != null)
            return Result.fail(error);
        Optional<Film> op1 = filmRepository.findById(dto.getFilmId());
        if (!op1.isPresent())
            return Result.fail("Film not found");
        Film film = op1.get();;
        Integer durations = film.getDuration(); // in seconds
        LocalDateTime endTime = startTime.plusMinutes(durations);
        Optional<Room> op2 = roomRepository.findById(dto.getRoomId());
        if (!op2.isPresent())
            return Result.fail("Room not found");
        Room room = op2.get();
        Schedule schedule = new Schedule();
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setFilm(film);
        schedule.setRoom(room);
        scheduleRepository.save(schedule);
        List<Schedule> schedules = room.getSchedules();
        if (schedules == null) {
            schedules = new ArrayList<>();
        }
        schedules.add(schedule);
        room.setSchedules(schedules);
        roomRepository.save(room);
        return Result.success("Success", schedule);
    }

    private LocalDateTime convertToLocalDateTimeFromString(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        return dateTime;
    }

    private String checkTime(LocalDateTime dateTime) {
        // so sanh voi thoi gian hien tai
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.isBefore(now))
            return "Invalid start time";
        // neu thoi gian start truoc 6 tieng
        if (dateTime.isBefore(now.plusHours(6)))
            return "Please schedule 6 hours before the film starts";
        return null;
    }
}
