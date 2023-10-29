package kr.apartribebackend.comment.eception;

import kr.apartribebackend.global.exception.RootException;

public class CannotReflectLikeToCommentException extends RootException {

    public CannotReflectLikeToCommentException() {
        super("댓글을 찾을 수 없어 좋아요가 반영되지 않았습니다.");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
