package com.connectruck.foodtruck.auth.controller.testcontroller;

import com.connectruck.foodtruck.auth.annotation.Authentication;
import com.connectruck.foodtruck.auth.annotation.AuthenticationPrincipal;
import com.connectruck.foodtruck.auth.annotation.Authorization;
import com.connectruck.foodtruck.user.domain.Role;
import java.util.Map;
import org.springframework.http.ResponseEntity;
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

    @Authorization(Role.OWNER)
    @GetMapping("/api/authorization")
    public void authorization() {
    }

    @GetMapping("/api/resolve-principal")
    public ResponseEntity<Map<String, Long>> resolvePrincipal(@AuthenticationPrincipal final Long userId) {
        return ResponseEntity.ok(Map.of("userId", userId));
    }
}
