package kr.apartribebackend.article.controller;

import kr.apartribebackend.article.domain.Category;
import kr.apartribebackend.article.dto.ArticleDto;
import kr.apartribebackend.article.dto.ArticleResponse;
import kr.apartribebackend.article.service.ArticleService;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController @RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping({"/api/article/{id}", "/api/article/"})
    public APIResponse<ArticleResponse> findSingleArticle(@PathVariable final Optional<Long> id) {
        final Long articleId = id.orElse(0L);
        final ArticleDto articleDto = articleService.findSingleArticleById(articleId);
        final ArticleResponse articleResponse = ArticleResponse.from(articleDto);
        final APIResponse<ArticleResponse> apiResponse = APIResponse.SUCCESS(articleResponse);
        return apiResponse;
    }

    @GetMapping("/api/article")
    public APIResponse<PageResponse<ArticleResponse>> findMultipleArticlesByCategory(
            @RequestParam(required = false, defaultValue = "") final Category category,
            @PageableDefault final Pageable pageable
    ) {
        final Page<ArticleResponse> articleResponse = articleService
                .findMultipleArticlesByCategory(category, pageable)
                .map(ArticleResponse::from);

        final PageResponse<ArticleResponse> pageResponse = PageResponse.from(articleResponse);
        final APIResponse<PageResponse<ArticleResponse>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    @GetMapping({"/api/article/{id}/like", "/api/article/like"})
    public void updateLikeByArticleId(@PathVariable final Optional<Long> id) {
        final Long articleId = id.orElse(0L);
        articleService.updateLikeByArticleId(articleId);
    }

    @GetMapping("/api/article/best")
    public APIResponse<List<ArticleResponse>> findTop5ArticleViaLiked() {
        final List<ArticleResponse> articleResponses = articleService
                .findTop5ArticleViaLiked()
                .stream()
                .map(ArticleResponse::from)
                .toList();
        return APIResponse.SUCCESS(articleResponses);
    }

}
