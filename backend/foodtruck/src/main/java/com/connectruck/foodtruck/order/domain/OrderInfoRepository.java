package com.connectruck.foodtruck.order.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface OrderInfoRepository extends Repository<OrderInfo, Long> {

    OrderInfo save(final OrderInfo orderInfo);

    Optional<OrderInfo> findById(final Long id);

    Page<OrderInfo> findByTruckId(final Long truckId, final Pageable pageable);

    Page<OrderInfo> findByTruckIdAndStatus(final Long truckId, final OrderStatus status, final Pageable pageable);
}
