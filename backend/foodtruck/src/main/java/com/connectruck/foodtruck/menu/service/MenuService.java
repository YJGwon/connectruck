package com.connectruck.foodtruck.menu.service;

import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.menu.domain.MenuRepository;
import com.connectruck.foodtruck.menu.dto.MenusResponse;
import com.connectruck.foodtruck.truck.domain.TruckRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final TruckRepository truckRepository;

    public MenusResponse findByTruckId(final Long truckId) {
        final List<Menu> menus = menuRepository.findByTruckId(truckId);
        return MenusResponse.of(menus);
    }

    public MenusResponse findByOwnerId(final Long ownerId) {
        final Long truckId = getTruckIdByOwnerId(ownerId);
        return findByTruckId(truckId);
    }

    private Long getTruckIdByOwnerId(Long ownerId) {
        return truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("소유한 푸드트럭", "ownerId", ownerId))
                .getId();
    }
}
