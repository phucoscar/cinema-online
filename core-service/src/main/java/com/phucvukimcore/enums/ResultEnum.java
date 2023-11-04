package com.phucvukimcore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {
    SUCCESS(200, "Success"),
    FAIL(400, "Fail");

    private Integer code;
    private String desc;
}
