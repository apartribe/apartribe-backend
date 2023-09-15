package kr.apartribebackend.token.email.exception;

import kr.apartribebackend.global.exception.RootException;

public class NotAuthenticatedEmailException extends RootException {
    
    public NotAuthenticatedEmailException() {
        super("인증되지않은 이메일입니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
