package com.vukimphuc.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucvukimcore.base.Result;
import com.vukimphuc.cloudinary.CloudinaryService;
import com.vukimphuc.dto.request.FilmDto;
import com.vukimphuc.entity.Film;
import com.vukimphuc.entity.Thumnail;
import com.vukimphuc.entity.Type;
import com.vukimphuc.repository.FilmRepository;
import com.vukimphuc.repository.FilmTypeRepository;
import com.vukimphuc.repository.ThumbnailsRepository;
import com.vukimphuc.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
}
