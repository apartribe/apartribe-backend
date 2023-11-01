package kr.apartribebackend.comment.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.comment.dto.*;
import kr.apartribebackend.comment.service.CommentService;
import kr.apartribebackend.global.annotation.ApartUser;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.likes.dto.CommentLikedRes;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @ApartUser
    @PostMapping("/api/{apartId}/{boardId}/comment")
    public ResponseEntity<APIResponse<SingleCommentResponse>> appendCommentToBoard(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @PathVariable final String apartId,
            @PathVariable final Long boardId,
            @Valid @RequestBody final AppendCommentReq appendCommentReq
    ) {
        final MemberDto memberDto = authenticatedMember.toDto();
        final CommentDto commentDto = appendCommentReq.toDto();
        final CommentDto savedCommentDto = commentService
                .appendCommentToBoard(apartId, memberDto, boardId, commentDto);
        final SingleCommentResponse singleCommentResponse = SingleCommentResponse.from(savedCommentDto);
        final APIResponse<SingleCommentResponse> apiResponse = APIResponse.SUCCESS(singleCommentResponse);
        return ResponseEntity.status(CREATED).body(apiResponse);
    }

    @ApartUser
    @PostMapping("/api/{apartId}/{boardId}/comment/reply")
    public ResponseEntity<APIResponse<SingleCommentResponse>> appendCommentReplyToBoard(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @PathVariable final String apartId,
            @PathVariable final Long boardId,
            @Valid @RequestBody final AppendCommentReplyReq appendCommentReplyReq
    ) {
        final Long parentId = appendCommentReplyReq.parentId();
        final MemberDto memberDto = authenticatedMember.toDto();
        final CommentDto commentDto = appendCommentReplyReq.toDto();
        final CommentDto savedCommentDto = commentService
                .appendCommentReplyToBoard(memberDto, boardId, parentId, commentDto);
        final SingleCommentResponse singleCommentResponse = SingleCommentResponse.from(savedCommentDto);
        final APIResponse<SingleCommentResponse> apiResponse = APIResponse.SUCCESS(singleCommentResponse);
        return ResponseEntity.status(CREATED).body(apiResponse);
    }

    @GetMapping("/api/{apartId}/{boardId}/comment")
    public APIResponse<List<CommentResProjection>> findCommentsByBoardId(
            @PathVariable final String apartId,
            @PathVariable final Long boardId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember) {
        final List<CommentResProjection> commentResProjections = commentService
                .findCommentsByBoardId(authenticatedMember.toDto(), boardId);
        final APIResponse<List<CommentResProjection>> apiResponse = APIResponse.SUCCESS(commentResProjections);
        return apiResponse;
    }

    @GetMapping("/api/{apartId}/{boardId}/comment/total")
    public APIResponse<CommentCountRes> totalCountsForBoardComments(
            @PathVariable final String apartId,
            @PathVariable final Long boardId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        final CommentCountRes commentCountRes = commentService
                .totalCountsForBoardComments(authenticatedMember.toDto(), boardId);
        final APIResponse<CommentCountRes> apiResponse = APIResponse.SUCCESS(commentCountRes);
        return apiResponse;
    }

    @ApartUser
    @PutMapping("/api/{apartId}/{boardId}/comment")
    public APIResponse<SingleCommentResponse> updateComment(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @PathVariable final String apartId,
            @PathVariable final Long boardId,
            @Valid @RequestBody final UpdateCommentReq updateCommentReq
    ) {
        final MemberDto memberDto = authenticatedMember.toDto();
        final CommentDto commentDto = updateCommentReq.toDto();
        final CommentDto updatedCommentDto = commentService.updateCommentForBoard(memberDto, boardId, commentDto);
        final SingleCommentResponse singleCommentResponse = SingleCommentResponse.from(updatedCommentDto);
        final APIResponse<SingleCommentResponse> apiResponse = APIResponse.SUCCESS(singleCommentResponse);
        return apiResponse;
    }

    @ApartUser
    @DeleteMapping("/api/{apartId}/{boardId}/{commentId}/comment")
    public void deleteComment(
            @PathVariable final String apartId,
            @PathVariable final Long boardId,
            @PathVariable final Long commentId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        final MemberDto memberDto = authenticatedMember.toDto();
        commentService.deleteCommentForBoard(memberDto, boardId, commentId);
    }

    @GetMapping("/api/{apartId}/{boardId}/{commentId}/comment/like")
    public APIResponse<CommentLikedRes> updateLikeByCommentId(
            @PathVariable final String apartId,
            @PathVariable final Long boardId,
            @PathVariable final Long commentId,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember) {
        final CommentLikedRes commentLikedRes = commentService
                .updateLikeByCommentId(authenticatedMember.toDto(), apartId, boardId, commentId);
        final APIResponse<CommentLikedRes> apiResponse = APIResponse.SUCCESS(commentLikedRes);
        return apiResponse;
    }

    @GetMapping("/api/{apartId}/comment/best")
    public APIResponse<List<BestCommentResponse>> bestCommentUntilLastWeek(@PathVariable final String apartId) {
        final List<BestCommentResponse> bestCommentResponses = commentService.bestCommentRankViaLastWeek(apartId);
        final APIResponse<List<BestCommentResponse>> apiResponse = APIResponse.SUCCESS(bestCommentResponses);
        return apiResponse;
    }

}

//    @ApartUser(checkApartment = false)
//    @GetMapping("/api/{apartId}/{boardId}/comment")
//    public APIResponse<PageResponse<CommentRes>> findCommentsByBoardId(
//            @PathVariable final String apartId,
//            @PathVariable final Long boardId,
//            @PageableDefault final Pageable pageable,
//            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember) {
//        final Page<CommentRes> commentsByBoardId = commentService
//                .findCommentsByBoardId(authenticatedMember.toDto(), boardId, pageable);
//        final PageResponse<CommentRes> pageResponse = PageResponse.from(commentsByBoardId);
//        final APIResponse<PageResponse<CommentRes>> apiResponse = APIResponse.SUCCESS(pageResponse);
//        return apiResponse;
//    }
