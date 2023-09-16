package kr.apartribebackend.global.resolver;

import jakarta.servlet.http.HttpServletRequest;
import kr.apartribebackend.global.annotation.AuthResolver;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;



@Slf4j @RequiredArgsConstructor
public class TokenResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(Member.class) &&
                parameter.hasParameterAnnotation(AuthResolver.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolved");
        final HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String email = httpServletRequest.getHeader("EMAIL");
        if (email == null)
            throw new RuntimeException("이메일 헤더를 찾을 수 없음");
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음."));
    }
}
