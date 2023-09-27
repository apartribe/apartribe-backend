package kr.apartribebackend.global.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO 전체적인 리팩토링 필요
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String accessTokenSecretKey;

    @Value("${application.security.jwt.refresh-token.secret-key}")
    private String refreshTokenSecretKey;

    @Value("${application.security.jwt.expiration}")
    private long accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public String getDecodedTokenExcludeSignature(String jwt) {
        return Arrays.stream(jwt.split("\\."))
                .limit(2)
                .map(Base64.getDecoder()::decode)
                .map(String::new)
                .collect(Collectors.joining(","));
    }

    public boolean isAccessTokenValid(String accessToken) {
        return !isAccessTokenExpired(accessToken);
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            isRefreshTokenExpired(refreshToken);
        } catch (ExpiredJwtException exception) {
            return true;
        }
        return false;
    }

    public boolean isAccessTokenExpired(String accessToken) {
        return isTokenExpired(accessToken, TokenType.ACCESS);
    }

    public boolean isRefreshTokenExpired(String refreshToken) {
        return isTokenExpired(refreshToken, TokenType.REFRESH);
    }

    private boolean isTokenValid(String token, TokenType tokenType) {
        return !isTokenExpired(token, tokenType);
    }

    private boolean isTokenExpired(String token, TokenType tokenType) {
        return extractExpiration(token, tokenType).before(new Date());
    }

    public Date extractExpiration(String token, TokenType tokenType) {
        return extractClaim(token, tokenType, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, TokenType tokenType, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token, TokenType tokenType) {
        String secretKey = accessTokenSecretKey;
        if (tokenType.equals(TokenType.REFRESH)) secretKey = refreshTokenSecretKey;
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    ////////////////////////////// GENERATE & BUILD //////////////////////////////

    public String generateAccessToken(String subject) {
        return generateAccessToken(subject, new HashMap<>());
    }

    public String generateAccessToken(String subject, Map<String, ?> extraClaims) {
        return buildToken(subject,
                extraClaims,
                accessTokenExpiration);
    }

    public String generateRefreshToken(String subject) {
        return buildToken(subject,
                new HashMap<>(Map.of("type", "refresh")),
                refreshTokenExpiration,
                getSigningKey(refreshTokenSecretKey));
    }

    private String buildToken(String subject, Map<String, ?> extraClaims, long expiration) {
        return buildToken(subject,
                extraClaims,
                expiration,
                getSigningKey(accessTokenSecretKey));
    }

    private String buildToken(String subject, Map<String, ?> extraClaims, long expiration, Key key) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuer("APARTRIBE")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSigningKey(String keyType) {
        return Keys.hmacShaKeyFor(keyType.getBytes());
    }

    public enum TokenType {
        ACCESS,
        REFRESH
    }

}
