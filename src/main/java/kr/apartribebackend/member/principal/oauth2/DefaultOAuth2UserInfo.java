package kr.apartribebackend.member.principal.oauth2;


import kr.apartribebackend.member.domain.AuthStatus;
import kr.apartribebackend.member.domain.MemberType;
import kr.apartribebackend.member.dto.MemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public abstract class DefaultOAuth2UserInfo implements OAuth2UserInfo {

    private OAuth2User oAuth2User;

    public DefaultOAuth2UserInfo(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public MemberDto toDto(String encodedPassword) {
        return MemberDto.builder()
                .authStatus(AuthStatus.INCOMPLETE)
                .email(getEmail())
                .nickname(getNickname())
                .name(getName())
                .memberType(MemberType.SOCIAL)
                .profileImageUrl(getProfileImage() != null ? getProfileImage() : "")
                .password(encodedPassword)
                .build();
    }
}
