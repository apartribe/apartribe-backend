package kr.apartribebackend.apart.exception;

import kr.apartribebackend.global.exception.RootException;

public class ApartRequestNotMatchWithMemberApartException extends RootException {

    public ApartRequestNotMatchWithMemberApartException() {
        super("요청한 apart 정보와 회원이 속한 apart 정보가 일치하지 않습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
