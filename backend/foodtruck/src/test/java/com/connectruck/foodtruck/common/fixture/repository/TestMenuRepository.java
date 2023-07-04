package com.connectruck.foodtruck.common.fixture.repository;

import com.connectruck.foodtruck.menu.domain.Menu;
import org.springframework.data.repository.Repository;

public interface TestMenuRepository extends Repository<Menu, Long> {


    Menu save(final Menu menu);
}
