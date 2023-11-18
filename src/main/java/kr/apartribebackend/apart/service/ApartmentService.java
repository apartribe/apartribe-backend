package kr.apartribebackend.apart.service;


import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.apart.exception.*;
import kr.apartribebackend.apart.repository.ApartmentRepository;
import kr.apartribebackend.category.domain.ArticleCategory;
import kr.apartribebackend.category.domain.TogetherCategory;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.global.utils.EntityFactory;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.domain.Position;
import kr.apartribebackend.member.domain.UserType;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.springframework.util.StringUtils.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 1. Member 가 Apart 에 등록되어있으면 Exception
     * 2. 인증한 Apart 가 존재하면 Member 에 Apart 를 등록하고, AUTHSTATUS 를 COMPLETED 로 설정
     * 3. 인증한 Apart 가 존재하지 않으면 AUTHSTATUS 를 PENDING 으로 설정
     * @param memberDto
     * @param apartmentDto
     * @param member
     */
    public void authenticateApartment(final Position position,
                                      final UserType userType,
                                      final ApartmentDto apartmentDto,
                                      final Member member) {
        checkMemberHaveApartments(member);
        if (hasText(member.getApartCode()) || hasText(member.getApartName())) {
            throw new AlreadyAuthenticateApartException();
        }
        final Apartment findApartment = apartmentRepository.findApartmentByCode(apartmentDto.getCode())
                .orElse(null);
        if (findApartment != null) {
            log.info("인증하신 아파트는 사이트에 이미 존재하니, 사용자를 아파트에 등록하고, COMPLETED 로 처리합니다.");
            member.changeApartment(findApartment);
            member.authenticateApartInfo(apartmentDto.getCode(), apartmentDto.getName());
        } else {
            log.info("인증하신 아파트는 사이트에 존재하지 않으니, PENDING 으로 처리합니다.");
            member.pendingApartInfo(apartmentDto.getCode(), apartmentDto.getName());
        }
        member.updateUserTypeAndPosition(userType, position);
    }

    /**
     * 1. Member 가 Apart 에 등록되어있으면 Exception
     * 2. 생성하려는 아파트 커뮤니티가 존재하면 Exception
     * 3. 만들으려는 아파트 커뮤니티와 사전에 인증한 아파트 정보와 일치하지 않으면 Exception
     *
     * - Apart 엔티티를 저장
     * - Member 에 Apart 를 등록하고
     * - 디폴트 커뮤니티를 등록하고, AUTHSTATUS 를 COMPLETED 로 설정
     * @param memberDto
     * @param apartmentDto
     * @param member
     */
    public void constructCommunity(final MemberDto memberDto,
                                   final ApartmentDto apartmentDto,
                                   final Member member) {
        checkMemberHaveApartments(member);
        if (apartmentRepository.existsByCodeAndName(apartmentDto.getCode(), apartmentDto.getName())) {
            throw new ApartAlreadyExistsException();
        }
        if (member.getApartCode() == null || member.getApartName() == null) {
            throw new ApartmentNotAuthenticateException();
        }
        if (
                !member.getApartCode().equals(apartmentDto.getCode()) ||
                !member.getApartName().equals(apartmentDto.getName())
        ) {
            throw new ApartmentMemberIntegrityException();
        }
        final Apartment apartment = apartmentRepository.save(apartmentDto.toEntity());
        log.info("새로운 아파트 커뮤니티가 생성됩니다.");
        member.changeApartment(apartment);
        member.authenticateApartInfo(apartmentDto.getCode(), apartmentDto.getName());
        buildDefaultCommunity(member, apartment);
    }

    public boolean existsByCode(final String apartCode) {
        return apartmentRepository.existsByCode(apartCode);
    }

    public ApartmentDto findApartByCode(final String apartCode) {
        return apartmentRepository.findApartmentByCode(apartCode)
                .map(ApartmentDto::from)
                .orElseThrow(ApartNonExistsException::new);
    }

    private void checkMemberHaveApartments(final Member member) {
        if (member.getApartment() != null) {
            throw new ApartMemberDuplicateException();
        }
    }

    private void buildDefaultCommunity(final Member member, final Apartment apartment) {
        ArticleCategory articleCategory1 = EntityFactory.createArticleCategory(apartment, "자유 게시판").build();
        ArticleCategory articleCategory2 = EntityFactory.createArticleCategory(apartment, "고양이 집사 모임").build();
        ArticleCategory articleCategory3 = EntityFactory.createArticleCategory(apartment, "신혼 부부 정보 공유").build();
        TogetherCategory togetherCategory1 = EntityFactory.createTogetherCategory(apartment, "동호회").build();
        TogetherCategory togetherCategory2 = EntityFactory.createTogetherCategory(apartment, "친목회").build();
        TogetherCategory togetherCategory3 = EntityFactory.createTogetherCategory(apartment, "카풀").build();
        TogetherCategory togetherCategory4 = EntityFactory.createTogetherCategory(apartment, "공동구매").build();
        categoryRepository.saveAll(
                List.of(
                        articleCategory1, articleCategory2, articleCategory3,
                        togetherCategory1, togetherCategory2, togetherCategory3, togetherCategory4
                )
        );
    }

}
