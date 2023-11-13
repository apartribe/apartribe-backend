package kr.apartribebackend.member.principal.oauth2;


import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class NaverUserInfo extends DefaultOAuth2UserInfo {

    private Map<String, Object> attributes = getAttributes();

    public NaverUserInfo(OAuth2User oAuth2User) {
        super(oAuth2User);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getProfileImage() {
        return null;
    }
}
