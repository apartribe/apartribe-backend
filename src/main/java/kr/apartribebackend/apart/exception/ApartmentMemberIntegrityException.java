package kr.apartribebackend.apart.exception;

import kr.apartribebackend.global.exception.RootException;

public class ApartmentMemberIntegrityException extends RootException {

    public ApartmentMemberIntegrityException() {
        super("사전에 인증한 아파트 정보와 일치하지 않습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
