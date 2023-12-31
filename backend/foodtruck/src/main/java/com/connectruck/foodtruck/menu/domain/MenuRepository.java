package com.connectruck.foodtruck.menu.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MenuRepository extends Repository<Menu, Long> {

    Optional<Menu> findById(Long id);

    List<Menu> findByTruckId(Long truckId);
}
