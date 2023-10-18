package kr.apartribebackend.member.repository;


import kr.apartribebackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String Email);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmail(String Email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByEmailAndName(String email, String name);

    @Query(value = "select m from Member as m inner join m.refreshToken as t where m.email = :email")
    Optional<Member> findRefreshTokenByEmail(@Param("email") String email);

    @Query(value = "select m from Member as m left join fetch m.apartment as a where m.email = :email")
    Optional<Member> findMemberWithApartInfoByEmail(@Param("email") String email);

    @Query(value = "select m from Member as m" +
            " inner join fetch m.refreshToken as r" +
            " left join fetch m.apartment as a" +
            " where m.nickname = :nickname and m.createdAt = :createdAt and r.token = :token")
    Optional<Member> findMemberWithRefreshTokenAndApartInfoByNicknameAndCreatedAt(
            @Param("nickname") String nickname,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("token") String token
    );

}
