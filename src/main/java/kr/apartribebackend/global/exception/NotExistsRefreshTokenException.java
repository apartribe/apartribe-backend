package kr.apartribebackend.global.exception;

import io.jsonwebtoken.JwtException;

public class NotExistsRefreshTokenException extends JwtException {

    public NotExistsRefreshTokenException() {
        super(null);
    }
}
