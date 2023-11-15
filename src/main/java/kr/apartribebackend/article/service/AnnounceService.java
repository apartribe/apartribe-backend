package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.*;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.exception.CantDeleteBoardCauseInvalidMemberException;
import kr.apartribebackend.article.exception.CantUpdateBoardCauseInvalidMemberException;
import kr.apartribebackend.article.repository.BoardRepository;
import kr.apartribebackend.article.repository.announce.AnnounceRepository;
import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.attachment.service.AttachmentService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AnnounceService {

    private final BoardRepository boardRepository;
    private final AnnounceRepository announceRepository;
    private final LikeService likeService;
    private final CommentRepository commentRepository;
    private final BoardLikedRepository boardLikedRepository;
    private final CommentLikedRepository commentLikedRepository;

    /**
     * 공지사항 게시글 단일 조회 (1) - 쿼리 여러방 나눠서 실행
     * @param memberDto
     * @param apartId
     * @param announceId
     * @return
     */
    public SingleAnnounceWithLikedResponse findSingleAnnounceById(final MemberDto memberDto,
                                                                  final String apartId,
                                                                  final Long announceId) {
        final SingleAnnounceResponse singleAnnounceResponse = announceRepository.findAnnounceForApartId(apartId, announceId)
                .map(announce -> SingleAnnounceResponse.from(announce, announce.getMember()))
                .orElseThrow(ArticleNotFoundException::new);

        final BoardLikedRes memberLikedToBoard = likeService.isMemberLikedToBoard(memberDto.getId(), announceId);
        return SingleAnnounceWithLikedResponse.from(singleAnnounceResponse, memberLikedToBoard);
    }

    /**
     * 공지사항 게시글 단일 조회 (2) - SubQuery 를 포함한 한방쿼리 실행 + apartCode 정보
     * @param memberDto
     * @param apartId
     * @param announceId
     * @return
     */
    public SingleAnnounceResponseProjection findSingleAnnounceById2(final MemberDto memberDto,
                                                                    final String apartId,
                                                                    final Long announceId) {
        return announceRepository.findAnnounceWithApartCodeForApartId(memberDto, apartId, announceId);
    }

    /**
     * 공지사항 게시글 전체 조회 + 페이징
     * @param apartId
     * @param level
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<AnnounceResponse> findMultipleAnnouncesByLevel(final String apartId,
                                                               final Level level,
                                                               final Pageable pageable) {
        return announceRepository.findAnnouncesByLevel(apartId, level, pageable);
    }

    /**
     * 공지사항 게시글 등록
     * @param announceDto
     * @param memberDto
     * @return
     */
    public Announce appendArticle(final AnnounceDto announceDto,
                                  final MemberDto memberDto) {
        final Member member = memberDto.toEntity();
        final Announce article = announceDto.toEntity(member);
        return announceRepository.save(article);
    }

    /**
     * 공지사항 게시글 업데이트
     * @param apartId
     * @param announceId
     * @param announceDto
     * @param memberDto
     * @return
     */
    public SingleAnnounceResponse updateAnnounce(final String apartId,
                                                 final Long announceId,
                                                 final AnnounceDto announceDto,
                                                 final MemberDto memberDto) {
        final Announce announceEntity = announceRepository.findAnnounceForApartId(apartId, announceId)
                .orElseThrow(ArticleNotFoundException::new);
        if (!announceEntity.getMember().getId().equals(memberDto.getId())) {
            throw new CantUpdateBoardCauseInvalidMemberException();
        }
        if (announceDto.getThumbnail() == null) {
            final Announce updatedAnnounce = announceEntity.updateAnnounce(
                    announceDto.getLevel(), announceDto.getTitle(), announceDto.getContent(),
                    announceDto.getFloatFrom(), announceDto.getFloatTo(), announceDto.isOnlyApartUser()
            );
            return SingleAnnounceResponse.from(updatedAnnounce, updatedAnnounce.getMember());
        }
        final Announce updatedAnnounce = announceEntity.updateAnnounce(
                announceDto.getLevel(), announceDto.getTitle(), announceDto.getContent(),
                announceDto.getFloatFrom(), announceDto.getFloatTo(), announceDto.getThumbnail(),
                announceDto.isOnlyApartUser()
        );
        return SingleAnnounceResponse.from(updatedAnnounce, updatedAnnounce.getMember());

    }

    /**
     * 공지사항 게시글 삭제
     * @param memberDto
     * @param apartId
     * @param announceId
     */
    public void removeAnnounce(final MemberDto memberDto, final String apartId, final Long announceId) {
        final Announce findedAnnounce = announceRepository.findAnnounceForApartId(apartId, announceId)
                .orElseThrow(ArticleNotFoundException::new);
        if (!findedAnnounce.getMember().getId().equals(memberDto.getId())) {
            throw new CantDeleteBoardCauseInvalidMemberException();
        }
        boardLikedRepository.deleteAllInBatch(findedAnnounce.getBoardLikedList());
        if (!findedAnnounce.getComments().isEmpty()) {
            final List<Comment> commentsForBoard = commentRepository.findCommentsByBoardId(findedAnnounce.getId());
            final List<Long> commentIdsForBoard = commentsForBoard.stream().map(Comment::getId).toList();
            final List<CommentLiked> commentLikedsForBoardComments = likeService.findCommentLikedsInCommentIds(commentIdsForBoard);
            commentLikedRepository.deleteAllInBatch(commentLikedsForBoardComments);
            final List<Comment> parentCommentsForBoard = commentsForBoard.stream().filter(comment -> comment.getParent() == null).toList();
            final List<Comment> parentCommentRepliesForBoard = commentsForBoard.stream().filter(comment -> !parentCommentsForBoard.contains(comment)).toList();
            commentRepository.deleteAllInBatch(parentCommentRepliesForBoard);
            commentRepository.deleteAllInBatch(commentsForBoard);
        }
        boardRepository.delete(findedAnnounce);
    }

    /**
     * 공지사항 게시글 좋아요
     * @param memberDto
     * @param apartId
     * @param announceId
     * @return
     */
    public BoardLikedRes updateLikeByAnnounceId(final MemberDto memberDto, final String apartId, final Long announceId) {
        final Board announce = boardRepository.findBoardWithMemberAndApartmentForApartId(apartId, announceId)
                .orElseThrow(CannotReflectLikeToArticleException::new);
        if (announce.isOnlyApartUser()) {
            if (!announce.getMember().getApartment().getCode().equals(memberDto.getApartmentDto().getCode())) {
                throw new CantLikeToBoardCauseBoardIsApartUserOnlyException();
            }
        }
        final BoardLiked boardLiked = likeService.findBoardLikedByMember(memberDto.getId(), announce.getId())
                .orElse(null);
        if (boardLiked != null) {
            return likeService.decreaseLikesToBoard(boardLiked, announce);
        }
        return likeService.increaseLikesToBoard(memberDto.toEntity(), announce);
    }

    /**
     * 공지사항 위젯
     * @param apartId
     * @return
     */
    @Transactional(readOnly = true)
    public List<AnnounceWidgetRes> findWidgetValues(final String apartId) {
        return announceRepository.findWidgetValues(apartId);
    }

}

//    public void appendArticle(final AnnounceDto announceDto,
//                              final MemberDto memberDto,
//                              final List<MultipartFile> file) throws IOException {
//        final Announce announce = appendArticle(announceDto, memberDto);
//        final List<Attachment> attachments = attachmentService.saveFiles(file);
//        for (Attachment attachment : attachments) {
//            attachment.registBoard(announce);
//        }
//        attachmentService.saveAttachments(attachments);
//    }