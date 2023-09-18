package kr.apartribebackend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.apartribebackend.global.filter.DelegatingRedirectUrlFilter;
import kr.apartribebackend.global.filter.JsonLoginAuthenticationFilter;
import kr.apartribebackend.global.handler.JsonLoginSuccessHandler;
import kr.apartribebackend.global.provider.JsonLoginAuthenticationProvider;
import kr.apartribebackend.global.service.JsonLoginUserDetailsService;
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
                .addFilterAfter(jsonLoginAuthenticationFilter(), DelegatingRedirectUrlFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
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

}
