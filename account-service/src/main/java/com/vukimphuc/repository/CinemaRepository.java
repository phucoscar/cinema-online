package com.vukimphuc.repository;

import com.vukimphuc.entity.Cinema;
import com.vukimphuc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
    Optional<Cinema> findByAdmin(User user);
}
