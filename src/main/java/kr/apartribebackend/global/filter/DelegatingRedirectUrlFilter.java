package kr.apartribebackend.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.apartribebackend.global.utils.ClientRedirectUrlHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Slf4j @RequiredArgsConstructor
public class DelegatingRedirectUrlFilter extends OncePerRequestFilter {

    private final ClientRedirectUrlHolder clientRedirectUrlHolder;

    private Set<String> oauth2RedirectPaths = Set.of("/oauth2/authorization/kakao");

    public void setOauth2RedirectPaths(Set<String> oauth2RedirectPaths) {
        this.oauth2RedirectPaths = oauth2RedirectPaths;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("DelegatingRedirectUrlFilter Triggered");
        retrieveRedirectUrl(request)
                .ifPresent(clientRedirectUrlHolder::setRedirectUrl);
        doFilter(request, response, filterChain);
    }

    private Optional<String> retrieveRedirectUrl(HttpServletRequest request) {
        if (isRequestFromOAuth2(request))
            return Optional.ofNullable(request.getParameter("redirect_url"));
        return Optional.empty();
    }

    private boolean isRequestFromOAuth2(HttpServletRequest request){
        return oauth2RedirectPaths
                .stream()
                .anyMatch(oauth2RedirectPaths -> request.getRequestURI().equals(oauth2RedirectPaths));
    }

}
