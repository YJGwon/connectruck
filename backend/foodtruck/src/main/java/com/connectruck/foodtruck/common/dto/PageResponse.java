package com.connectruck.foodtruck.common.dto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public record PageResponse(
        int size,
        int totalPages,
        int currentPage,
        boolean hasNext
) {

    public static PageResponse of(final Page<?> page) {
        return new PageResponse(page.getSize(), page.getTotalPages(), page.getNumber(), page.hasNext());
    }

    public static PageResponse of(final Slice<?> slice) {
        return new PageResponse(slice.getSize(), 0, slice.getNumber(), slice.hasNext());
    }
}
