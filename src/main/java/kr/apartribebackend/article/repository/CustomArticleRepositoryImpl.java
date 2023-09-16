package kr.apartribebackend.article.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.article.dto.Top5ArticleResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.apartribebackend.article.domain.QArticle.*;

@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Top5ArticleResponse> findTop5ArticleViaLiked() {
        return jpaQueryFactory
                .select(Projections.fields(Top5ArticleResponse.class, article.title.as("title")))
                .from(article)
                .orderBy(article.liked.desc())
                .limit(5)
                .fetch();
    }
}
