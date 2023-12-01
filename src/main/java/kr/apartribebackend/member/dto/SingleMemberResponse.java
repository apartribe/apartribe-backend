package kr.apartribebackend.member.dto;


import com.querydsl.core.annotations.QueryProjection;
import kr.apartribebackend.member.domain.AuthStatus;
import kr.apartribebackend.member.domain.Position;
import kr.apartribebackend.member.domain.UserType;
import lombok.Getter;


@Getter
public class SingleMemberResponse {

    private String email;
    private String name;
    private String nickname;
    private String profileImageUrl;
    private String apartCode;
    private String apartName;
    private String userType;
    private String position;
    private String authStatus;

    @QueryProjection
    public SingleMemberResponse(String email,
                                String name,
                                String nickname,
                                String profileImageUrl,
                                String apartCode,
                                String apartName,
                                AuthStatus authStatus,
                                Position position,
                                UserType userType) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.apartCode = apartCode != null ? apartCode : "EMPTY";
        this.apartName = apartName != null ? apartName : "EMPTY";
        this.authStatus = authStatus.getStatus();
        if (position != null && userType != null && authStatus == AuthStatus.COMPLETED) {
            this.userType = userType.getName();
            this.position = position.getName();
        }
    }
}
