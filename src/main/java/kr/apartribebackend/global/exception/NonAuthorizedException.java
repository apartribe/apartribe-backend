package kr.apartribebackend.global.exception;

public class NonAuthorizedException extends RootException {

    public NonAuthorizedException() {
        super("해당 엔드포인트는 인증되지않은 사용자는 접근이 불가합니다.");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
