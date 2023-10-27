package kr.apartribebackend.advertise.exception;

import kr.apartribebackend.global.exception.RootException;

public class AdvertiseExpiredTokenException extends RootException {

    public AdvertiseExpiredTokenException() {
        super("광고/제휴 문의 인증코드가 일정시간이 지나 만료되었습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
