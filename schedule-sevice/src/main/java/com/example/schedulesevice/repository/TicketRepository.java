package com.example.schedulesevice.repository;

import com.example.schedulesevice.entity.Booking;
import com.example.schedulesevice.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByScheduleId(Integer id);

    Integer countByScheduleId(Integer id);

    @Query(value = "SELECT t.booking FROM Ticket t WHERE t.schedule.id = ?1 GROUP BY t.booking", nativeQuery = true)
    List<Booking> findBookingsByScheduleId(Integer scheduleId);

}
