package com.vukimphuc.service;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.FilmDto;

public interface FilmService {
    Result createFilm(FilmDto filmDto);
}
