package kr.apartribebackend.article.repository.announce;

import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.AnnounceResponse;
import kr.apartribebackend.article.dto.announce.SingleAnnounceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomAnnounceRepository {

    Page<AnnounceResponse> findAnnouncesByLevel(String apartId, Level level, Pageable pageable);

    List<SingleAnnounceResponse> findJoinedAnnounceById(Long announceId);

}
