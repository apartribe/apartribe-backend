package kr.apartribebackend.member.principal.oauth2;

public class NaverUserInfo implements OAuth2UserInfo {

    private String id;
    private String name;
    private String nickname;
    private String email;
    private String password;

    public NaverUserInfo(String id, String name, String nickname, String email, String password) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
