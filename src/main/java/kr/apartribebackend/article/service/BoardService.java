//package kr.apartribebackend.article.service;
//
//import kr.apartribebackend.article.domain.Announce;
//import kr.apartribebackend.article.domain.Article;
//import kr.apartribebackend.article.domain.Board;
//import kr.apartribebackend.article.domain.Together;
//import kr.apartribebackend.article.exception.ArticleNotFoundException;
//import kr.apartribebackend.article.exception.CantDeleteBoardCauseInvalidMemberException;
//import kr.apartribebackend.article.repository.ArticleRepository;
//import kr.apartribebackend.article.repository.BoardRepository;
//import kr.apartribebackend.article.repository.announce.AnnounceRepository;
//import kr.apartribebackend.article.repository.together.TogetherRepository;
//import kr.apartribebackend.comment.domain.Comment;
//import kr.apartribebackend.comment.repository.CommentRepository;
//import kr.apartribebackend.likes.domain.CommentLiked;
//import kr.apartribebackend.likes.repository.BoardLikedRepository;
//import kr.apartribebackend.likes.repository.CommentLikedRepository;
//import kr.apartribebackend.likes.service.LikeService;
//import kr.apartribebackend.member.dto.MemberDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//import static kr.apartribebackend.article.domain.BoardType.*;
//
//@Transactional
//@RequiredArgsConstructor
//@Service
//public class BoardService {
//
//    private final BoardRepository boardRepository;
//    private final AnnounceRepository announceRepository;
//    private final ArticleRepository articleRepository;
//    private final TogetherRepository togetherRepository;
//    private final CommentRepository commentRepository;
//    private final LikeService likeService;
//    private final BoardLikedRepository boardLikedRepository;
//    private final CommentLikedRepository commentLikedRepository;
//
//    public void removeAnnounce(final MemberDto memberDto, final String apartId, final Long boardId) {
//        removeBoard(memberDto, apartId, boardId, ANNOUNCE);
//    }
//
//    public void removeArticle(final MemberDto memberDto, final String apartId, final Long boardId) {
//        removeBoard(memberDto, apartId, boardId, ARTICLE);
//    }
//
//    public void removeTogether(final MemberDto memberDto, final String apartId, final Long boardId) {
//        removeBoard(memberDto, apartId, boardId, TOGETHER);
//    }
//
//    public void removeBoard(final MemberDto memberDto, final String apartId, final Long boardId, final String boardType) {
//        final Optional<? extends Board> findedBoard;
//        if (boardType.equals(ARTICLE)) {
//            findedBoard = articleRepository.findArticleForApartId(apartId, boardId);
//        }
//        if (boardType.equals(ANNOUNCE)) {
//            findedBoard =  togetherRepository.findTogetherForApartId(apartId, boardId);
//        }
//        if (boardType.equals(TOGETHER)) {
//            findedBoard =  announceRepository.findAnnounceForApartId(apartId, boardId);
//            Article article = castToImplementedType(findedBoard, Article.class);
//        }
//
//        final Announce findedAnnounce = announceRepository.findAnnounceForApartId(apartId, boardId);
//
//        if (!findedAnnounce.getMember().getId().equals(memberDto.getId())) {
//            throw new CantDeleteBoardCauseInvalidMemberException();
//        }
//        boardLikedRepository.deleteAllInBatch(findedAnnounce.getBoardLikedList());
//        if (!findedAnnounce.getComments().isEmpty()) {
//            final List<Comment> commentsForBoard = commentRepository.findCommentsByBoardId(findedAnnounce.getId());
//            final List<Long> commentIdsForBoard = commentsForBoard.stream().map(Comment::getId).toList();
//            final List<CommentLiked> commentLikedsForBoardComments = likeService.findCommentLikedsInCommentIds(commentIdsForBoard);
//            commentLikedRepository.deleteAllInBatch(commentLikedsForBoardComments);
//            final List<Comment> parentCommentsForBoard = commentsForBoard.stream().filter(comment -> comment.getParent() == null).toList();
//            final List<Comment> parentCommentRepliesForBoard = commentsForBoard.stream().filter(comment -> !parentCommentsForBoard.contains(comment)).toList();
//            commentRepository.deleteAllInBatch(parentCommentRepliesForBoard);
//            commentRepository.deleteAllInBatch(commentsForBoard);
//        }
//        boardRepository.delete(findedAnnounce);
//    }
//
////    public <T extends Board> T decideAndExtractBoardEntity(final String apartId,
////                                                           final Long boardId,
////                                                           final String boardType) {
////        return switch (boardType) {
////            case ARTICLE -> castToImplementedType(articleRepository.findArticleForApartId(apartId, boardId), Article.class);
////            case TOGETHER -> castToImplementedType(togetherRepository.findTogetherForApartId(apartId, boardId), Together.class);
////            case ANNOUNCE -> castToImplementedType(announceRepository.findAnnounceForApartId(apartId, boardId), Announce.class);
////            default -> throw new IllegalStateException("Invalid BoardType");
////        };
////    }
////
////    public <T extends Board> T castToImplementedType(final Optional<? extends Board> board,
////                                                     final Class<T> targetType) {
////        final Board findedBoard = board.orElseThrow(ArticleNotFoundException::new);
////        if (targetType.isInstance(findedBoard)){
////            return targetType.cast(findedBoard);
////        } else {
////            throw new IllegalStateException("Invalid BoardType");
////        }
////    }
//
//    public <T extends Board> T decideAndExtractBoardEntity(final String apartId,
//                                                           final Long boardId,
//                                                           final String boardType) {
//    }
//
//    public <T extends Board> T castToImplementedType(final Optional<? extends Board> board,
//                                                     final Class<T> targetType) {
//        final Board findedBoard = board.orElseThrow(ArticleNotFoundException::new);
//        if (targetType.isInstance(findedBoard)){
//            return targetType.cast(findedBoard);
//        } else {
//            throw new IllegalStateException("Invalid BoardType");
//        }
//    }
//
//}
//
////    public <T extends Board> T castToImplementedType(final String apartId,
////                                                     final Long boardId,
////                                                     final Board board,
////                                                     final Class<T> targetType) {
////        if (targetType.isInstance(board)){
////            return targetType.cast(board);
////        } else {
////            throw new IllegalStateException("Invalid BoardType");
////        }
////    }
//
////    public Optional<? extends Board> decideAndExtractBoardEntity(final String apartId,
////                                                                 final Long boardId,
////                                                                 final String boardType) {
////        return switch (boardType) {
////            case ARTICLE -> articleRepository.findArticleForApartId(apartId, boardId);
////            case TOGETHER -> togetherRepository.findTogetherForApartId(apartId, boardId);
////            case ANNOUNCE -> announceRepository.findAnnounceForApartId(apartId, boardId);
////            default -> throw new IllegalStateException("Invalid BoardType");
////        };
////    }
//
//
////    private <T extends Board> T convertToImplementedEntity(final String apartId,
////                                                           final Long boardId,
////                                                           final Board board) {
////        if (board instanceof Announce) {
////            return decideAndExtractBoardEntity(apartId, boardId, Announce.class);
////        } else if (board instanceof Together) {
////        } else if (board instanceof Article) {
////
////        }
////        throw new IllegalStateException("Invalid BoardType");
////    }
