package kr.apartribebackend.member.principal.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoUserInfo extends DefaultOAuth2UserInfo {

    private Map<String, Object> attributes = getAttributes();

    public KakaoUserInfo(OAuth2User oAuth2User) {
        super(oAuth2User);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("sub"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("name"));
    }

    @Override
    public String getNickname() {
        return this.getId() + "_" + attributes.get("nickname");
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    @Override
    public String getPassword() {
        return String.valueOf(attributes.get("sub"));
    }

    @Override
    public String getProfileImage() {
        return String.valueOf(attributes.get("picture"));
    }
}
