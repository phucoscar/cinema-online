package com.phucvukimcore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilmTypeEnum {

    COMEDY(1, "Hài"),
    ACTION(2, "Hành động"),
    HORROR(3, "Kinh dị"),
    FAMILY(4, "Gia đình"),
    ROMANCE(5, "Tình cảm");

    private Integer code;
    private String name;
}
