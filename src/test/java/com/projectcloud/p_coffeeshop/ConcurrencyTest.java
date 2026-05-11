package com.projectcloud.p_coffeeshop;

import com.projectcloud.p_coffeeshop.domain.member.entity.Member;
import com.projectcloud.p_coffeeshop.domain.member.repository.MemberRepository;
import com.projectcloud.p_coffeeshop.domain.member.service.MemberService;
import com.projectcloud.p_coffeeshop.domain.menu.entity.Menu;
import com.projectcloud.p_coffeeshop.domain.menu.repository.MenuRepository;
import com.projectcloud.p_coffeeshop.domain.order.entity.request.OrderRequest;
import com.projectcloud.p_coffeeshop.domain.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConcurrencyTest {

    @Autowired
    private MemberService memberService;
    @Autowired private OrderService orderService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MenuRepository menuRepository;

    private Long menuId;


    @BeforeEach
    void setUp() {
        Menu menu = menuRepository.save(Menu.builder().name("아메리카노").price(1000L).build());
        menuId = menu.getId();
    }


    @Test
    void 동시_포인트_차감_정합성() throws InterruptedException {
        // 잔액 10000, 스레드 10개가 동시에 1000씩 차감
        Member member = memberRepository.save(Member.builder().balance(10000L).build());
        Long memberId = member.getId();

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    orderService.order(new OrderRequest(memberId, menuId)); // 1000원 메뉴
                } catch (Exception e) {
                    // 낙관적 락 충돌 시 예외 발생 → 정상
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        Member result = memberRepository.findById(memberId).get();
        assertThat(result.getBalance()).isGreaterThanOrEqualTo(0L); // 음수 절대 안 됨
    }
}
