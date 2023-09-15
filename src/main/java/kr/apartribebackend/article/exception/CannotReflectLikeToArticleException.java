package kr.apartribebackend.article.exception;

import kr.apartribebackend.global.exception.RootException;

public class CannotReflectLikeToArticleException extends RootException {
    public CannotReflectLikeToArticleException() {
        super("게시글을 찾을 수 없어 좋아요가 반영되지 않았습니다.");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
