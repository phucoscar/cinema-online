package com.vukimphuc.service;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.EditFilmDto;
import com.vukimphuc.dto.request.FilmDto;

public interface FilmService {

    Result getAllFilms();

    // phan trang
    Result getFilms(int page, int perPage);

    Result getAllFilmsOrderByCreatedDate();

    Result createFilm(FilmDto filmDto);

    Result editFilm(EditFilmDto filmDto);

    Result getFilmById(Integer id);

    Result deleteFilmById(Integer id);

    Result searchFilmByName(String name);

    Result updateAvgScore(Integer filmId);
}
