package kr.apartribebackend.advertise.exception;

import kr.apartribebackend.global.exception.RootException;

public class AdvertiseIntegrityException extends RootException {

    public AdvertiseIntegrityException() {
        super("광고/제휴 문의는 인증 후 사용할 수 있습니다.");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
