package kr.apartribebackend.comment.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.comment.dto.AppendCommentReq;
import kr.apartribebackend.comment.dto.BestCommentResponse;
import kr.apartribebackend.comment.dto.CommentDto;
import kr.apartribebackend.comment.service.CommentService;
import kr.apartribebackend.global.annotation.AuthResolver;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/article/comment/{id}")
    public void appendCommentToArticle(
            @AuthResolver final Member member,        // TODO 현재는 ArgumentResolver 로 대체. 하지만 나중에 토큰값에서 꺼내오는것으로 변경해야 한다.
            @PathVariable final Optional<Long> id,
            @Valid @RequestBody final AppendCommentReq appendCommentReq
    ) {
        final MemberDto memberDto = MemberDto.from(member);
        final Long articleId = id.orElse(0L);
        final CommentDto commentDto = appendCommentReq.toDto();
        commentService.appendCommentToArticle(memberDto, articleId, commentDto);
    }

    @GetMapping("/api/article/comment/best")
    public APIResponse<List<BestCommentResponse>> bestCommentUntilLastWeek() {
        final List<BestCommentResponse> bestCommentResponses = commentService.bestCommentRankViaLastWeek();
        final APIResponse<List<BestCommentResponse>> apiResponse = APIResponse.SUCCESS(bestCommentResponses);
        return apiResponse;
    }

}
