package kr.apartribebackend.comment.repository;

import kr.apartribebackend.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends
        JpaRepository<Comment, Long>, CustomCommentRepository {

    @Query(value = "select c from Comment as c" +
            " inner join fetch c.board as b" +
            " where b.id = :boardId and c.id = :commentId")
    Optional<Comment> findCommentWithBoardByBoardIdAndCommentId(
            @Param("boardId") final Long boardId,
            @Param("commentId") final Long commentId);

    @Query(value = "select c from Comment as c" +
            " inner join fetch c.board as b" +
            " inner join fetch c.member as m" +
            " where b.id = :boardId and c.id = :commentId")
    Optional<Comment> findCommentWithBoardAndMemberByBoardIdAndCommentId(
            @Param("boardId") final Long boardId,
            @Param("commentId") final Long commentId);

    @Query(value = "select c from Comment as c" +
            " inner join fetch c.board as b" +
            " inner join fetch c.member as m" +
            " inner join fetch m.apartment as a" +
            " where b.id = :boardId and c.id = :commentId")
    Optional<Comment> findCommentWithBoardAndMemberAndApartmentByBoardIdAndCommentId(
            @Param("boardId") final Long boardId,
            @Param("commentId") final Long commentId);

    @Query(value = "select c from Comment as c" +
            " inner join fetch c.member as m" +
            " inner join c.board as b" +
            " where b.id = :boardId and c.id = :commentId")
    Optional<Comment> findCommentWithMemberByBoardIdAndCommentId(
            @Param("boardId") final Long boardId,
            @Param("commentId") final Long commentId);

    @Query(value = "select c from Comment as c where c.board.id = :boardId")
    List<Comment> findCommentsByBoardId(@Param("boardId") Long boardId);

    @Query(value = "select c from Comment as c where c.member.id = :memberId and c.parent.id is not null")
    List<Comment> findChildCommentsByMemberId(@Param("memberId") Long memberId);

    @Query(value = "select c from Comment as c where c.member.id = :memberId and c.parent.id is null")
    List<Comment> findParentCommentsByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from Comment as c where c.id in :commentIds")
    int deleteCommentsUsingCommentIds(@Param("commentIds") List<Long> commentIds);

    @Query(value = "select c from Comment as c where c.board.id in :boardIds and c.parent.id is null")
    List<Comment> findParentCommentsInBoardIds(@Param("boardIds") List<Long> boardIds);

    @Query(value = "select c from Comment as c where c.board.id in :boardIds and c.parent.id is not null")
    List<Comment> findChildCommentsInBoardIds(@Param("boardIds") List<Long> boardIds);

    @Query(value = "select c from Comment as c where c.board.id in :boardIds")
    List<Comment> findCommentsInBoardIds(@Param("boardIds") List<Long> boardIds);

}
