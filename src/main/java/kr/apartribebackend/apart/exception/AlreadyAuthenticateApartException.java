package kr.apartribebackend.apart.exception;

import kr.apartribebackend.global.exception.RootException;

public class AlreadyAuthenticateApartException extends RootException {

    public AlreadyAuthenticateApartException() {
        super("아파트를 이미 인증한 사용자입니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
