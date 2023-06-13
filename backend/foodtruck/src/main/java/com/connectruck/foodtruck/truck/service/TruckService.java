package com.connectruck.foodtruck.truck.service;

import com.connectruck.foodtruck.truck.domain.Truck;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import com.connectruck.foodtruck.truck.dto.TrucksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TruckService {

    private final TruckRepository truckRepository;

    public TrucksResponse findAll(final int page, final int size) {
        final Slice<Truck> trucks = truckRepository.findAll(PageRequest.of(page, size));
        return TrucksResponse.of(trucks);
    }
}
