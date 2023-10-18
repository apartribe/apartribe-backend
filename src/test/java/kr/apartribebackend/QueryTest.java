package kr.apartribebackend;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.apart.domain.QApartment;
import kr.apartribebackend.apart.repository.ApartmentRepository;
import kr.apartribebackend.article.domain.*;
import kr.apartribebackend.article.dto.ArticleResponse;
import kr.apartribebackend.article.dto.SingleCommentResponse;
import kr.apartribebackend.article.dto.announce.AnnounceResponse;
import kr.apartribebackend.article.dto.together.TogetherResponse;
import kr.apartribebackend.article.repository.ArticleRepository;
import kr.apartribebackend.article.repository.BoardRepository;
import kr.apartribebackend.article.repository.announce.AnnounceRepository;
import kr.apartribebackend.article.repository.together.TogetherRepository;
import kr.apartribebackend.category.domain.ArticleCategory;
import kr.apartribebackend.category.domain.QCategory;
import kr.apartribebackend.category.domain.TogetherCategory;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.repository.CommentRepository;
import kr.apartribebackend.global.config.JpaConfig;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.domain.QMember;
import kr.apartribebackend.member.dto.MemberArticleRes;
import kr.apartribebackend.member.dto.MemberBoardResponse;
import kr.apartribebackend.member.dto.MemberCommentRes;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kr.apartribebackend.apart.domain.QApartment.*;
import static kr.apartribebackend.article.domain.Level.*;
import static kr.apartribebackend.article.domain.QAnnounce.*;
import static kr.apartribebackend.article.domain.QArticle.*;
import static kr.apartribebackend.article.domain.QBoard.board;
import static kr.apartribebackend.article.domain.QTogether.*;
import static kr.apartribebackend.category.domain.QCategory.*;
import static kr.apartribebackend.comment.domain.QComment.comment;
import static kr.apartribebackend.member.domain.QMember.*;


