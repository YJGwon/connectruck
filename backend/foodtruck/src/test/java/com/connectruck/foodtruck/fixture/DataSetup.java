package com.connectruck.foodtruck.fixture;

import com.connectruck.foodtruck.truck.domain.Truck;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class DataSetup {

    private final TestTruckRepository testTruckRepository;

    public DataSetup(final TestTruckRepository testTruckRepository) {
        this.testTruckRepository = testTruckRepository;
    }

    public Truck saveTruck() {
        final Truck truck = Truck.ofNewWithNoThumbnail(
                "핫도그쿨냥이",
                "서울 마포구 성산동 509-7",
                LocalTime.of(11, 0),
                LocalTime.of(21, 0)
        );
        return testTruckRepository.save(truck);
    }
}
