package kr.apartribebackend.global.aop;


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
    public ResponseEntity<ErrorResponse> bindingResultException(final MethodArgumentNotValidException exception) {
        final Map<String, String> fieldValidation = !exception.hasFieldErrors() ? Map.of() : exception
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        Objects.requireNonNull(DefaultMessageSourceResolvable::getDefaultMessage)));

        final ErrorResponse errorResponse = ErrorResponse.of(fieldValidation);
        return ResponseEntity
                .status(errorResponse.code())
                .body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        final ErrorResponse errorResponse =
                ErrorResponse.BAD_REQUEST("요청 파싱 오류. 정확한 JSON 을 전달해주세요.");

        return ResponseEntity
                .status(errorResponse.code())
                .body(errorResponse);
    }

    @ExceptionHandler(RootException.class)
    public ResponseEntity<ErrorResponse> rootException(final RootException exception) {
        final String exceptionMessage = exception.getMessage();
        ErrorResponse errorResponse = ErrorResponse.NOT_FOUND(exceptionMessage);
        log.info("{} --> {}", exception.getClass().getSimpleName(), exception.getLocalizedMessage());

        return ResponseEntity
                .status(errorResponse.code())
                .body(errorResponse);
    }

}
