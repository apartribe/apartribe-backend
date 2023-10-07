package kr.apartribebackend.article.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.article.dto.together.AppendTogetherReq;
import kr.apartribebackend.article.service.TogetherService;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RequiredArgsConstructor
@RestController
public class TogetherController {

    private final TogetherService togetherService;

    @GetMapping({"/api/together/{id}", "/api/together/"})
    public void findSingleArticle() {
        System.out.println("triggered findSingleArticle");
    }

    @GetMapping("/api/together")
    public void findMultipleArticlesByCategory() {
        System.out.println("triggered mapping3");
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
    public void attachmentToAWS() {
        System.out.println("triggered mapping3");
    }

    @GetMapping({"/api/together/{id}/like", "/api/together/like"})
    public void updateLikeByBoardId() {
        System.out.println("triggered mapping3");
    }

}
