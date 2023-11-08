package com.vukimphuc.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucvukimcore.base.Result;
import com.vukimphuc.cloudinary.CloudinaryService;
import com.vukimphuc.dto.request.FilmDto;
import com.vukimphuc.entity.Film;
import com.vukimphuc.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilmServiceImpl implements FilmService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Result createFilm(FilmDto filmDto) {
        Film film;
        return null;
    }
}
