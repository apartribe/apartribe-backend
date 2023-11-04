package kr.apartribebackend.article.repository.announce;


import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnnounceRepository extends
        JpaRepository<Announce, Long>, CustomAnnounceRepository {

    @Query(value = "select a from Announce as a where a.member.id = :memberId")
    List<Announce> findAnnounceByMemberId(@Param("memberId") Long memberId);

}
