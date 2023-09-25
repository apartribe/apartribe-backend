package kr.apartribebackend.comment.eception;

import kr.apartribebackend.global.exception.RootException;

public class CommentDepthException extends RootException {

    public CommentDepthException() {
        super("댓글의 depth 는 2를 넘을 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
