package kr.apartribebackend.article.exception;

import kr.apartribebackend.global.exception.RootException;

public class CantCreateAnnounceCauseInvalidUserType extends RootException {

    public CantCreateAnnounceCauseInvalidUserType() {
        super("manager 유저타입 사용자만 공지사항을 작성할 수 있습니다.");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
