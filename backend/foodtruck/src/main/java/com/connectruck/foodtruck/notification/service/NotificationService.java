package com.connectruck.foodtruck.notification.service;

import com.connectruck.foodtruck.notification.exception.NotificationException;
import com.connectruck.foodtruck.truck.service.TruckService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private static final String ID_FORMAT = "%d_%d";

    private final TruckService truckService;

    public SseEmitter subscribeOrders(final Long ownerId) {
        final SseEmitter sseEmitter = new SseEmitter();

        final Long truckId = truckService.findByOwnerId(ownerId).id();
        final SseEventBuilder eventBuilder = SseEmitter.event()
                .id(generateId(truckId))
                .name("connect")
                .data("connected on orders for " + truckId);
        notify(sseEmitter, eventBuilder);

        return sseEmitter;
    }

    private String generateId(final Long truckId) {
        return String.format(ID_FORMAT, truckId, System.currentTimeMillis());
    }

    private void notify(final SseEmitter sseEmitter, final SseEventBuilder eventBuilder) {
        try {
            sseEmitter.send(eventBuilder);
        } catch (IOException e) {
            throw new NotificationException(e);
        }
    }
}
