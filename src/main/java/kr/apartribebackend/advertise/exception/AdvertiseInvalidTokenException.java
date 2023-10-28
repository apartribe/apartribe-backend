package kr.apartribebackend.advertise.exception;

import kr.apartribebackend.global.exception.RootException;

public class AdvertiseInvalidTokenException extends RootException {

    public AdvertiseInvalidTokenException() {
        super("인증코드가 일치하지 않습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
