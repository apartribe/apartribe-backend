package kr.apartribebackend.advertise.dto;


import kr.apartribebackend.advertise.domain.Advertise;
import kr.apartribebackend.advertise.domain.AdvertiseTokenStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @AllArgsConstructor @Builder
public class AdvertiseDto {

    private Long id;
    private boolean collectData;
    private String name;
    private String email;
    private String token;
    private String title;
    private String content;
    private LocalDateTime expiredAt;
    private AdvertiseTokenStatus advertiseTokenStatus;

    public Advertise toEntity() {
        return Advertise.builder()
                .id(id)
                .collectDataAgree(collectData)
                .name(name)
                .email(email)
                .token(token)
                .title(title)
                .content(content)
                .expiredAt(expiredAt != null ? expiredAt : LocalDateTime.now().plusDays(3))
                .advertiseTokenStatus(advertiseTokenStatus != null ? advertiseTokenStatus : AdvertiseTokenStatus.INCOMPLETE)
                .build();
    }

}
