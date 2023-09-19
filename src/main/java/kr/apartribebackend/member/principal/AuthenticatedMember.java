package kr.apartribebackend.member.principal;

import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class AuthenticatedMember implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private Set<? extends GrantedAuthority> authorities;
    private Member originalEntity;

    public void setOriginalEntity(Member originalEntity) {
        this.originalEntity = originalEntity;
    }

    public AuthenticatedMember(Long id,
                               String email,
                               String password,
                               String name,
                               String nickname,
                               Set<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.authorities = authorities;
    }

    public static AuthenticatedMember from(MemberDto memberDto) {
        return new AuthenticatedMember(
                memberDto.getId(),
                memberDto.getEmail(),
                memberDto.getPassword(),
                memberDto.getName(),
                memberDto.getNickname(),
                Set.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public Member getOriginalEntity() {
        return originalEntity;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public MemberDto toDto() {
        return MemberDto.builder()
                .id(id)
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(password)
                .build();
    }

}


//public record AuthenticatedMember(
//        Long id,
//        String email,
//        String password,
//        String name,
//        String nickname,
//        Set<? extends GrantedAuthority> authorities
//) implements UserDetails {
//
//    public static AuthenticatedMember from(MemberDto memberDto) {
//        return new AuthenticatedMember(
//                memberDto.getId(),
//                memberDto.getEmail(),
//                memberDto.getPassword(),
//                memberDto.getName(),
//                memberDto.getNickname(),
//                Set.of(new SimpleGrantedAuthority("ROLE_USER"))
//        );
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return nickname;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    public MemberDto toDto() {
//        return MemberDto.builder()
//                .id(id)
//                .email(email)
//                .name(name)
//                .nickname(nickname)
//                .password(password)
//                .build();
//    }
//}
