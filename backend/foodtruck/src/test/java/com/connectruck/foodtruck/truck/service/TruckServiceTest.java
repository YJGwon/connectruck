package com.connectruck.foodtruck.truck.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.common.testbase.ServiceTestBase;
import com.connectruck.foodtruck.truck.dto.TrucksResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TruckServiceTest extends ServiceTestBase {

    @Autowired
    private TruckService truckService;

    @DisplayName("모든 푸드트럭에 대해 페이지 단위로 조회한다.")
    @Test
    void findAll() {
        // given
        dataSetup.saveTruck();
        dataSetup.saveTruck();
        dataSetup.saveTruck();

        // when
        final int page = 0;
        final int size = 2;
        final TrucksResponse response = truckService.findAll(page, size);

        // then
        assertAll(
                () -> assertThat(response.trucks()).hasSize(2),
                () -> assertThat(response.page()).isEqualTo(new PageResponse(size, page, true))
        );
    }
}
