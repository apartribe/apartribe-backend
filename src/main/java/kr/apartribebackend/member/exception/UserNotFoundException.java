package kr.apartribebackend.member.exception;


import kr.apartribebackend.global.exception.RootException;

public class UserNotFoundException extends RootException {

    public UserNotFoundException() {
        super("사용자를 찾을 수 없습니다");
    }

}