@ActiveProfiles("test")
@Import({JpaConfig.class, QueryDslTestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(showSql = false) @Commit
class QueryTest {

    @PersistenceContext EntityManager entityManager;
    @Autowired ArticleRepository articleRepository;
    @Autowired AnnounceRepository announceRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ApartmentRepository apartmentRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired TogetherRepository togetherRepository;

    JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    public void beforeEach() {
        jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Test
    @DisplayName(value = "JPA Inheritance 제네릭 레포지토리 테스트")
    public void genericRepository() {
        Member member = EntityUtils.createMember(1);
        Member member2 = EntityUtils.createMember(2);
        memberRepository.save(member);
        memberRepository.save(member2);
    }

    @Test
    @DisplayName("ARTICLE 리스트 페이징")
    public void apartTest() {
        Apartment apart1 = EntityUtils.createApart(1);
        Apartment apart2 = EntityUtils.createApart(2);
        apartmentRepository.save(apart1);
        apartmentRepository.save(apart2);

        Member member1 = EntityUtils.createMember(1);
        Member member2 = EntityUtils.createMember(2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        member1.changeApartment(apart1);
        member2.changeApartment(apart2);

        ArticleCategory articleCategory1 = EntityUtils.createArticleCategory(1);
        ArticleCategory articleCategory2 = EntityUtils.createArticleCategory(2);
        categoryRepository.save(articleCategory1);
        categoryRepository.save(articleCategory2);

        Article article1 = EntityUtils.createArticle(1, member1, articleCategory1);
        Article article2 = EntityUtils.createArticle(2, member2, articleCategory2);
        articleRepository.save(article1);
        articleRepository.save(article2);

        entityManager.flush();
        entityManager.clear();

        PageRequest pageable = PageRequest.of(0, 3);
        List<Article> content = jpaQueryFactory
                .selectFrom(article)
                .innerJoin(article.member, member).fetchJoin()
                .innerJoin(article.category, category).fetchJoin()
                .innerJoin(member.apartment, apartment).fetchJoin()
                .where(
                        apartment.code.eq("code 1"),
                        category.name.eq("article_category 1")
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        final List<ArticleResponse> articleResponses = content.stream().map(ArticleResponse::from).toList();
        for (ArticleResponse articleRespons : articleResponses) {
            System.out.println("articleRespons = " + articleRespons);
        }
    }

    @Test
    @DisplayName("Announce 리스트 페이징")
    public void apartTest2() {
        Apartment apart1 = EntityUtils.createApart(1);
        Apartment apart2 = EntityUtils.createApart(2);
        apartmentRepository.save(apart1);
        apartmentRepository.save(apart2);

        Member member1 = EntityUtils.createMember(1);
        Member member2 = EntityUtils.createMember(2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        member1.changeApartment(apart1);
        member2.changeApartment(apart2);

        Announce announce1 = EntityUtils.createAnnounce(1, member1, CRITICAL);
        Announce announce2 = EntityUtils.createAnnounce(2, member1, CRITICAL);
        Announce announce3 = EntityUtils.createAnnounce(3, member2, EMERGENCY);
        Announce announce4 = EntityUtils.createAnnounce(4, member2, GENERAL);
        announceRepository.saveAll(List.of(announce1, announce2, announce3, announce4));

        entityManager.flush();
        entityManager.clear();

        PageRequest pageable = PageRequest.of(0, 3);
        List<Announce> content = jpaQueryFactory
                .selectFrom(announce)
                .innerJoin(announce.member, member).fetchJoin()
                .innerJoin(member.apartment, apartment).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(
                        apartment.code.eq("code 2"),
                        announce.level.eq(EMERGENCY)
                )
                .fetch();

        final List<AnnounceResponse> announceResponses = content.stream().map(AnnounceResponse::from).toList();
        for (AnnounceResponse announceRespons : announceResponses) {
            System.out.println("announceRespons = " + announceRespons);
        }
    }

    @Test
    @DisplayName("Together 리스트 페이징")
    public void apartTest3() {
        Apartment apart1 = EntityUtils.createApart(1);
        Apartment apart2 = EntityUtils.createApart(2);
        apartmentRepository.save(apart1);
        apartmentRepository.save(apart2);

        Member member1 = EntityUtils.createMember(1);
        Member member2 = EntityUtils.createMember(2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        member1.changeApartment(apart1);
        member2.changeApartment(apart2);

        TogetherCategory togetherCategory1 = EntityUtils.createTogetherCategory(1);
        TogetherCategory togetherCategory2 = EntityUtils.createTogetherCategory(2);
        categoryRepository.save(togetherCategory1);
        categoryRepository.save(togetherCategory2);

        Together together1 = EntityUtils.createTogether(1, member1, togetherCategory1);
        Together together2 = EntityUtils.createTogether(2, member2, togetherCategory2);
        togetherRepository.save(together1);
        togetherRepository.save(together2);

        entityManager.flush();
        entityManager.clear();

        PageRequest pageable = PageRequest.of(0, 3);
        List<Together> content = jpaQueryFactory
                .selectFrom(together)
                .innerJoin(together.member, member).fetchJoin()
                .innerJoin(together.category, category).fetchJoin()
                .innerJoin(member.apartment, apartment).fetchJoin()
                .where(
                        apartment.code.eq("code 1"),
                        category.name.eq("together_category 1")
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        final List<TogetherResponse> togetherResponses = content.stream().map(TogetherResponse::from).toList();
        for (TogetherResponse togetherRespons : togetherResponses) {
            System.out.println("togetherRespons = " + togetherRespons);
        }
    }

    @Test
    @DisplayName("내가 쓴 글")
    public void 내가_쓴_글() {
        Apartment apart1 = EntityUtils.createApart(1);
        Apartment apart2 = EntityUtils.createApart(2);
        apartmentRepository.save(apart1);
        apartmentRepository.save(apart2);

        Member member1 = EntityUtils.createMember(1);
        Member member2 = EntityUtils.createMember(2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        member1.changeApartment(apart1);
        member2.changeApartment(apart2);

        TogetherCategory togetherCategory1 = EntityUtils.createTogetherCategory(1);
        TogetherCategory togetherCategory2 = EntityUtils.createTogetherCategory(2);
        categoryRepository.save(togetherCategory1);
        categoryRepository.save(togetherCategory2);

        Together together1 = EntityUtils.createTogether(1, member1, togetherCategory1);
        Together together2 = EntityUtils.createTogether(2, member2, togetherCategory2);
        togetherRepository.save(together1);
        togetherRepository.save(together2);

        entityManager.flush();
        entityManager.clear();

//        MemberArticleRes
        PageRequest pageable = PageRequest.of(0, 3);
        List<Board> content = jpaQueryFactory
                .select(board)
                .from(board)
                .innerJoin(board.member, member).fetchJoin()
                .innerJoin(member.apartment, apartment).fetchJoin()
                .where(
                        apartment.code.eq("code 1"),
                        member.id.eq(1L)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<MemberBoardResponse> results = content.stream().map(MemberBoardResponse::from).collect(Collectors.toList());
        for (MemberBoardResponse result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    @DisplayName("내가 쓴 댓글")
    public void 내가_쓴_댓글() {
        Apartment apart1 = EntityUtils.createApart(1);
        Apartment apart2 = EntityUtils.createApart(2);
        apartmentRepository.save(apart1);
        apartmentRepository.save(apart2);

        Member member1 = EntityUtils.createMember(1);
        Member member2 = EntityUtils.createMember(2);
        Member member3 = EntityUtils.createMember(3);
        Member member4 = EntityUtils.createMember(4);
        memberRepository.saveAll(List.of(member1, member2, member3, member4));
        member1.changeApartment(apart1);
        member2.changeApartment(apart2);
        member3.changeApartment(apart2);
        member4.changeApartment(apart1);

        TogetherCategory togetherCategory1 = EntityUtils.createTogetherCategory(1);
        TogetherCategory togetherCategory2 = EntityUtils.createTogetherCategory(2);
        categoryRepository.saveAll(List.of(togetherCategory1, togetherCategory2));

        Together together1 = EntityUtils.createTogether(1, member1, togetherCategory1);
        Together together2 = EntityUtils.createTogether(2, member2, togetherCategory2);
        togetherRepository.saveAll(List.of(together1, together2));

        Comment comment1 = EntityUtils.createComment(1, member1, together1);
        Comment comment2 = EntityUtils.createComment(2, member1, together1);
        Comment comment3 = EntityUtils.createComment(3, member2, together1);
        Comment comment4 = EntityUtils.createComment(4, member1, together1);
        commentRepository.saveAll(List.of(comment1, comment2, comment3, comment4));

        entityManager.flush();
        entityManager.clear();

        PageRequest pageable = PageRequest.of(0, 10);
        List<MemberCommentRes> fetch = jpaQueryFactory
                .select(Projections.fields(
                        MemberCommentRes.class,
                        comment.id,
                        comment.content,
                        comment.createdBy,
                        comment.createdAt))
                .from(comment)
                .innerJoin(comment.member, member)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartment.code.eq("code 2"),
                        member.name.eq("member2")
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (MemberCommentRes memberCommentRes : fetch) {
            System.out.println("memberCommentRes = " + memberCommentRes);
        }
    }

}