package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.article.dto.together.TogetherDto;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.repository.TogetherRepository;
import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.attachment.service.AttachmentService;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.domain.CategoryTag;
import kr.apartribebackend.category.exception.CategoryNonExistsException;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
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

    private final TogetherRepository togetherRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentService attachmentService;

    @Transactional
    public Together appendTogether(final String category,
                                   final MemberDto memberDto,
                                   final TogetherDto togetherDto) {
        final Category categoryEntity = categoryRepository.findCategoryByTagAndName(TOGETHER, category)
                .orElseThrow(CategoryNonExistsException::new);
        final Member member = memberDto.toEntity();
        final Together together = togetherDto.toEntity(categoryEntity, member);
        return togetherRepository.save(together);
    }

    @Transactional
    public void appendTogether(final String category,
                               final MemberDto memberDto,
                               final TogetherDto togetherDto,
                               final List<MultipartFile> file) throws IOException {
        final Together together = appendTogether(category, memberDto, togetherDto);
        final List<Attachment> attachments = attachmentService.saveFiles(file);
        for (Attachment attachment : attachments) {
            attachment.registBoard(together);
        }
        attachmentService.saveAttachments(attachments);
    }

    @Transactional
    public void updateLikeByTogetherId(final Long togetherId) {
        togetherRepository.findById(togetherId)
                .ifPresentOrElse(Board::reflectArticleLike,
                        () -> { throw new CannotReflectLikeToArticleException(); });
    }

}
