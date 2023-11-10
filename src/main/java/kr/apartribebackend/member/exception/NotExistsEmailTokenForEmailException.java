package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class NotExistsEmailTokenForEmailException extends RootException {

    public NotExistsEmailTokenForEmailException() {
        super("해당 이메일은 인증되지 않았습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
