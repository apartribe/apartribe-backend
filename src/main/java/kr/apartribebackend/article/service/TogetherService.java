package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.article.dto.together.*;
import kr.apartribebackend.article.exception.CantDeleteBoardCauseInvalidMemberException;
import kr.apartribebackend.article.exception.CantUpdateBoardCauseInvalidMemberException;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.repository.CommentRepository;
import kr.apartribebackend.likes.domain.CommentLiked;
import kr.apartribebackend.likes.dto.BoardLikedRes;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.repository.BoardRepository;
import kr.apartribebackend.article.repository.together.TogetherRepository;
import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.exception.CategoryNonExistsException;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.likes.domain.BoardLiked;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static kr.apartribebackend.category.domain.CategoryTag.*;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TogetherService {

    private final BoardRepository boardRepository;
    private final TogetherRepository togetherRepository;
    private final CategoryRepository categoryRepository;
    private final LikeService likeService;
    private final CommentLikedRepository commentLikedRepository;
    private final BoardLikedRepository boardLikedRepository;
    private final CommentRepository commentRepository;

    /**
     * 함께해요 단일 게시물 조회 (1) - 쿼리 여러번 나눠서 실행
     * @param memberDto
     * @param apartId
     * @param togetherId
     * @return
     */
    @Transactional
    public SingleTogetherWithLikedResponse findSingleTogetherById(final MemberDto memberDto,
                                                                  final String apartId,
                                                                  final Long togetherId) {
        final SingleTogetherResponse singleTogetherResponse = togetherRepository
                .findTogetherForApartId(apartId, togetherId)
                .map(together -> SingleTogetherResponse.from(together, together.getMember()))
                .orElseThrow(ArticleNotFoundException::new);

        final BoardLikedRes memberLikedToBoard = likeService.isMemberLikedToBoard(memberDto.getId(), togetherId);
        return SingleTogetherWithLikedResponse.from(singleTogetherResponse, memberLikedToBoard);
    }

    /**
     * 함께해요 게시글 단일 조회 (2) - SubQuery 를 포함한 한방쿼리 실행 + apartCode 정보
     * @param memberDto
     * @param apartId
     * @param togetherId
     * @return
     */
    @Transactional
    public SingleTogetherResponseProjection findSingleTogetherById2(final MemberDto memberDto,
                                                                    final String apartId,
                                                                    final Long togetherId) {
        return togetherRepository.findTogetherWithApartCodeForApartId(memberDto, apartId, togetherId);
    }

    /**
     * 함께해요 게시글 전체 조회 + 페이징
     * @param apartId
     * @param category
     * @param pageable
     * @return
     */
    public Page<TogetherResponse> findMultipleTogethersByCategory(final String apartId,
                                                                  final String category,
                                                                  final Pageable pageable) {
        return togetherRepository.findMultipleTogethersByCategory(apartId, category, pageable);
    }

    /**
     * 함께해요 게시글 작성
     * @param apartId
     * @param category
     * @param memberDto
     * @param togetherDto
     * @return
     */
    @Transactional
    public Together appendTogether(final String apartId,
                                   final String category,
                                   final MemberDto memberDto,
                                   final TogetherDto togetherDto) {
        final Category categoryEntity = categoryRepository.findCategoryByTagAndNameWithApart(apartId, TOGETHER, category)
                .orElseThrow(CategoryNonExistsException::new);
        final Member member = memberDto.toEntity();
        final Together together = togetherDto.toEntity(categoryEntity, member);
        return togetherRepository.save(together);
    }

    /**
     * 함께해요 게시글 좋아요
     * @param memberDto
     * @param apartId
     * @param togetherId
     * @return
     */
    @Transactional
    public BoardLikedRes updateLikeByTogetherId(final MemberDto memberDto, final String apartId, final Long togetherId) {
        final Board together = boardRepository.findBoardWithMemberAndApartmentForApartId(apartId, togetherId)
                .orElseThrow(CannotReflectLikeToArticleException::new);
        if (together.isOnlyApartUser()) {
            if (!together.getMember().getApartment().getCode().equals(memberDto.getApartmentDto().getCode())) {
                throw new CantLikeToBoardCauseBoardIsApartUserOnlyException();
            }
        }
        final BoardLiked boardLiked = likeService.findBoardLikedByMember(memberDto.getId(), together.getId())
                .orElse(null);
        if (boardLiked != null) {
            return likeService.decreaseLikesToBoard(boardLiked, together);
        }
        return likeService.increaseLikesToBoard(memberDto.toEntity(), together);
    }

    /**
     * 함께해요 게시글 수정
     * @param apartId
     * @param togetherId
     * @param category
     * @param togetherDto
     * @param memberDto
     * @return
     */
    @Transactional
    public SingleTogetherResponse updateTogether(final String apartId,
                                                 final Long togetherId,
                                                 final String category,
                                                 final TogetherDto togetherDto,
                                                 final MemberDto memberDto) {
        final Together togetherEntity = togetherRepository.findTogetherForApartId(apartId, togetherId)
                .orElseThrow(ArticleNotFoundException::new);
        final Category categoryEntity = categoryRepository.findCategoryByTagAndNameWithApart(apartId, TOGETHER, category)
                .orElseThrow(CategoryNonExistsException::new);
        if (!togetherEntity.getMember().getId().equals(memberDto.getId())) {
            throw new CantUpdateBoardCauseInvalidMemberException();
        }
        final Together updatedTogether = togetherEntity.updateTogether(
                categoryEntity, togetherDto.getTitle(), togetherDto.getDescription(),
                togetherDto.getContent(), togetherDto.getRecruitFrom(), togetherDto.getRecruitTo(),
                togetherDto.getMeetTime(), togetherDto.getTarget(), togetherDto.getLocation(),
                togetherDto.isContributeStatus(), togetherDto.getRecruitStatus(), togetherDto.getThumbnail(),
                togetherDto.isOnlyApartUser()
        );
        return SingleTogetherResponse.from(updatedTogether, togetherEntity.getMember());
    }

    /**
     * 함께해요 게시글 삭제
     * @param memberDto
     * @param apartId
     * @param togetherId
     */
    @Transactional
    public void removeTogether(final MemberDto memberDto, final String apartId, final Long togetherId) {
        final Together findedTogether = togetherRepository.findTogetherForApartId(apartId, togetherId)
                .orElseThrow(ArticleNotFoundException::new);
        if (!findedTogether.getMember().getId().equals(memberDto.getId())) {
            throw new CantDeleteBoardCauseInvalidMemberException();
        }
        boardLikedRepository.deleteAllInBatch(findedTogether.getBoardLikedList());
        if (!findedTogether.getComments().isEmpty()) {
            final List<Comment> commentsForBoard = commentRepository.findCommentsByBoardId(findedTogether.getId());
            final List<Long> commentIdsForBoard = commentsForBoard.stream().map(Comment::getId).toList();
            final List<CommentLiked> commentLikedsForBoardComments = likeService.findCommentLikedsInCommentIds(commentIdsForBoard);
            commentLikedRepository.deleteAllInBatch(commentLikedsForBoardComments);
            final List<Comment> parentCommentsForBoard = commentsForBoard.stream().filter(comment -> comment.getParent() == null).toList();
            final List<Comment> parentCommentRepliesForBoard = commentsForBoard.stream().filter(comment -> !parentCommentsForBoard.contains(comment)).toList();
            commentRepository.deleteAllInBatch(parentCommentRepliesForBoard);
            commentRepository.deleteAllInBatch(commentsForBoard);
        }
        boardRepository.delete(findedTogether);
    }
}
