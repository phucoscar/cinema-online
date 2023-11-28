package com.vukimphuc.repository;

import com.vukimphuc.entity.Film;
import com.vukimphuc.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findAllByFilm(Film film);
}
