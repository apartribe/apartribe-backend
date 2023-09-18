package kr.apartribebackend.global.provider;

import kr.apartribebackend.member.principal.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j @RequiredArgsConstructor
public class JsonLoginAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String emailPrincipal = authentication.getPrincipal().toString();
        final AuthenticatedMember authenticatedMember =
                (AuthenticatedMember) userDetailsService.loadUserByUsername(emailPrincipal);
        final String encodedPassword = authenticatedMember.getPassword();
        final String rawPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(rawPassword, encodedPassword))
            throw new BadCredentialsException("입력한 비밀번호와 일치하는 회원이 없습니다.");
        return UsernamePasswordAuthenticationToken.authenticated(
                authenticatedMember, null, authenticatedMember.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
