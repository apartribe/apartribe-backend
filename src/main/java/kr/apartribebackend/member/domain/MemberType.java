package kr.apartribebackend.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberType {

    GENERAL("일반"),
    SOCIAL("소셜");

    private final String name;
}
