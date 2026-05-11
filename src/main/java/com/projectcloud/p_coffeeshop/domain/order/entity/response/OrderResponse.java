package com.projectcloud.p_coffeeshop.domain.order.entity.response;

import com.projectcloud.p_coffeeshop.domain.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private Long memberId;
    private Long menuId;
    private String menuName;
    private Long amount;
    private Long remainBalance;
    private LocalDateTime orderedAt;

    public static OrderResponse from(Order order, Long remainBalance) {
        return new OrderResponse(
                order.getId(),
                order.getMember().getId(),
                order.getMenu().getId(),
                order.getMenu().getName(),
                order.getAmount(),
                remainBalance,
                order.getOrderedAt()
        );
    }
}
