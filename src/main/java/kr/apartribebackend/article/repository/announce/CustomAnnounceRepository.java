package kr.apartribebackend.article.repository.announce;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.AnnounceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomAnnounceRepository {

    Page<AnnounceResponse> findAnnouncesByLevel(String apartId, Level level, Pageable pageable);

    Optional<Announce> findJoinedAnnounceById(String apartId, Long announceId);

}
