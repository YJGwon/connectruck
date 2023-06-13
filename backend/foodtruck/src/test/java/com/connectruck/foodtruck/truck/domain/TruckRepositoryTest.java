package com.connectruck.foodtruck.truck.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("classpath:truncate.sql")
public class TruckRepositoryTest {

    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private TestTruckRepository testTruckRepository;

    @DisplayName("모든 푸드트럭에 대해 페이지 단위로 조회한다.")
    @Test
    void findAll_perPage() {
        // given
        saveTruck();
        saveTruck();
        saveTruck();

        // when
        final int page = 0;
        final int size = 2;
        final Slice<Truck> foundPage = truckRepository.findAll(PageRequest.of(page, size));

        // then
        assertThat(foundPage.getContent()).hasSize(size);
    }

    private Truck saveTruck() {
        final Truck truck = Truck.ofNewWithNoThumbnail(
                "핫도그쿨냥이",
                "서울 마포구 성산동 509-7",
                LocalTime.of(11, 0),
                LocalTime.of(21, 0)
        );
        return testTruckRepository.save(truck);
    }
}
