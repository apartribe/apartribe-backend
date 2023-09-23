package kr.apartribebackend.article.dto;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleDto {

    private Long id;
    private String title;
    private String content;
    private int liked;
    private int saw;
    private kr.apartribebackend.category.domain.Category category;
    private Member member;
    private LocalDateTime createdAt;
    private String createdBy;

    @Builder
    private ArticleDto(Long id,
                       String title,
                       String content,
                       int liked,
                       int saw,
                       Category category,
                       Member member,
                       LocalDateTime createdAt,
                       String createdBy) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.liked = liked;
        this.saw = saw;
        this.category = category;
        this.member = member;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public static ArticleDto from(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .liked(article.getLiked())
                .saw(article.getSaw())
                .category(article.getCategory())
                .member(article.getMember())
                .createdAt(article.getCreatedAt())
                .createdBy(article.getCreatedBy())
                .build();
    }

    public Article toEntity(Member member) {
        return Article.builder()
                .id(id)
                .title(title)
                .content(content)
                .liked(liked)
                .saw(saw)
                .category(category)
                .member(member)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .build();
    }

    public Article toEntity(Category category, Member member) {
        return Article.builder()
                .id(id)
                .title(title)
                .content(content)
                .liked(liked)
                .saw(saw)
                .category(category)
                .member(member)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .build();
    }

}


//package kr.apartribebackend.article.dto;
//
//import kr.apartribebackend.article.domain.Article;
//import kr.apartribebackend.article.domain.Category;
//import kr.apartribebackend.member.domain.Member;
//import kr.apartribebackend.member.dto.MemberDto;
//import lombok.Builder;
//import lombok.Getter;
//
//import java.time.LocalDateTime;
//
//@Getter
//public class ArticleDto {
//
//    private Long id;
//    private String title;
//    private String content;
//    private int liked;
//    private int saw;
//    private Category category;
//    private Member member;
//    private LocalDateTime createdAt;
//    private String createdBy;
//
//    @Builder
//    private ArticleDto(Long id,
//                      String title,
//                      String content,
//                      int liked,
//                      int saw,
//                      Category category,
//                      Member member,
//                      LocalDateTime createdAt,
//                      String createdBy) {
//        this.id = id;
//        this.title = title;
//        this.content = content;
//        this.liked = liked;
//        this.saw = saw;
//        this.category = category;
//        this.member = member;
//        this.createdAt = createdAt;
//        this.createdBy = createdBy;
//    }
//
//    public static ArticleDto from(Article article) {
//        return ArticleDto.builder()
//                .id(article.getId())
//                .title(article.getTitle())
//                .content(article.getContent())
//                .liked(article.getLiked())
//                .saw(article.getSaw())
//                .category(article.getCategory())
//                .member(article.getMember())
//                .createdAt(article.getCreatedAt())
//                .createdBy(article.getCreatedBy())
//                .build();
//    }
//
//    public Article toEntity(Member member) {
//        return Article.builder()
//                .id(id)
//                .title(title)
//                .content(content)
//                .liked(liked)
//                .saw(saw)
//                .category(category)
//                .member(member)
//                .createdAt(createdAt)
//                .createdBy(createdBy)
//                .build();
//    }
//}
