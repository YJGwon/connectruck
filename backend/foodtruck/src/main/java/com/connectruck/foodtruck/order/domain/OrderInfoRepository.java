package com.connectruck.foodtruck.order.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface OrderInfoRepository extends Repository<OrderInfo, Long> {

    OrderInfo save(final OrderInfo orderInfo);

    Optional<OrderInfo> findById(final Long id);
}
