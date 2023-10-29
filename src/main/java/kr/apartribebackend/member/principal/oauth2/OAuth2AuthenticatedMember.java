package kr.apartribebackend.member.principal.oauth2;


import kr.apartribebackend.member.domain.AuthStatus;
import kr.apartribebackend.member.domain.MemberType;
import kr.apartribebackend.member.dto.MemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public record OAuth2AuthenticatedMember(
        OAuth2UserInfo oAuth2UserInfo,
        Map<String, Object> attributes,
        Set<? extends GrantedAuthority> authorities
) implements OAuth2User {

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return oAuth2UserInfo.getName();
    }

    public MemberDto toDto(String password) {
        return MemberDto.builder()
                .authStatus(AuthStatus.INCOMPLETE)
                .email(oAuth2UserInfo.getEmail())
                .nickname(oAuth2UserInfo.getNickname())
                .name(oAuth2UserInfo.getName())
                .memberType(MemberType.SOCIAL)
                .profileImageUrl("")
                .password(password)
                .build();
    }

}
