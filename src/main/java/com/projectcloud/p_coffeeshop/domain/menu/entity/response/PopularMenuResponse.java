package com.projectcloud.p_coffeeshop.domain.menu.entity.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopularMenuResponse {

    private int rank;
    private Long menuId;
    private String name;
    private Long price;
    private Long orderCount;
}