package kr.apartribebackend.comment.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.comment.dto.AppendCommentReq;
import kr.apartribebackend.comment.dto.BestCommentResponse;
import kr.apartribebackend.comment.dto.CommentDto;
import kr.apartribebackend.comment.service.CommentService;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/article/comment/{id}")
    public ResponseEntity<Void> appendCommentToArticle(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,        // TODO 수정 완료
            @PathVariable final Optional<Long> id,
            @Valid @RequestBody final AppendCommentReq appendCommentReq
    ) {
        final MemberDto memberDto = authenticatedMember.toDto();
        final Long articleId = id.orElse(0L);
        final CommentDto commentDto = appendCommentReq.toDto();
        commentService.appendCommentToArticle(memberDto, articleId, commentDto);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/api/article/comment/best")
    public APIResponse<List<BestCommentResponse>> bestCommentUntilLastWeek() {
        final List<BestCommentResponse> bestCommentResponses = commentService.bestCommentRankViaLastWeek();
        final APIResponse<List<BestCommentResponse>> apiResponse = APIResponse.SUCCESS(bestCommentResponses);
        return apiResponse;
    }

}
