package kr.apartribebackend.article.exception;

import kr.apartribebackend.global.exception.RootException;

public class NotApartUserBoardException extends RootException {

    public NotApartUserBoardException() {
        super("해당 게시글은 아파트 주민 전용 게시글입니다.");
    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
