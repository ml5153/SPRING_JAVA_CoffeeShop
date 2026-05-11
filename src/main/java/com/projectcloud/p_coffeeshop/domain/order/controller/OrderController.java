package com.projectcloud.p_coffeeshop.domain.order.controller;

import com.projectcloud.p_coffeeshop.domain.order.entity.request.OrderRequest;
import com.projectcloud.p_coffeeshop.domain.order.entity.response.OrderResponse;
import com.projectcloud.p_coffeeshop.domain.order.service.OrderService;
import com.projectcloud.p_coffeeshop.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> order(
            @RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(ApiResponse.success(orderService.order(request)));
    }
}
