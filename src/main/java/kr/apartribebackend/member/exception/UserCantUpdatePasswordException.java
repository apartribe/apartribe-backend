package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class UserCantUpdatePasswordException extends RootException {

    public UserCantUpdatePasswordException() {
        super("일치하는 회원정보가 없어 비밀번호를 변경하지 못했습니다.");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
