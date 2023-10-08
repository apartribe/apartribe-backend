package kr.apartribebackend;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.repository.ArticleRepository;
import kr.apartribebackend.article.repository.announce.AnnounceRepository;
import kr.apartribebackend.category.domain.ArticleCategory;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.repository.CommentRepository;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(String... args) throws Exception {
        log.info("ApplicationInit Called() -2-");

        if (memberRepository.existsByEmail("bcl0206@naver.com"))
            return;

        Member bcl = createUser("방충림", "white_h4ck3r_bcl", "bcl0206@naver.com", passwordEncoder.encode("방충림1!"), "");
        Member jieun2 = createUser("이지은", "scary_girl", "jieun2@apartlive.com", passwordEncoder.encode("qwer1234!"), "");
        memberRepository.saveAll(List.of(bcl, jieun2));

        List<Announce> announces1 = createAnnounces(30, bcl, CRITICAL, "비상 공지사항", "비상 콘텐츠 비사아아아아아아아앙 ㅇ에에에ㅔ에엥에에에에ㅔ엥!!!!!");
        List<Announce> announces2 = createAnnounces(30, bcl, GENERAL, "일반 공지사항", "일반 콘텐츠 에에에ㅔ에엥......!!!!!");
        List<Announce> announces3 = createAnnounces(30, jieun2, EMERGENCY, "긴급 공지사항", "비상 콘텐츠 비사사사사사아아앙ㅇ앙아!!!");
        announces1.addAll(announces2);
        announces1.addAll(announces3);
        announceRepository.saveAll(announces1);

        ArticleCategory category1 = createCategory("인기");
        ArticleCategory category2 = createCategory("신혼 부부 정보 공유");
        ArticleCategory category3 = createCategory("고양이 집사 모임");
        categoryRepository.saveAll(List.of(category1, category2, category3));

        List<Article> articles1 = createArticles(30, jieun2, category1, "인기 제목", "인기 내용");
        List<Article> articles2 = createArticles(30, jieun2, category2, "신혼 부부 정보 공유 제목", "신혼 부부 정보 공유 내용");
        List<Article> articles3 = createArticles(30, bcl, category3, "고양이 집사 모임", "야옹");
        articles1.addAll(articles2);
        articles1.addAll(articles3);
        articleRepository.saveAll(articles1);

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

    private static Member createUser(String name,
                                     String nickname,
                                     String email,
                                     String password,
                                     String profileImage) {
        return Member.builder()
                .name(name)
                .nickname(nickname)
                .email(email)
                .password(password)
                .profileImageUrl(profileImage)
                .build();
    }

    private static ArticleCategory createCategory(String name) {
        return ArticleCategory.builder()
                .name(name)
                .build();
    }

    private static Announce createAnnounce(Member member, Level level, String title, String content) {
        return Announce.builder()
                .member(member)
                .level(level)
                .title(title)
                .content(content)
                .build();
    }

    private static List<Announce> createAnnounces(Integer count, Member member, Level level, String title, String content) {
        return IntStream.rangeClosed(0, count)
                .mapToObj(integer -> createAnnounce(member, level, title + " " + integer, content + integer + " " + integer))
                .collect(Collectors.toList());
    }

    private static Article createArticle(Member member, ArticleCategory category, String title, String content) {
        return Article.builder()
                .member(member)
                .category(category)
                .title(title)
                .content(content)
                .build();
    }

    private static List<Article> createArticles(Integer count, Member member, ArticleCategory category, String title, String content) {
        return IntStream.rangeClosed(0, count)
                .mapToObj(integer -> createArticle(member, category, title + " " + integer, content + integer + " " + integer))
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

}


