package kr.apartribebackend.apart.exception;

import kr.apartribebackend.global.exception.RootException;

public class ApartNonExistsException extends RootException {

    public ApartNonExistsException() {
        super("아파트 정보를 찾을 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
