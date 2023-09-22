package kr.apartribebackend.article.repository.announce;

import kr.apartribebackend.article.dto.announce.SingleAnnounceResponse;

import java.util.List;

public interface CustomAnnounceRepository {

    List<SingleAnnounceResponse> findJoinedAnnounceById(Long announceId);

}
