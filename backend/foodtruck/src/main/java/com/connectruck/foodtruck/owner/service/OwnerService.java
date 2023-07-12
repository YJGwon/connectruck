package com.connectruck.foodtruck.owner.service;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import com.connectruck.foodtruck.owner.domain.OwnerOrderInfoRepository;
import com.connectruck.foodtruck.owner.domain.OwnerTruckRepository;
import com.connectruck.foodtruck.owner.dto.OwnerOrdersResponse;
import com.connectruck.foodtruck.owner.dto.OwnerTruckResponse;
import com.connectruck.foodtruck.truck.domain.Truck;
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

    private final OwnerTruckRepository ownerTruckRepository;
    private final OwnerOrderInfoRepository ownerOrderInfoRepository;

    public OwnerTruckResponse findOwningTruck(final Long ownerId) {
        final Truck found = getOwningTruck(ownerId);
        return OwnerTruckResponse.of(found);
    }

    public OwnerOrdersResponse findOrdersOfOwningTruck(final Long ownerId, final int page, final int size) {
        final Long truckId = getOwningTruck(ownerId).getId();

        final Sort latest = Sort.by(DESC, "createdAt");
        final PageRequest pageRequest = PageRequest.of(page, size, latest);
        final Page<OrderInfo> found = ownerOrderInfoRepository.findByTruckId(truckId, pageRequest);

        return OwnerOrdersResponse.of(found);
    }

    private Truck getOwningTruck(final Long ownerId) {
        return ownerTruckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("푸드트럭", "ownerId", ownerId));
    }
}
