package kr.apartribebackend.token.email.exception;

import kr.apartribebackend.global.exception.RootException;

public class EmailTokenDuplicateException extends RootException {

    public EmailTokenDuplicateException() {
        super("이미 사용된 토큰입니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
