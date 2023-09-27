package kr.apartribebackend.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.apartribebackend.global.dto.TokenResponse;
import kr.apartribebackend.global.service.JwtService;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.token.refresh.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

// TODO 전체적인 리팩토리 필요
@Slf4j @RequiredArgsConstructor
public class JsonLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final ObjectMapper objectMapper;

    @Override @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("JsonLoginSuccessHandler Triggered");
        final AuthenticatedMember authenticatedMember = (AuthenticatedMember) authentication.getPrincipal();

        final String accessToken = jwtService.generateAccessToken(
                authenticatedMember.getUsername(), Map.of("email", authenticatedMember.getEmail(), "role", "추가해야함")
        );

        final String refToken = jwtService.generateRefreshToken(authenticatedMember.getUsername());
        final Member member = ((AuthenticatedMember) authentication.getPrincipal()).getOriginalEntity();
        final RefreshToken refreshToken = RefreshToken.builder()
                .token(refToken)
                .build();
        if (member.getRefreshToken() != null) {
            member.changeRefreshTokenValue(refreshToken);
        } else {
            member.changeRefreshToken(refreshToken);
        }

        final TokenResponse tokenResponse = TokenResponse.of(accessToken, refToken);
        final String tokenResponseJson = objectMapper.writeValueAsString(tokenResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().println(tokenResponseJson);
    }
}


//    final ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refToken)
//            .path("/")
//            .httpOnly(true)
//            .sameSite("None")
//            .secure(true)
//            .maxAge(60 * 60 * 24)
//            .build();

//    response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
//    response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + accessToken);