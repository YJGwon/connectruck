package com.connectruck.foodtruck.owner.service;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.order.domain.OrderInfoRepository;
import com.connectruck.foodtruck.order.domain.OrderStatus;
import com.connectruck.foodtruck.owner.dto.OwnerOrdersResponse;
import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerService {

    private final TruckRepository truckRepository;
    private final OrderInfoRepository orderInfoRepository;

    public OwnerOrdersResponse findOrdersOfOwningTruckByStatus(final Long ownerId, final String rawStatus,
                                                               final int page, final int size) {
        final Long truckId = getOwningTruck(ownerId).getId();

        final Sort latest = Sort.by(DESC, "createdAt");
        final PageRequest pageRequest = PageRequest.of(page, size, latest);

        final OrderStatus status = OrderStatus.valueOf(rawStatus.toUpperCase());
        final Page<OrderInfo> found = getOrdersByTruckIdAndStatus(status, truckId, pageRequest);
        return OwnerOrdersResponse.of(found);
    }

    private Truck getOwningTruck(final Long ownerId) {
        return truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("푸드트럭", "ownerId", ownerId));
    }

    private Page<OrderInfo> getOrdersByTruckIdAndStatus(final OrderStatus status, final Long truckId,
                                                        final PageRequest pageRequest) {
        if (status == OrderStatus.ALL) {
            return orderInfoRepository.findByTruckId(truckId, pageRequest);
        }
        return orderInfoRepository.findByTruckIdAndStatus(truckId, status, pageRequest);
    }
}
