package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class UserCantDeleteException extends RootException {
    public UserCantDeleteException() {
        super("사용자를 찾을 수 없어 사용자를 삭제하지 못했습니다.");
    }
}
