package kr.apartribebackend.comment.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.comment.dto.AppendCommentReq;
import kr.apartribebackend.comment.dto.CommentDto;
import kr.apartribebackend.comment.service.CommentService;
import kr.apartribebackend.global.annotation.AuthResolver;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
