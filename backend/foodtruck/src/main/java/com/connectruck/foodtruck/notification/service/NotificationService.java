package com.connectruck.foodtruck.notification.service;

import com.connectruck.foodtruck.notification.repository.SseEmitterRepository;
import com.connectruck.foodtruck.truck.service.TruckService;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private static final Long SUBSCRIBE_TIME_OUT = 5L * 60L * 1000L;
    private static final String SSE_EVENT_ID_FORMAT = "%d_%d";

    private final SseEmitterRepository sseEmitterRepository;

    private final TruckService truckService;

    public SseEmitter subscribeOrders(final Long ownerId) {
        final Long truckId = truckService.findByOwnerId(ownerId).id();

        final SseEmitter sseEmitter = new SseEmitter(SUBSCRIBE_TIME_OUT);
        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onCompletion(() -> {
            log.info("SSE connection complete - {}", truckId);
            sseEmitterRepository.deleteById(truckId);
        });
        sseEmitterRepository.save(truckId, sseEmitter);

        final SseEventBuilder eventBuilder = SseEmitter.event()
                .id(generateSseEventId(truckId))
                .name("connect")
                .data("connected on orders for " + truckId);
        send(sseEmitter, eventBuilder);

        log.info("SSE connection started - {}", truckId);
        return sseEmitter;
    }

    public void notifyOrderCreated(final Long truckId, final Long orderId) {
        final Optional<SseEmitter> found = sseEmitterRepository.findById(truckId);
        if (found.isEmpty()) {
            return;
        }

        final SseEmitter sseEmitter = found.get();
        final SseEventBuilder eventBuilder = SseEmitter.event()
                .id(generateSseEventId(truckId))
                .name("order created")
                .data(orderId);
        send(sseEmitter, eventBuilder);
    }

    private String generateSseEventId(final Long truckId) {
        return String.format(SSE_EVENT_ID_FORMAT, truckId, System.currentTimeMillis());
    }

    private void send(final SseEmitter sseEmitter, final SseEventBuilder eventBuilder) {
        try {
            sseEmitter.send(eventBuilder);
        } catch (IOException e) {
            sseEmitter.completeWithError(e);
        }
    }
}
