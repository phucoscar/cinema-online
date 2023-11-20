package com.example.schedulesevice.service.impl;

import com.example.schedulesevice.dto.request.ScheduleDto;
import com.example.schedulesevice.dto.response.ShowResponse;
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
import java.util.*;

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

    @Override
    public Result findAllCurrentScheduleInCinema(Integer cinemaId) {
       // Room room = roomRepository.findByCinemaId(cinemaId);
        /*
        * Lấy những schedule có ngày start từ now -> 6 ngày kế tiếp
        * */
        return null;
    }

    @Override
    public Result findAllScheduleInCinemaByDay(Integer cinemaId, String date) {
        List<Room> rooms = roomRepository.findByCinemaId(cinemaId);
        LocalDateTime dateStart = convertToLocalDateTimeFromString(date);
        Map<Integer, Integer> map = new HashMap<>();
        List<ShowResponse> result = new ArrayList<>();
        int index = 0;
        for (Room room: rooms) {
            List<Schedule> schedules = scheduleRepository.findByTimeStartAndRoomId(dateStart, room.getId());
            for (Schedule schedule: schedules) {
                Film film = schedule.getFilm();
                if (map.containsKey(film.getId())) {
                    int position = map.get(film.getId());
                    ShowResponse response = result.get(position);
                    List<Schedule> sche = response.getSchedules();
                    sche.add(schedule);
                    response.setSchedules(sche);
                    result.set(position, response);
                }
                else {
                    ShowResponse response = new ShowResponse();
                    response.setFilm(film);
                    response.setSchedules(Arrays.asList(schedule));
                    result.add(response);
                    map.put(film.getId(), index);
                    index++;
                }
            }
        }
        return Result.success("Success", result);
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
