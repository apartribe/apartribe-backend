package kr.apartribebackend.article.repository.announce;


import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceRepository extends
        JpaRepository<Announce, Long>, CustomAnnounceRepository {

    Page<Announce> findAnnouncesByLevel(Level level, Pageable pageable);
}
