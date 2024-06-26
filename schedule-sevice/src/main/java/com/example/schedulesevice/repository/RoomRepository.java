package com.example.schedulesevice.repository;

import com.example.schedulesevice.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByCinemaId(Integer cinemaId);
}
