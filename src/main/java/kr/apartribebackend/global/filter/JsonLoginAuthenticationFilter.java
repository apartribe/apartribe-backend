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
            final LoginReq loginRequest = objectMapper.readValue(request.getInputStream(), LoginReq.class);
            final UsernamePasswordAuthenticationToken unauthenticatedToken =
                    UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.email(), loginRequest.password());
            return getAuthenticationManager().authenticate(unauthenticatedToken);
    }

}
