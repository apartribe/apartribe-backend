package kr.apartribebackend.category.exception;

import kr.apartribebackend.global.exception.RootException;

public class ModifiedCategoryRequestException extends RootException {

    public ModifiedCategoryRequestException() {
        super("카테고리 작성은 카테고리만 해당 회원이 속한 아파트에만 가능합니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
