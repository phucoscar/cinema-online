package com.vukimphuc.repository;

import com.vukimphuc.entity.Cinema;
import com.vukimphuc.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByCinema(Cinema cinema);
}
