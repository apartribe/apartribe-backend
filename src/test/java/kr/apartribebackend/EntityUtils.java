package kr.apartribebackend;

import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.article.domain.*;
import kr.apartribebackend.category.domain.ArticleCategory;
import kr.apartribebackend.category.domain.TogetherCategory;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.member.domain.Member;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntityUtils {

    public static Apartment createApart(int memberIndex) {
        return Apartment.builder()
                .code(String.format("code %s", memberIndex))
                .name(String.format("name %s", memberIndex))
                .build();
    }

    public static Member createMember(int memberIndex) {
        return Member.builder()
                .nickname(String.format("member%s_nickname", memberIndex))
                .name(String.format("member%s", memberIndex))
                .password(String.format("member%s_password", memberIndex))
                .email(String.format("member%s_email", memberIndex))
                .profileImageUrl(String.format("member%sprofile_link", memberIndex))
                .build();
    }

    public static ArticleCategory createArticleCategory(int articleIndex) {
        return ArticleCategory.builder()
                .name(String.format("article_category %s", articleIndex))
                .build();
    }

    public static TogetherCategory createTogetherCategory(int articleIndex) {
        return TogetherCategory.builder()
                .name(String.format("together_category %s", articleIndex))
                .build();
    }

    public static Article createArticle(int articleIndex, Member member, ArticleCategory articleCategory) {
        return Article.builder()
                .member(member)
                .title(String.format("article%s_title", articleIndex))
                .content(String.format("article%s_content", articleIndex))
                .category(articleCategory)
                .build();
    }

    public static Announce createAnnounce(int articleIndex, Member member, Level level) {
        return Announce.builder()
                .member(member)
                .title(String.format("announce_title%s", articleIndex))
                .content(String.format("announce_content%s", articleIndex))
                .level(level)
                .build();
    }

    public static Together createTogether(int togetherIndex, Member member, TogetherCategory category) {
        return Together.builder()
                .member(member)
                .title(String.format("together_title%s", togetherIndex))
                .content(String.format("together_content%s", togetherIndex))
                .category(category)
                .description(String.format("together_desc%s", togetherIndex))
                .recruitFrom(LocalDate.now().plusDays(1))
                .recruitTo(LocalDate.now().plusDays(3))
                .recruitStatus(RecruitStatus.STILL)
                .meetTime(String.format("together_meettime%s", togetherIndex))
                .target(String.format("together_target%s", togetherIndex))
                .location(String.format("together_location%s", togetherIndex))
                .contributeStatus(true)
                .build();
    }

    public static Comment createComment(int commentIndex, Member member, Board board) {
        return Comment.builder()
                .board(board)
                .member(member)
                .content(String.format("comment%s_content", commentIndex))
                .build();
    }

    public static Set<Comment> createComments(int commentCount, Member member, Board board) {
        return IntStream.rangeClosed(1, commentCount)
                .mapToObj(integer -> createComment(integer, member, board))
                .collect(Collectors.toSet());
    }

}
