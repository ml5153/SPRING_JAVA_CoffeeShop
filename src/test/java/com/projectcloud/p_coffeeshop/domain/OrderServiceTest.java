package com.projectcloud.p_coffeeshop.domain;

import com.projectcloud.p_coffeeshop.domain.member.entity.Member;
import com.projectcloud.p_coffeeshop.domain.member.repository.MemberRepository;
import com.projectcloud.p_coffeeshop.domain.menu.entity.Menu;
import com.projectcloud.p_coffeeshop.domain.menu.repository.MenuRepository;
import com.projectcloud.p_coffeeshop.domain.order.entity.request.OrderRequest;
import com.projectcloud.p_coffeeshop.domain.order.entity.response.OrderResponse;
import com.projectcloud.p_coffeeshop.domain.order.repository.OrderRepository;
import com.projectcloud.p_coffeeshop.domain.order.service.OrderService;
import com.projectcloud.p_coffeeshop.global.client.DataPlatformClient;
import com.projectcloud.p_coffeeshop.global.error.CommonException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock private MemberRepository memberRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private DataPlatformClient dataPlatformClient;

    @Test
    void 주문_성공() {
        Member member = Member.builder().balance(10000L).build();
        Menu menu = Menu.builder().name("아메리카노").price(3000L).build();

        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));

        OrderResponse response = orderService.order(new OrderRequest(1L, 1L));

        assertThat(response.getAmount()).isEqualTo(3000L);
        assertThat(response.getRemainBalance()).isEqualTo(7000L);
        verify(dataPlatformClient).send(any(), any(), any());
    }

    @Test
    void 잔액_부족_주문_실패() {
        Member member = Member.builder().balance(1000L).build();
        Menu menu = Menu.builder().name("아메리카노").price(3000L).build();

        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(menuRepository.findById(1L)).willReturn(Optional.of(menu));

        assertThatThrownBy(() -> orderService.order(new OrderRequest(1L, 1L)))
                .isInstanceOf(CommonException.class);
    }

    @Test
    void 존재하지_않는_메뉴_주문_실패() {
        Member member = Member.builder().balance(10000L).build();
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(menuRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.order(new OrderRequest(1L, 999L)))
                .isInstanceOf(CommonException.class);
    }
}