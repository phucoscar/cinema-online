package com.vukimphuc.service;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.FilmDto;

public interface FilmService {

    Result getAllFilms();

    Result createFilm(FilmDto filmDto);

    Result editFilm(FilmDto filmDto);

    Result getFilmById(Integer id);

    Result deleteFilmById(Integer id);
}
