package kr.apartribebackend.article.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.article.domain.Category;
import kr.apartribebackend.article.dto.AppendArticleReq;
import kr.apartribebackend.article.dto.ArticleDto;
import kr.apartribebackend.article.dto.ArticleResponse;
import kr.apartribebackend.article.service.ArticleService;
import kr.apartribebackend.global.annotation.AuthResolver;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.PageResponse;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
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

    @PostMapping("/api/article")
    public void appendArticle(
            @AuthResolver Member member,           // TODO 현재는 ArgumentResolver 로 대체. 하지만 나중에 토큰값에서 꺼내오는것으로 변경해야 한다.
            @Valid @RequestBody final AppendArticleReq appendArticleReq
    ) {
        MemberDto memberDto = MemberDto.from(member);
        final ArticleDto articleDto = appendArticleReq.toDto();
        articleService.appendArticle(articleDto, memberDto);
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
