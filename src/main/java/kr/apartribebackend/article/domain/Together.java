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

    @Column(name = "MEET_TIME", nullable = false)
    private String meetTime;

    @Column(name = "TARGET", nullable = false)
    private String target;

    @Column(name = "LOCATION", nullable = false)
    private String location;

    @Column(name = "CONTRIBUE_STATUS")
    private boolean contributeStatus;

    /////////////////////////////// BUSINESS LOGIC ///////////////////////////////

}