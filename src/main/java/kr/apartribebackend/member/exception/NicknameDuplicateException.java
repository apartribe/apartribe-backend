package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class NicknameDuplicateException extends RootException {

    public NicknameDuplicateException() {
        super("해당 닉네임으로 가입된 아이디가 존재합니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
