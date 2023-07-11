package com.connectruck.foodtruck.owner.dto;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.order.domain.OrderInfo;
import java.util.List;
import org.springframework.data.domain.Page;

public record OwnerOrdersResponse(
        PageResponse page,
        List<OwnerOrderResponse> orders
) {

    public static OwnerOrdersResponse of(final Page<OrderInfo> orders) {
        final List<OwnerOrderResponse> orderResponses = orders.get()
                .map(OwnerOrderResponse::of)
                .toList();
        return new OwnerOrdersResponse(PageResponse.of(orders), orderResponses);
    }
}
