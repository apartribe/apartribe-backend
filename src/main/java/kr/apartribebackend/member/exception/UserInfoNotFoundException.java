package kr.apartribebackend.member.exception;


import kr.apartribebackend.global.exception.RootException;

public class UserInfoNotFoundException extends RootException {

    public UserInfoNotFoundException() {
        super("해당 정보와 일치하는 계정정보를 찾을 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
