package com.connectruck.foodtruck.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public record PageResponse(
        @Schema(description = "페이지 크기", example = "20")
        int size,
        @Schema(description = "총 페이지 수", example = "2")
        int totalPages,
        @Schema(description = "현재 페이지", example = "0")
        int currentPage,
        @Schema(description = "다음 페이지 유무", example = "true")
        boolean hasNext
) {

    public static PageResponse of(final Page<?> page) {
        return new PageResponse(page.getSize(), page.getTotalPages(), page.getNumber(), page.hasNext());
    }

    public static PageResponse of(final Slice<?> slice) {
        return new PageResponse(slice.getSize(), 0, slice.getNumber(), slice.hasNext());
    }
}
