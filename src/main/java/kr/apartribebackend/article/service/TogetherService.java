package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.article.dto.together.SingleTogetherResponse;
import kr.apartribebackend.article.dto.together.TogetherDto;
import kr.apartribebackend.article.dto.together.TogetherResponse;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.repository.together.TogetherRepository;
import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.attachment.service.AttachmentService;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.exception.CategoryNonExistsException;
import kr.apartribebackend.category.repository.CategoryRepository;
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
    public SingleTogetherResponse updateTogether(final Long togetherId,
                               final String category,
                               final TogetherDto togetherDto,
                               final MemberDto memberDto) {
        final Together togetherEntity = togetherRepository.findById(togetherId)
                .orElseThrow(ArticleNotFoundException::new);
        final Category categoryEntity = categoryRepository.findCategoryByTagAndName(TOGETHER, category)
                .orElseThrow(CategoryNonExistsException::new);
        // TODO 토큰에서 뽑아온 사용자 정보와 작성된 게시물의 createdBy 를 검증해야하지만, 지금은 Dummy 라 검증할 수가 없다. 알아두자.
        final Together updatedTogether = togetherEntity.updateTogether(
                categoryEntity, togetherDto.getTitle(), togetherDto.getDescription(),
                togetherDto.getContent(), togetherDto.getRecruitFrom(), togetherDto.getRecruitTo(),
                togetherDto.getMeetTime(), togetherDto.getTarget(), togetherDto.getLocation(),
                togetherDto.isContributeStatus(), togetherDto.getRecruitStatus(), togetherDto.getThumbnail()
        );
        return SingleTogetherResponse.from(updatedTogether);
    }

    @Transactional
    public void updateLikeByTogetherId(final Long togetherId) {
        togetherRepository.findById(togetherId)
                .ifPresentOrElse(Board::reflectArticleLike,
                        () -> { throw new CannotReflectLikeToArticleException(); });
    }

    public Page<TogetherResponse> findMultipleTogethersByCategory(final String apartId,
                                                                  final String category,
                                                                  final Pageable pageable) {
        return togetherRepository.findMultipleTogethersByCategory(apartId, category, pageable);
    }

    @Transactional
    public SingleTogetherResponse findSingleTogetherById(final String apartId, final Long togetherId) {
        return togetherRepository.findTogetherForApartId(apartId, togetherId)
                .map(together -> SingleTogetherResponse.from(together, together.getMember()))
                .orElseThrow(ArticleNotFoundException::new);
    }

}
