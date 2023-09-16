package kr.apartribebackend.comment.repository;


import kr.apartribebackend.comment.dto.BestCommentResponse;

import java.util.List;

public interface CustomCommentRepository {

    List<BestCommentResponse> bestCommentRankViaLastWeek();

}
