package kr.apartribebackend.article.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RecruitStatus {

    STILL("모집중"),
    END("모집 완료");

    @Getter
    private final String name;
}
