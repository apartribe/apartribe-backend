package kr.apartribebackend.token.email.exception;

import kr.apartribebackend.global.exception.RootException;

public class EmailTokenInvalidException extends RootException {

    public EmailTokenInvalidException() {
        super("이메일 인증코드가 일치하지 않습니다.");
    }
}
