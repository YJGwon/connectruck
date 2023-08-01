package com.connectruck.foodtruck.menu.service;

import com.connectruck.foodtruck.common.exception.ClientException;
import com.connectruck.foodtruck.common.exception.NotFoundException;
import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.menu.domain.MenuRepository;
import com.connectruck.foodtruck.menu.dto.MenuDescriptionRequest;
import com.connectruck.foodtruck.menu.dto.MenuSoldOutRequest;
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

    @Transactional
    public void updateDescription(final MenuDescriptionRequest request, final Long menuId, final Long ownerId) {
        final Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> NotFoundException.of("메뉴", "menuId", menuId));

        checkOwnerOfMenu(ownerId, menu);

        menu.changeDescription(request.description());
    }

    @Transactional
    public void updateSoldOut(final MenuSoldOutRequest request, final Long menuId, final Long ownerId) {
        final Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> NotFoundException.of("메뉴", "menuId", menuId));

        checkOwnerOfMenu(ownerId, menu);

        menu.changeSoldOut(request.soldOut());
    }

    private void checkOwnerOfMenu(final Long ownerId, final Menu menu) {
        final Long truckId = getTruckIdByOwnerId(ownerId);
        if (!menu.isTruckId(truckId)) {
            throw new ClientException("소유하지 않은 푸드트럭의 메뉴입니다.", "소유하지 않은 푸드트럭의 메뉴를 처리할 수 없습니다.");
        }
    }

    private Long getTruckIdByOwnerId(Long ownerId) {
        return truckRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> NotFoundException.of("소유한 푸드트럭", "ownerId", ownerId))
                .getId();
    }
}
