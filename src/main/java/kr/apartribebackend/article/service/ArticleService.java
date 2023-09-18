package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.Category;
import kr.apartribebackend.article.dto.*;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.repository.ArticleRepository;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Page<ArticleDto> findMultipleArticlesByCategory(final Category category, final Pageable pageable) {
        if (category.equals(Category.ALL))
            return findAllArticles(pageable);
        return articleRepository.findArticlesByCategory(category, pageable)
                .map(ArticleDto::from);
    }

    public Page<ArticleDto> findAllArticles(final Pageable pageable) {
        return articleRepository.findAll(pageable)
                .map(ArticleDto::from);
    }

    @Transactional
    public void updateLikeByArticleId(final Long articleId) {
        articleRepository.findById(articleId)
                .ifPresentOrElse(Article::reflectArticleLike,
                        () -> { throw new CannotReflectLikeToArticleException(); });
    }

    @Transactional
    public SingleArticleResponse findSingleArticleById(final Long articleId) {
        return articleRepository.findJoinedArticleById(articleId)
                .stream().findFirst()
                .orElseThrow(ArticleNotFoundException::new);
    }

    public List<Top5ArticleResponse> findTop5ArticleViaLiked() {
        return articleRepository.findTop5ArticleViaLiked();
    }

    public List<Top5ArticleResponse> findTop5ArticleViaView() {
        return articleRepository.findTop5ArticleViaView();
    }

    @Transactional
    public void appendArticle(final ArticleDto articleDto, final MemberDto memberDto) {
        final Member member = memberDto.toEntity();
        final Article article = articleDto.toEntity(member);
        articleRepository.save(article);
    }

    public List<ArticleInCommunityRes> searchArticleInCommunity(String title) {
        return articleRepository.searchArticleInCommunity(title);
    }
}
