package kr.apartribebackend.member.principal;

import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class AuthenticatedMember implements UserDetails {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String apartCode;
    private String apartName;
    private LocalDateTime createdAt;
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
                               String apartCode,
                               String apartName,
                               LocalDateTime createdAt,
                               Set<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.authorities = authorities;
        this.apartCode = apartCode;
        this.createdAt = createdAt;
        this.apartName = apartName;
    }

    public static AuthenticatedMember from(MemberDto memberDto) {
        return new AuthenticatedMember(
                memberDto.getId(),
                memberDto.getEmail(),
                memberDto.getPassword(),
                memberDto.getName(),
                memberDto.getNickname(),
                "EMPTY",
                "EMPTY",
                memberDto.getCreatedAt(),
                Set.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public static AuthenticatedMember from(MemberDto memberDto, ApartmentDto apartmentDto) {
        return new AuthenticatedMember(
                memberDto.getId(),
                memberDto.getEmail(),
                memberDto.getPassword(),
                memberDto.getName(),
                memberDto.getNickname(),
                apartmentDto.getCode(),
                apartmentDto.getName(),
                memberDto.getCreatedAt(),
                Set.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Override
    public String getUsername() { return nickname; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

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
