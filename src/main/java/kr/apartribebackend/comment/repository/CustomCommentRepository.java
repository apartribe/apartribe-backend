package kr.apartribebackend.comment.repository;


import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.dto.BestCommentResponse;
import kr.apartribebackend.comment.dto.CommentCountRes;
import kr.apartribebackend.comment.dto.CommentRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomCommentRepository {

    List<BestCommentResponse> bestCommentRankViaLastWeek(final String apartCode);

    Page<CommentRes> findCommentsByBoardId(final Long memberId, final Long boardId, final Pageable pageable);

    Optional<Comment> findCommentForApartId(final String apartId, final Long boardId, final Long commentId);

    CommentCountRes totalCountsForBoardComments(final Long id, final Long boardId);
}
