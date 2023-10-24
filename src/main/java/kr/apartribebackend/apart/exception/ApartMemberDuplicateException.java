package kr.apartribebackend.apart.exception;

import kr.apartribebackend.global.exception.RootException;

public class ApartMemberDuplicateException extends RootException {

    public ApartMemberDuplicateException() {
        super("해당 회원은 다른 아파트에 소속되어있어 아파트를 인증 및 등록할 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
