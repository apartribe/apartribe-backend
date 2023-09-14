package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class NicknameDuplicateException extends RootException {

    public NicknameDuplicateException() {
        super("해당 이메일로 가입된 아이디가 존재합니다.");
    }
}
