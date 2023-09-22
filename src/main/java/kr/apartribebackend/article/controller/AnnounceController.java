package kr.apartribebackend.article.controller;


import jakarta.validation.Valid;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.AnnounceDto;
import kr.apartribebackend.article.dto.announce.AnnounceResponse;
import kr.apartribebackend.article.dto.announce.AppendAnnounceReq;
import kr.apartribebackend.article.dto.announce.SingleAnnounceResponse;
import kr.apartribebackend.article.service.AnnounceService;
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

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RequiredArgsConstructor
@RestController
public class AnnounceController {

    private final AnnounceService announceService;

    @GetMapping({"/api/announce/{id}", "/api/announce/"})
    public APIResponse<SingleAnnounceResponse> findSingleArticle(@PathVariable final Optional<Long> id) {
        final Long announceId = id.orElse(0L);
        final SingleAnnounceResponse singleAnnounceById = announceService.findSingleAnnounceById(announceId);
        final APIResponse<SingleAnnounceResponse> apiResponse = APIResponse.SUCCESS(singleAnnounceById);
        return apiResponse;
    }

    @GetMapping("/api/announce")
    public APIResponse<PageResponse<AnnounceResponse>> findMultipleArticlesByCategory(
            @RequestParam(required = false, defaultValue = "") final Level level,
            @PageableDefault final Pageable pageable
    ) {
        final Page<AnnounceResponse> announceResponses = announceService
                .findMultipleAnnouncesByLevel(level, pageable)
                .map(AnnounceResponse::from);

        final PageResponse<AnnounceResponse> pageResponse = PageResponse.from(announceResponses);
        final APIResponse<PageResponse<AnnounceResponse>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    @PostMapping(value = "/api/announce", consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> attachmentToAWS(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestPart final AppendAnnounceReq announceInfo,
            @RequestPart(required = false) final List<MultipartFile> file) throws IOException
    {
        final MemberDto memberDto = authenticatedMember.toDto();
        final AnnounceDto announceDto = announceInfo.toDto();
        if (file != null)
            announceService.appendArticle(announceDto, memberDto, file);
        else
            announceService.appendArticle(announceDto, memberDto);
        return ResponseEntity.status(CREATED).build();
    }

    // TODO 첨부파일을 제외한 JSON 으로는 게시글 등록하는 API. 현재는 Deprecated 시킴.
    //    @PostMapping("/api/article")
    //    public ResponseEntity<Void> appendArticle(
    //            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
    //            @Valid @RequestBody final AppendArticleReq appendArticleReq
    //    ) {
    //        final MemberDto memberDto = authenticatedMember.toDto();
    //        final ArticleDto articleDto = appendArticleReq.toDto();
    //        articleService.appendArticle(articleDto, memberDto);
    //        return ResponseEntity.status(CREATED).build();
    //    }

}
