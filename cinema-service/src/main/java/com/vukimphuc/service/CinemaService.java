package com.vukimphuc.service;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.CinemaDto;

public interface CinemaService {
    Result createCinema(CinemaDto dto);

    Result findCinemaById(Integer id);

    Result findAll();
}
