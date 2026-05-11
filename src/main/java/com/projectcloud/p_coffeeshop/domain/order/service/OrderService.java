package com.projectcloud.p_coffeeshop.domain.order.service;

import com.projectcloud.p_coffeeshop.domain.member.entity.Member;
import com.projectcloud.p_coffeeshop.domain.member.repository.MemberRepository;
import com.projectcloud.p_coffeeshop.domain.menu.entity.Menu;
import com.projectcloud.p_coffeeshop.domain.menu.repository.MenuRepository;
import com.projectcloud.p_coffeeshop.domain.order.entity.Order;
import com.projectcloud.p_coffeeshop.domain.order.entity.request.OrderRequest;
import com.projectcloud.p_coffeeshop.domain.order.entity.response.OrderResponse;
import com.projectcloud.p_coffeeshop.domain.order.repository.OrderRepository;
import com.projectcloud.p_coffeeshop.global.client.DataPlatformClient;
import com.projectcloud.p_coffeeshop.global.error.CommonError;
import com.projectcloud.p_coffeeshop.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final DataPlatformClient dataPlatformClient;

    @Transactional
    public OrderResponse order(OrderRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new CommonException(CommonError.USER_NOT_FOUND));

        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new CommonException(CommonError.CUSTOMER_NOT_FOUND));

        member.deduct(menu.getPrice());

        Order order = Order.builder()
                .member(member)
                .menu(menu)
                .amount(menu.getPrice())
                .orderedAt(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        // 데이터 플랫폼 실시간 전송 (Mock)
        dataPlatformClient.send(member.getId(), menu.getId(), menu.getPrice());

        return OrderResponse.from(order, member.getBalance());
    }
}