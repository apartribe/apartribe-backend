package kr.apartribebackend.token.refresh.dto;

import kr.apartribebackend.token.refresh.domain.RefreshToken;
import lombok.Builder;

@Builder
public class RefreshTokenDto {

    private String token;

    public static RefreshTokenDto from(RefreshToken refreshToken) {
        return RefreshTokenDto
                .builder()
                .token(refreshToken.getToken())
                .build();
    }
}
