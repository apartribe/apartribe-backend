package kr.apartribebackend.global.aspect;

import kr.apartribebackend.apart.exception.NeedApartmentAuthException;
import kr.apartribebackend.apart.exception.NeedToMakeCommunityException;
import kr.apartribebackend.global.annotation.ApartUser;
import kr.apartribebackend.member.domain.AuthStatus;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class ApartUserAspect {

    @Before("@annotation(apartUser)")
    public void apartUserPointcut(final ApartUser apartUser) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final AuthenticatedMember authenticatedMember = (AuthenticatedMember) authentication.getPrincipal();
        if (authenticatedMember.getAuthStatus() == AuthStatus.INCOMPLETE) {
            throw new NeedApartmentAuthException();
        } else if (authenticatedMember.getAuthStatus() == AuthStatus.PENDING) {
            throw new NeedToMakeCommunityException();
        }
    }
}
