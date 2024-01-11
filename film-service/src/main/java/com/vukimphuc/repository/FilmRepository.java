package com.vukimphuc.repository;

import com.vukimphuc.entity.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {
    @Query(value = "SELECT * FROM Film e WHERE e.name LIKE %?1%", nativeQuery = true)
    List<Film> searchByName(String name);
}
