package com.connectruck.foodtruck.user.controller;

import com.connectruck.foodtruck.user.dto.CheckAvailableResponse;
import com.connectruck.foodtruck.user.dto.UserRequest;
import com.connectruck.foodtruck.user.dto.UsernameRequest;
import com.connectruck.foodtruck.user.sevice.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "아이디 검사")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 body")
    @PostMapping("/check-username")
    public CheckAvailableResponse checkUsername(@RequestBody @Valid UsernameRequest request) {
        return userService.checkUsername(request);
    }

    @Operation(summary = "회원 가입")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 body")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid UserRequest request) {
        userService.create(request);
        final URI location = URI.create("/api/users/me");
        return ResponseEntity.created(location).build();
    }
}
