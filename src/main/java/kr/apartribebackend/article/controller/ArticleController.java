package kr.apartribebackend.article.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.article.domain.Category;
import kr.apartribebackend.article.dto.*;
import kr.apartribebackend.article.service.ArticleService;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.PageResponse;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;


@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping({"/api/article/{id}", "/api/article/"})
    public APIResponse<SingleArticleResponse> findSingleArticle(@PathVariable final Optional<Long> id) {
        final Long articleId = id.orElse(0L);
        final SingleArticleResponse singleArticleById = articleService.findSingleArticleById(articleId);
        final APIResponse<SingleArticleResponse> apiResponse = APIResponse.SUCCESS(singleArticleById);
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
    public ResponseEntity<Void> appendArticle(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,           // TODO 수정완료
            @Valid @RequestBody final AppendArticleReq appendArticleReq
    ) {
        final MemberDto memberDto = authenticatedMember.toDto();
        final ArticleDto articleDto = appendArticleReq.toDto();
        articleService.appendArticle(articleDto, memberDto);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping({"/api/article/{id}/like", "/api/article/like"})
    public void updateLikeByArticleId(@PathVariable final Optional<Long> id) {
        final Long articleId = id.orElse(0L);
        articleService.updateLikeByArticleId(articleId);
    }

    @GetMapping("/api/article/best/liked")
    public APIResponse<List<Top5ArticleResponse>> findTop5ArticleViaLiked() {
        final List<Top5ArticleResponse> articleResponses = articleService
                .findTop5ArticleViaLiked();
        return APIResponse.SUCCESS(articleResponses);
    }

    @GetMapping("/api/article/best/view")
    public APIResponse<List<Top5ArticleResponse>> findTop5ArticleViaView() {
        final List<Top5ArticleResponse> articleResponses = articleService
                .findTop5ArticleViaView();
        return APIResponse.SUCCESS(articleResponses);
    }

    @GetMapping("/api/article/search")
    public APIResponse<List<ArticleInCommunityRes>> searchArticleInCommunity(@RequestParam final String title) {
        final List<ArticleInCommunityRes> articleInCommunityRes = articleService.searchArticleInCommunity(title);
        final APIResponse<List<ArticleInCommunityRes>> apiResponse = APIResponse.SUCCESS(articleInCommunityRes);
        return apiResponse;
    }

}
