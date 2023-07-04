package com.connectruck.foodtruck.menu.service;

import com.connectruck.foodtruck.menu.domain.Menu;
import com.connectruck.foodtruck.menu.domain.MenuRepository;
import com.connectruck.foodtruck.menu.dto.MenusResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public MenusResponse findByParticipationId(final Long participationId) {
        final List<Menu> menus = menuRepository.findByParticipationId(participationId);
        return MenusResponse.of(menus);
    }
}
