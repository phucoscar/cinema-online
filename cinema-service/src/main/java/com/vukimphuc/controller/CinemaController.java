package com.vukimphuc.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.CinemaDto;
import com.vukimphuc.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/cinema")
public class CinemaController {
    @Autowired
    private CinemaService cinemaService;

    @PostMapping("/create-cinema")
    public Result createCinema(@RequestBody CinemaDto cinemaDto)  {
        return cinemaService.createCinema(cinemaDto);
    }

    @PostMapping("/by-admin")
    public Result findCinemaByAdmin(@RequestParam Integer adminId) {
        System.out.println(adminId);
        return cinemaService.findCinemaByAdmin(adminId);
    }

    @PostMapping("/{id}")
    public Result findCinema(@PathVariable Integer id) {
        return cinemaService.findCinemaById(id);
    }

    @PostMapping
    public Result findAllCinema() {
        return cinemaService.findAll();
    }
}
