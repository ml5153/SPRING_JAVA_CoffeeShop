package com.projectcloud.p_coffeeshop.global.client;

import org.springframework.stereotype.Component;

@Component
public class DataPlatformClient {

    public void send(Long memberId, Long menuId, Long amount) {
        // Mock - 실제 외부 전송 로직 대신 로그로 대체
        System.out.println("[DataPlatform] memberId=" + memberId + ", menuId=" + menuId + ", amount=" + amount);
    }
}