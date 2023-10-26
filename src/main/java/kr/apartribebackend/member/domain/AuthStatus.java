package kr.apartribebackend.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthStatus {

    COMPLETED("인증 완료"),
    PENDING("인증 보류"),
    INCOMPLETE("인증 미완료");

    private final String status;
}
