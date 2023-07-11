package com.connectruck.foodtruck.owner.domain;

import com.connectruck.foodtruck.order.domain.OrderInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface OwnerOrderInfoRepository extends Repository<OrderInfo, Long> {

    Page<OrderInfo> findByTruckId(final Long truckId, final Pageable pageable);
}
