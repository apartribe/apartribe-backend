package kr.apartribebackend.oauth;

import jakarta.validation.Valid;
import kr.apartribebackend.member.domain.entity.Member;
import kr.apartribebackend.member.domain.repository.MemberRepository;
import kr.apartribebackend.oauth.dto.MemberJoinReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/auth/member/join")
    public ResponseEntity<Void> memberJoin(@Valid @RequestBody final MemberJoinReq memberJoinReq) {
        final Member member = memberJoinReq.toDto().toEntity();
        member.changePassword(
                passwordEncoder.encode(memberJoinReq.password())
        );
        memberRepository.save(member);

        return ResponseEntity
                .status(CREATED)
                .build();
    }

}
