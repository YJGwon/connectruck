package com.connectruck.foodtruck.auth.controller.testcontroller;

import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.user.domain.Role;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Authorization(Role.OWNER)
@RestController
public class AuthorizationTestController {

    @GetMapping("/api/type-authorization")
    public void authorization() {
    }
}
