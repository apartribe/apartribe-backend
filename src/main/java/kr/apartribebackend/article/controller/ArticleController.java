package kr.apartribebackend.article.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.article.domain.Article;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


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
            @RequestParam(required = false, defaultValue = "") final String category,
            @PageableDefault final Pageable pageable
    ) {
        final Page<ArticleResponse> articleResponse = articleService
                .findMultipleArticlesByCategory(category, pageable);

        final PageResponse<ArticleResponse> pageResponse = PageResponse.from(articleResponse);
        final APIResponse<PageResponse<ArticleResponse>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    @PostMapping("/api/article")
    public ResponseEntity<Void> appendArticle(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final AppendArticleReq articleInfo
    ) {
        final String category = articleInfo.category();
        final MemberDto memberDto = authenticatedMember.toDto();
        final ArticleDto articleDto = articleInfo.toDto();
        articleService.appendArticle(category, articleDto, memberDto);
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping(value = "/api/article/attach", consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> attachmentToAWS(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestPart final AppendArticleReq articleInfo,
            @RequestPart(required = false) final List<MultipartFile> file) throws IOException
    {
        final String category = articleInfo.category();
        final MemberDto memberDto = authenticatedMember.toDto();
        final ArticleDto articleDto = articleInfo.toDto();
        if (file != null)
            articleService.appendArticle(category, articleDto, memberDto, file);
        else
            articleService.appendArticle(category, articleDto, memberDto);
        return ResponseEntity.status(CREATED).build();
    }

    @PutMapping({"/api/article/{id}", "/api/article/"})
    public APIResponse<SingleArticleResponse> updateArticle(
            @PathVariable final Optional<Long> id,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final UpdateArticleReq articleInfo
    ) {
        final Long articleId = id.orElse(0L);
        final SingleArticleResponse singleArticleResponse = articleService
                .updateArticle(articleId, articleInfo.category(), articleInfo.toDto(), authenticatedMember.toDto());
        final APIResponse<SingleArticleResponse> apiResponse = APIResponse.SUCCESS(singleArticleResponse);
        return apiResponse;
    }

    @GetMapping({"/api/article/{id}/like", "/api/article/like"})
    public void updateLikeByBoardId(@PathVariable final Optional<Long> id) {
        final Long articleId = id.orElse(0L);
        articleService.updateLikeByArticleId(articleId);
    }

//    @DeleteMapping("/api/article")
//    public void removeArticle(
//            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
//            @RequestParam Long articleId
//    ) {
//        Article board = Article.builder().id(articleId).build();
//        boardService.removeArticle(board);
//    }

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

    @GetMapping("/api/{apartId}/article/search")
    public APIResponse<List<ArticleInCommunityRes>> searchArticleInCommunity(
            @PathVariable final String apartId,
            @RequestParam final String title
    ) {
        final List<ArticleInCommunityRes> articleInCommunityRes =
                articleService.searchArticleInCommunity(apartId, title);

        final APIResponse<List<ArticleInCommunityRes>> apiResponse = APIResponse.SUCCESS(articleInCommunityRes);
        return apiResponse;
    }

}
