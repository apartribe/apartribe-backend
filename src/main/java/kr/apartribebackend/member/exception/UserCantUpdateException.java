package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class UserCantUpdateException extends RootException {

    public UserCantUpdateException() {
        super("사용자를 찾을 수 없어 사용자 정보를 업데이트하지 못했습니다.");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
