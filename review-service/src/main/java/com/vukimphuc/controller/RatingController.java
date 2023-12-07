package com.vukimphuc.controller;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.RatingDTO;
import com.vukimphuc.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/create-rating")
    public Result createRating(@RequestBody RatingDTO ratingDTO) {
        return ratingService.createRating(ratingDTO);
    }

    @GetMapping("/film-ratings")
    public Result getRatingsInAFilm(@RequestParam Integer filmId) {
        return ratingService.getFilmRatings(filmId);
    }
}
