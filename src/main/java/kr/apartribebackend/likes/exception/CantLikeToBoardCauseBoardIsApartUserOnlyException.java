package kr.apartribebackend.likes.exception;

import kr.apartribebackend.global.exception.RootException;

public class CantLikeToBoardCauseBoardIsApartUserOnlyException extends RootException {

    public CantLikeToBoardCauseBoardIsApartUserOnlyException() {
        super("해당 게시글은 주민 전용 게시글이라 좋아요를 누를 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
