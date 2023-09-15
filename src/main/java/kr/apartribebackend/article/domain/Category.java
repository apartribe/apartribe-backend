package kr.apartribebackend.article.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    ALL("전체"),
    GENERAL("일반"),
    EMERGENCY("긴급"),
    CRITICAL("비상");

    private final String name;
}
