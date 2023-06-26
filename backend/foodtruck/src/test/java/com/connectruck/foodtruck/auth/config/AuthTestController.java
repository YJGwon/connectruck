package com.connectruck.foodtruck.auth.config;

import com.connectruck.foodtruck.auth.annotation.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthTestController {

    @GetMapping("/api/no-auth")
    public void noAuth() {
    }

    @Authentication
    @GetMapping("/api/authentication")
    public void authentication() {
    }
}
