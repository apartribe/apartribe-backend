package kr.apartribebackend.member.principal;

import kr.apartribebackend.member.dto.MemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public record AuthenticatedMember(
        Long id,
        String email,
        String password,
        String name,
        String nickname,
        Set<? extends GrantedAuthority> authorities
) implements UserDetails {

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
}
