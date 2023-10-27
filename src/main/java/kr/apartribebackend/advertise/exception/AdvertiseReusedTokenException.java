package kr.apartribebackend.advertise.exception;

import kr.apartribebackend.global.exception.RootException;

public class AdvertiseReusedTokenException extends RootException {

    public AdvertiseReusedTokenException() {
        super("이미 인증된 토큰입니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
