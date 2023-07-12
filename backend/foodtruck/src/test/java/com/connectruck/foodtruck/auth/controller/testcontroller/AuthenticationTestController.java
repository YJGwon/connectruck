package com.connectruck.foodtruck.auth.controller.testcontroller;

import com.connectruck.foodtruck.auth.annotation.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Authentication
@RestController
public class AuthenticationTestController {

    @GetMapping("/api/type-authentication")
    public void authentication() {
    }
}
