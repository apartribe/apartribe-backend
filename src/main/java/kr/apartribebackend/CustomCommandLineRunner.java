package kr.apartribebackend;

import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.apart.repository.ApartmentRepository;
import kr.apartribebackend.article.domain.*;
import kr.apartribebackend.article.repository.ArticleRepository;
import kr.apartribebackend.article.repository.together.TogetherRepository;
import kr.apartribebackend.article.repository.announce.AnnounceRepository;
import kr.apartribebackend.category.domain.ArticleCategory;
import kr.apartribebackend.category.domain.TogetherCategory;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.repository.CommentRepository;
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

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static kr.apartribebackend.article.domain.Level.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final AnnounceRepository announceRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TogetherRepository togetherRepository;
    private final ApartmentRepository apartmentRepository;

    @Override @Transactional
    public void run(String... args) throws Exception {
        log.info("ApplicationInit Called() -2-");

        if (memberRepository.existsByEmailAndMemberType("bcl0206@naver.com", MemberType.GENERAL))
            return;

        Apartment apart1 = createApart("dyWaf", "루원시티1차SK리더스뷰");
        Apartment apart2 = createApart("0x65", "헥스아파트");
        apartmentRepository.save(apart1);
        apartmentRepository.save(apart2);

        Member bcl = createUser("방충림", "white_h4ck3r_bcl", "bcl0206@naver.com", passwordEncoder.encode("방충림1!"), "", MemberType.GENERAL, UserType.RESIDENT, Position.BUILDING_REPRESENTATIVE);
        Member jieun2 = createUser("이지은", "scary_girl", "jieun2@apartlive.com", passwordEncoder.encode("qwer1234!"), "", MemberType.GENERAL, UserType.MANAGER, Position.OWNER);
        Member revi1337 = createUser("이경학", "?_?_*_<", "david122123@gmail.com", passwordEncoder.encode("asdf"), "", MemberType.GENERAL, UserType.RESIDENT, Position.ADMINISTRATOR);
        memberRepository.saveAll(List.of(bcl, jieun2, revi1337));
        bcl.rememberApartInfo(apart1.getCode(), apart1.getName());
        jieun2.rememberApartInfo(apart1.getCode(), apart1.getName());
        revi1337.rememberApartInfo(apart2.getCode(), apart2.getName());

        bcl.changeApartment(apart1);
        jieun2.changeApartment(apart1);
        revi1337.changeApartment(apart2);

        bcl.authenticateApartInfo(apart1.getCode(), apart1.getName());
        jieun2.authenticateApartInfo(apart1.getCode(), apart1.getName());
        revi1337.authenticateApartInfo(apart2.getCode(), apart2.getName());

        List<Announce> announces1 = createAnnounces(30, bcl, CRITICAL, "비상 공지사항", "비상 콘텐츠 비사아아아아아아아앙 ㅇ에에에ㅔ에엥에에에에ㅔ엥!!!!!", "https://miro.medium.com/v2/resize:fit:1200/0*8IQEdpp7lezZZ6To.png");
        List<Announce> announces2 = createAnnounces(30, bcl, GENERAL, "일반 공지사항", "일반 콘텐츠 에에에ㅔ에엥......!!!!!", "https://miro.medium.com/v2/resize:fit:1200/0*8IQEdpp7lezZZ6To.png");
        List<Announce> announces3 = createAnnounces(30, jieun2, EMERGENCY, "긴급 공지사항", "비상 콘텐츠 비사사사사사아아앙ㅇ앙아!!!", "https://miro.medium.com/v2/resize:fit:1200/0*8IQEdpp7lezZZ6To.png");
        announces1.addAll(announces2);
        announces1.addAll(announces3);
        announceRepository.saveAll(announces1);

        ArticleCategory category1 = createArticleCategory(apart1,"자유 게시판");
        ArticleCategory category2 = createArticleCategory(apart1,"신혼 부부 정보 공유");
        ArticleCategory category3 = createArticleCategory(apart1,"고양이 집사 모임");
        categoryRepository.saveAll(List.of(category1, category2, category3));
        List<Article> articles1 = createArticles(30, jieun2, category1, "자유 게시 제목", "자유 게시 내용", "https://miro.medium.com/v2/resize:fit:1200/0*8IQEdpp7lezZZ6To.png");
        List<Article> articles2 = createArticles(30, jieun2, category2, "신혼 부부 정보 공유 제목", "신혼 부부 정보 공유 내용", "https://miro.medium.com/v2/resize:fit:1200/0*8IQEdpp7lezZZ6To.png");
        List<Article> articles3 = createArticles(30, bcl, category3, "고양이 집사 모임", "야옹", "https://miro.medium.com/v2/resize:fit:1200/0*8IQEdpp7lezZZ6To.png");
        articles1.addAll(articles2);
        articles1.addAll(articles3);
        articleRepository.saveAll(articles1);

        TogetherCategory category4 = createTogetherCategory(apart1, "동호회");
        TogetherCategory category5 = createTogetherCategory(apart1, "친목회");
        TogetherCategory category6 = createTogetherCategory(apart1, "카풀");
        TogetherCategory category7 = createTogetherCategory(apart1, "공동구매");
        categoryRepository.saveAll(List.of(category4, category5, category6, category7));
        List<Together> togethers1 = createTogethers(30, jieun2, "함께해요 타이틀", "함께해요 제목", category4, "설명 주저리주저리", LocalDate.now(), LocalDate.now().plusDays(3), RecruitStatus.END,"만나는 시간", "우리 집", true, "유아", "https://miro.medium.com/v2/resize:fit:1200/0*8IQEdpp7lezZZ6To.png");
        List<Together> togethers2 = createTogethers(30, bcl, "함께해요 타이틀", "함께해요 제목", category5, "설명 주저리주저리", LocalDate.now(), LocalDate.now().plusDays(4), RecruitStatus.STILL,"만나는 시간", "니 집", false, "청소년", "https://miro.medium.com/v2/resize:fit:1200/0*8IQEdpp7lezZZ6To.png");
        List<Together> togethers3 = createTogethers(30, bcl, "함께해요 타이틀", "함께해요 제목", category6, "설명 주저리주저리", LocalDate.now(), LocalDate.now().plusDays(5), RecruitStatus.STILL, "만나는 시간", "모두의 집", true, "성인", "https://miro.medium.com/v2/resize:fit:1200/0*8IQEdpp7lezZZ6To.png");
        List<Together> togethers4 = createTogethers(15, jieun2, "함께해요 타이틀", "함께해요 제목", category7, "설명 주저리주저리", LocalDate.now(), LocalDate.now().plusDays(5), RecruitStatus.STILL, "만나는 시간", "모두의 집", true, "성인", "https://miro.medium.com/v2/resize:fit:1200/0*8IQEdpp7lezZZ6To.png");
        togethers1.addAll(togethers2);
        togethers1.addAll(togethers3);
        togethers1.addAll(togethers4);
        togetherRepository.saveAll(togethers1);

        // announce
        List<Comment> announceParents = new ArrayList<>();
        for (Announce announce : announces1) {
            if (announce.getId() % 2 == 0) {
                announceParents.add(createComment(bcl, announce, "댓글" + announce.getId(), null));
            } else {
                announceParents.add(createComment(jieun2, announce, "댓글" + announce.getId(), null));
            }
        }
        commentRepository.saveAll(announceParents);
        List<Comment> announceCommentReplies = new ArrayList<>();
        for (Comment parent : announceParents) {
            if (parent.getId() % 2 == 0) {
                announceCommentReplies.add(createComment(jieun2, parent.getBoard(), "대댓글" + parent.getBoard().getId(), parent));
            } else {
                announceCommentReplies.add(createComment(bcl, parent.getBoard(), "대댓글" + parent.getBoard().getId(), parent));
            }
        }
        commentRepository.saveAll(announceCommentReplies);

        // article
        List<Comment> articleParents = new ArrayList<>();
        for (Article article : articles1) {
            if (article.getId() % 2 == 0) {
                articleParents.add(createComment(jieun2, article, "댓글" + article.getId(), null));
            } else  {
                articleParents.add(createComment(bcl, article, "댓글" + article.getId(), null));
            }
        }
        commentRepository.saveAll(articleParents);
        List<Comment> articleCommentReplies = new ArrayList<>();
        for (Comment parent : articleParents) {
            if (parent.getId() % 2 == 0) {
                articleCommentReplies.add(createComment(jieun2, parent.getBoard(), "대댓글" + parent.getBoard().getId(), parent));
            } else {
                articleCommentReplies.add(createComment(bcl, parent.getBoard(), "대댓글" + parent.getBoard().getId(), parent));
            }
        }
        commentRepository.saveAll(articleCommentReplies);
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

    private static Announce createAnnounce(Member member,
                                           Level level,
                                           String title,
                                           String content,
                                           String thumbnail,
                                           LocalDate floatFrom,
                                           LocalDate floatTo) {
        return Announce.builder()
                .member(member)
                .level(level)
                .title(title)
                .content(content)
                .thumbnail(thumbnail)
                .floatFrom(floatFrom)
                .floatTo(floatTo)
                .build();
    }

    private static List<Announce> createAnnounces(Integer count,
                                                  Member member,
                                                  Level level,
                                                  String title,
                                                  String content,
                                                  String thumbnail) {
        LocalDate now = LocalDate.now();
        LocalDate to = LocalDate.parse(String.format("%s-%s-%s", now.getYear(), now.getMonthValue(), 30));
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return IntStream.rangeClosed(1, count)
                .mapToObj(integer ->
                        createAnnounce(member,
                                level,
                                title + " " + integer,
                                content + integer + " " + integer,
                                thumbnail,
                                LocalDate.parse(String.format("%s-%s-%s", now.getYear(), now.getMonthValue(), decimalFormat.format(integer))),
                                to
                        )
                )
                .collect(Collectors.toList());
    }

    private static Article createArticle(Member member, ArticleCategory category, String title, String content, String thumbnail) {
        return Article.builder()
                .member(member)
                .category(category)
                .title(title)
                .content(content)
                .thumbnail(thumbnail)
                .build();
    }

    private static List<Article> createArticles(Integer count, Member member, ArticleCategory category, String title, String content, String thumbnail) {
        return IntStream.rangeClosed(0, count)
                .mapToObj(integer -> createArticle(member, category, title + " " + integer, content + integer + " " + integer, thumbnail))
                .collect(Collectors.toList());
    }

    private static Comment createComment(Member member, Board board, String content, Comment parent) {
        Comment comment = Comment.builder()
                .member(member)
                .board(board)
                .content(content)
                .build();
        if (parent != null)
            comment.registParent(parent);
        return comment;
    }

    private static List<Comment> createComments(Integer count, Member member, Board board, String content, Comment parent) {
        return IntStream.rangeClosed(0, count)
                .mapToObj(integer -> createComment(member, board, content + " " + integer, null))
                .collect(Collectors.toList());
    }

    private static Together createTogether(Member member,
                                           String title,
                                           String content,
                                           TogetherCategory category,
                                           String description,
                                           LocalDate recruitFrom,
                                           LocalDate recruitTo,
                                           RecruitStatus recruitStatus,
                                           String meetTime,
                                           String location,
                                           boolean contributeStatus,
                                           String target,
                                           String thumbnail) {
        return Together.builder()
                .member(member)
                .title(title)
                .content(content)
                .category(category)
                .description(description)
                .recruitFrom(recruitFrom)
                .recruitTo(recruitTo)
                .recruitStatus(recruitStatus)
                .meetTime(meetTime)
                .location(location)
                .contributeStatus(contributeStatus)
                .target(target)
                .thumbnail(thumbnail)
                .build();
    }

    private static List<Together> createTogethers(Integer count,
                                                  Member member,
                                                  String title,
                                                  String content,
                                                  TogetherCategory category,
                                                  String description,
                                                  LocalDate recruitFrom,
                                                  LocalDate recruitTo,
                                                  RecruitStatus recruitStatus,
                                                  String meetTime,
                                                  String location,
                                                  boolean contributeStatus,
                                                  String target,
                                                  String thumbnail) {
        return IntStream.rangeClosed(0, count)
                .mapToObj(integer -> createTogether(
                        member,
                        title + " " + integer,
                        content + integer + " " + integer,
                        category,
                        description,
                        recruitFrom,
                        recruitTo,
                        recruitStatus,
                        meetTime,
                        location,
                        contributeStatus,
                        target,
                        thumbnail
                        )
                )
                .collect(Collectors.toList());
    }

}


