package kr.apartribebackend.apart.exception;

import kr.apartribebackend.global.exception.RootException;

public class NeedToMakeCommunityException extends RootException {

    public NeedToMakeCommunityException() {
        super("해당 사용자는 아파트 인증이 되어있지만, 커뮤니티가 존재하지 않아 요청하신 동작을 수행할 수 없습니다. 아파트 커뮤니티를 생성해주세요.");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
