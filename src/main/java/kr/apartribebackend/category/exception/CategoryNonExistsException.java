package kr.apartribebackend.category.exception;


import kr.apartribebackend.global.exception.RootException;

public class CategoryNonExistsException extends RootException {

    public CategoryNonExistsException() {
        super("존재하는 카테고리를 찾을 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
