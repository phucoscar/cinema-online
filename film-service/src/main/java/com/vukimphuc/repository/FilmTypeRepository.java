package com.vukimphuc.repository;

import com.vukimphuc.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmTypeRepository extends JpaRepository<Type, Integer> {
}
