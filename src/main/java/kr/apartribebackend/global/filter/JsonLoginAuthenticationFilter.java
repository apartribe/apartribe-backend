package kr.apartribebackend.global.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.apartribebackend.global.dto.LoginReq;
import kr.apartribebackend.global.service.JwtService;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
        final String loginRequestString = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        final LoginReq loginReq = objectMapper.readValue(loginRequestString, LoginReq.class);
        log.info("loginReq = {}", loginReq);
        final UsernamePasswordAuthenticationToken unauthenticatedToken =
                    UsernamePasswordAuthenticationToken.unauthenticated(loginReq.email(), loginReq.password());
            return getAuthenticationManager().authenticate(unauthenticatedToken);
    }

}
