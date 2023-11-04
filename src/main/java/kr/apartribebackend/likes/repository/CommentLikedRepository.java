package kr.apartribebackend.likes.repository;

import kr.apartribebackend.likes.domain.CommentLiked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentLikedRepository
        extends JpaRepository<CommentLiked, Long>, CustomCommentLikedRepository {

    @Query(value = "select cl from CommentLiked as cl where cl.comment.id in :commentIds")
    List<CommentLiked> findCommentLikedsInCommentIds(@Param("commentIds") List<Long> commentIds);

    @Query(value = "select cl from CommentLiked as cl where cl.member.id = :memberId")
    List<CommentLiked> findCommentLikedsByMemberId(@Param("memberId") Long memberId);

}
