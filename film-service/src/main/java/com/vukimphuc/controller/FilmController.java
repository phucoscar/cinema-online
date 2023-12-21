package com.vukimphuc.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.EditFilmDto;
import com.vukimphuc.dto.request.FilmDto;
import com.vukimphuc.service.FilmService;
import com.vukimphuc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/film")
public class FilmController {
    @Autowired
    private UserService userService;

    @Autowired
    private FilmService filmService;

    @PostMapping(value = "/create-film")
    public Result createFilm(@ModelAttribute FilmDto filmDto) {
        Result result = filmService.createFilm(filmDto);
        return result;
    }

    @PostMapping("/{id}")
    public Result getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping
    public Result getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/update-score")
    public Result updateFilmRatingScore(@RequestParam Integer filmId) {
        return filmService.updateAvgScore(filmId);
    }

    @PostMapping("/edit")
    public Result editFilm(@ModelAttribute EditFilmDto filmDto) {
        return filmService.editFilm(filmDto);
    }

    @PostMapping("/delete/{id}")
    public Result deleteFilmById(@PathVariable Integer id) {
        return filmService.deleteFilmById(id);
    }
}
