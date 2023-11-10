package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class ExpiredResetPasswordLinkException extends RootException {

    public ExpiredResetPasswordLinkException() {
        super("비밀번호 재설정 링크가 만료되었습니다.");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
