package kr.apartribebackend;

import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.apart.repository.ApartmentRepository;
import kr.apartribebackend.category.domain.ArticleCategory;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.domain.TogetherCategory;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.domain.MemberType;
import kr.apartribebackend.member.domain.Position;
import kr.apartribebackend.member.domain.UserType;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApartmentRepository apartmentRepository;

    @Override @Transactional
    public void run(String... args) throws Exception {
        log.info("ApplicationInit Called() -2-");

        if (memberRepository.existsByEmailAndMemberType("david122123@gmail.com", MemberType.GENERAL)) {
            return;
        }

        Apartment apart1 = createApart("dyWaf", "루원시티1차SK리더스뷰");
        Apartment apart2 = createApart("dC7df", "올림픽파크포레온");
        Apartment apart3 = createApart("aBP2b", "아크로리버파크");
        Apartment apart4 = createApart("dCU9d", "흑석자이");
        Apartment apart5 = createApart("cuk38", "더샵송도마리나베이");
        List<Apartment> apartmentList = List.of(apart1, apart2, apart3, apart4, apart5);
        apartmentRepository.saveAll(apartmentList);

        Member rootUser = createUser("최고_사용자_이경학", "Root", "david122123@gmail.com", passwordEncoder.encode("testUser123!"), "", MemberType.GENERAL, UserType.MANAGER, Position.ADMINISTRATOR);
        memberRepository.save(rootUser);

        rootUser.rememberApartInfo(apart1.getCode(), apart1.getName());
        rootUser.changeApartment(apart1);
        rootUser.authenticateApartInfo(apart1.getCode(), apart1.getName());

        ArrayList<Category> futureCategoryList = new ArrayList<>();
        for (Apartment apartment : apartmentList) {
            for (String articleCategorySubject : List.of("자유 게시판", "신혼 부부 정보 공유", "고양이 집사 모임")) {
                futureCategoryList.add(
                        createArticleCategory(apartment, articleCategorySubject)
                );
            }
            for (String togetherCategorySubject : List.of("동호회", "친목회", "카풀", "공동구매")) {
                futureCategoryList.add(
                        createTogetherCategory(apartment, togetherCategorySubject)
                );
            }
        }
        categoryRepository.saveAll(futureCategoryList);
    }

    public static Apartment createApart(String apartCode, String apartName) {
        return Apartment.builder()
                .code(apartCode)
                .name(apartName)
                .build();
    }

    private static Member createUser(String name,
                                     String nickname,
                                     String email,
                                     String password,
                                     String profileImage,
                                     MemberType memberType,
                                     UserType userType,
                                     Position position) {
        return Member.builder()
                .name(name)
                .nickname(nickname)
                .email(email)
                .password(password)
                .profileImageUrl(profileImage)
                .memberType(memberType)
                .userType(userType)
                .position(position)
                .build();
    }

    private static ArticleCategory createArticleCategory(Apartment apartment, String name) {
        return ArticleCategory.builder()
                .apartment(apartment)
                .name(name)
                .build();
    }

    private static TogetherCategory createTogetherCategory(Apartment apartment, String name) {
        return TogetherCategory.builder()
                .apartment(apartment)
                .name(name)
                .build();
    }

}


