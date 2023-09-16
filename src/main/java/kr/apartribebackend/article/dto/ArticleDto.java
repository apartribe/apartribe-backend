package kr.apartribebackend.article.dto;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.Category;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleDto {

    private Long id;
    private String title;
    private String content;
    private int liked;
    private int saw;
    private Category category;
    private Member member;

    @Builder
    private ArticleDto(Long id,
                      String title,
                      String content,
                      int liked,
                      int saw,
                      Category category,
                      Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.liked = liked;
        this.saw = saw;
        this.category = category;
        this.member = member;
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
                .build();
    }

    public Article toEntity(Member member) {
        return Article.builder()
                .title(title)
                .content(content)
                .category(category)
                .member(member)
                .build();
    }
}
