package com.projectcloud.p_coffeeshop.global.common;

public record BaseErrorResponse(
        int status,
        String code,
        String errorMessage
) {}
