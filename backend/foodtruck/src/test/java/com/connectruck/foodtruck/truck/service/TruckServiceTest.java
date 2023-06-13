package com.connectruck.foodtruck.truck.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.connectruck.foodtruck.fixture.DataSetup;
import com.connectruck.foodtruck.common.dto.PageResponse;
import com.connectruck.foodtruck.truck.dto.TrucksResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
class TruckServiceTest {

    private final TruckService truckService;
    private final DataSetup dataSetup;

    TruckServiceTest(@Autowired final TruckService truckService,
                     @Autowired final DataSetup dataSetup) {
        this.truckService = truckService;
        this.dataSetup = dataSetup;
    }

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
