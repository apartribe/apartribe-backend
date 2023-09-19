package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class UserCantUpdateNicknameException extends RootException {

    public UserCantUpdateNicknameException() {
        super("중복된 닉네임이 존재합니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
