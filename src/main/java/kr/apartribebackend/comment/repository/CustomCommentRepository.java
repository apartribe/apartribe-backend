package kr.apartribebackend.comment.repository;


import kr.apartribebackend.comment.dto.BestCommentResponse;
import kr.apartribebackend.comment.dto.CommentRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCommentRepository {

    List<BestCommentResponse> bestCommentRankViaLastWeek(final String apartCode);

    Page<CommentRes> findCommentsByBoardId(final Long boardId, final Pageable pageable);

}
