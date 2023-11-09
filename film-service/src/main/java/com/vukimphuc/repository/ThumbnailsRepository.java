package com.vukimphuc.repository;

import com.vukimphuc.entity.Thumnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailsRepository extends JpaRepository<Thumnail, Integer> {
}
