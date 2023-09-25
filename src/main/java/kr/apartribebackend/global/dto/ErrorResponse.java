package kr.apartribebackend.global.dto;


import java.util.HashMap;
import java.util.Map;

import static kr.apartribebackend.global.dto.HttpStatusCode.*;


public record ErrorResponse (
        int code,
        String error,
        Map<String, String> validation
){

    public static ErrorResponse NOT_FOUND(String error) {
        return ErrorResponse.of(NOT_FOUND, error);
    }

    public static ErrorResponse SERVER_ERROR(String error) {
        return ErrorResponse.of(SERVER_ERROR, error);
    }

    public static ErrorResponse BAD_REQUEST(String error) {
        return ErrorResponse.of(BAD_REQUEST, error);
    }

    public static ErrorResponse BAD_REQUEST(Map<String, String> validation) {
        return ErrorResponse.of(BAD_REQUEST, validation);
    }

    public static ErrorResponse of(int code, String error) {
        return ErrorResponse.of(code, error, new HashMap<>());
    }

    public static ErrorResponse of(int code, Map<String, String> validation) {
        return ErrorResponse.of(code, "", validation);
    }

    public static ErrorResponse of(int code, String error, Map<String, String> validation) {
        return new ErrorResponse(code, error, validation);
    }

}
