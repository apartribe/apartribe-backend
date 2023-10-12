package kr.apartribebackend.apart.exception;

import kr.apartribebackend.global.exception.RootException;

public class ApartAlreadyExistsException extends RootException {

    public ApartAlreadyExistsException() {
        super("아파트가 이미 존재합니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
