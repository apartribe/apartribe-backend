package kr.apartribebackend.article.domain;


import jakarta.persistence.*;
import kr.apartribebackend.likes.domain.BoardLiked;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static kr.apartribebackend.article.domain.BoardType.ANNOUNCE;

@Getter @SuperBuilder
@Entity @Table(name = "ANNOUNCE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = ANNOUNCE)
public class Announce extends Board {

    @Column(name = "LEVEL")
    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(name = "FLOAT_FROM")
    private LocalDate floatFrom;

    @Column(name = "FLOAT_TO")
    private LocalDate floatTo;

    public Announce updateAnnounce(Level level,
                                   String title,
                                   String content,
                                   LocalDate floatFrom,
                                   LocalDate floatTo,
                                   String thumbnail,
                                   boolean onlyApartUser) {
        this.level = level;
        setTitle(title);
        setContent(content);
        setThumbnail(thumbnail);
        this.floatFrom = floatFrom;
        this.floatTo = floatTo;
        setOnlyApartUser(onlyApartUser);
        return this;
    }

    public Announce updateAnnounce(Level level,
                                   String title,
                                   String content,
                                   LocalDate floatFrom,
                                   LocalDate floatTo,
                                   boolean onlyApartUser) {
        this.level = level;
        setTitle(title);
        setContent(content);
        this.floatFrom = floatFrom;
        this.floatTo = floatTo;
        setOnlyApartUser(onlyApartUser);
        return this;
    }

}
