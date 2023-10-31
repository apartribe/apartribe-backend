package kr.apartribebackend.likes.repository;

import kr.apartribebackend.likes.domain.CommentLiked;

import java.util.Optional;

public interface CustomCommentLikedRepository {

    Optional<CommentLiked> findCommentLikedByMember(Long memberId, Long commentId);

    Integer isMemberLikedToComment(Long memberId, Long commentId);

}
