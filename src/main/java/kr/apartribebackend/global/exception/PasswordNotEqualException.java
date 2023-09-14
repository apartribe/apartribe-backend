package kr.apartribebackend.global.exception;

public class PasswordNotEqualException extends RootException{
    public PasswordNotEqualException() {
        super("패스워드와 패스워드확인이 일치하지 않습니다");
    }
}
