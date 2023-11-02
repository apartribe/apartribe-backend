package kr.apartribebackend.article.exception;

import kr.apartribebackend.global.exception.RootException;

public class CantDeleteBoardCauseInvalidMemberException extends RootException {

    public CantDeleteBoardCauseInvalidMemberException() {
        super("게시글을 작성한 작성자 정보와 일치하지 않아 게시글을 삭제할 수 없습니다.");

    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
