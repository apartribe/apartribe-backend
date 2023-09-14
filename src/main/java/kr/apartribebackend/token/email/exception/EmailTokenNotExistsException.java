package kr.apartribebackend.token.email.exception;

import kr.apartribebackend.global.exception.RootException;

public class EmailTokenNotExistsException extends RootException {

    public EmailTokenNotExistsException() {
        super("이메일과 일치하는 토큰이 존재하지 않습니다.");
    }

}

