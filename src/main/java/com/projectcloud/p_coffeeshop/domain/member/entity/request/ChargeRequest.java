package com.projectcloud.p_coffeeshop.domain.member.entity.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChargeRequest {

    @NotNull(message = "충전 금액은 필수입니다")
    @Min(value = 1, message = "충전 금액은 1원 이상이어야 합니다")
    private Long amount;

    public ChargeRequest(Long amount) {
        this.amount = amount;
    }
}