package kr.apartribebackend.likes.repository;

import kr.apartribebackend.likes.domain.Liked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikedRepository extends JpaRepository<Liked, Long> {

    @Query(value = "select l from Liked as l where l.member.id = :memberId")
    List<Liked> findLikedsByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from Liked as l where l.id in :likedIds")
    int deleteLikedsUsingLikedIds(@Param("likedIds") List<Long> likedIds);

}
