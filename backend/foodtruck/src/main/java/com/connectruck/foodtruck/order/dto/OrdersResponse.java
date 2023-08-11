package com.connectruck.foodtruck.order.dto;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import java.util.List;
import org.springframework.data.domain.Page;

public record OrdersResponse(
        PageResponse page,
        List<OrderSummaryResponse> orders
) {

    public static OrdersResponse of(final Page<OrderInfo> orders) {
        final List<OrderSummaryResponse> orderResponses = orders.get()
                .map(OrderSummaryResponse::of)
                .toList();
        return new OrdersResponse(PageResponse.of(orders), orderResponses);
    }
}
