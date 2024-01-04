package com.example.schedulesevice.repository;

import com.example.schedulesevice.entity.Cinema;
import com.example.schedulesevice.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query(value = "SELECT * FROM Schedule WHERE start_time >= ?1 AND start_time < ?2 AND room_id = ?3", nativeQuery = true)
    List<Schedule> findByDateRangeAndRoomId(LocalDateTime startDate, LocalDateTime endDate, Integer roomId);


    List<Schedule> findByRoom_CinemaAndEndTimeBefore(Cinema cinema, LocalDateTime time);

    Page<Schedule> findByRoom_CinemaAndEndTimeBeforeOrderByStartTimeDesc(Cinema cinema, LocalDateTime time, Pageable pageable);

    List<Schedule> findByRoom_CinemaAndEndTimeAfter(Cinema cinema, LocalDateTime time);

    Page<Schedule> findByRoom_CinemaAndEndTimeAfterOrderByStartTimeDesc(Cinema cinema, LocalDateTime time, Pageable pageable);

    List<Schedule> findAllByRoom_CinemaAndStartTimeAfterAndStartTimeBefore(Cinema cinema, LocalDateTime start, LocalDateTime end);
}
