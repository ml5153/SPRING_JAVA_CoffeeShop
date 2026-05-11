package com.projectcloud.p_coffeeshop.domain.member.controller;

import com.projectcloud.p_coffeeshop.domain.member.entity.request.ChargeRequest;
import com.projectcloud.p_coffeeshop.domain.member.entity.response.ChargeResponse;
import com.projectcloud.p_coffeeshop.domain.member.service.MemberService;
import com.projectcloud.p_coffeeshop.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/{memberId}/charge")
    public ResponseEntity<ApiResponse<ChargeResponse>> charge(
            @PathVariable Long memberId,
            @RequestBody @Valid ChargeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(memberService.charge(memberId, request)));
    }
}
