package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class MalformedProfileImageLinkException extends RootException {

    public MalformedProfileImageLinkException() {
        super("악의적인 혹은 비정상적인 문자가 포함되어있습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
