package kr.apartribebackend.category.exception;

import kr.apartribebackend.global.exception.RootException;

public class CategoryAlreadyExistsException extends RootException {

    public CategoryAlreadyExistsException() {
        super("카테고리가 이미 존재합니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
