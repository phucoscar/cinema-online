package com.vukimphuc.dto.response;

import com.vukimphuc.entity.Film;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListFilmResponse {
    private List<Film> films;
    private PageInfo pageInfo;
}
