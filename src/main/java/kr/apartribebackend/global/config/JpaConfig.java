package kr.apartribebackend.global.config;

import kr.apartribebackend.member.principal.AuthenticatedMember;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getClass()
                .equals(AnonymousAuthenticationToken.class) ? Optional.ofNullable("AnonymousUser") :
                Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(AuthenticatedMember.class::cast)
                .map(AuthenticatedMember::getUsername);
    }
}
