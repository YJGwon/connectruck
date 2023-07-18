package com.connectruck.foodtruck.notification.repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterRepository {

    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(final Long id, final SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
    }

    public Optional<SseEmitter> findById(final Long id) {
        return Optional.ofNullable(emitters.get(id));
    }

    public void deleteById(final Long id) {
        emitters.remove(id);
    }
}
