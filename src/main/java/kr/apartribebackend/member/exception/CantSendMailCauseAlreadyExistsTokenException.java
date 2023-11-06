package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class CantSendMailCauseAlreadyExistsTokenException extends RootException {

    public CantSendMailCauseAlreadyExistsTokenException() {
        super("해당 이메일로 가입된 아이디가 존재하여 이메일 발송을 하지 않았습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
