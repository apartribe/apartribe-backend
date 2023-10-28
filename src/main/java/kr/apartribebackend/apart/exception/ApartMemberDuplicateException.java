package kr.apartribebackend.apart.exception;

import kr.apartribebackend.global.exception.RootException;

public class ApartMemberDuplicateException extends RootException {

    public ApartMemberDuplicateException() {
        super("해당 회원은 이미 아파트에 소속되어있어 아파트 인증, 등록, 커뮤니티 생성을 할 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
