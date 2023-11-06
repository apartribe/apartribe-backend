package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class InvalidMemberInfoResetPasswordException extends RootException {

    public InvalidMemberInfoResetPasswordException() {
        super("존재하지 않는 비밀번호 재설정 식별자입니다.");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
