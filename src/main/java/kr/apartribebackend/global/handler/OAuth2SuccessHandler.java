package kr.apartribebackend.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.apartribebackend.global.utils.ClientRedirectUrlHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Slf4j @RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ClientRedirectUrlHolder clientRedirectUrlHolder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        final OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        final String redirectUriComponentString = retrieveUriComponent(clientRedirectUrlHolder);
        response.sendRedirect(redirectUriComponentString);
    }

    private String retrieveUriComponent(ClientRedirectUrlHolder clientRedirectUrlHolder) {
        return UriComponentsBuilder.fromUriString(clientRedirectUrlHolder.getRedirectUrl())
                .queryParam("accessToken", UUID.randomUUID().toString())
                .queryParam("refreshToken", UUID.randomUUID().toString())
                .toUriString();
    }

}
