package kr.apartribebackend.article.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.article.dto.*;
import kr.apartribebackend.article.dto.SingleArticleResponseProjection;
import kr.apartribebackend.article.service.ArticleService;
import kr.apartribebackend.global.annotation.ApartUser;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.PageResponse;
import kr.apartribebackend.likes.dto.BoardLikedRes;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/api/{apartId}/article/{articleId}")
    public APIResponse<SingleArticleResponseProjection> findSingleArticle(
            @PathVariable final String apartId,
            @PathVariable final Long articleId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        final SingleArticleResponseProjection singleArticleResponseProjection = articleService
                .findSingleArticleById2(authenticatedMember.toDto(), apartId, articleId);
        final APIResponse<SingleArticleResponseProjection> apiResponse = APIResponse.SUCCESS(singleArticleResponseProjection);
        return apiResponse;
    }

    @GetMapping("/api/{apartId}/article")
    public APIResponse<PageResponse<ArticleResponse>> findMultipleArticlesByCategory(
            @PathVariable final String apartId,
            @RequestParam(required = false, defaultValue = "") final String category,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        final Page<ArticleResponse> articleResponse =
                articleService.findMultipleArticlesByCategory(apartId, category, pageable);
        final PageResponse<ArticleResponse> pageResponse = PageResponse.from(articleResponse);
        final APIResponse<PageResponse<ArticleResponse>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    @ApartUser
    @PostMapping("/api/{apartId}/article")
    public ResponseEntity<Void> appendArticle(
            @PathVariable final String apartId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final AppendArticleReq articleInfo
    ) {
        final String category = articleInfo.category();
        final MemberDto memberDto = authenticatedMember.toDto();
        final ArticleDto articleDto = articleInfo.toDto();
        articleService.appendArticle(apartId, category, articleDto, memberDto);
        return ResponseEntity.status(CREATED).build();
    }

    @ApartUser
    @PostMapping(value = "/api/{apartId}/article/attach", consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> attachmentToAWS(
            @PathVariable final String apartId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestPart final AppendArticleReq articleInfo,
            @RequestPart(required = false) final List<MultipartFile> file) throws IOException
    {
        final String category = articleInfo.category();
        final MemberDto memberDto = authenticatedMember.toDto();
        final ArticleDto articleDto = articleInfo.toDto();
        if (file != null)
            articleService.appendArticle(apartId, category, articleDto, memberDto, file);
        else
            articleService.appendArticle(apartId, category, articleDto, memberDto);
        return ResponseEntity.status(CREATED).build();
    }

    @ApartUser
    @PutMapping("/api/{apartId}/article/{articleId}")
    public APIResponse<SingleArticleResponse> updateArticle(
            @PathVariable final String apartId,
            @PathVariable final Long articleId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final UpdateArticleReq articleInfo
    ) {
        final SingleArticleResponse singleArticleResponse = articleService
                .updateArticle(apartId, articleId, articleInfo.category(), articleInfo.toDto(), authenticatedMember.toDto());
        final APIResponse<SingleArticleResponse> apiResponse = APIResponse.SUCCESS(singleArticleResponse);
        return apiResponse;
    }

    @GetMapping("/api/{apartId}/article/{articleId}/like")
    public APIResponse<BoardLikedRes> updateLikeByBoardId(
            @PathVariable final String apartId,
            @PathVariable final Long articleId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        final BoardLikedRes boardLikedRes = articleService
                .updateLikeByArticleId(authenticatedMember.toDto(), apartId, articleId);
        final APIResponse<BoardLikedRes> apiResponse = APIResponse.SUCCESS(boardLikedRes);
        return apiResponse;
    }

    @GetMapping("/api/{apartId}/article/best/liked")
    public APIResponse<List<Top5ArticleResponse>> findTop5ArticleViaLiked(
            @PathVariable final String apartId
    ) {
        final List<Top5ArticleResponse> articleResponses = articleService
                .findTop5ArticleViaLiked(apartId);
        return APIResponse.SUCCESS(articleResponses);
    }

    @GetMapping("/api/{apartId}/article/best/view")
    public APIResponse<List<Top5ArticleResponse>> findTop5ArticleViaView(
            @PathVariable final String apartId
    ) {
        final List<Top5ArticleResponse> articleResponses = articleService
                .findTop5ArticleViaView(apartId);
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

    @ApartUser
    @DeleteMapping("/api/{apartId}/article/{articleId}")
    public void removeArticle(
            @PathVariable final String apartId,
            @PathVariable final Long articleId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        articleService.removeArticle(authenticatedMember.toDto(), apartId, articleId);
    }

}
