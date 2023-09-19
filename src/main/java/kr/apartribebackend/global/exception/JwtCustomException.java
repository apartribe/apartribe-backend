package kr.apartribebackend.global.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.security.core.AuthenticationException;

public class JwtCustomException extends AuthenticationException {

    private JwtException jwtException;

    public JwtCustomException(JwtException jwtException) {
        super(jwtException.getLocalizedMessage());
        this.jwtException = jwtException;
    }

    public JwtException getJwtException() {
        return jwtException;
    }
}
