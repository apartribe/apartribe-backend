package kr.apartribebackend.global.resolver;

import kr.apartribebackend.global.exception.NonAuthorizedException;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.exception.UserNotFoundException;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;



public class AuthenticationResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class) &&
                parameter.getParameterType().isAssignableFrom(AuthenticatedMember.class);
    }

    @Override
    public AuthenticatedMember resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser"))
            throw new NonAuthorizedException();
        return (AuthenticatedMember) authentication.getPrincipal();
    }
}
