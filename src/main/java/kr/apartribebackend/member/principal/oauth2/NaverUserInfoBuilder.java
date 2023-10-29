package kr.apartribebackend.member.principal.oauth2;

public class NaverUserInfoBuilder implements OAuth2UserInfoBuilder {

    private String id;
    private String name;
    private String nickname;
    private String email;
    private String password;

    private NaverUserInfoBuilder() {}

    public static OAuth2UserInfoBuilder newInstance() {
        return new NaverUserInfoBuilder();
    }

    @Override
    public OAuth2UserInfoBuilder id(Object id) {
        this.id = String.valueOf(id);
        return this;
    }

    @Override
    public OAuth2UserInfoBuilder name(Object name) {
        this.name = String.valueOf(name);
        return this;
    }

    @Override
    public OAuth2UserInfoBuilder nickname(Object nickname) {
        this.nickname = String.valueOf(nickname);
        return this;
    }

    @Override
    public OAuth2UserInfoBuilder email(Object email) {
        this.email = String.valueOf(email);
        return this;
    }

    @Override
    public OAuth2UserInfoBuilder password(Object password) {
        this.password = String.valueOf(password);
        return this;
    }

    @Override
    public OAuth2UserInfo build() {
        return new NaverUserInfo(id, name, nickname, email, password);
    }

}
