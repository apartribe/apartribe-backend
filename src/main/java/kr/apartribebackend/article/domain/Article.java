package kr.apartribebackend.article.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter @SuperBuilder
@Entity @Table(name = "ARTICLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "ARTICLE")
public class Article extends Board {

    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    private Category category;

}

//package kr.apartribebackend.article.domain;
//
//
//import jakarta.persistence.*;
//import kr.apartribebackend.comment.domain.Comment;
//import kr.apartribebackend.global.domain.BaseEntity;
//import kr.apartribebackend.member.domain.Member;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Entity @Table(name = "ARTICLE")
//public class Article extends BaseEntity {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ARTICLE_ID")
//    private Long id;
//
//    @Column(name = "TITLE", nullable = false, length = 200)
//    private String title;
//
//    @Lob
//    @Column(name = "CONTENT", nullable = false)
//    private String content;
//
//    @Column(name = "LIKES")
//    private int liked;
//
//    @Column(name = "SAW")
//    private int saw;
//
//    @Column(name = "CATEGORY")
//    @Enumerated(EnumType.STRING)
//    private Category category;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MEMBER_ID")
//    private Member member;
//
//    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
//    @OrderBy("createdAt desc")
//    private final List<Comment> comments = new ArrayList<>();
//
//    @Builder
//    private Article(Long id,
//                   String title,
//                   String content,
//                   int liked,
//                   int saw,
//                   Category category,
//                   Member member) {
//        this.id = id;
//        this.title = title;
//        this.content = content;
//        this.liked = liked;
//        this.saw = saw;
//        this.category = category;
//        this.member = member;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Article article)) return false;
//        return id != null && Objects.equals(id, article.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//
//    /////////////////////////////// BUSINESS LOGIC ///////////////////////////////
//
//    public void reflectArticleLike() {
//        this.liked += 1;
//    }
//
//    public void reflectArticleSaw() {
//        this.saw += 1;
//    }
//
//}
