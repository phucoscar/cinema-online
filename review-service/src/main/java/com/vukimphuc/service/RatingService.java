package com.vukimphuc.service;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.RatingDTO;

public interface RatingService {

    Result createRating(RatingDTO dto);

    Result getFilmRatings(Integer filmId);
}
