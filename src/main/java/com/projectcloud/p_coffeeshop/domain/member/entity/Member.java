package com.projectcloud.p_coffeeshop.domain.member.entity;

import com.projectcloud.p_coffeeshop.global.common.BaseEntity;
import com.projectcloud.p_coffeeshop.global.error.CommonError;
import com.projectcloud.p_coffeeshop.global.error.CommonException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Builder
    private Member(Long balance) {
        this.balance = balance;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long balance;

    @Version
    private Long version;

    public void charge(long amount) {
        this.balance += amount;
    }

    public void deduct(long amount) {
        if(this.balance < amount) {
            throw new CommonException(CommonError.INSUFFICIENT_BALANCE);
        }
        this.balance -= amount;
    }

}
