package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.AnnounceDto;
import kr.apartribebackend.article.dto.announce.SingleAnnounceResponse;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.repository.announce.AnnounceRepository;
import kr.apartribebackend.attachment.domain.Attachment;
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
    public Page<AnnounceDto> findMultipleAnnouncesByLevel(final Level level,
                                                          final Pageable pageable) {
        if (level.equals(Level.ALL))
            return findAllAnnounces(pageable);
        return announceRepository.findAnnouncesByLevel(level, pageable)
                .map(AnnounceDto::from);
    }

    @Transactional(readOnly = true)
    public Page<AnnounceDto> findAllAnnounces(final Pageable pageable) {
        return announceRepository.findAll(pageable)
                .map(AnnounceDto::from);
    }

    public void updateLikeByAnnounceId(final Long announceId) {
        announceRepository.findById(announceId)
                .ifPresentOrElse(Board::reflectArticleLike,
                        () -> { throw new CannotReflectLikeToArticleException(); });
    }

    public SingleAnnounceResponse findSingleAnnounceById(final Long announceId) {
        return announceRepository.findJoinedAnnounceById(announceId)
                .stream().findFirst()
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

}
