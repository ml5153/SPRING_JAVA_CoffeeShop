package com.projectcloud.p_coffeeshop.global.config;

import com.projectcloud.p_coffeeshop.domain.menu.entity.Menu;
import com.projectcloud.p_coffeeshop.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final MenuRepository menuRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (menuRepository.count() == 0) {
            menuRepository.saveAll(List.of(
                    Menu.builder().name("아메리카노").price(3000L).build(),
                    Menu.builder().name("카페라떼").price(4000L).build(),
                    Menu.builder().name("바닐라라떼").price(4500L).build(),
                    Menu.builder().name("카푸치노").price(4000L).build(),
                    Menu.builder().name("에스프레소").price(2500L).build()
            ));
        }
    }
}