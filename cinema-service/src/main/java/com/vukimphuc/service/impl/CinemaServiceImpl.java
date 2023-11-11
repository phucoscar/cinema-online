package com.vukimphuc.service.impl;

import com.phucvukimcore.base.Result;
import com.phucvukimcore.enums.RoleEnums;
import com.vukimphuc.dto.request.CinemaDto;
import com.vukimphuc.entity.Cinema;
import com.vukimphuc.entity.User;
import com.vukimphuc.repository.CinemaRepository;
import com.vukimphuc.repository.UserRepository;
import com.vukimphuc.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Result createCinema(CinemaDto dto) {
        Cinema cinema = new Cinema();
        cinema.setName(dto.getName());
        cinema.setAddress(dto.getAddress());
        Optional<User> op = userRepository.findById(dto.getAdminId());
        if (!op.isPresent())
            return Result.fail("Admin not found");
        User user = op.get();
        if (!user.getRole().getId().equals(RoleEnums.ADMIN.getCode()))
            return Result.fail("User is not an admin");
        cinema.setAdmin(user);
        cinemaRepository.save(cinema);
        user.setManagedCinema(cinema);
        userRepository.save(user);
        return Result.success("Success", cinema);
    }

    @Override
    public Result findCinemaById(Integer id) {
        Optional<Cinema> op = cinemaRepository.findById(id);
        if (op.isPresent()) {
            return Result.success("Success", op.get());
        }
        return Result.fail("Cinema not found");
    }

    @Override
    public Result findAll() {
        return Result.success("Success", cinemaRepository.findAll());
    }
}
