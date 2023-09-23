package kr.apartribebackend.member.dto;


public record MemberResponse(
        String email,
        String name,
        String nickname,
        String profileImageUrl
) {

    public static MemberResponse from(MemberDto memberDto) {
        return new MemberResponse(
                memberDto.getEmail(),
                memberDto.getName(),
                memberDto.getNickname(),
                memberDto.getProfileImageUrl()
        );
    }

}
