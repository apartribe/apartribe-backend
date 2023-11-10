package kr.apartribebackend.article.repository.announce;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.AnnounceResponse;
import kr.apartribebackend.article.dto.announce.AnnounceWidgetRes;
import kr.apartribebackend.article.dto.announce.SingleAnnounceResponseProjection;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.NotApartUserBoardException;
import kr.apartribebackend.member.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomAnnounceRepository {

    List<AnnounceWidgetRes> findWidgetValues(String apartId);

    Page<AnnounceResponse> findAnnouncesByLevel(String apartId, Level level, Pageable pageable);

    /**
     * 공지사항 게시글 단일 조회 (1) - 쿼리 여러방 나눠서 실행
     * @param apartId
     * @param announceId
     * @return
     */
    Optional<Announce> findAnnounceForApartId(String apartId, Long announceId);

    /**
     * 공지사항 게시글 단일 조회 (2) - SubQuery 를 포함한 한방쿼리 실행 + apartCode 정보
     * @param memberId
     * @param apartId
     * @param announceId
     * @return
     */
    Optional<SingleAnnounceResponseProjection> findAnnounceForApartId(Long memberId, String apartId, Long announceId);

    /**
     * 공지사항 게시글 단일 조회 (2) 를 이용한 단일 게시글 조회 로직.
     * 해당 메서드에서 조회된 게시글이 아파트주민 전용 게시글인지 아닌지 검증하는 로직이 포함되어 있다.
     * @param memberDto
     * @param apartId
     * @param articleId
     * @return
     */
    default SingleAnnounceResponseProjection findAnnounceWithApartCodeForApartId(MemberDto memberDto,
                                                                                 String apartId,
                                                                                 Long articleId) {
        final SingleAnnounceResponseProjection singleAnnounceResponseProjection =
                findAnnounceForApartId(memberDto.getId(), apartId, articleId).orElseThrow(ArticleNotFoundException::new);
        if (singleAnnounceResponseProjection.isOnlyApartUser()) {
            if (!memberDto.getApartmentDto().getCode().equals(singleAnnounceResponseProjection.getApartCode())) {
                throw new NotApartUserBoardException();
            }
        }
        return singleAnnounceResponseProjection;
    }

}
