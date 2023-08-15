package com.connectruck.foodtruck.notification.service;

import static com.connectruck.foodtruck.notification.domain.SseEventGroupType.OWNER_ORDER;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.notification.domain.SseEmitterRepository;
import com.connectruck.foodtruck.notification.domain.SseEvent;
import com.connectruck.foodtruck.notification.domain.SseEventGroup;
import com.connectruck.foodtruck.notification.domain.SseEventRepository;
import com.connectruck.foodtruck.order.message.OrderMessage;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private static final Long SUBSCRIBE_TIME_OUT = 7L * 60L * 1000L;

    private final SseEmitterRepository sseEmitterRepository;
    private final SseEventRepository sseEventRepository;
    private final TruckRepository truckRepository;

    public SseEmitter subscribeOrders(final Long ownerId, final String lastEventId) {
        final Long truckId = truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("소유한 푸드트럭", "ownerId", ownerId))
                .getId();

        final SseEmitter sseEmitter = createSseEmitter(truckId);
        final SseEventGroup group = new SseEventGroup(OWNER_ORDER, truckId);
        sendInitialEvent(group, sseEmitter);
        sendLickedEvents(lastEventId, group, sseEmitter);

        sseEmitterRepository.save(truckId, sseEmitter);
        log.info("SSE connection started - {}", truckId);
        return sseEmitter;
    }

    public void notifyOrderToOwner(final OrderMessage orderMessage) {
        final SseEvent sseEvent = createOrderEvent(orderMessage);
        sseEventRepository.save(sseEvent);

        final Optional<SseEmitter> found = sseEmitterRepository.findById(orderMessage.truckId());
        if (found.isEmpty()) {
            return;
        }

        final SseEmitter sseEmitter = found.get();
        send(sseEmitter, sseEvent);
    }

    private SseEmitter createSseEmitter(final Long truckId) {
        final SseEmitter sseEmitter = new SseEmitter(SUBSCRIBE_TIME_OUT);
        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onCompletion(() -> {
            log.info("SSE connection complete - {}", truckId);
            sseEmitterRepository.deleteById(truckId);
        });
        return sseEmitter;
    }

    private void sendInitialEvent(final SseEventGroup group, final SseEmitter sseEmitter) {
        final SseEvent sseEvent = new SseEvent(
                group,
                "connect",
                "connected on " + group.getId()
        );
        send(sseEmitter, sseEvent);
    }

    private void sendLickedEvents(final String lastEventId, final SseEventGroup group, final SseEmitter sseEmitter) {
        if (lastEventId.isBlank()) {
            return;
        }

        final long timestamp = SseEvent.getTimestampFrom(lastEventId);
        final List<SseEvent> lickedEvents = sseEventRepository.findByGroupAndTimestampGraterThan(group, timestamp);
        for (SseEvent lickedEvent : lickedEvents) {
            send(sseEmitter, lickedEvent);
        }
    }

    private SseEvent createOrderEvent(final OrderMessage orderMessage) {
        final SseEventGroup group = new SseEventGroup(OWNER_ORDER, orderMessage.truckId());
        final String name = "order " + orderMessage.status().name().toLowerCase();
        final String data = Long.toString(orderMessage.orderId());
        return new SseEvent(group, name, data);
    }

    private void send(final SseEmitter sseEmitter, final SseEvent sseEvent) {
        try {
            final SseEventBuilder eventBuilder = SseEmitter.event()
                    .id(sseEvent.generateEventId())
                    .name(sseEvent.getName())
                    .data(sseEvent.getData());
            sseEmitter.send(eventBuilder);
        } catch (IOException e) {
            sseEmitter.completeWithError(e);
        }
    }
}
