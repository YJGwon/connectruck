package com.connectruck.foodtruck.notification.service;

import com.connectruck.foodtruck.notification.domain.SseEmitterRepository;
import com.connectruck.foodtruck.notification.domain.SseEvent;
import com.connectruck.foodtruck.notification.domain.SseEventRepository;
import com.connectruck.foodtruck.truck.service.TruckService;
import java.io.IOException;
import java.util.List;
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

    private static final Long SUBSCRIBE_TIME_OUT = 60L * 60L * 1000L;
    private static final String SSE_EVENT_ID_FORMAT = "%d_%d";

    private final SseEmitterRepository sseEmitterRepository;
    private final SseEventRepository sseEventRepository;

    private final TruckService truckService;

    public SseEmitter subscribeOrders(final Long ownerId, final String lastEventId) {
        final Long truckId = truckService.findByOwnerId(ownerId).id();

        final SseEmitter sseEmitter = new SseEmitter(SUBSCRIBE_TIME_OUT);
        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onCompletion(() -> {
            log.info("SSE connection complete - {}", truckId);
            sseEmitterRepository.deleteById(truckId);
        });
        sendInitialEvent(truckId, sseEmitter);
        log.info("SSE connection started - {}", truckId);

        sendLickedEvents(lastEventId, truckId, sseEmitter);

        sseEmitterRepository.save(truckId, sseEmitter);
        return sseEmitter;
    }

    public void notifyOrderCreated(final Long truckId, final Long orderId) {
        final SseEvent sseEvent = new SseEvent(
                generateSseEventId(truckId),
                "order created",
                orderId.toString()
        );
        sseEventRepository.save(truckId, sseEvent);

        final Optional<SseEmitter> found = sseEmitterRepository.findById(truckId);
        if (found.isEmpty()) {
            return;
        }

        final SseEmitter sseEmitter = found.get();
        send(sseEmitter, sseEvent);
    }

    private String generateSseEventId(final Long truckId) {
        return String.format(SSE_EVENT_ID_FORMAT, truckId, System.currentTimeMillis());
    }

    private void sendInitialEvent(final Long truckId, final SseEmitter sseEmitter) {
        final SseEvent sseEvent = new SseEvent(
                generateSseEventId(truckId),
                "connect",
                "connected on orders for " + truckId
        );
        send(sseEmitter, sseEvent);
    }

    private void sendLickedEvents(final String lastEventId, final Long truckId, final SseEmitter sseEmitter) {
        if (lastEventId.isBlank()) {
            return;
        }

        final List<SseEvent> lickedEvents = sseEventRepository.findByGroupIdAndIdGraterThan(truckId, lastEventId);
        for (SseEvent lickedEvent : lickedEvents) {
            send(sseEmitter, lickedEvent);
        }
    }

    private void send(final SseEmitter sseEmitter, final SseEvent sseEvent) {
        try {
            final SseEventBuilder eventBuilder = SseEmitter.event()
                    .id(sseEvent.getId())
                    .name(sseEvent.getName())
                    .data(sseEvent.getData());
            sseEmitter.send(eventBuilder);
        } catch (IOException e) {
            sseEmitter.completeWithError(e);
        }
    }
}
