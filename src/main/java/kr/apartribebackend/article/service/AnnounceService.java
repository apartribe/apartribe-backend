package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.*;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.repository.BoardRepository;
import kr.apartribebackend.article.repository.announce.AnnounceRepository;
import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.attachment.service.AttachmentService;
import kr.apartribebackend.likes.domain.BoardLiked;
import kr.apartribebackend.likes.dto.BoardLikedRes;
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
    private final AttachmentService attachmentService;
    private final LikeService likeService;

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
     * 공지사항 게시글 단일 조회 (2) - SubQuery 를 포함한 한방쿼리 실행
     * @param memberDto
     * @param apartId
     * @param announceId
     * @return
     */
    public SingleAnnounceResponseProjection findSingleAnnounceById2(final MemberDto memberDto,
                                                                    final String apartId,
                                                                    final Long announceId) {
        return announceRepository.findAnnounceForApartId(memberDto.getId(), apartId, announceId)
                .orElseThrow(ArticleNotFoundException::new);
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
     * 공지사항 게시글 등록 + AWS 업로드
     * @param announceDto
     * @param memberDto
     * @param file
     * @throws IOException
     */
    public void appendArticle(final AnnounceDto announceDto,
                              final MemberDto memberDto,
                              final List<MultipartFile> file) throws IOException {
        final Announce announce = appendArticle(announceDto, memberDto);
        final List<Attachment> attachments = attachmentService.saveFiles(file);
        for (Attachment attachment : attachments) {
            attachment.registBoard(announce);
        }
        attachmentService.saveAttachments(attachments);
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
        // TODO 토큰에서 뽑아온 사용자 정보와 작성된 게시물의 createdBy 를 검증해야하지만, 지금은 Dummy 라 검증할 수가 없다. 알아두자.
        final Announce updatedAnnounce = announceEntity.updateAnnounce(
                announceDto.getLevel(),
                announceDto.getTitle(),
                announceDto.getContent(),
                announceDto.getFloatFrom(),
                announceDto.getFloatTo(),
                announceDto.getThumbnail()
        );
        return SingleAnnounceResponse.from(updatedAnnounce, updatedAnnounce.getMember());
    }

    /**
     * 공지사항 게시글 좋아요
     * @param memberDto
     * @param apartId
     * @param announceId
     * @return
     */
    public BoardLikedRes updateLikeByAnnounceId(final MemberDto memberDto, final String apartId, final Long announceId) {
        final Board announce = boardRepository.findBoardForApartId(apartId, announceId)
                .orElseThrow(CannotReflectLikeToArticleException::new);

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
