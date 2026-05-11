package com.projectcloud.p_coffeeshop.domain.order.entity.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = "사용자 ID는 필수입니다")
    private Long memberId;

    @NotNull(message = "메뉴 ID는 필수입니다")
    private Long menuId;

    public OrderRequest(Long memberId, Long menuId) {
        this.memberId = memberId;
        this.menuId = menuId;
    }
}
