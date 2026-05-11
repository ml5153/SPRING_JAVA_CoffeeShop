package com.projectcloud.p_coffeeshop.domain.member.service;

import com.projectcloud.p_coffeeshop.domain.member.entity.Member;
import com.projectcloud.p_coffeeshop.domain.member.entity.request.ChargeRequest;
import com.projectcloud.p_coffeeshop.domain.member.entity.response.ChargeResponse;
import com.projectcloud.p_coffeeshop.domain.member.repository.MemberRepository;
import com.projectcloud.p_coffeeshop.global.error.CommonError;
import com.projectcloud.p_coffeeshop.global.error.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public ChargeResponse charge(Long memberId, ChargeRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(CommonError.USER_NOT_FOUND));

        member.charge(request.getAmount());

        return new ChargeResponse(member.getId(), member.getBalance());
    }
}