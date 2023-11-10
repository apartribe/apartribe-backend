package kr.apartribebackend.comment.repository;


import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.dto.BestCommentResponse;
import kr.apartribebackend.comment.dto.CommentCountRes;
import kr.apartribebackend.comment.dto.CommentResProjection;

import java.util.List;
import java.util.Optional;

public interface CustomCommentRepository {

    List<BestCommentResponse> bestCommentRankViaLastWeek(final String apartCode);

    List<CommentResProjection> findCommentsByBoardId(final Long memberId, final Long boardId);

    Optional<Comment> findCommentForApartId(final String apartId, final Long boardId, final Long commentId);

    Optional<Comment> findCommentWithMemberAndApartmentForApartId(final String apartId, final Long boardId, final Long commentId);

    CommentCountRes totalCountsForBoardComments(final Long id, final Long boardId);
}
