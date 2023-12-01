package kr.apartribebackend.global.config;

import kr.apartribebackend.article.converter.CategoryConverter;
import kr.apartribebackend.article.converter.LevelConverter;
import kr.apartribebackend.global.resolver.AuthenticationResolver;
import kr.apartribebackend.global.resolver.TokenResolver;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration @RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new CategoryConverter());
        registry.addConverter(new LevelConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new TokenResolver(memberRepository));
        resolvers.add(new AuthenticationResolver());
    }
}
