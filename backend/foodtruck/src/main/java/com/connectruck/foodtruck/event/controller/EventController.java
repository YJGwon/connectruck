package com.connectruck.foodtruck.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    @Operation(summary = "행사 정보 조회")
    @GetMapping("/{eventId}")
    public void findOne(@PathVariable final long eventId) {

    }
}
