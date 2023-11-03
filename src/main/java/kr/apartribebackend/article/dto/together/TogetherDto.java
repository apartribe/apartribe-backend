package kr.apartribebackend.article.dto.together;


import kr.apartribebackend.article.domain.RecruitStatus;
import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TogetherDto {

    private Category category;
    private String title;
    private String description;
    private String content;
    private LocalDate recruitFrom;
    private LocalDate recruitTo;
    private RecruitStatus recruitStatus;
    private String meetTime;
    private String target;
    private String location;
    private boolean contributeStatus;
    private String thumbnail;
    private LocalDateTime createdAt;
    private String createdBy;
    private boolean onlyApartUser;

    @Builder
    private TogetherDto(Category category,
                       String title,
                       String description,
                       String content,
                       LocalDate recruitFrom,
                       LocalDate recruitTo,
                       RecruitStatus recruitStatus,
                       String meetTime,
                       String target,
                       String location,
                       boolean contributeStatus,
                       String thumbnail,
                       String createdBy,
                       LocalDateTime createdAt,
                       boolean onlyApartUser) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.content = content;
        this.recruitFrom = recruitFrom;
        this.recruitTo = recruitTo;
        this.recruitStatus = recruitStatus;
        this.meetTime = meetTime;
        this.target = target;
        this.location = location;
        this.contributeStatus = contributeStatus;
        this.thumbnail = thumbnail;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.onlyApartUser = onlyApartUser;
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
                .recruitStatus(recruitStatus)
                .meetTime(meetTime)
                .target(target)
                .location(location)
                .contributeStatus(contributeStatus)
                .thumbnail(thumbnail)
                .onlyApartUser(onlyApartUser)
                .build();
    }
}
