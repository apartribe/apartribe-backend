package kr.apartribebackend.comment.eception;

import kr.apartribebackend.global.exception.RootException;

public class CannotFoundParentCommentInBoardException extends RootException {

    public CannotFoundParentCommentInBoardException() {
        super("게시물에 댓글 찾을 수 없어 대댓글을 작성하지 못했습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
