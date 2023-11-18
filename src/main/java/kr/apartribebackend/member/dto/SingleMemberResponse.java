package kr.apartribebackend.member.dto;


import com.querydsl.core.annotations.QueryProjection;
import kr.apartribebackend.member.domain.AuthStatus;
import kr.apartribebackend.member.domain.Badge;
import kr.apartribebackend.member.domain.Position;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SingleMemberResponse {

    private String email;
    private String name;
    private String nickname;
    private String profileImageUrl;
    private String apartCode;
    private String apartName;
    private List<String> badges = new ArrayList<>();

    @QueryProjection
    public SingleMemberResponse(String email,
                                String name,
                                String nickname,
                                String profileImageUrl,
                                String apartCode,
                                String apartName,
                                AuthStatus authStatus,
                                Position position) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.apartCode = apartCode != null ? apartCode : "EMPTY";
        this.apartName = apartName != null ? apartName : "EMPTY";
        this.badges.add(authStatus.getStatus());
        this.badges.add(position.getName());
    }

//    public static SingleMemberResponse from(Member member) {
//        return new SingleMemberResponse(
//                member.getEmail(),
//                member.getName(),
//                member.getNickname(),
//                member.getProfileImageUrl(),
//                member.getApartment() != null ? member.getApartment().getCode() : "EMPTY",
//                member.getApartment() != null ? member.getApartment().getName() : "EMPTY"
//        );
//    }
}


//package kr.apartribebackend.member.dto;
//
//
//import kr.apartribebackend.member.domain.Member;
//
//public record SingleMemberResponse(
//        String email,
//        String name,
//        String nickname,
//        String profileImageUrl,
//        String apartCode,
//        String apartName
//) {
//
//    public static SingleMemberResponse from(Member member) {
//        return new SingleMemberResponse(
//                member.getEmail(),
//                member.getName(),
//                member.getNickname(),
//                member.getProfileImageUrl(),
//                member.getApartment() != null ? member.getApartment().getCode() : "EMPTY",
//                member.getApartment() != null ? member.getApartment().getName() : "EMPTY"
//        );
//    }
//
//}
