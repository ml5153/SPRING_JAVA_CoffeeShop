package com.projectcloud.p_coffeeshop.domain.order.entity;

import com.projectcloud.p_coffeeshop.domain.member.entity.Member;
import com.projectcloud.p_coffeeshop.domain.menu.entity.Menu;
import com.projectcloud.p_coffeeshop.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Builder
    private Order(Member member, Menu menu, Long amount, LocalDateTime orderedAt) {
        this.member = member;
        this.menu = menu;
        this.amount = amount;
        this.orderedAt = orderedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private LocalDateTime orderedAt;

}
