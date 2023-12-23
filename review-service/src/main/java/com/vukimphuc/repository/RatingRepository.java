package com.vukimphuc.repository;

import com.vukimphuc.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    List<Rating> findAllByFilmId(Integer filmId);

    Optional<Rating> findByFilmIdAndUserId(Integer filmId, Integer userId);
}
