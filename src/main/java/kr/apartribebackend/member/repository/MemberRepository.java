package kr.apartribebackend.member.repository;


import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.domain.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String Email);

    Optional<Member> findByEmailAndMemberType(String Email, MemberType memberType);

    boolean existsByEmailAndMemberType(String Email, MemberType memberType);

    boolean existsByNickname(String nickname);

    Optional<Member> findByEmailAndName(String email, String name);

    @Query(value = "select m from Member as m left join fetch m.apartment as a where m.email = :email")
    Optional<Member> findMemberWithApartInfoByEmail(@Param("email") String email);

    @Query(value = "select m from Member as m left join fetch m.apartment as a where m.email = :email and m.memberType = :memberType")
    Optional<Member> findMemberWithApartInfoByEmailAndMemberType(
            @Param("email") String email,
            @Param("memberType") MemberType memberType
    );

    @Query(value = "select m from Member as m" +
            " inner join fetch m.refreshToken as r" +
            " where m.nickname = :nickname and r.token = :token")
    Member findMemberWithRefreshTokenAndApartInfoByNickname(
            @Param("nickname") String nickname,
            @Param("token") String token
    );

    @Query(value = "select m from Member as m" +
            " inner join fetch m.refreshToken as r" +
            " where m.nickname = :nickname and r.token = :token")
    Member findMemberWithRefreshTokenByNicknameAndRefreshTokenValue(
            @Param("nickname") String nickname,
            @Param("token") String token
    );

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Member as m set m.password = :password where m.id = :memberId")
    int changePasswordByMemberId(@Param("memberId") Long memberId, @Param("password") String password);
}
