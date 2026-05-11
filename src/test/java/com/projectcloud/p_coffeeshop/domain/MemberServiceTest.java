package com.projectcloud.p_coffeeshop.domain;

import com.projectcloud.p_coffeeshop.domain.member.entity.Member;
import com.projectcloud.p_coffeeshop.domain.member.entity.request.ChargeRequest;
import com.projectcloud.p_coffeeshop.domain.member.entity.response.ChargeResponse;
import com.projectcloud.p_coffeeshop.domain.member.repository.MemberRepository;
import com.projectcloud.p_coffeeshop.domain.member.service.MemberService;
import com.projectcloud.p_coffeeshop.global.error.CommonException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void 포인트_충전_성공() {
        Member member = Member.builder().balance(1000L).build();
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        ChargeResponse response = memberService.charge(1L, new ChargeRequest(5000L));

        assertThat(response.getBalance()).isEqualTo(6000L);
    }

    @Test
    void 존재하지_않는_유저_충전_실패() {
        given(memberRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.charge(999L, new ChargeRequest(5000L)))
                .isInstanceOf(CommonException.class);
    }
}