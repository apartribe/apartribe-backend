package kr.apartribebackend.article.controller;


import jakarta.validation.Valid;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.*;
import kr.apartribebackend.article.exception.CantCreateAnnounceCauseInvalidUserType;
import kr.apartribebackend.article.service.AnnounceService;
import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.global.annotation.ApartUser;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.PageResponse;
import kr.apartribebackend.likes.dto.BoardLikedRes;
import kr.apartribebackend.member.domain.UserType;
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

import static org.springframework.http.HttpStatus.CREATED;


@RequiredArgsConstructor
@RestController
public class AnnounceController {

    private final AnnounceService announceService;

    @GetMapping("/api/{apartId}/announce/{announceId}")
    public APIResponse<SingleAnnounceResponseProjection> findSingleArticle(
            @PathVariable final String apartId,
            @PathVariable final Long announceId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        final SingleAnnounceResponseProjection singleAnnounceResponseProjection = announceService
                .findSingleAnnounceById2(authenticatedMember.toDto(), apartId, announceId);
        final APIResponse<SingleAnnounceResponseProjection> apiResponse =
                APIResponse.SUCCESS(singleAnnounceResponseProjection);
        return apiResponse;
    }

    @GetMapping("/api/{apartId}/announce")
    public APIResponse<PageResponse<AnnounceResponse>> findMultipleArticlesByCategory(
            @PathVariable final String apartId,
            @RequestParam(required = false, defaultValue = "") final Level level,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        final Page<AnnounceResponse> announceResponses = announceService
                .findMultipleAnnouncesByLevel(apartId, level, pageable);

        final PageResponse<AnnounceResponse> pageResponse = PageResponse.from(announceResponses);
        final APIResponse<PageResponse<AnnounceResponse>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    @ApartUser
    @PostMapping("/api/{apartId}/announce")
    public ResponseEntity<Void> appendArticle(
            @PathVariable final String apartId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final AppendAnnounceReq announceInfo
    ) {
        final MemberDto memberDto = authenticatedMember.toDto();
        if (memberDto.getUserType() == null || memberDto.getUserType() != UserType.MANAGER) {
            throw new CantCreateAnnounceCauseInvalidUserType();
        }
        final AnnounceDto announceDto = announceInfo.toDto();
        announceService.appendArticle(announceDto, memberDto);
        return ResponseEntity.status(CREATED).build();
    }

    @ApartUser
    @PutMapping("/api/{apartId}/announce/{announceId}")
    public APIResponse<SingleAnnounceResponse> updateAnnounce(
            @PathVariable final String apartId,
            @PathVariable final Long announceId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final UpdateAnnounceReq updateAnnounceReq
    ) {
        final SingleAnnounceResponse singleAnnounceResponse = announceService
                .updateAnnounce(apartId, announceId, updateAnnounceReq.toDto(), authenticatedMember.toDto());
        final APIResponse<SingleAnnounceResponse> apiResponse = APIResponse.SUCCESS(singleAnnounceResponse);
        return apiResponse;
    }

    @ApartUser
    @DeleteMapping("/api/{apartId}/announce/{announceId}")
    public void removeAnnounce(
            @PathVariable final String apartId,
            @PathVariable final Long announceId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        announceService.removeAnnounce(authenticatedMember.toDto(), apartId, announceId);
    }

    @GetMapping("/api/{apartId}/announce/{announceId}/like")
    public APIResponse<BoardLikedRes> updateLikeByBoardId(
            @PathVariable final String apartId,
            @PathVariable final Long announceId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember) {
        final BoardLikedRes boardLikedRes = announceService
                .updateLikeByAnnounceId(authenticatedMember.toDto(), apartId, announceId);
        final APIResponse<BoardLikedRes> apiResponse = APIResponse.SUCCESS(boardLikedRes);
        return apiResponse;
    }

    @GetMapping("/api/{apartId}/announce/widget")
    public APIResponse<List<AnnounceWidgetRes>> announceWidgets(
            @PathVariable final String apartId
    ) {
        final List<AnnounceWidgetRes> widgetValues = announceService.findWidgetValues(apartId);
        final APIResponse<List<AnnounceWidgetRes>> apiResponse = APIResponse.SUCCESS(widgetValues);
        return apiResponse;
    }

}
