package com.projectcloud.p_coffeeshop.global.common;


import com.projectcloud.p_coffeeshop.global.error.CommonError;

public record ApiResponse<T>(
        boolean success,       // 성공 여부 (true/false)
        T data,               // 성공 시 실제 데이터 (제네릭)
        BaseErrorResponse error   // 실패 시 에러 정보
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> fail(BaseErrorResponse errorResponse) {
        return new ApiResponse<>(false, null, errorResponse);
    }

    public static ApiResponse<Void> fail(CommonError error) {
        return fail(new BaseErrorResponse(
                error.getStatus().value(),
                error.getCode(),
                error.getMessage()
        ));
    }

    public static ApiResponse<Void> fail(int status, String code, String message) {
        return new ApiResponse<>(
                false, null, new BaseErrorResponse(status, code, message)
        );
    }
}