package kr.apartribebackend.comment.service;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.repository.BoardRepository;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.dto.*;
import kr.apartribebackend.comment.eception.*;
import kr.apartribebackend.comment.repository.CommentRepository;
import kr.apartribebackend.likes.domain.CommentLiked;
import kr.apartribebackend.likes.dto.CommentLikedRes;
import kr.apartribebackend.likes.exception.CantLikeToBoardCauseBoardIsApartUserOnlyException;
import kr.apartribebackend.likes.exception.CantLikeToCommentCauseBoardIsApartUserOnlyException;
import kr.apartribebackend.likes.repository.CommentLikedRepository;
import kr.apartribebackend.likes.service.LikeService;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final LikeService likeService;
    private final CommentLikedRepository commentLikedRepository;

    public CommentDto appendCommentToBoard(final String apartCode,
                                           final MemberDto memberDto,
                                           final Long boardId,
                                           final CommentDto commentDto) {
        final Board board = boardRepository.findBoardWithMemberAndApartmentForApartId(apartCode, boardId)
                .orElseThrow(CannotApplyCommentException::new);
        if (board.isOnlyApartUser()) {
            if (!board.getMember().getApartment().getCode().equals(memberDto.getApartmentDto().getCode())) {
                throw new CantApplyCommentToBoardCauseBoardIsApartUserOnlyException();
            }
        }
        final Comment comment = commentDto.toEntity(memberDto.toEntity(), board);
        comment.registBoard(board);
        final Comment savedComment = commentRepository.save(comment);
        return CommentDto.from(savedComment, memberDto.toEntity());
    }

    public CommentDto appendCommentReplyToBoard(final MemberDto memberDto,
                                                final Long boardId,
                                                final Long parentId,
                                                final CommentDto commentDto) {
        final Comment boardComment = commentRepository
                .findCommentWithBoardAndMemberAndApartmentByBoardIdAndCommentId(boardId, parentId)
                .orElseThrow(CannotApplyReplyCommentException::new);
        if (boardComment.getParent() != null) {
            throw new CommentDepthException();
        }
        final Board board = boardComment.getBoard();
        if (board.isOnlyApartUser()) {
            if (!board.getMember().getApartment().getCode().equals(memberDto.getApartmentDto().getCode())) {
                throw new CantApplyCommentReplyToBoardCauseBoardIsApartUserOnlyException();
            }
        }
        final Comment comment = commentDto.toEntity(memberDto.toEntity(), board);
        comment.registParent(boardComment);
        comment.registBoard(board);
        final Comment savedComment = commentRepository.save(comment);
        return CommentDto.from(savedComment, memberDto.toEntity());
    }

    public List<BestCommentResponse> bestCommentRankViaLastWeek(final String apartCode) {
        return commentRepository.bestCommentRankViaLastWeek(apartCode);
    }

    public List<CommentResProjection> findCommentsByBoardId(final MemberDto memberDto, final Long boardId) {
        return commentRepository.findCommentsByBoardId(memberDto.getId(), boardId);
    }

    public CommentCountRes totalCountsForBoardComments(final MemberDto memberDto, final Long boardId) {
        return commentRepository.totalCountsForBoardComments(memberDto.getId(), boardId);
    }

    public CommentDto updateCommentForBoard(final MemberDto memberDto,
                                            final Long boardId,
                                            final CommentDto commentDto) {
        final Comment comment = commentRepository
                .findCommentWithMemberByBoardIdAndCommentId(boardId, commentDto.getId())
                .orElseThrow(CantUpdateCauseCantFindCommentException::new);
        final Long requestMemberId = memberDto.getId();
        final Member commentMember = comment.getMember();
        if (!requestMemberId.equals(commentMember.getId())) {
            throw new CantUpdateCommentCauseInvalidMemberException();
        }
        final Comment updatedComment = comment.updateComment(commentDto.getContent());
        return CommentDto.from(updatedComment, commentMember);
    }

    public CommentLikedRes updateLikeByCommentId(final MemberDto memberDto,
                                                 final String apartId,
                                                 final Long boardId,
                                                 final Long commentId) {
        final Comment comment = commentRepository.findCommentWithMemberAndApartmentForApartId(apartId, boardId, commentId)
                .orElseThrow(CannotReflectLikeToCommentException::new);
        final Board commentForBoard = comment.getBoard();
        if (commentForBoard.isOnlyApartUser()) {
            if (!commentForBoard.getMember().getApartment().getCode().equals(memberDto.getApartmentDto().getCode())) {
                throw new CantLikeToCommentCauseBoardIsApartUserOnlyException();
            }
        }
        final CommentLiked commentLiked = likeService.findCommentLikedByMember(memberDto.getId(), comment.getId())
                .orElse(null);
        if (commentLiked != null) {
            return likeService.decreaseLikesToComment(commentLiked, comment);
        }
        return likeService.increaseLikesToComment(memberDto.toEntity(), comment);
    }

    public void deleteCommentForBoard(final MemberDto memberDto, final Long boardId, final Long commentId) {
        final Comment boardComment = commentRepository.findCommentWithBoardByBoardIdAndCommentId(boardId, commentId)
                .orElseThrow(CannotDeleteCommentException::new);
        if (!memberDto.getId().equals(boardComment.getMember().getId())) {
            throw new CantDeleteCommentCauseInvalidMemberException();
        }

        ArrayList<CommentLiked> futureDeletedCommentLikedList = new ArrayList<>();
        ArrayList<Comment> futureDeletedCommentList = new ArrayList<>();
        if (boardComment.getParent() == null) { // 부모댓글일때
            if (!boardComment.getCommentLikedList().isEmpty()) {                                    // 부모댓글에 좋아요가 있으면
                futureDeletedCommentLikedList.addAll(boardComment.getCommentLikedList());           // 좋아요 삭제리스트에 부모댓글의 좋아요들을 넣고
            }
            if (!boardComment.getChildren().isEmpty()) {                                            // 부모대댓글이 존재하면
                List<Long> list = boardComment.getChildren().stream().map(Comment::getId).toList(); // 부모대댓글들의 id 들을 뽑아서
                List<CommentLiked> commentLikedsInBoardIds = commentLikedRepository.findCommentLikedsInCommentIds(list);// 대댓글들이 가진 좋아요들을 싹다 db 에서 fetch 하여
                futureDeletedCommentLikedList.addAll(commentLikedsInBoardIds);                      // 좋아요 삭제리스트에 부모대댓글들의 좋아요들을 넣고
                futureDeletedCommentList.addAll(boardComment.getChildren());                        // 댓글 삭제리스트에 부모대댓글을 싹다 넣고
            }
            commentLikedRepository.deleteAllInBatch(futureDeletedCommentLikedList);                 // 쌓여진 좋아요들을 모두 삭제
            commentRepository.deleteAllInBatch(futureDeletedCommentList);                           // 쌓여진 댓글을 모두 삭제
            commentRepository.delete(boardComment);                                                 // 부모 댓글 삭제
        } else {                                // 대댓글일때
            if (!boardComment.getCommentLikedList().isEmpty()) {                                    // 대댓글에 좋아요가 있으면
                futureDeletedCommentLikedList.addAll(boardComment.getCommentLikedList());           // 좋아요 삭제리스트에 대댓글의 좋아요들을 넣고
            }
            commentLikedRepository.deleteAllInBatch(futureDeletedCommentLikedList);                 // 쌓여진 좋아요들을 모두 삭제
            commentRepository.delete(boardComment);                                                 // 대댓글 삭제
        }
    }
}
