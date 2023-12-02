package com.vukimphuc.dto.response;

import com.vukimphuc.entity.Cinema;
import com.vukimphuc.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountResponse {
    private User user;
    private Cinema cinema;
}
