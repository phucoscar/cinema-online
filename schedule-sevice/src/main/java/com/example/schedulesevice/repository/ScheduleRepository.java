package com.example.schedulesevice.repository;

import com.example.schedulesevice.entity.Cinema;
import com.example.schedulesevice.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query(value = "SELECT * FROM Schedule WHERE DATE (start_time) = DATE (?1) AND room_id = ?2", nativeQuery = true)
    List<Schedule> findByTimeStartAndRoomId(LocalDateTime timeStart, Integer roomId);

    List<Schedule> findByRoom_CinemaAndAndEndTimeBefore(Cinema cinema, LocalDateTime time);

    List<Schedule> findByRoom_CinemaAndEndTimeAfter(Cinema cinema, LocalDateTime time);
}
