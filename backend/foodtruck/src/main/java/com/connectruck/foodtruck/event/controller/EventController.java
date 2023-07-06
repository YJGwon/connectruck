package com.connectruck.foodtruck.event.controller;

import com.connectruck.foodtruck.event.dto.EventResponse;
import com.connectruck.foodtruck.event.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @Operation(summary = "행사 정보 조회")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 행사 id")
    @GetMapping("/{eventId}")
    public EventResponse findOne(@PathVariable final long eventId) {
        return eventService.findById(eventId);
    }
}
