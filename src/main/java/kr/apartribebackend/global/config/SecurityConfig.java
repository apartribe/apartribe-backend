package kr.apartribebackend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.apartribebackend.global.filter.DelegatingRedirectUrlFilter;
import kr.apartribebackend.global.filter.JsonLoginAuthenticationFilter;
import kr.apartribebackend.global.filter.JwtExceptionTranslationFilter;
import kr.apartribebackend.global.filter.JwtValidationFilter;
import kr.apartribebackend.global.handler.JsonLoginFailureHandler;
import kr.apartribebackend.global.handler.JsonLoginSuccessHandler;
import kr.apartribebackend.global.handler.JwtAuthenticationEntryPoint;
import kr.apartribebackend.global.handler.OAuth2SuccessHandler;
import kr.apartribebackend.global.provider.JsonLoginAuthenticationProvider;
import kr.apartribebackend.global.service.JsonLoginUserDetailsService;
import kr.apartribebackend.global.service.JwtService;
import kr.apartribebackend.global.service.OAuth2UserService;
import kr.apartribebackend.global.utils.ClientRedirectUrlHolder;
import kr.apartribebackend.member.repository.MemberRepository;
import kr.apartribebackend.token.refresh.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public SecurityConfig(ObjectMapper objectMapper,
                          MemberRepository memberRepository,
                          RefreshTokenRepository refreshTokenRepository,
                          JwtService jwtService,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                .addFilterAfter(jwtExceptionTranslationFilter(), DelegatingRedirectUrlFilter.class)
                .addFilterAfter(jwtValidationFilter(), JwtExceptionTranslationFilter.class)
                .addFilterAfter(jsonLoginAuthenticationFilter(), JwtValidationFilter.class)
                .build();
//                .addFilterAfter(jwtExceptionTranslationFilter(), LogoutFilter.class)
//                .addFilterAfter(jwtValidationFilter(), JwtExceptionTranslationFilter.class)
//                .addFilterAfter(jsonLoginAuthenticationFilter(), JwtValidationFilter.class)
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
    public JwtExceptionTranslationFilter jwtExceptionTranslationFilter() {
        final JwtExceptionTranslationFilter jwtExceptionTranslationFilter = new JwtExceptionTranslationFilter();
        jwtExceptionTranslationFilter.setAuthenticationEntryPoint(jwtAuthenticationEntryPoint());
        return jwtExceptionTranslationFilter;
    }

    @Bean
    public JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter() {
        final JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter =
                new JsonLoginAuthenticationFilter(objectMapper);
        jsonLoginAuthenticationFilter.setAuthenticationManager(authenticationManager());
        jsonLoginAuthenticationFilter.setAuthenticationSuccessHandler(jsonLoginSuccessHandler());
        jsonLoginAuthenticationFilter.setAuthenticationFailureHandler(jsonLoginFailureHandler());
        return jsonLoginAuthenticationFilter;
    }

    @Bean
    public JwtValidationFilter jwtValidationFilter() {
        final JwtValidationFilter jwtValidationFilter =
                new JwtValidationFilter(jwtService, memberRepository, refreshTokenRepository, objectMapper);
        jwtValidationFilter.setFilterExcludePath(Set.of("/api/auth"));
        jwtValidationFilter.setReIssuedTokenPath("/api/reissue/token");
        return jwtValidationFilter;
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
    public AuthenticationSuccessHandler jsonLoginSuccessHandler() {
        return new JsonLoginSuccessHandler(jwtService, objectMapper);
    }

    @Bean
    public AuthenticationFailureHandler jsonLoginFailureHandler() {
        return new JsonLoginFailureHandler(handlerExceptionResolver);
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();
        jwtAuthenticationEntryPoint.setHandlerExceptionResolver(handlerExceptionResolver);
        return jwtAuthenticationEntryPoint;
    }

    @Bean
    public AuthenticationSuccessHandler OAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(clientRedirectUrlHolder(), jwtService);
    }

    @Bean
    public OAuth2UserService oAuth2UserService() { return new OAuth2UserService(memberRepository, passwordEncoder()); }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

}
