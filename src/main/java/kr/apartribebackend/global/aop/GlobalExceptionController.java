package kr.apartribebackend.global.aop;


import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.ErrorResponse;
import kr.apartribebackend.global.exception.RootException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> bindingResultException(final MethodArgumentNotValidException exception) {
        final Map<String, String> fieldValidation = !exception.hasFieldErrors() ? Map.of() : exception
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        Objects.requireNonNull(DefaultMessageSourceResolvable::getDefaultMessage)));

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

}
