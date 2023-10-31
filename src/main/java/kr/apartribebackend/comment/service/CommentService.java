package kr.apartribebackend.comment.service;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.repository.BoardRepository;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.dto.*;
import kr.apartribebackend.comment.eception.*;
import kr.apartribebackend.comment.repository.CommentRepository;
import kr.apartribebackend.global.dto.PageCondition;
import kr.apartribebackend.likes.domain.CommentLiked;
import kr.apartribebackend.likes.dto.CommentLikedRes;
import kr.apartribebackend.likes.service.LikeService;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final LikeService likeService;

    @Transactional
    public CommentDto appendCommentToBoard(final String apartCode,
                                           final MemberDto memberDto,
                                           final Long boardId,
                                           final CommentDto commentDto) {
        final Board board = boardRepository.findBoardForApartId(apartCode, boardId)
                .orElseThrow(CannotApplyCommentException::new);
        final Comment comment = commentDto.toEntity(memberDto.toEntity(), board);
        comment.registBoard(board);
        final Comment savedComment = commentRepository.save(comment);
        return CommentDto.from(savedComment, memberDto.toEntity());
    }

    @Transactional
    public CommentDto appendCommentReplyToBoard(final MemberDto memberDto,
                                                final Long boardId,
                                                final Long parentId,
                                                final CommentDto commentDto) {
        final Comment boardComment = commentRepository
                .findCommentWithBoardByBoardIdAndCommentId(boardId, parentId)
                .orElseThrow(CannotApplyReplyCommentException::new);
        if (boardComment.getParent() != null) {
            throw new CommentDepthException();
        }
        final Board board = boardComment.getBoard();
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

    @Transactional
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

    @Transactional
    public CommentLikedRes updateLikeByCommentId(final MemberDto memberDto,
                                                 final String apartId,
                                                 final Long boardId,
                                                 final Long commentId) {
        final Comment comment = commentRepository.findCommentForApartId(apartId, boardId, commentId)
                .orElseThrow(CannotReflectLikeToCommentException::new);

        final CommentLiked commentLiked = likeService.findCommentLikedByMember(memberDto.getId(), comment.getId())
                .orElse(null);
        if (commentLiked != null) {
            return likeService.decreaseLikesToComment(commentLiked, comment);
        }
        return likeService.increaseLikesToComment(memberDto.toEntity(), comment);
    }
}

//    @Transactional
//    public CommentDto updateCommentForBoard(final String apartCode,
//                                            final MemberDto memberDto,
//                                            final Long boardId,
//                                            final CommentDto commentDto) {
//        final Board board = boardRepository.findBoardForApartId(apartCode, boardId)
//                .orElseThrow(ArticleNotFoundException::new);
//        final Comment comment = commentRepository.findCommentByBoardIdAndCommentId(boardId, commentDto.getId())
//                .orElseThrow(CannotFoundParentCommentInBoardException::new);
//        final Long requestMemberId = memberDto.getId();
//        final Member commentMember = comment.getMember();
//        if (!requestMemberId.equals(commentMember.getId())) {
//            throw new CantUpdateCommentCauseInvalidMember();
//        }
//        final Comment updatedComment = comment.updateComment(commentDto.getContent());
//        return CommentDto.from(updatedComment, commentMember);
//    }

//    public List<MemberCommentRes> fetchCommentsForMember(final MemberDto memberDto) {
//        final Member memberEntity = memberDto.toEntity();
//        final List<MemberCommentRes> commentsForMember = commentRepository
//                .findCommentsForMember(memberEntity);
//        return commentsForMember;
//    }

//    @Transactional
//    public CommentDto appendCommentReplyToBoard(final String apartCode,
//                                                final MemberDto memberDto,
//                                                final Long boardId,
//                                                final Long parentId,
//                                                final CommentDto commentDto) {
//        final Board board = boardRepository.findBoardForApartId(apartCode, boardId)
//                .orElseThrow(CannotApplyCommentException::new);
//        final Comment boardComment = commentRepository.findCommentByBoardIdAndCommentId(boardId, parentId)
//                .orElseThrow(CannotFoundParentCommentInBoardException::new);
//        if (boardComment.getParent() != null) {
//            throw new CommentDepthException();
//        }
//        final Member member = board.getMember();
//        final Comment comment = commentDto.toEntity(memberDto.toEntity(), board);
//        comment.registParent(boardComment);
//        comment.registBoard(board);
//        final Comment savedComment = commentRepository.save(comment);
//        return CommentDto.from(savedComment, member);
//    }