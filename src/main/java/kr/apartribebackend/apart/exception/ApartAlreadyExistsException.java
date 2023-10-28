package kr.apartribebackend.apart.exception;

import kr.apartribebackend.global.exception.RootException;

public class ApartAlreadyExistsException extends RootException {

    public ApartAlreadyExistsException() {
        super("해당 아파트에 대한 커뮤니티가 이미 존재하여 커뮤니티를 생성할 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
