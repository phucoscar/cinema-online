package com.vukimphuc.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucvukimcore.base.Result;
import com.phucvukimcore.util.JsonUtil;
import com.vukimphuc.cloudinary.CloudinaryService;
import com.vukimphuc.dto.request.EditFilmDto;
import com.vukimphuc.dto.request.FilmDto;
import com.vukimphuc.dto.response.ListFilmResponse;
import com.vukimphuc.dto.response.PageInfo;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result getAllFilms() {
//        List<Film> films = null;
//
//        try {
//            films = (List<Film>) redisTemplate.opsForValue().get("films");
//        } catch (Exception e) {
//            return Result.fail("Xảy ra lỗi trong quá trình chuyển đồi dữ liệu từ Redis");
//        }
//
//        if (films == null || films.size() == 0) {
//            films = filmRepository.findAll();
//            redisTemplate.delete("films");
//            redisTemplate.opsForValue().set("films", films);
//            redisTemplate.expire("films", 200, TimeUnit.SECONDS);
//        }
        List<Film> films = filmRepository.findAll();
        return Result.success("Success", films);
    }

    @Override
    public Result getFilms(int page, int perPage) {
//        List<Film> films = null;
//        films = (List<Film>) redisTemplate.opsForValue().get("films");
//
//        if (films == null || films.size() == 0) {
//            films = filmRepository.findAll();
//            redisTemplate.delete("films");
//            redisTemplate.opsForValue().set("films", films);
//            redisTemplate.expire("films", 200, TimeUnit.SECONDS);
//        }
//
        List<Film> films = filmRepository.findAll();
        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, films.size());

        ListFilmResponse response = new ListFilmResponse();
        response.setFilms(films.subList(start, end));

        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotalItems(films.size());
        pageInfo.setPageSize(end-start);
        pageInfo.setTotalPages(films.size() % perPage == 0? films.size() / perPage : films.size() / perPage + 1);
        response.setPageInfo(pageInfo);
        return Result.success("Success", response);
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
            return Result.fail("Ngày phát hành không đúng định dạng");
        } catch (IOException ex) {
            return Result.fail("Xảy ra lỗi trong quá trình upload ảnh");
        }
        film.setDuration(filmDto.getDuration());
        List<Type> types = getListFilmTypes(filmDto.getTypeIds());
        film.setTypes(types);
        filmRepository.save(film);
        thumbnailsRepository.saveAll(thumnails);
        return Result.success("Success", film);
    }


    //TODO: sửa phim
    @Override
    public Result editFilm(EditFilmDto filmDto) {
        Integer id = filmDto.getId();
        Optional<Film> op = filmRepository.findById(id);
        if (!op.isPresent())
            return Result.fail("Không tồn tại phim");
        Film film = op.get();
        List<Thumnail> thumnails = film.getThumnails();
        if (filmDto.getDeleteThumbnails() != null) {

            for (Thumnail thumnail: thumnails) {
                for (Integer idThum: filmDto.getDeleteThumbnails()) {
                    if (thumnail.getId().equals(idThum)) {
                        thumnails.remove(thumnail);
                        Thumnail delThumnail = thumbnailsRepository.findById(idThum).get();
                        try {
                            cloudinaryService.deleteFile(delThumnail.getPublicId());
                        } catch (IOException ex) {
                        }
                        thumbnailsRepository.deleteById(idThum);
                    }
                }
            }

        }
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        try {
            Date releaseDate = convertToDate(filmDto.getReleaseDate());
            film.setReleaseDate(releaseDate);

            if (filmDto.getThumnails() != null) {
                for (MultipartFile file: filmDto.getThumnails()) {
                    Map<String, String> map = cloudinaryService.uploadFile(file);
                    Thumnail thumnail = new Thumnail();
                    thumnail.setUrl(map.get("url"));
                    thumnail.setPublicId(map.get("publicId"));
                    thumnail.setFilm(film);
                    thumnails.add(thumnail);
                }
            }
            film.setThumnails(thumnails);
        } catch (ParseException ex) {
            return Result.fail("Ngày khởi chiếu không đúng định dạng");
        } catch (IOException ex) {
            return Result.fail("Xảy ra lỗi trong quá trình upload ảnh");
        }
        film.setDuration(filmDto.getDuration());
        List<Type> types = getListFilmTypes(filmDto.getTypeIds());
        film.setTypes(types);
        filmRepository.save(film);
        thumbnailsRepository.saveAll(thumnails);
        return Result.success("Success", film);
    }

    @Override
    public Result getFilmById(Integer id) {
        Optional<Film> op = filmRepository.findById(id);
        if (!op.isPresent())
            return Result.fail("Không tìm thấy phim");
        Film film = op.get();
        return Result.success("Success", film);
    }

    @Transactional
    @Override
    public Result deleteFilmById(Integer id) {
        Optional<Film> op = filmRepository.findById(id);
        if (!op.isPresent())
            return Result.fail("Không tìm thấy phim");
        Film film = op.get();
        List<Thumnail> thumnails = film.getThumnails();
        for(Thumnail thumnail: thumnails) {
            try {
                cloudinaryService.deleteFile(thumnail.getPublicId());
            } catch (IOException e){
                return Result.fail("Xảy ra lỗi trong quá trình xóa ảnh");
            }
        }
        filmRepository.delete(film);
        return Result.success("Suceess", film);
    }

    @Override
    public Result searchFilmByName(String name) {
        if (name == null) {
            return Result.fail("Không tìm thấy phim");
        }
        List<Film> films = filmRepository.searchByName(name);
        if (films.isEmpty())
            return Result.fail("Không tìm thấy phim");
        return Result.success("Success", films);
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
