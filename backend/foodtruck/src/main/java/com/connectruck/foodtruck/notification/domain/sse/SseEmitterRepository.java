package com.connectruck.foodtruck.notification.domain.sse;

import java.util.Optional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseEmitterRepository {

    void save(Long id, SseEmitter sseEmitter);

    Optional<SseEmitter> findById(Long id);

    void deleteById(Long id);
}
