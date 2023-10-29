package kr.apartribebackend.comment.eception;

import kr.apartribebackend.global.exception.RootException;

public class CannotFoundCommentException extends RootException {

    public CannotFoundCommentException() {
        super("게시물에 댓글 찾을 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
