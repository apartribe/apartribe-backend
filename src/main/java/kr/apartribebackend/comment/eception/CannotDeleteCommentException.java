package kr.apartribebackend.comment.eception;

import kr.apartribebackend.global.exception.RootException;

public class CannotDeleteCommentException extends RootException {

    public CannotDeleteCommentException() {
        super("댓글을 찾을 수 없어 댓글을 삭제할 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
