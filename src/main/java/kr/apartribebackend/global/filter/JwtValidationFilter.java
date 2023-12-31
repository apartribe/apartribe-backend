package kr.apartribebackend.global.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.global.dto.ReIssueTokenReq;
import kr.apartribebackend.global.dto.TokenResponse;
import kr.apartribebackend.global.exception.NotExistsRefreshTokenException;
import kr.apartribebackend.global.service.JwtService;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.domain.MemberType;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.member.repository.MemberRepository;
import kr.apartribebackend.token.refresh.domain.RefreshToken;
import kr.apartribebackend.token.refresh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;
    private String reIssuedTokenPath = "/api/reissue/token";
    private Set<String> filterExcludePath = Set.of("/api/auth");

    public void setReIssuedTokenPath(String reIssuedTokenPath) {
        this.reIssuedTokenPath = reIssuedTokenPath;
    }

    public void setFilterExcludePath(Set<String> filterExcludePath) {
        this.filterExcludePath = filterExcludePath;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (requestURIContainsExcludeURI(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().equals(reIssuedTokenPath)) {
            final String reIssueTokenString = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            final ReIssueTokenReq reIssueTokenReq = objectMapper.readValue(reIssueTokenString, ReIssueTokenReq.class);
            if (reIssueTokenReq == null) {
                return;
            }
            final Claims extractedAllClaims = jwtService.extractAllClaims(
                    reIssueTokenReq.refreshToken(), JwtService.TokenType.REFRESH);
            final String subjectNickname = extractedAllClaims.getSubject();
            final String tokenType = (String) extractedAllClaims.get("type");

            final Member member = memberRepository.findMemberWithRefreshTokenByNicknameAndRefreshTokenValue(
                    subjectNickname, reIssueTokenReq.refreshToken()
            );
            if (member == null || tokenType == null || !tokenType.equals("refresh")) {
                throw new NotExistsRefreshTokenException();
            }
            final String dbRefreshToken = member.getRefreshToken().getToken();
            if (!dbRefreshToken.equals(reIssueTokenReq.refreshToken())) {
                throw new NotExistsRefreshTokenException();
            }
            final String reIssuedRefreshToken = jwtService.generateRefreshToken(subjectNickname);
            final RefreshToken newRefreshToken = RefreshToken.builder().token(reIssuedRefreshToken).build();
            final Long refreshTokenId = member.getRefreshToken().getId();
            refreshTokenRepository.updateToken(newRefreshToken.getToken(), refreshTokenId);
            final String reIssuedAccessToken = jwtService.generateAccessToken(
                    member.getNickname(),
                    Map.of(
                            "email", member.getEmail(),
                            "role", "추가해야함",
                            "memberType", member.getMemberType()
                    )
            );
            final String reIssuedTokenResponse =
                    objectMapper.writeValueAsString(TokenResponse.of(reIssuedAccessToken, reIssuedRefreshToken));

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().println(reIssuedTokenResponse);
            return;
        }

        final String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String accessToken = authHeader.substring(7);
        if (jwtService.isAccessTokenValid(accessToken)) {
            final Claims claims = jwtService.extractAllClaims(accessToken, JwtService.TokenType.ACCESS);
            final String userEmail = claims.get("email", String.class);
            final String memberTypeString = (String) claims.get("memberType");
            final MemberType memberType = Arrays.stream(MemberType.values()).filter(m -> m.name().equals(memberTypeString))
                    .findFirst()
                    .orElse(MemberType.GENERAL);
            final Member member = memberRepository.findMemberWithApartInfoByEmailAndMemberType(userEmail, memberType)
                    .orElse(null);
            if (member != null) {
                final AuthenticatedMember authenticatedMember = AuthenticatedMember.from(
                        MemberDto.from(member), ApartmentDto.from(member.getApartment())
                );
                authenticatedMember.setOriginalEntity(member);
                final UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                        .authenticated(authenticatedMember, null, authenticatedMember.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean requestURIContainsExcludeURI(HttpServletRequest httpServletRequest) {
        return filterExcludePath.stream()
                .anyMatch(excludePath -> httpServletRequest.getRequestURI().contains(excludePath));
    }

}
