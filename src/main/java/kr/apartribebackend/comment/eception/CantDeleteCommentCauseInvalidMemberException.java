package kr.apartribebackend.comment.eception;

import kr.apartribebackend.global.exception.RootException;

public class CantDeleteCommentCauseInvalidMemberException extends RootException {

    public CantDeleteCommentCauseInvalidMemberException() {
        super("댓글을 작성한 작성자 정보와 일치하지 않아 댓글을 삭제할 수 없습니다.");

    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
