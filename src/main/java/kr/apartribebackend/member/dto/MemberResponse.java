package kr.apartribebackend.member.dto;


public record MemberResponse(
        Long id,
        String email,
        String name,
        String nickname
) {

    public static MemberResponse from(MemberDto memberDto) {
        return new MemberResponse(
                memberDto.getId(),
                memberDto.getEmail(),
                memberDto.getName(),
                memberDto.getNickname()
        );
    }

}
