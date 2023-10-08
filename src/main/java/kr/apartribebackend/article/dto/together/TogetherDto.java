package kr.apartribebackend.article.dto.together;


import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TogetherDto {

    private Category category;
    private String title;
    private String description;
    private String content;
    private LocalDate recruitFrom;
    private LocalDate recruitTo;
    private String meetTime;
    private String target;
    private String location;
    private boolean contributeStatus;

    @Builder
    private TogetherDto(Category category,
                       String title,
                       String description,
                       String content,
                       LocalDate recruitFrom,
                       LocalDate recruitTo,
                       String meetTime,
                       String target,
                       String location,
                       boolean contributeStatus) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.content = content;
        this.recruitFrom = recruitFrom;
        this.recruitTo = recruitTo;
        this.meetTime = meetTime;
        this.target = target;
        this.location = location;
        this.contributeStatus = contributeStatus;
    }

    public Together toEntity(Category category, Member member) {
        return Together.builder()
                .member(member)
                .category(category)
                .title(title)
                .description(description)
                .content(content)
                .recruitFrom(recruitFrom)
                .recruitTo(recruitTo)
                .meetTime(meetTime)
                .target(target)
                .location(location)
                .contributeStatus(contributeStatus)
                .build();
    }
}
