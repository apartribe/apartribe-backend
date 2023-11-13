package kr.apartribebackend.member.principal.oauth2;

import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfo extends OAuth2User {

    String getId();

    String getName();

    String getNickname();

    String getEmail();

    String getPassword();

    String getProfileImage();

    MemberDto toDto(String password);

    void setDetails(Member member);

    Member getDetails();

}
