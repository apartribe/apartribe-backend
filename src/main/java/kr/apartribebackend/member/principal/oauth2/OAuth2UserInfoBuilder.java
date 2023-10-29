package kr.apartribebackend.member.principal.oauth2;

public interface OAuth2UserInfoBuilder {

    OAuth2UserInfoBuilder id(Object id);

    OAuth2UserInfoBuilder name(Object name);

    OAuth2UserInfoBuilder nickname(Object nickname);

    OAuth2UserInfoBuilder email(Object email);

    OAuth2UserInfoBuilder password(Object password);

    OAuth2UserInfo build();

}
