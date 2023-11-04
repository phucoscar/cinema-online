package com.phucvukimcore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TicketClassEnums {
    REGULAR(0, "REGULAR"),
    VIP(1, "VIP");

    private Integer code;
    private String description;
}
