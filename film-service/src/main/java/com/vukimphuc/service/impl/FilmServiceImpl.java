package com.vukimphuc.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucvukimcore.base.Result;
import com.vukimphuc.cloudinary.CloudinaryService;
import com.vukimphuc.dto.request.FilmDto;
import com.vukimphuc.entity.Film;
import com.vukimphuc.entity.Rating;
import com.vukimphuc.entity.Thumnail;
import com.vukimphuc.entity.Type;
import com.vukimphuc.repository.FilmRepository;
import com.vukimphuc.repository.FilmTypeRepository;
import com.vukimphuc.repository.RatingRepository;
import com.vukimphuc.repository.ThumbnailsRepository;
import com.vukimphuc.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmTypeRepository filmTypeRepository;

    @Autowired
    private ThumbnailsRepository thumbnailsRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public Result getAllFilms() {
        List<Film> films = filmRepository.findAll();
        return Result.success("Success", films);
    }

    @Override
    public Result getAllFilmsOrderByCreatedDate() {
        return null;
    }

    @Transactional
    @Override
    public Result createFilm(FilmDto filmDto) {
        Film film = new Film();
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        List<Thumnail> thumnails = new ArrayList<>();
        try {
            Date releaseDate = convertToDate(filmDto.getReleaseDate());
            film.setReleaseDate(releaseDate);

            for (MultipartFile file: filmDto.getThumnails()) {
                Map<String, String> map = cloudinaryService.uploadFile(file);
                Thumnail thumnail = new Thumnail();
                thumnail.setUrl(map.get("url"));
                thumnail.setPublicId(map.get("publicId"));
                thumnail.setFilm(film);
                thumnails.add(thumnail);
            }
            film.setThumnails(thumnails);
        } catch (ParseException ex) {
            return Result.fail("Release date is invalid");
        } catch (IOException ex) {
            return Result.fail("Error while uploading thumbnails");
        }
        film.setDuration(filmDto.getDuration());
        List<Type> types = getListFilmTypes(filmDto.getTypeIds());
        film.setTypes(types);
        filmRepository.save(film);
        thumbnailsRepository.saveAll(thumnails);
        return Result.success("Success", film);
    }

    @Override
    public Result editFilm(FilmDto filmDto) {
        Integer id = filmDto.getId();
        Optional<Film> op = filmRepository.findById(id);
        if (!op.isPresent())
            return Result.fail("Film not found");
        Film film = op.get();
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        try {
            Date releaseDate = convertToDate(filmDto.getReleaseDate());
            film.setReleaseDate(releaseDate);
        } catch (ParseException ex) {
            return Result.fail("Release date is invalid");
        }
        film.setDuration(filmDto.getDuration());
        List<Type> types = getListFilmTypes(filmDto.getTypeIds());
        film.setTypes(types);
        filmRepository.save(film);
        return Result.success("Success", film);
    }

    @Override
    public Result getFilmById(Integer id) {
        Optional<Film> op = filmRepository.findById(id);
        if (!op.isPresent())
            return Result.fail("Film not found");
        Film film = op.get();
        return Result.success("Success", film);
    }

    @Transactional
    @Override
    public Result deleteFilmById(Integer id) {
        Optional<Film> op = filmRepository.findById(id);
        if (!op.isPresent())
            return Result.fail("Film not found");
        Film film = op.get();
        List<Thumnail> thumnails = film.getThumnails();
        for(Thumnail thumnail: thumnails) {
            try {
                cloudinaryService.deleteFile(thumnail.getPublicId());
            } catch (IOException e){
                return Result.fail("Error while detele thumnail");
            }
        }
        filmRepository.delete(film);
        return Result.success("Suceess", film);
    }

    private Date convertToDate(String strDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(strDate);
    }

    private List<Type> getListFilmTypes(List<Integer> ids) {
        List<Type> types = new ArrayList<>();
        for(Integer id: ids) {
            Optional<Type> op = filmTypeRepository.findById(id);
            op.ifPresent(types::add);
        }
        return types;
    }

    @Override
    public Result updateAvgScore(Integer filmId) {
        Optional<Film> op = filmRepository.findById(filmId);
        if (!op.isPresent())
            return Result.fail("Phim không tồn tại");
        Film film = op.get();
        List<Rating> ratings = ratingRepository.findAllByFilm(film);
        if (ratings == null || ratings.isEmpty())
            return Result.success();
        List<Integer> stars = ratings.stream().map(Rating::getStar).collect(Collectors.toList());
        int sum = 0;
        for (Integer star: stars) {
            sum += star;
        }

        float avgScore = (float) sum / stars.size();
        avgScore = (float) (Math.round(avgScore * Math.pow(10, 1)) / Math.pow(10, 1));
        film.setScore(avgScore);
        return Result.success("Success",filmRepository.save(film));
    }
}
