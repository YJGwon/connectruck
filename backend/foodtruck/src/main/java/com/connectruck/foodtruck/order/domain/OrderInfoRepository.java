package com.connectruck.foodtruck.order.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface OrderInfoRepository extends Repository<OrderInfo, Long> {

    OrderInfo save(OrderInfo orderInfo);

    Optional<OrderInfo> findById(Long id);

    Page<OrderInfo> findByTruckId(Long truckId, Pageable pageable);

    Page<OrderInfo> findByTruckIdAndStatus(Long truckId, OrderStatus status, Pageable pageable);
}
