package kr.apartribebackend.global.aspect;

import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.apart.exception.ApartRequestNotMatchWithMemberApartException;
import kr.apartribebackend.apart.exception.NeedApartmentAuthException;
import kr.apartribebackend.apart.exception.NeedToMakeCommunityException;
import kr.apartribebackend.global.annotation.ApartUser;
import kr.apartribebackend.member.domain.AuthStatus;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
@Aspect
public class ApartUserAspect {

    @Before("@annotation(apartUser)")
    public void apartUserPointcut(final JoinPoint joinPoint, final ApartUser apartUser) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final AuthenticatedMember authenticatedMember = (AuthenticatedMember) authentication.getPrincipal();

        if (authenticatedMember.getAuthStatus() == AuthStatus.INCOMPLETE) {
            throw new NeedApartmentAuthException();
        } else if (authenticatedMember.getAuthStatus() == AuthStatus.PENDING) {
            throw new NeedToMakeCommunityException();
        }
        final String apartPathVariable = retrieveApartPathVariable(joinPoint);
        if (apartPathVariable != null) {
            final ApartmentDto apartmentDto = authenticatedMember.getApartmentDto();
            if (!apartPathVariable.equals(apartmentDto.getCode())) {
                throw new ApartRequestNotMatchWithMemberApartException();
            }
        }
    }

    private String retrieveApartPathVariable(final JoinPoint joinPoint) {
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final String[] parameterNames = methodSignature.getParameterNames();
        final Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            final String parameterName = parameterNames[i];
            final Object argument = args[i];
            if (parameterName.equals("apartId") && argument instanceof String apartIdValue) {
                return apartIdValue;
            }
        }
        return null;
    }
}
