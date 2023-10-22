package kr.apartribebackend.article.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.article.dto.SingleArticleResponse;
import kr.apartribebackend.article.dto.UpdateArticleReq;
import kr.apartribebackend.article.dto.together.*;
import kr.apartribebackend.article.service.TogetherService;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.PageResponse;
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
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RequiredArgsConstructor
@RestController
public class TogetherController {

    private final TogetherService togetherService;

    @GetMapping("/api/{apartId}/together/{togetherId}")
    public APIResponse<SingleTogetherResponse> findSingleTogether(
            @PathVariable final String apartId,
            @PathVariable final Long togetherId
    ) {
        final SingleTogetherResponse singleArticleById = togetherService.findSingleTogetherById(apartId, togetherId);
        final APIResponse<SingleTogetherResponse> apiResponse = APIResponse.SUCCESS(singleArticleById);
        return apiResponse;
    }

    @GetMapping("/api/{apartId}/together")
    public APIResponse<PageResponse<TogetherResponse>> findMultipleTogethers(
            @PathVariable final String apartId,
            @RequestParam(required = false, defaultValue = "") final String category,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        final Page<TogetherResponse> togetherResponses =
                togetherService.findMultipleTogethersByCategory(apartId, category, pageable);
        final PageResponse<TogetherResponse> pageResponse = PageResponse.from(togetherResponses);
        final APIResponse<PageResponse<TogetherResponse>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    @PostMapping("/api/together")
    public ResponseEntity<Void> appendArticle(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final AppendTogetherReq appendTogetherReq
    ) {
        togetherService.appendTogether(appendTogetherReq.category(), authenticatedMember.toDto(), appendTogetherReq.toDto());
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping(value = "/api/together/attach", consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> attachmentToAWS(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestPart final AppendTogetherReq togetherInfo,
            @RequestPart(required = false) final List<MultipartFile> file) throws IOException {
        final String category = togetherInfo.category();
        final MemberDto memberDto = authenticatedMember.toDto();
        final TogetherDto togetherDto = togetherInfo.toDto();
        if (file != null)
            togetherService.appendTogether(category, memberDto, togetherDto, file);
        else
            togetherService.appendTogether(category, memberDto, togetherDto);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping({"/api/together/{id}/like", "/api/together/like"})
    public void updateLikeByBoardId(@PathVariable final Optional<Long> id) {
        final Long togetherId = id.orElse(0L);
        togetherService.updateLikeByTogetherId(togetherId);
    }

    @PutMapping({"/api/together/{id}", "/api/together/"})
    public APIResponse<SingleTogetherResponse> updateTogether(
            @PathVariable final Optional<Long> id,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final UpdateTogetherReq updateTogetherReq
    ) {
        final Long togetherId = id.orElse(0L);
        final SingleTogetherResponse singleTogetherResponse = togetherService.updateTogether(
                togetherId, updateTogetherReq.category(), updateTogetherReq.toDto(), authenticatedMember.toDto());
        final APIResponse<SingleTogetherResponse> apiResponse = APIResponse.SUCCESS(singleTogetherResponse);
        return apiResponse;
    }

}
