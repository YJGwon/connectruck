package com.connectruck.foodtruck.event.controller;

import com.connectruck.foodtruck.event.dto.EventResponse;
import com.connectruck.foodtruck.event.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "event", description = "행사 관련 API")
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @Operation(summary = "행사 정보 조회")
    @GetMapping("/{eventId}")
    public EventResponse findOne(@PathVariable final long eventId) {
        return eventService.findById(eventId);
    }
}
