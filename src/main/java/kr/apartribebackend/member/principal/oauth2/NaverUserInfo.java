package kr.apartribebackend.member.principal.oauth2;


import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class NaverUserInfo extends DefaultOAuth2UserInfo {

    private Map<String, Object> attributes;

    public NaverUserInfo(OAuth2User oAuth2User) {
        super(oAuth2User);
        Object naverAttributes = oAuth2User.getAttributes().get("response");
        if (naverAttributes instanceof Map) {
            this.attributes = (Map<String, Object>) naverAttributes;
        }
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("name"));
    }

    @Override
    public String getNickname() {
        return String.valueOf(attributes.get("nickname"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    @Override
    public String getPassword() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProfileImage() {
        return String.valueOf(attributes.get("profile_image"));
    }
}
