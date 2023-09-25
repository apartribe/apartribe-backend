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
    public void appendCommentToBoard(final MemberDto memberDto,
                                     final Long boardId,
                                     final Long parentId,
                                     final CommentDto commentDto) {
        final Board board = boardRepository.findById(boardId)
                .orElseThrow(CannotApplyCommentException::new);
        final Comment comment = commentDto.toEntity(memberDto.toEntity(), board);
        if (parentId != null) {
            Comment parentComment = commentRepository
                    .findCommentByBoardIdAndCommentId(boardId, parentId)
                    .orElseThrow(CannotFoundParentCommentInBoardException::new);
            if (parentComment.getParent() != null)
                throw new CommentDepthException();
            comment.registParent(parentComment);
        }
        comment.registBoard(board);
        commentRepository.save(comment);
    }

    public List<BestCommentResponse> bestCommentRankViaLastWeek() {
        return commentRepository.bestCommentRankViaLastWeek();
    }

    public Page<CommentRes> findCommentsByBoardId(final Long boardId, final Pageable pageable) {
        return commentRepository.findCommentsByBoardId(boardId, pageable);
    }

}

//    @Transactional
//    public void appendCommentToBoard(final MemberDto memberDto,
//                                     final Long articleId,
//                                     final Long parentId,
//                                     final CommentDto commentDto) {
//        boardRepository.findById(articleId)
//                .ifPresentOrElse(article -> {
//                    final Comment comment = commentDto.toEntity(memberDto.toEntity(), article);
//                    if (parentId != null) {
//                        commentRepository.findById(parentId)
//                                .ifPresentOrElse(com -> {
//                                    if (com.getParent() != null)
//                                        throw new CommentDepthException();
//                                    comment.registParent(com);
//                                }, () -> { throw new ParentCommentNotFoundException(); }); }
//                    comment.registBoard(article);
//                    commentRepository.save(comment); }, () -> { throw new CannotApplyCommentException(); });
//    }


//        articleRepository.findById(articleId)
//                .ifPresentOrElse(article ->
//                                commentRepository.save(
//                                        commentDto.toEntity(memberDto.toEntity(), article))
//                , () -> { throw new CannotApplyCommentException(); });




//package kr.apartribebackend.comment.service;
//
//import kr.apartribebackend.article.repository.ArticleRepository;
//import kr.apartribebackend.comment.domain.Comment;
//import kr.apartribebackend.comment.dto.BestCommentResponse;
//import kr.apartribebackend.comment.dto.CommentDto;
//import kr.apartribebackend.comment.eception.CannotApplyCommentException;
//import kr.apartribebackend.comment.repository.CommentRepository;
//import kr.apartribebackend.member.dto.MemberDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//@Service
//public class CommentService {
//
//    private final CommentRepository commentRepository;
//
//    private final ArticleRepository articleRepository;
//
//    @Transactional
//    public void appendCommentToArticle(final MemberDto memberDto,
//                                       final Long articleId,
//                                       final CommentDto commentDto) {
//        articleRepository.findById(articleId)
//                .ifPresentOrElse(article -> {
//                    final Comment comment = commentDto.toEntity(memberDto.toEntity(), article);
//                    comment.registBoard(article);
//                    commentRepository.save(comment);
//                }, () -> { throw new CannotApplyCommentException(); });
//    }
//
//    public List<BestCommentResponse> bestCommentRankViaLastWeek() {
//        return commentRepository.bestCommentRankViaLastWeek();
//    }
//}
//
////        articleRepository.findById(articleId)
////                .ifPresentOrElse(article ->
////                                commentRepository.save(
////                                        commentDto.toEntity(memberDto.toEntity(), article))
////                , () -> { throw new CannotApplyCommentException(); });