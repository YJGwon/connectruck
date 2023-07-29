package com.connectruck.foodtruck.notification.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.notification.domain.SseEmitterRepository;
import com.connectruck.foodtruck.notification.domain.SseEvent;
import com.connectruck.foodtruck.notification.domain.SseEventRepository;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
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

    private static final String SSE_GROUP_TYPE_OWNER_ORDER = "owner-order";
    private static final Long SUBSCRIBE_TIME_OUT = 7L * 60L * 1000L;
    private static final String SSE_EVENT_ID_DELIMITER = "_";

    private final SseEmitterRepository sseEmitterRepository;
    private final SseEventRepository sseEventRepository;
    private final TruckRepository truckRepository;

    public SseEmitter subscribeOrders(final Long ownerId, final String lastEventId) {
        final Long truckId = truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("소유한 푸드트럭", "ownerId", ownerId))
                .getId();

        final SseEmitter sseEmitter = new SseEmitter(SUBSCRIBE_TIME_OUT);
        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onCompletion(() -> {
            log.info("SSE connection complete - {}", truckId);
            sseEmitterRepository.deleteById(truckId);
        });
        sendInitialEvent(truckId, sseEmitter);
        log.info("SSE connection started - {}", truckId);

        sendLickedEvents(lastEventId, sseEmitter);

        sseEmitterRepository.save(truckId, sseEmitter);
        return sseEmitter;
    }

    public void notifyOrderCreated(final Long truckId, final Long orderId) {
        final SseEvent sseEvent = new SseEvent(
                SSE_GROUP_TYPE_OWNER_ORDER,
                truckId,
                "order created",
                orderId.toString()
        );
        sseEventRepository.save(sseEvent);

        final Optional<SseEmitter> found = sseEmitterRepository.findById(truckId);
        if (found.isEmpty()) {
            return;
        }

        final SseEmitter sseEmitter = found.get();
        send(sseEmitter, sseEvent);
    }

    private void sendInitialEvent(final Long truckId, final SseEmitter sseEmitter) {
        final SseEvent sseEvent = new SseEvent(
                SSE_GROUP_TYPE_OWNER_ORDER,
                truckId,
                "connect",
                "connected on orders for " + truckId
        );
        send(sseEmitter, sseEvent);
    }

    private void sendLickedEvents(final String lastEventId, final SseEmitter sseEmitter) {
        if (lastEventId.isBlank()) {
            return;
        }

        final StringTokenizer stringTokenizer = new StringTokenizer(lastEventId, SSE_EVENT_ID_DELIMITER);
        final long truckId = Long.parseLong(stringTokenizer.nextToken());
        final long timestamp = Long.parseLong(stringTokenizer.nextToken());

        final List<SseEvent> lickedEvents = sseEventRepository
                .findByTypeAndTargetIdAndTimestampGraterThan(SSE_GROUP_TYPE_OWNER_ORDER, truckId, timestamp);
        for (SseEvent lickedEvent : lickedEvents) {
            send(sseEmitter, lickedEvent);
        }
    }

    private void send(final SseEmitter sseEmitter, final SseEvent sseEvent) {
        try {
            final SseEventBuilder eventBuilder = SseEmitter.event()
                    .id(sseEvent.getGroupId() + SSE_EVENT_ID_DELIMITER + sseEvent.getTimestamp())
                    .name(sseEvent.getName())
                    .data(sseEvent.getData());
            sseEmitter.send(eventBuilder);
        } catch (IOException e) {
            sseEmitter.completeWithError(e);
        }
    }
}
