package kr.apartribebackend.global.service;

import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.principal.oauth2.KakaoUserInfo;
import kr.apartribebackend.member.principal.oauth2.NaverUserInfo;
import kr.apartribebackend.member.principal.oauth2.OAuth2UserInfo;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static kr.apartribebackend.member.domain.MemberType.*;


@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private static final String KAKAO_REGISTRATION_ID = "kakao";
    private static final String NAVER_REGISTRATION_ID = "naver";
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public OAuth2UserInfo loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);
        final ClientRegistration clientRegistration = userRequest.getClientRegistration();
        final OAuth2UserInfo oAuth2UserInfo = buildOAuth2UserInfo(clientRegistration, oAuth2User);
        decisionForSignUpForceToOAuth2AuthenticatedMember(oAuth2UserInfo);
        return oAuth2UserInfo;
    }

    private void decisionForSignUpForceToOAuth2AuthenticatedMember(OAuth2UserInfo oAuth2UserInfo) {
        memberRepository.findByEmailAndMemberType(oAuth2UserInfo.getEmail(), SOCIAL)
                .ifPresentOrElse(
                        oAuth2UserInfo::setDetails,
                        () -> oAuth2UserInfo.setDetails(
                                signUpForceForOAuth2AuthenticatedMember(oAuth2UserInfo)
                        )
                );
    }

    private Member signUpForceForOAuth2AuthenticatedMember(OAuth2UserInfo oAuth2UserInfo) {
        final String oAuth2UserPasswordPrefix = UUID.randomUUID().toString().substring(0, 8);
        final MemberDto memberDto = oAuth2UserInfo.toDto(
                passwordEncoder.encode(oAuth2UserPasswordPrefix + "_" + oAuth2UserInfo.getId())
        );
        final Member member = memberDto.toEntity();
        return memberRepository.save(member);
    }

    private OAuth2UserInfo buildOAuth2UserInfo(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        return switch (clientRegistration.getRegistrationId()) {
            case KAKAO_REGISTRATION_ID -> new KakaoUserInfo(oAuth2User);
            case NAVER_REGISTRATION_ID -> new NaverUserInfo(oAuth2User);
            default -> throw new IllegalStateException("Invalid Client Registration Id");
        };
    }
}


//package kr.apartribebackend.global.service;
//
//import kr.apartribebackend.member.domain.Member;
//import kr.apartribebackend.member.dto.MemberDto;
//import kr.apartribebackend.member.principal.oauth2.KakaoUserInfoBuilder;
//import kr.apartribebackend.member.principal.oauth2.NaverUserInfoBuilder;
//import kr.apartribebackend.member.principal.oauth2.OAuth2AuthenticatedMember;
//import kr.apartribebackend.member.principal.oauth2.OAuth2UserInfo;
//import kr.apartribebackend.member.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//
//@Slf4j
//@RequiredArgsConstructor
//public class OAuth2UserService extends DefaultOAuth2UserService {
//
//    private static final String KAKAO_REGISTRATION_ID = "kakao";
//    private static final String NAVER_REGISTRATION_ID = "naver";
//    private static final String KAKAO_ATTRIBUTE_NAME = "sub";
//    private static final String NAVER_ATTRIBUTE_NAME = "id";
//    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Transactional
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        final OAuth2User oAuth2User = super.loadUser(userRequest);
//        final ClientRegistration clientRegistration = userRequest.getClientRegistration();
//        final OAuth2UserInfo oAuth2UserInfo = buildOAuth2UserInfo(clientRegistration, oAuth2User.getAttributes());
//        log.info("[{} OAuth2 User : {} Logged In]", oAuth2UserInfo.getNickname());
//        final OAuth2AuthenticatedMember oAuth2AuthenticatedMember = new OAuth2AuthenticatedMember(
//                oAuth2UserInfo, oAuth2User.getAttributes(), Set.of(new SimpleGrantedAuthority("ROLE_USER")) // TODO 권한 추후에 해결 필요.
//        );
//        decisionForSignUpForceToOAuth2AuthenticatedMember(oAuth2AuthenticatedMember);
//        return oAuth2AuthenticatedMember;
//    }
//
//    private void decisionForSignUpForceToOAuth2AuthenticatedMember(OAuth2AuthenticatedMember oAuth2AuthenticatedMember) {
//        memberRepository.findByEmail(oAuth2AuthenticatedMember.oAuth2UserInfo().getEmail())
//                .ifPresentOrElse(
//                        member -> {},
//                        () -> signUpForceForOAuth2AuthenticatedMember(oAuth2AuthenticatedMember)
//                );
//    }
//
//    private void signUpForceForOAuth2AuthenticatedMember(OAuth2AuthenticatedMember oAuth2AuthenticatedMember) {
//        final OAuth2UserInfo oAuth2UserInfo = oAuth2AuthenticatedMember.oAuth2UserInfo();
//        final String oAuth2UserPasswordPrefix = UUID.randomUUID().toString().substring(0, 8);
//        final MemberDto memberDto = oAuth2AuthenticatedMember.toDto(
//                passwordEncoder.encode(oAuth2UserPasswordPrefix + "_" + oAuth2UserInfo.getId())
//        );
//        final Member member = memberDto.toEntity();
//        memberRepository.save(member);
//    }
//
//    private OAuth2UserInfo buildOAuth2UserInfo(ClientRegistration clientRegistration,
//                                               Map<String, Object> oAuth2UserAttributes) {
//        return switch (clientRegistration.getRegistrationId()) {
//            case KAKAO_REGISTRATION_ID -> KakaoUserInfoBuilder.newInstance()
//                    .id(oAuth2UserAttributes.get(KAKAO_ATTRIBUTE_NAME))
//                    .name(oAuth2UserAttributes.get("name"))
//                    .email(oAuth2UserAttributes.get("email"))
//                    .nickname(oAuth2UserAttributes.get(KAKAO_ATTRIBUTE_NAME) + "_" + oAuth2UserAttributes.get("nickname"))
//                    .build();
//
//            case NAVER_REGISTRATION_ID -> NaverUserInfoBuilder.newInstance()
//                    .id(String.valueOf(oAuth2UserAttributes.get(NAVER_ATTRIBUTE_NAME)))
//                    .build();
//
//            default -> throw new IllegalStateException("Invalid Registration Id");
//        };
//    }
//}
