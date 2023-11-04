package kr.apartribebackend.likes.exception;

import kr.apartribebackend.global.exception.RootException;

public class CantLikeToCommentCauseBoardIsApartUserOnlyException extends RootException {

    public CantLikeToCommentCauseBoardIsApartUserOnlyException() {
        super("해당 게시글은 주민 전용 게시글이라 댓글에 좋아요를 누를 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
