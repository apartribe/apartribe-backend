package kr.apartribebackend.global.aop;


import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.ErrorResponse;
import kr.apartribebackend.global.exception.JwtCustomException;
import kr.apartribebackend.global.exception.RootException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> bindingResultException(final MethodArgumentNotValidException exception) {
        final Map<String, String> fieldValidation = new HashMap<>();
        if (exception.hasFieldErrors()) {
            final List<FieldError> fieldErrors = exception.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                final String field = fieldError.getField();
                final String defaultMessage = fieldError.getDefaultMessage();
                fieldValidation.put(field, defaultMessage);
            }
        }
        final ErrorResponse errorResponse = ErrorResponse.BAD_REQUEST(fieldValidation);
        final APIResponse<ErrorResponse> apiResponse = APIResponse.VALID_ERROR(errorResponse);

        return ResponseEntity
                .status(errorResponse.code())
                .body(apiResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> httpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        final ErrorResponse errorResponse =
                ErrorResponse.BAD_REQUEST("요청 파싱 오류. 정확한 JSON 을 전달해주세요.");
        final APIResponse<ErrorResponse> apiResponse = APIResponse.ERROR(errorResponse);

        return ResponseEntity
                .status(errorResponse.code())
                .body(apiResponse);
    }

    @ExceptionHandler(RootException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> rootException(final RootException exception) {
        final String exceptionMessage = exception.getMessage();
        final int statusCode = exception.getStatusCode();
        final ErrorResponse errorResponse = ErrorResponse.of(statusCode, exceptionMessage);

        log.info("{} --> {}", exception.getClass().getSimpleName(), exception.getLocalizedMessage());
        final APIResponse<ErrorResponse> apiResponse = APIResponse.ERROR(errorResponse);

        return ResponseEntity
                .status(errorResponse.code())
                .body(apiResponse);
    }

    @ExceptionHandler(JwtCustomException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> jwtCustomException(final JwtCustomException exception) {
        final String exceptionName = exception.getJwtException().getClass().getSimpleName();
        final ErrorResponse errorResponse;
        switch (exceptionName) {
            case "ExpiredJwtException" -> errorResponse = ErrorResponse.of(401, "토큰 만료.");
            case "SignatureException" -> errorResponse = ErrorResponse.of(401, "토큰 서명 검증 실패.");
            default -> errorResponse = ErrorResponse.of(401, "토큰 에러");
        }
        final APIResponse<ErrorResponse> apiResponse = APIResponse.ERROR(errorResponse);
        return ResponseEntity
                .status(errorResponse.code())
                .body(apiResponse);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<APIResponse<ErrorResponse>> authenticationException(final AuthenticationException exception) {
        ErrorResponse errorResponse = ErrorResponse.of(401, "아이디 혹은 패스워드가 잘못되었습니다.");
        APIResponse<ErrorResponse> apiResponse = APIResponse.ERROR(errorResponse);
        return ResponseEntity
                .status(errorResponse.code())
                .body(apiResponse);
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> invalidContentTypeException(final InvalidContentTypeException exception) {
        ErrorResponse errorResponse = ErrorResponse.of(400, "잘못된 Content-Type 요청입니다.");
        APIResponse<ErrorResponse> apiResponse = APIResponse.ERROR(errorResponse);
        return ResponseEntity
                .status(errorResponse.code())
                .body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> methodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        ErrorResponse errorResponse = ErrorResponse.of(400, "잘못된 요청입니다.");
        APIResponse<ErrorResponse> apiResponse = APIResponse.ERROR(errorResponse);
        return ResponseEntity
                .status(errorResponse.code())
                .body(apiResponse);
    }


}
