package kr.apartribebackend.article.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.QArticle;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.apartribebackend.article.domain.QArticle.*;

@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Article> findTop5ArticleViaLiked() {
        return jpaQueryFactory
                .selectFrom(article)
                .orderBy(article.liked.desc())
                .limit(5)
                .fetch();
    }
}
