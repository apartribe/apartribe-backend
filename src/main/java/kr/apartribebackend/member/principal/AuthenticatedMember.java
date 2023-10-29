package kr.apartribebackend.member.principal;

import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.member.domain.AuthStatus;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.token.refresh.dto.RefreshTokenDto;
import lombok.Builder;
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
    private String profileImage;
    private AuthStatus authStatus;
    private String apartCode;
    private String apartName;
    private RefreshTokenDto refreshTokenDto;
    private ApartmentDto apartmentDto;
    private LocalDateTime createdAt;
    private Set<? extends GrantedAuthority> authorities;
    private Member originalEntity;

    public void setOriginalEntity(Member originalEntity) {
        this.originalEntity = originalEntity;
    }

    @Builder
    private AuthenticatedMember(Long id,
                               String email,
                               String password,
                               String name,
                               String nickname,
                               String profileImage,
                               AuthStatus authStatus,
                               String apartCode,
                               String apartName,
                               RefreshTokenDto refreshTokenDto,
                               ApartmentDto apartmentDto,
                               LocalDateTime createdAt,
                               Set<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.authStatus = authStatus;
        this.apartCode = apartCode;
        this.apartName = apartName;
        this.authorities = authorities;
        this.refreshTokenDto = refreshTokenDto;
        this.apartmentDto = apartmentDto;
        this.createdAt = createdAt;
    }

    public static AuthenticatedMember from(MemberDto memberDto, ApartmentDto apartmentDto) {
        return AuthenticatedMember.builder()
                .id(memberDto.getId())
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
                .name(memberDto.getName())
                .nickname(memberDto.getNickname())
                .profileImage(memberDto.getProfileImageUrl())
                .apartmentDto(apartmentDto)
                .createdAt(memberDto.getCreatedAt())
                .authorities(Set.of(new SimpleGrantedAuthority("ROLE_USER")))
                .authStatus(memberDto.getAuthStatus())
                .apartCode(memberDto.getApartCode())
                .apartName(memberDto.getApartName())
                .build();
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
                .refreshTokenDto(refreshTokenDto)
                .authStatus(authStatus)
                .apartCode(apartCode)
                .apartName(apartName)
                .apartmentDto(apartmentDto)
                .createdAt(createdAt)
                .profileImageUrl(profileImage)
                .build();
    }

}

//    public static AuthenticatedMember from(MemberDto memberDto, RefreshTokenDto refreshTokenDto, ApartmentDto apartmentDto) {
//        return AuthenticatedMember.builder()
//                .id(memberDto.getId())
//                .email(memberDto.getEmail())
//                .password(memberDto.getPassword())
//                .name(memberDto.getName())
//                .nickname(memberDto.getNickname())
//                .refreshTokenDto(refreshTokenDto)
//                .apartmentDto(apartmentDto)
//                .createdAt(memberDto.getCreatedAt())
//                .authorities(Set.of(new SimpleGrantedAuthority("ROLE_USER")))
//                .build();
//    }

//    public static AuthenticatedMember from(MemberDto memberDto) {
//        return AuthenticatedMember.builder()
//                .id(memberDto.getId())
//                .email(memberDto.getEmail())
//                .password(memberDto.getPassword())
//                .name(memberDto.getName())
//                .nickname(memberDto.getNickname())
//                .createdAt(memberDto.getCreatedAt())
//                .authorities(Set.of(new SimpleGrantedAuthority("ROLE_USER")))
//                .build();
//    }


//package kr.apartribebackend.member.principal;
//
//import kr.apartribebackend.apart.dto.ApartmentDto;
//import kr.apartribebackend.member.domain.Member;
//import kr.apartribebackend.member.dto.MemberDto;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//
//@Getter
//public class AuthenticatedMember implements UserDetails {
//
//    private Long id;
//    private String email;
//    private String password;
//    private String name;
//    private String nickname;
//    private String apartCode;
//    private String apartName;
//    private LocalDateTime createdAt;
//    private Set<? extends GrantedAuthority> authorities;
//    private Member originalEntity;
//
//    public void setOriginalEntity(Member originalEntity) {
//        this.originalEntity = originalEntity;
//    }
//
//    public AuthenticatedMember(Long id,
//                               String email,
//                               String password,
//                               String name,
//                               String nickname,
//                               String apartCode,
//                               String apartName,
//                               LocalDateTime createdAt,
//                               Set<? extends GrantedAuthority> authorities) {
//        this.id = id;
//        this.email = email;
//        this.password = password;
//        this.name = name;
//        this.nickname = nickname;
//        this.authorities = authorities;
//        this.apartCode = apartCode;
//        this.createdAt = createdAt;
//        this.apartName = apartName;
//    }
//
//    public static AuthenticatedMember from(MemberDto memberDto, ApartmentDto apartmentDto) {
//        return new AuthenticatedMember(
//                memberDto.getId(),
//                memberDto.getEmail(),
//                memberDto.getPassword(),
//                memberDto.getName(),
//                memberDto.getNickname(),
//                apartmentDto.getCode(),
//                apartmentDto.getName(),
//                memberDto.getCreatedAt(),
//                Set.of(new SimpleGrantedAuthority("ROLE_USER"))
//        );
//    }
//
//    @Override
//    public String getUsername() { return nickname; }
//
//    @Override
//    public boolean isAccountNonExpired() { return true; }
//
//    @Override
//    public boolean isAccountNonLocked() { return true; }
//
//    @Override
//    public boolean isCredentialsNonExpired() { return true; }
//
//    @Override
//    public boolean isEnabled() { return true; }
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
//
//}
