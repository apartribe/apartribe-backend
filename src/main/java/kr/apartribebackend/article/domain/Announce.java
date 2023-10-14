package kr.apartribebackend.article.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter @SuperBuilder
@Entity @Table(name = "ANNOUNCE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "ANNOUNCE")
public class Announce extends Board {

    @Column(name = "LEVEL")
    @Enumerated(EnumType.STRING)
    private Level level;

    public Announce updateAnnounce(Level level,
                                   String title,
                                   String content) {
        this.level = level;
        setTitle(title);
        setContent(content);
        return this;
    }

}
