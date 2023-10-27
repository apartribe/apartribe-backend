package kr.apartribebackend.member.dto.agreements;

import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.domain.agreements.Agreements;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AgreementsDto {

    private Long id;
    private boolean goeFourteen;
    private boolean confirmCopyright;
    private boolean useAgree;
    private boolean dataCollectAgree;
    private boolean advertiseAgree;
    private MemberDto memberDto;

    @Builder
    public AgreementsDto(Long id,
                         boolean goeFourteen,
                         boolean confirmCopyright,
                         boolean useAgree,
                         boolean dataCollectAgree,
                         boolean advertiseAgree,
                         MemberDto memberDto) {
        this.id = id;
        this.goeFourteen = goeFourteen;
        this.confirmCopyright = confirmCopyright;
        this.useAgree = useAgree;
        this.dataCollectAgree = dataCollectAgree;
        this.advertiseAgree = advertiseAgree;
        this.memberDto = memberDto;
    }

    public Agreements toEntity(Member member) {
        return Agreements.builder()
                .goeFourteen(goeFourteen)
                .confirmCopyright(confirmCopyright)
                .useAgree(useAgree)
                .dataCollectAgree(dataCollectAgree)
                .advertiseAgree(advertiseAgree)
                .member(member)
                .build();
    }
}
