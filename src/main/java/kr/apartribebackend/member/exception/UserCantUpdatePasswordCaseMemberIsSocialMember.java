package kr.apartribebackend.member.exception;

import kr.apartribebackend.global.exception.RootException;

public class UserCantUpdatePasswordCaseMemberIsSocialMember extends RootException {

    public UserCantUpdatePasswordCaseMemberIsSocialMember() {
        super("해당 사용자는 소셜 로그인 사용자이기 때문에 비밀번호를 변경할 수 없습니다.");
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
