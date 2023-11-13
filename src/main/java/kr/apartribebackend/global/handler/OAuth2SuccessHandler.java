package kr.apartribebackend.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.apartribebackend.global.service.JwtService;
import kr.apartribebackend.global.utils.ClientRedirectUrlHolder;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.principal.oauth2.OAuth2UserInfo;
import kr.apartribebackend.token.refresh.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ClientRedirectUrlHolder clientRedirectUrlHolder;
    private final JwtService jwtService;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        final OAuth2UserInfo oAuth2UserInfo  = (OAuth2UserInfo) authentication.getPrincipal();
        generateTokenEntriesAndRedirectToUrl(oAuth2UserInfo, response);
    }

    private void generateTokenEntriesAndRedirectToUrl(OAuth2UserInfo oAuth2UserInfo,
                                                      HttpServletResponse response) throws IOException {
        final String accessToken = jwtService.generateAccessToken(
                oAuth2UserInfo.getNickname(),
                Map.of(
                        "email", oAuth2UserInfo.getEmail(),
                        "role", "추가해야함",
                        "memberType", oAuth2UserInfo.getDetails().getMemberType()
                )
        );
        final String refreshToken = jwtService.generateRefreshToken(oAuth2UserInfo.getNickname());
        final RefreshToken refToken = RefreshToken.builder().token(refreshToken).build();
        final Member member = oAuth2UserInfo.getDetails();
        if (member.getRefreshToken() != null) {
            member.changeRefreshTokenValue(refToken);
        } else {
            member.changeRefreshToken(refToken);
        }
        retrieveUriComponentAndSendRedirect(clientRedirectUrlHolder, accessToken, refreshToken, response);
    }

    private void retrieveUriComponentAndSendRedirect(ClientRedirectUrlHolder clientRedirectUrlHolder,
                                                     String accessToken,
                                                     String refreshToken,
                                                     HttpServletResponse response) throws IOException {
        final String uriString = UriComponentsBuilder.fromUriString(clientRedirectUrlHolder.getRedirectUrl())
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .toUriString();

        response.sendRedirect(uriString);
    }

}
