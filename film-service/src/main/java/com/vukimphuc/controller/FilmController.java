package com.vukimphuc.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.FilmDto;
import com.vukimphuc.service.FilmService;
import com.vukimphuc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin/film")
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
}