package com.sparta.ottoon.auth.jwt;

import com.sparta.ottoon.auth.entity.TokenError;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

// JwtUtil : - JWT 토큰을 생성하고 검증
@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
    public static final Long accessTokenExpiration = 60 * 60 * 1000L; // 60분
    public static final Long refreshTokenExpiration = 24 * 60 * 60 * 1000L; // 1일
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 빈으로 생성 후 초기화
     */
    @PostConstruct
    public void init() {
        // Base64로 인코딩된 secretKey를 decode
        byte[] bytes = Base64.getDecoder().decode(secretKey);

        // HMAC SHA 키 생성
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * username과 만료시간 정보를 담아 토큰 생성
     * @param username
     * @param expiration
     * @return
     */
    public String createToken(String username, long expiration) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // token에 username 정보 넣어주기
                        .setExpiration(new Date(date.getTime() + expiration)) // token에 만료시간 정보 넣어주기
                        .setIssuedAt(date) // token 생성일자
                        .signWith(key, signatureAlgorithm) // JWT 서명에 사용할 key와 알고리즘 설정
                        .compact();
    }

    /**
     * request header에서 token 가져오기
     * @param request
     * @return
     */
    public String getJwtTokenFromHeader(HttpServletRequest request) {
        // 헤더에서 'Authorization'의 값을 가져온다.
        String bearerToken = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);

        // bearerToken이 null이나 빈칸이 아니고, 'Bearer '로 시작한다며
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7); // 'Bearer ' 잘라서 반환
        }
        return null;
    }

    /**
     * token의 유효성 검사
     * @param token
     * @return
     */
    public TokenError validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return TokenError.VALID;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            return TokenError.INVALID_SIGN;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            return TokenError.EXPRIED;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            return TokenError.UNSUPPORTED;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            return TokenError.EMPTY_CLAIMS;
        }
    }

    /**
     * token에서 유저의 정보 가져오기
     * @param token
     * @return
     */
    public Claims getUserInfoFromToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우에도 클레임에서 사용자 이름을 추출
            return e.getClaims();
        }
    }
}
