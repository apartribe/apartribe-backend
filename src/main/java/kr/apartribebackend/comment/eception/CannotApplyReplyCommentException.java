package kr.apartribebackend.comment.eception;

import kr.apartribebackend.global.exception.RootException;

public class CannotApplyReplyCommentException extends RootException {

    public CannotApplyReplyCommentException() {
        super("댓글을 찾을 수 없어 대댓글을 작성하지 못했습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
