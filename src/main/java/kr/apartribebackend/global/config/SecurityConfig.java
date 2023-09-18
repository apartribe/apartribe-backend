package kr.apartribebackend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.apartribebackend.global.filter.DelegatingRedirectUrlFilter;
import kr.apartribebackend.global.filter.JsonLoginAuthenticationFilter;
import kr.apartribebackend.global.handler.JsonLoginSuccessHandler;
import kr.apartribebackend.global.handler.OAuth2SuccessHandler;
import kr.apartribebackend.global.provider.JsonLoginAuthenticationProvider;
import kr.apartribebackend.global.service.JsonLoginUserDetailsService;
import kr.apartribebackend.global.service.OAuth2UserService;
import kr.apartribebackend.global.utils.ClientRedirectUrlHolder;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.Set;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService()))
                        .successHandler(OAuth2SuccessHandler()))
                .addFilterAfter(delegatingRedirectUrlFilter(), LogoutFilter.class)
                .addFilterAfter(jsonLoginAuthenticationFilter(), DelegatingRedirectUrlFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ClientRedirectUrlHolder clientRedirectUrlHolder() { return new ClientRedirectUrlHolder(); }

    @Bean
    public DelegatingRedirectUrlFilter delegatingRedirectUrlFilter() {
        final DelegatingRedirectUrlFilter delegatingRedirectUrlFilter =
                new DelegatingRedirectUrlFilter(clientRedirectUrlHolder());
        delegatingRedirectUrlFilter.setOauth2RedirectPaths(Set.of("/oauth2/authorization/kakao"));
        return delegatingRedirectUrlFilter;
    }


    @Bean
    public JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter() {
        final JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter =
                new JsonLoginAuthenticationFilter(objectMapper);
        jsonLoginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        jsonLoginAuthenticationFilter.setAuthenticationSuccessHandler(JsonLoginSuccessHandler());
        return jsonLoginAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new JsonLoginAuthenticationProvider(userDetailsService(), passwordEncoder());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new JsonLoginUserDetailsService(memberRepository);
    }

    @Bean
    public AuthenticationSuccessHandler JsonLoginSuccessHandler() {
        return new JsonLoginSuccessHandler();
    }

    @Bean
    public AuthenticationSuccessHandler OAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(clientRedirectUrlHolder());
    }

    @Bean
    public OAuth2UserService oAuth2UserService() { return new OAuth2UserService(); }

}
