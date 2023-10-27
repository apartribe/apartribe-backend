package kr.apartribebackend.member.dto;


import kr.apartribebackend.member.domain.Member;

public record SingleMemberResponse(
        String email,
        String name,
        String nickname,
        String profileImageUrl,
        String apartCode,
        String apartName
) {

    public static SingleMemberResponse from(Member member) {
        return new SingleMemberResponse(
                member.getEmail(),
                member.getName(),
                member.getNickname(),
                member.getProfileImageUrl(),
                member.getApartment() != null ? member.getApartment().getCode() : "EMPTY",
                member.getApartment() != null ? member.getApartment().getName() : "EMPTY"
        );
    }

}
