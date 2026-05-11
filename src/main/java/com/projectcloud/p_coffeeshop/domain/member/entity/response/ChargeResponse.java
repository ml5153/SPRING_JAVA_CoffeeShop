package com.projectcloud.p_coffeeshop.domain.member.entity.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChargeResponse {
    private Long memberId;
    private Long balance;
}

