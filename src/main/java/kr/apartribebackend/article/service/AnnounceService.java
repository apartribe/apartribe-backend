package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.AnnounceDto;
import kr.apartribebackend.article.dto.announce.AnnounceResponse;
import kr.apartribebackend.article.dto.announce.SingleAnnounceResponse;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.repository.announce.AnnounceRepository;
import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.attachment.repository.AttachmentRepository;
import kr.apartribebackend.attachment.service.AttachmentService;
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

    private final AnnounceRepository announceRepository;
    private final AttachmentService attachmentService;

    @Transactional(readOnly = true)
    public Page<AnnounceResponse> findMultipleAnnouncesByLevel(final String apartId,
                                                               final Level level,
                                                               final Pageable pageable) {
        return announceRepository.findAnnouncesByLevel(apartId, level, pageable);
    }

    @Transactional(readOnly = true)
    public Page<AnnounceResponse> findAllAnnounces(final Pageable pageable) {
        return announceRepository.findAll(pageable)
                .map(AnnounceResponse::from);
    }

    public void updateLikeByAnnounceId(final Long announceId) {
        announceRepository.findById(announceId)
                .ifPresentOrElse(Board::reflectArticleLike,
                        () -> { throw new CannotReflectLikeToArticleException(); });
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

    public SingleAnnounceResponse findSingleAnnounceById(final String apartId, final Long announceId) {
        return announceRepository.findJoinedAnnounceById(apartId, announceId)
                .map(announce -> SingleAnnounceResponse.from(announce, announce.getMember()))
                .orElseThrow(ArticleNotFoundException::new);
    }

    public Announce appendArticle(final AnnounceDto announceDto,
                                  final MemberDto memberDto) {
        final Member member = memberDto.toEntity();
        final Announce article = announceDto.toEntity(member);
        return announceRepository.save(article);
    }


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

    public SingleAnnounceResponse updateAnnounce(final String apartId,
                                                 final Long announceId,
                                                 final AnnounceDto announceDto,
                                                 final MemberDto memberDto) {
        final Announce announceEntity = announceRepository.findAnnounceForApartId(apartId, announceId)
                .orElseThrow(ArticleNotFoundException::new);
        // TODO 토큰에서 뽑아온 사용자 정보와 작성된 게시물의 createdBy 를 검증해야하지만, 지금은 Dummy 라 검증할 수가 없다. 알아두자.
        final Announce updatedAnnounce = announceEntity
                .updateAnnounce(announceDto.getLevel(), announceDto.getTitle(), announceDto.getContent());
        return SingleAnnounceResponse.from(updatedAnnounce, updatedAnnounce.getMember());
    }

}
