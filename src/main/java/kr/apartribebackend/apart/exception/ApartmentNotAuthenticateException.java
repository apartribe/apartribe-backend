package kr.apartribebackend.apart.exception;

import kr.apartribebackend.global.exception.RootException;

public class ApartmentNotAuthenticateException extends RootException {

    public ApartmentNotAuthenticateException() {
        super("해당 사용자는 아파트를 인증하지 않아 커뮤니티를 생성할 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
