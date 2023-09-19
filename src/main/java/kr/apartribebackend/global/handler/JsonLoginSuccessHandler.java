package kr.apartribebackend.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.apartribebackend.global.service.JwtService;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.member.repository.MemberRepository;
import kr.apartribebackend.token.refresh.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

// TODO 전체적인 리팩토리 필요
@Slf4j @RequiredArgsConstructor
public class JsonLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String BEARER = "Bearer ";

    private final JwtService jwtService;

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("JsonLoginSuccessHandler Triggered");
        final AuthenticatedMember authenticatedMember = (AuthenticatedMember) authentication.getPrincipal();

        final String accessToken = jwtService.generateAccessToken(
//                authenticatedMember.getUsername(), Map.of("email", authenticatedMember.email())
                authenticatedMember.getUsername(), Map.of("email", authenticatedMember.getEmail(), "role", "추가해야함")
        );

        final String refreshToken = memberRepository.findRefreshTokenByEmail(authenticatedMember.email())
                .map(Member::getRefreshToken)
                .map(RefreshToken::getToken)
                .orElse(null);

        final ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + accessToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
