package kr.apartribebackend.member.principal.oauth2;

public interface OAuth2UserInfo {

    String getId();

    String getName();

    String getNickname();

    String getEmail();

    String getPassword();

}
