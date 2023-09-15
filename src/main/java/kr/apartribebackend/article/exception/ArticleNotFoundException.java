package kr.apartribebackend.article.exception;


import kr.apartribebackend.global.exception.RootException;

public class ArticleNotFoundException extends RootException {

    public ArticleNotFoundException() {
        super("게시글을 찾을 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
