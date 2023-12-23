package com.vukimphuc.service;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.RatingDTO;
import com.vukimphuc.dto.response.RatingDtoResponse;

public interface RatingService {

    Result createRating(RatingDTO dto);

    Result getFilmRatings(Integer filmId);

    RatingDtoResponse getRatingInAFilmByUser(Integer filmId, Integer userId);
}
