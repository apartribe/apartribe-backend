package kr.apartribebackend.global.controller;


import jakarta.servlet.http.HttpServletRequest;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
public class GlobalBasicErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> commonErrorHandlerMethod(final HttpServletRequest request) {
        final String requestUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");
        final String attribute = (String) request.getAttribute("jakarta.servlet.error.message");
        final Integer errorStatusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");

        final ErrorResponse errorResponse;
        final APIResponse<ErrorResponse> apiResponse;

        return switch (errorStatusCode) {
            case 404 ->  {
                errorResponse = ErrorResponse.NOT_FOUND(requestUri + " 엔드포인드를 찾을 수 없습니다.");
                apiResponse = APIResponse.ERROR(errorResponse);
                yield ResponseEntity.status(errorResponse.code()).body(apiResponse);
            }
            case 400 -> {
                errorResponse = ErrorResponse.BAD_REQUEST(requestUri + "잘못된 요청");
                apiResponse = APIResponse.ERROR(errorResponse);
                yield ResponseEntity.status(errorResponse.code()).body(apiResponse);
            }
            default -> {
                errorResponse = ErrorResponse.SERVER_ERROR(requestUri + " 애플리케이션 에러 발생");
                apiResponse = APIResponse.ERROR(errorResponse);
                yield ResponseEntity.status(errorResponse.code()).body(apiResponse);
            }
        };
    }

}
