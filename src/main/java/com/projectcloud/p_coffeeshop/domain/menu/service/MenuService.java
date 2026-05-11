package com.projectcloud.p_coffeeshop.domain.menu.service;

import com.projectcloud.p_coffeeshop.domain.menu.entity.response.MenuResponse;
import com.projectcloud.p_coffeeshop.domain.menu.entity.response.PopularMenuResponse;
import com.projectcloud.p_coffeeshop.domain.menu.repository.MenuRepository;
import com.projectcloud.p_coffeeshop.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;


    public List<MenuResponse> getMenus() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .toList();
    }

    public List<PopularMenuResponse> getPopularMenus() {
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        List<Object[]> results = orderRepository.findTop3MenusByOrderCount(from);

        List<PopularMenuResponse> popular = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            Object[] row = results.get(i);
            popular.add(new PopularMenuResponse(
                    i + 1,
                    (Long) row[0],
                    (String) row[1],
                    (Long) row[2],
                    (Long) row[3]
            ));
        }
        return popular;
    }
}
