package kr.apartribebackend.member.dto;

import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.member.domain.*;
import kr.apartribebackend.token.refresh.dto.RefreshTokenDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter @NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private RefreshTokenDto refreshTokenDto;
    private ApartmentDto apartmentDto;
    private AuthStatus authStatus;
    private MemberType memberType;
    private UserType userType;
    private Position position;
    private String apartCode;
    private String apartName;

    @Builder
    private MemberDto(Long id,
                      String email,
                      String password,
                      String name,
                      String nickname,
                      String profileImageUrl,
                      LocalDateTime createdAt,
                      RefreshTokenDto refreshTokenDto,
                      ApartmentDto apartmentDto,
                      AuthStatus authStatus,
                      MemberType memberType,
                      UserType userType,
                      Position position,
                      String apartCode,
                      String apartName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = createdAt;
        this.refreshTokenDto = refreshTokenDto;
        this.apartmentDto = apartmentDto;
        this.authStatus = authStatus;
        this.memberType = memberType;
        this.userType = userType;
        this.position = position;
        this.apartCode = apartCode;
        this.apartName = apartName;
    }

    public static MemberDto from(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .createdAt(member.getCreatedAt())
                .authStatus(member.getAuthStatus())
                .memberType(member.getMemberType())
                .userType(member.getAuthStatus() == AuthStatus.COMPLETED ? member.getUserType() : null)
                .position(member.getAuthStatus() == AuthStatus.COMPLETED ? member.getPosition() : null)
                .apartCode(member.getApartCode())
                .apartName(member.getApartName())
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .authStatus(authStatus != null ? authStatus : AuthStatus.INCOMPLETE)
                .memberType(memberType != null ? memberType : MemberType.GENERAL)
                .userType(userType)
                .position(position)
                .build();
    }

}


//    public static MemberDto from(Member member, Apartment apartment) {
//        return MemberDto.builder()
//                .id(member.getId())
//                .email(member.getEmail())
//                .password(member.getPassword())
//                .name(member.getName())
//                .nickname(member.getNickname())
//                .profileImageUrl(member.getProfileImageUrl())
//                .createdAt(member.getCreatedAt())
//                .apartmentDto(ApartmentDto.from(apartment))
//                .build();
//    }

//    public Member toEntity(RefreshToken refreshToken) {
//        return Member.builder()
//                .id(id)
//                .email(email)
//                .password(password)
//                .name(name)
//                .nickname(nickname)
//                .profileImageUrl(profileImageUrl)
//                .refreshToken(refreshToken)
//                .build();
//    }

//    public static MemberDto from(Member member, RefreshToken refreshToken, Apartment apartment) {
//        return MemberDto.builder()
//                .id(member.getId())
//                .email(member.getEmail())
//                .password(member.getPassword())
//                .name(member.getName())
//                .nickname(member.getNickname())
//                .profileImageUrl(member.getProfileImageUrl())
//                .createdAt(member.getCreatedAt())
//                .refreshTokenDto(RefreshTokenDto.from(refreshToken))
//                .apartmentDto(ApartmentDto.from(apartment))
//                .build();
//    }