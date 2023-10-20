package kr.apartribebackend.comment.service;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.repository.BoardRepository;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.dto.BestCommentResponse;
import kr.apartribebackend.comment.dto.CommentDto;
import kr.apartribebackend.comment.dto.CommentRes;
import kr.apartribebackend.comment.eception.CannotApplyCommentException;
import kr.apartribebackend.comment.eception.CommentDepthException;
import kr.apartribebackend.comment.eception.CannotFoundParentCommentInBoardException;
import kr.apartribebackend.comment.repository.CommentRepository;
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

    @Transactional
    public CommentDto appendCommentToBoard(final String apartCode,
                                           final MemberDto memberDto,
                                           final Long boardId,
                                           final Long parentId,
                                           final CommentDto commentDto) {
        final Board board = boardRepository.findBoardForApartId(apartCode, boardId)
                .orElseThrow(CannotApplyCommentException::new);
        final Member member = board.getMember();
        final Comment comment = commentDto.toEntity(memberDto.toEntity(), board);
        if (parentId != null) {
            final Comment parentComment = commentRepository.findCommentByBoardIdAndCommentId(boardId, parentId)
                    .orElseThrow(CannotFoundParentCommentInBoardException::new);
            if (parentComment.getParent() != null) {
                throw new CommentDepthException();
            }
            comment.registParent(parentComment);
        }
        comment.registBoard(board);
        final Comment savedComment = commentRepository.save(comment);
        return CommentDto.from(savedComment, member);
    }

    public List<BestCommentResponse> bestCommentRankViaLastWeek() {
        return commentRepository.bestCommentRankViaLastWeek();
    }

    public Page<CommentRes> findCommentsByBoardId(final Long boardId, final Pageable pageable) {
        return commentRepository.findCommentsByBoardId(boardId, pageable);
    }

    @Transactional
    public CommentDto updateCommentForBoard(final MemberDto memberDto,
                                      final Long boardId,
                                      final CommentDto commentDto) {
        final Board board = boardRepository.findById(boardId)
                .orElseThrow(CannotApplyCommentException::new);
        final Comment comment = commentRepository
                .findCommentByBoardIdAndCommentId(boardId, commentDto.getId())
                .orElseThrow(CannotFoundParentCommentInBoardException::new);
        // TODO 토큰에서 뽑아온 사용자 정보와 작성된 게시물의 createdBy 를 검증해야하지만, 지금은 Dummy 라 검증할 수가 없다. 알아두자.
        final Comment updatedComment = comment.updateComment(commentDto.getContent());
        final CommentDto updatedCommentDto = CommentDto.from(updatedComment);
        return updatedCommentDto;
    }

//    public List<MemberCommentRes> fetchCommentsForMember(final MemberDto memberDto) {
//        final Member memberEntity = memberDto.toEntity();
//        final List<MemberCommentRes> commentsForMember = commentRepository
//                .findCommentsForMember(memberEntity);
//        return commentsForMember;
//    }
}
