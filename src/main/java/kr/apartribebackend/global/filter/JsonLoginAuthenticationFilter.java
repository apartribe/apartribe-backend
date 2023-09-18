package kr.apartribebackend.global.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.apartribebackend.global.dto.LoginReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Slf4j
public class JsonLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String BASE_LOGIN_URL = "/api/auth/login";

    private final ObjectMapper objectMapper;

    public JsonLoginAuthenticationFilter(ObjectMapper objectMapper) {
        super(BASE_LOGIN_URL);
        this.objectMapper = objectMapper;
    }

    @Override
        public Authentication attemptAuthentication(HttpServletRequest request,
                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
            log.info("JsonLoginAuthenticationFilter Triggered");
            final LoginReq loginRequest = objectMapper.readValue(request.getInputStream(), LoginReq.class);
            final UsernamePasswordAuthenticationToken unauthenticatedToken =
                    UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.email(), loginRequest.password());
            return getAuthenticationManager().authenticate(unauthenticatedToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("JsonLoginAuthenticationFilter Success Authentication");
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info("JsonLoginAuthenticationFilter Failure Authentication");
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
