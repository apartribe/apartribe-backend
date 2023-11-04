package kr.apartribebackend.comment.eception;

import kr.apartribebackend.global.exception.RootException;

public class CantApplyCommentToBoardCauseBoardIsApartUserOnlyException extends RootException {

    public CantApplyCommentToBoardCauseBoardIsApartUserOnlyException() {
        super("해당 게시글은 주민 전용 게시글이라 댓글을 작성할 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
