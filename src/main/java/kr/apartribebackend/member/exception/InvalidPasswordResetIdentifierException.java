package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class InvalidPasswordResetIdentifierException extends RootException {

    public InvalidPasswordResetIdentifierException() {
        super("비밀번호를 찾으려는 사용자의 정보와 일치하지 않습니다.");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
