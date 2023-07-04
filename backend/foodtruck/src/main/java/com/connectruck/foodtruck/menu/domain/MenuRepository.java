package com.connectruck.foodtruck.menu.domain;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface MenuRepository extends Repository<Menu, Long> {

    List<Menu> findByParticipationId(final long participationId);
}
