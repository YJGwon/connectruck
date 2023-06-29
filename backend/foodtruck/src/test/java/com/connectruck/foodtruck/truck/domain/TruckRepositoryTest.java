package com.connectruck.foodtruck.truck.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.connectruck.foodtruck.common.testbase.RepositoryTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

class TruckRepositoryTest extends RepositoryTestBase {

    @Autowired
    private TruckRepository truckRepository;

    @DisplayName("모든 푸드트럭에 대해 slice 단위로 조회한다.")
    @Test
    void findAllBy_perSlice() {
        // given
        dataSetup.saveTruck();
        dataSetup.saveTruck();
        dataSetup.saveTruck();

        // when
        final int page = 0;
        final int size = 2;
        final Slice<Truck> foundPage = truckRepository.findAllBy(PageRequest.of(page, size));

        // then
        assertThat(foundPage.getContent()).hasSize(size);
    }
}
