package com.connectruck.foodtruck.notification.domain;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class SseEmitterRepositoryImpl implements SseEmitterRepository {

    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public void save(final Long id, final SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
    }

    @Override
    public Optional<SseEmitter> findById(final Long id) {
        return Optional.ofNullable(emitters.get(id));
    }

    @Override
    public void deleteById(final Long id) {
        emitters.remove(id);
    }
}
