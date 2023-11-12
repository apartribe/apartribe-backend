package kr.apartribebackend.global.config;

import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.member.principal.oauth2.OAuth2UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("AnonymousUser");
            }
            if (authentication.getPrincipal() instanceof AuthenticatedMember authenticatedMember) {
                return Optional.of(authenticatedMember.getUsername());
            } else if (authentication.getPrincipal() instanceof OAuth2UserInfo oAuth2UserInfo) {
                return Optional.of(oAuth2UserInfo.getNickname());
            }
            throw new IllegalStateException("옳지 않은 인증객체");
        };
    }
}
