package com.phucvukimcore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnums {
    SUPER_ADMIN(1, "Super_admin"),
    ADMIN(2, "Admin"),
    CUSTOMER(3, "Customer");

    private Integer code;
    private String desc;
}
