package kr.apartribebackend.article.domain;


import jakarta.persistence.*;
import kr.apartribebackend.category.domain.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter @SuperBuilder
@Entity @Table(name = "TOGETHER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "TOGETHER")
public class Together extends Board {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "RECRUIT_FROM", nullable = false)
    private LocalDate recruitFrom;

    @Column(name = "RECRUIT_TO", nullable = false)
    private LocalDate recruitTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "RECRUIT_STATUS", nullable = false)
    private RecruitStatus recruitStatus;

    @Column(name = "MEET_TIME", nullable = false)
    private String meetTime;

    @Column(name = "TARGET", nullable = false)
    private String target;

    @Column(name = "LOCATION", nullable = false)
    private String location;

    @Column(name = "CONTRIBUE_STATUS")
    private boolean contributeStatus;

    /////////////////////////////// BUSINESS LOGIC ///////////////////////////////

    public Together updateTogether(Category category,
                                   String title,
                                   String description,
                                   String content,
                                   LocalDate recruitFrom,
                                   LocalDate recruitTo,
                                   String meetTime,
                                   String target,
                                   String location,
                                   Boolean contributeStatus,
                                   RecruitStatus recruitStatus) {
        this.category = category;
        setTitle(title);
        this.description = description;
        setContent(content);
        this.recruitFrom = recruitFrom;
        this.recruitTo = recruitTo;
        this.meetTime = meetTime;
        this.target = target;
        this.location = location;
        this.contributeStatus = contributeStatus;
        this.recruitStatus = recruitStatus;
        return this;
    }
}
