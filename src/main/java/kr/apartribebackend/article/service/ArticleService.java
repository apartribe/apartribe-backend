package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.dto.*;
import kr.apartribebackend.article.dto.SingleArticleResponseProjection;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.CantUpdateBoardCauseInvalidMemberException;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.exception.CantDeleteBoardCauseInvalidMemberException;
import kr.apartribebackend.article.repository.ArticleRepository;
import kr.apartribebackend.article.repository.BoardRepository;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.exception.CategoryNonExistsException;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.repository.CommentRepository;
import kr.apartribebackend.likes.domain.BoardLiked;
import kr.apartribebackend.likes.domain.CommentLiked;
import kr.apartribebackend.likes.dto.BoardLikedRes;
import kr.apartribebackend.likes.exception.CantLikeToBoardCauseBoardIsApartUserOnlyException;
import kr.apartribebackend.likes.repository.BoardLikedRepository;
import kr.apartribebackend.likes.repository.CommentLikedRepository;
import kr.apartribebackend.likes.service.LikeService;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.apartribebackend.category.domain.CategoryTag.*;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;
    private final LikeService likeService;
    private final BoardLikedRepository boardLikedRepository;
    private final CommentRepository commentRepository;
    private final CommentLikedRepository commentLikedRepository;

    /**
     * 커뮤니티 게시글 단일 조회 (1) - 쿼리를 나눠서 세번 실행
     * 1. 게시글의 조회수 를 1 증가시키는 쿼리
     * 2. SingleArticleResponse 가 조회되는 쿼리
     * 3. 게시글에 좋아요가 달려있는지 확인하는 쿼리
     * @param memberDto
     * @param apartId
     * @param articleId
     * @return
     */
    @Transactional
    public SingleArticleWithLikedResponse findSingleArticleById(final MemberDto memberDto,
                                                                final String apartId,
                                                                final Long articleId) {
        final SingleArticleResponse singleArticleResponse = articleRepository.findArticleForApartId(apartId, articleId)
                .map(article -> SingleArticleResponse.from(article, article.getMember()))
                .orElseThrow(ArticleNotFoundException::new);

        final BoardLikedRes memberLikedToBoard = likeService.isMemberLikedToBoard(memberDto.getId(), articleId);
        return SingleArticleWithLikedResponse.from(singleArticleResponse, memberLikedToBoard);
    }

    /**
     * 커뮤니티 게시글 단일 조회 (2) - SubQuery(좋아요 여부, 게시글 작성자 일치여부) + BulkQuery(조회수 증가) 를 이용한 한방쿼리 + apartCode 정보
     * @param memberDto
     * @param apartId
     * @param articleId
     * @return
     */
    @Transactional
    public SingleArticleResponseProjection findSingleArticleById2(final MemberDto memberDto,
                                                                  final String apartId,
                                                                  final Long articleId) {
        return articleRepository.findArticleWithApartCodeForApartId(memberDto, apartId, articleId);
    }

    /**
     * 커뮤니티 게시글 전체 조회 + 페이징
     * @param apartId
     * @param category
     * @param pageable
     * @return
     */
    public Page<ArticleResponse> findMultipleArticlesByCategory(final String apartId,
                                                                final String category,
                                                                final Pageable pageable) {
        return articleRepository.findArticlesByCategory(apartId, category, pageable);
    }

    /**
     * 커뮤니티 게시글 생성
     * @param apartId
     * @param category
     * @param articleDto
     * @param memberDto
     * @return
     */
    @Transactional
    public Article appendArticle(final String apartId,
                                 final String category,
                                 final ArticleDto articleDto,
                                 final MemberDto memberDto) {
        final Category categoryEntity = categoryRepository.findCategoryByTagAndNameWithApart(apartId, ARTICLE, category)
                .orElseThrow(CategoryNonExistsException::new);
        final Member memberEntity = memberDto.toEntity();
        final Article articleEntity = articleDto.toEntity(categoryEntity, memberEntity);
        return articleRepository.save(articleEntity);
    }

    /**
     * 커뮤니티 게시글 수정
     * @param apartId
     * @param articleId
     * @param category
     * @param articleDto
     * @param memberDto
     * @return
     */
    @Transactional
    public SingleArticleResponse updateArticle(final String apartId,
                                               final Long articleId,
                                               final String category,
                                               final ArticleDto articleDto,
                                               final MemberDto memberDto) {
        final Article articleEntity = articleRepository.findArticleForApartId(apartId, articleId)
                .orElseThrow(ArticleNotFoundException::new);
        final Category categoryEntity = categoryRepository.findCategoryByTagAndNameWithApart(apartId, ARTICLE, category)
                .orElseThrow(CategoryNonExistsException::new);
        if (!articleEntity.getMember().getId().equals(memberDto.getId())) {
            throw new CantUpdateBoardCauseInvalidMemberException();
        }
        if (articleDto.getThumbnail() == null) {
            final Article updatedArticle = articleEntity.updateArticle(
                    categoryEntity, articleDto.getTitle(), articleDto.getContent(), articleDto.isOnlyApartUser()
            );
            return SingleArticleResponse.from(updatedArticle, updatedArticle.getMember());
        }
        final Article updatedArticle = articleEntity.updateArticle(
                categoryEntity, articleDto.getTitle(), articleDto.getContent(),
                articleDto.getThumbnail(), articleDto.isOnlyApartUser()
        );
        return SingleArticleResponse.from(updatedArticle, updatedArticle.getMember());
    }

    /**
     * 커뮤니티 게시글 삭제
     * @param memberDto
     * @param apartId
     * @param articleId
     */
    @Transactional
    public void removeArticle(final MemberDto memberDto, final String apartId, final Long articleId) {
        final Article findedArticle = articleRepository.findArticleForApartId(apartId, articleId)
                .orElseThrow(ArticleNotFoundException::new);
        if (!findedArticle.getMember().getId().equals(memberDto.getId())) {
            throw new CantDeleteBoardCauseInvalidMemberException();
        }
        boardLikedRepository.deleteAllInBatch(findedArticle.getBoardLikedList());
        if (!findedArticle.getComments().isEmpty()) {
            final List<Comment> commentsForBoard = commentRepository.findCommentsByBoardId(findedArticle.getId());
            final List<Long> commentIdsForBoard = commentsForBoard.stream().map(Comment::getId).toList();
            final List<CommentLiked> commentLikedsForBoardComments = likeService.findCommentLikedsInCommentIds(commentIdsForBoard);
            commentLikedRepository.deleteAllInBatch(commentLikedsForBoardComments);
            final List<Comment> parentCommentsForBoard = commentsForBoard.stream().filter(comment -> comment.getParent() == null).toList();
            final List<Comment> parentCommentRepliesForBoard = commentsForBoard.stream().filter(comment -> !parentCommentsForBoard.contains(comment)).toList();
            commentRepository.deleteAllInBatch(parentCommentRepliesForBoard);
            commentRepository.deleteAllInBatch(commentsForBoard);
        }
        boardRepository.delete(findedArticle);
    }

    /**
     * 커뮤니티 게시글 좋아요
     * @param memberDto
     * @param apartId
     * @param articleId
     * @return
     */
    @Transactional
    public BoardLikedRes updateLikeByArticleId(final MemberDto memberDto, final String apartId, final Long articleId) {
        final Board article = boardRepository.findBoardWithMemberAndApartmentForApartId(apartId, articleId)
                .orElseThrow(CannotReflectLikeToArticleException::new);
        if (article.isOnlyApartUser()) {
            if (!article.getMember().getApartment().getCode().equals(memberDto.getApartmentDto().getCode())) {
                throw new CantLikeToBoardCauseBoardIsApartUserOnlyException();
            }
        }
        final BoardLiked boardLiked = likeService.findBoardLikedByMember(memberDto.getId(), article.getId())
                .orElse(null);
        if (boardLiked != null) {
            return likeService.decreaseLikesToBoard(boardLiked, article);
        }
        return likeService.increaseLikesToBoard(memberDto.toEntity(), article);
    }

    /**
     * 베스트 게시물 (좋아요 순) Widget
     * @param apartId
     * @return
     */
    public List<Top5ArticleResponse> findTop5ArticleViaLiked(final String apartId) {
        return articleRepository.findTop5ArticleViaLiked(apartId);
    }

    /**
     * 베스트 게시물 (조회수 순) Widget
     * @param apartId
     * @return
     */
    public List<Top5ArticleResponse> findTop5ArticleViaView(final String apartId) {
        return articleRepository.findTop5ArticleViaView(apartId);
    }

    /**
     * 커뮤니티 내 게시글 검색 Widget
     * @param apartId
     * @param title
     * @return
     */
    public List<ArticleInCommunityRes> searchArticleInCommunity(final String apartId, final String title) {
        return articleRepository.searchArticleInCommunity(apartId, title);
    }

}

//    public void removeArticle(final Board board) {
//        final List<Comment> comments = commentRepository.findCommentsForBoard(board);
//        final List<Comment> children = comments.stream()
//                .filter(comment -> !comment.getChildren().isEmpty())
//                .flatMap(comment -> comment.getChildren().stream())
//                .toList();
//
//        commentRepository.deleteAllInBatch(children);
//        commentRepository.deleteAllInBatch(comments);
//        boardRepository.delete(board);
//    }
