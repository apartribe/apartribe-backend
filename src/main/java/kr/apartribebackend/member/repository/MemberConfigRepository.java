package kr.apartribebackend.member.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.domain.QMember;
import kr.apartribebackend.member.dto.MemberArticleRes;
import kr.apartribebackend.member.dto.MemberCommentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.apartribebackend.article.domain.QArticle.*;
import static kr.apartribebackend.category.domain.QCategory.*;
import static kr.apartribebackend.comment.domain.QComment.comment;

@RequiredArgsConstructor
@Repository
public class MemberConfigRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<MemberCommentRes> findCommentsForMember(final Member member,
                                                        final Pageable pageable) {
        List<MemberCommentRes> memberCommentRes = jpaQueryFactory
                .select(Projections.fields(
                        MemberCommentRes.class,
                        comment.id,
                        comment.content,
                        comment.createdBy,
                        comment.createdAt))
                .from(comment)
                .innerJoin(comment.member, QMember.member)
                .where(QMember.member.id.eq(member.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(Wildcard.count)
                .from(comment)
                .where(comment.member.id.eq(member.getId()));

        return PageableExecutionUtils.getPage(memberCommentRes, pageable, countQuery::fetchOne);
    }

    public Page<MemberArticleRes> findArticlesForMember(final Member member,
                                                        final Pageable pageable) {
        List<MemberArticleRes> memberArticleRes = jpaQueryFactory
                .select(Projections.fields(
                        MemberArticleRes.class,
                        article.id,
                        category.name.as("category"),
                        article.title,
                        article.content,
                        article.createdBy,
                        article.createdAt))
                .from(article)
                .innerJoin(article.member, QMember.member)
                .innerJoin(article.category, category)
                .where(QMember.member.id.eq(member.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(Wildcard.count)
                .from(article)
                .innerJoin(article.member, QMember.member)
                .where(QMember.member.id.eq(member.getId()));

        return PageableExecutionUtils.getPage(memberArticleRes, pageable, countQuery::fetchOne);
    }
}
