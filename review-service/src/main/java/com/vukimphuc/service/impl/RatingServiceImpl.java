package com.vukimphuc.service.impl;

import com.phucvukimcore.base.Result;
import com.vukimphuc.dto.request.RatingDTO;
import com.vukimphuc.dto.response.RatingDtoResponse;
import com.vukimphuc.entity.Film;
import com.vukimphuc.entity.Rating;
import com.vukimphuc.entity.User;
import com.vukimphuc.repository.FilmRepository;
import com.vukimphuc.repository.RatingRepository;
import com.vukimphuc.repository.UserRepository;
import com.vukimphuc.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Result createRating(RatingDTO dto) {

        Optional<User> user = userRepository.findById(dto.getUserId());
        if (!user.isPresent())
            return Result.fail("Người dùng không tồn tại");

        Optional<Film> film = filmRepository.findById(dto.getFilmId());
        if (!film.isPresent())
            return Result.fail("Phim không tồn tại");

        Rating rating = new Rating();
        rating.setStar(dto.getStar());
        rating.setComment(dto.getComment());
        rating.setCreatedAt(LocalDateTime.now());
        rating.setFilm(film.get());
        rating.setUser(user.get());
        ratingRepository.save(rating);

        // call film-service to update avgScore
        String url = "http://localhost:8083/api/v1/film/update-score";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("filmId", dto.getFilmId());

        Result result = restTemplate.getForObject(builder.toUriString(), Result.class);

        return Result.success("Success", result);
    }

    @Override
    public Result getFilmRatings(Integer filmId) {
        List<Rating> ratings = ratingRepository.findAllByFilmId(filmId);
        return Result.success("Success", ratings);
    }

    @Override
    public RatingDtoResponse getRatingInAFilmByUser(Integer filmId, Integer userId) {
        Optional<Rating> op = ratingRepository.findByFilmIdAndUserId(filmId, userId);
        return op.map(rating -> new RatingDtoResponse(rating.getStar(), rating.getComment())).orElse(null);
    }
}
