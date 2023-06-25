package com.connectruck.foodtruck.user.dto;

import com.connectruck.foodtruck.user.domain.Role;

public record UserRequest(
        String username,
        String password,
        String phone,
        Role role
) {
}
