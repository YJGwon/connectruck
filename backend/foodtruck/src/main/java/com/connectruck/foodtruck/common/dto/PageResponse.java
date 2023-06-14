package com.connectruck.foodtruck.common.dto;

import org.springframework.data.domain.Slice;

public record PageResponse(
        int size,
        int currentPage,
        boolean hasNext
) {

    public static PageResponse of(final Slice<?> slice) {
        return new PageResponse(slice.getSize(), slice.getNumber(), slice.hasNext());
    }
}
