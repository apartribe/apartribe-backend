package kr.apartribebackend.apart.exception;

import kr.apartribebackend.global.exception.RootException;

public class NeedApartmentAuthException extends RootException {

    public NeedApartmentAuthException() {
        super("해당 endpoint 는 apart 인증이 완료된 사용자만 이용 가능합니다.");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
