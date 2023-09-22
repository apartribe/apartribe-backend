package kr.apartribebackend.article.repository.announce;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.dto.announce.SingleAnnounceResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.apartribebackend.article.domain.QAnnounce.*;
import static kr.apartribebackend.comment.domain.QComment.comment;

@RequiredArgsConstructor
public class CustomAnnounceRepositoryImpl implements CustomAnnounceRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SingleAnnounceResponse> findJoinedAnnounceById(Long announceId) {
        final List<Announce> results = jpaQueryFactory
                .selectFrom(announce)
                .leftJoin(announce.comments, comment).fetchJoin()
                .where(announce.id.eq(announceId))
                .fetch();

        final List<SingleAnnounceResponse> collect = results.stream()
                .map(SingleAnnounceResponse::from).toList();
        return collect;
    }

}
