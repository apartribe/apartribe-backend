package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class InvalidEmailTokenForEmailException extends RootException {

    public InvalidEmailTokenForEmailException() {
        super("해당 이메일로 인증한 인증코드와 요청으로 보낸 이메일 인증코드가 일치하지 않습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
