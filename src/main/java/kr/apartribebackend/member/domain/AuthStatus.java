package kr.apartribebackend.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthStatus implements Badge {

    COMPLETED("인증 완료"),
    PENDING("인증 보류"),
    INCOMPLETE("미인증");

    private final String status;

    @Override
    public String getName() {
        return status;
    }
}
