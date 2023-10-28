package kr.apartribebackend.article.repository.announce;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.AnnounceResponse;
import kr.apartribebackend.article.dto.announce.AnnounceWidgetRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomAnnounceRepository {

    Page<AnnounceResponse> findAnnouncesByLevel(String apartId, Level level, Pageable pageable);

    Optional<Announce> findAnnounceForApartId(String apartId, Long announceId);

    List<AnnounceWidgetRes> findWidgetValues(String apartId);

}
