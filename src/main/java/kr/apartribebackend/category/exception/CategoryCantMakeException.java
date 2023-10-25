package kr.apartribebackend.category.exception;

import kr.apartribebackend.global.exception.RootException;

public class CategoryCantMakeException extends RootException {

    public CategoryCantMakeException() {
        super("아파트 인증이 되어있지 않은 사용자는 카테고리를 등록할 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
