package kr.apartribebackend.comment.eception;

import kr.apartribebackend.global.exception.RootException;

public class CantUpdateCauseCantFindCommentException extends RootException {

    public CantUpdateCauseCantFindCommentException() {
        super("댓글 찾을 없기 때문에 수정할 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
