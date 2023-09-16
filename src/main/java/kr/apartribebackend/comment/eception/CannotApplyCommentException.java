package kr.apartribebackend.comment.eception;

import kr.apartribebackend.global.exception.RootException;


public class CannotApplyCommentException extends RootException {

    public CannotApplyCommentException() {
        super("게시글을 찾을 수 없어 댓글을 작성하지 못했습니다.");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
