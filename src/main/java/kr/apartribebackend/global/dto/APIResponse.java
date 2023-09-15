package kr.apartribebackend.global.dto;

import java.time.LocalDateTime;

import static kr.apartribebackend.global.dto.APIStatus.*;

public record APIResponse<T> (
        String type,
        LocalDateTime issuedAt,
        T data
) {

    public static <T extends ErrorResponse> APIResponse<T> ERROR(T errorResponse) {
        return new APIResponse<>(ERROR, LocalDateTime.now(), errorResponse);
    }

    public static <T extends ErrorResponse> APIResponse<T> VALID_ERROR(T errorResponse) {
        return new APIResponse<>(VALID, LocalDateTime.now(), errorResponse);
    }

    public static <T> APIResponse<T> SUCCESS(T elements) {
        return new APIResponse<>(SUCCESS, LocalDateTime.now(), elements);
    }

}
