package kr.apartribebackend.comment.repository;

import kr.apartribebackend.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends
        JpaRepository<Comment, Long>, CustomCommentRepository {

//    @Query(value = "select c from Comment as c inner join c.board as b where b.id = :boardId and c.id = :commentId")
//    Optional<Comment> findCommentByBoardIdAndCommentId(
//            @Param(value = "boardId") final Long boardId,
//            @Param(value = "commentId") final Long commentId);

    @Query(value = "select c from Comment as c" +
            " inner join fetch c.board as b" +
            " where b.id = :boardId and c.id = :commentId")
    Optional<Comment> findCommentWithBoardByBoardIdAndCommentId(
            @Param(value = "boardId") final Long boardId,
            @Param(value = "commentId") final Long commentId);

    @Query(value = "select c from Comment as c" +
            " inner join fetch c.member as m" +
            " inner join c.board as b" +
            " where b.id = :boardId and c.id = :commentId")
    Optional<Comment> findCommentWithMemberByBoardIdAndCommentId(
            @Param(value = "boardId") final Long boardId,
            @Param(value = "commentId") final Long commentId);

    @Query(value = "select c from Comment as c inner join c.board as b where b.id = :boardId and c.id = :commentId and c.createdBy = :createdBy")
    Optional<Comment> findCommentByBoardIdAndCommentIdWithCreatedBy(
            @Param(value = "boardId") final Long boardId,
            @Param(value = "commentId") final Long commentId,
            @Param(value = "createdBy") final String createdBy
    );

}
