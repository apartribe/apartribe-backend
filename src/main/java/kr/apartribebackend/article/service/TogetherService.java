package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.article.dto.together.TogetherDto;
import kr.apartribebackend.article.repository.TogetherRepository;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.exception.CategoryNonExistsException;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TogetherService {

    private final TogetherRepository togetherRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void appendTogether(final String category,
                               final MemberDto memberDto,
                               final TogetherDto togetherDto) {
        final Category categoryEntity = categoryRepository.findByName(category)
                .orElseThrow(CategoryNonExistsException::new);
        final Member member = memberDto.toEntity();
        final Together together = togetherDto.toEntity(categoryEntity, member);
        togetherRepository.save(together);
    }

}
