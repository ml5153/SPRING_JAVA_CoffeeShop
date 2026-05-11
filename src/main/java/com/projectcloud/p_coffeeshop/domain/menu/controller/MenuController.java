package com.projectcloud.p_coffeeshop.domain.menu.controller;

import com.projectcloud.p_coffeeshop.domain.menu.entity.response.MenuResponse;
import com.projectcloud.p_coffeeshop.domain.menu.entity.response.PopularMenuResponse;
import com.projectcloud.p_coffeeshop.domain.menu.service.MenuService;
import com.projectcloud.p_coffeeshop.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getMenus() {
        return ResponseEntity.ok(ApiResponse.success(menuService.getMenus()));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<PopularMenuResponse>>> getPopularMenus() {
        return ResponseEntity.ok(ApiResponse.success(menuService.getPopularMenus()));
    }
}