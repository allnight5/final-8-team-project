package com.team.final8teamproject.security.jwt;

import com.team.final8teamproject.base.entity.BaseEntity;
import com.team.final8teamproject.security.redis.RedisUtil;
import com.team.final8teamproject.user.dto.LoginResponseDto;
import com.team.final8teamproject.user.entity.UserRoleEnum;
import com.team.final8teamproject.security.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserDetailsServiceImpl userDetailsService;

    private final RedisUtil redisUtil;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L;            // 30분

//    private static final long ACCESS_TOKEN_TIME = 60 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L; //7일

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct// 의존성 주입이 이루어진 후 초기화를 수행하는 메소드
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);// Base64로 인코딩 되어있는 문자열을 byte 배열로 변환
        //* Base64 (64진법) : 바이너리(2진) 데이터를 문자 코드에 영향을 받지 않는 공통 ASCII문자로 표현하기 위해 만들어진 인코딩
        key = Keys.hmacShaKeyFor(bytes); // 바이트 배열을 HMAC-SHA 알고리즘을 사용해 Key객체로 반환, 이를 key변수에 대입
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     *  일반유저, 오너유저, 총관리자, 관리자 토큰 생성 메소드 부분
     */

    // 토큰 생성
    //유저(일반 사업자)정보를 가지고 AccessToken, RefreshToken 을 반환해주는 메서드
    public LoginResponseDto createUserToken(String username, UserRoleEnum role) {
        Date date = new Date();
        //권한 가져오기
        // BEARER : 인증 타입중 하나로 JWT 또는 OAuth에 대한 토큰을 사용 (RFC 6750 문서 확인)
        return getLoginResponseDto(Jwts.builder()
                .setSubject(username) // 토큰 용도
                .claim(AUTHORIZATION_KEY, role), date);
    }


    // 총관리자, 관리자 가지고 AccessToken, RefreshToken 을 반환해주는 메서드
    public LoginResponseDto createManagerToken(String general, UserRoleEnum role) {
        Date date = new Date();
        //권한 가져오기
        // BEARER : 인증 타입중 하나로 JWT 또는 OAuth에 대한 토큰을 사용 (RFC 6750 문서 확인)
        return getLoginResponseDto(Jwts.builder()
                .setSubject(general) // 토큰 용도
                .claim(AUTHORIZATION_KEY, role), date);
    }

    // 토큰 제발급
    //토큰(AccessToken, RefreshToken) 생성 메서드
    public String reCreateUserToken(String username, UserRoleEnum role) {
        Date date = new Date();
        //권한 가져오기
        // BEARER : 인증 타입중 하나로 JWT 또는 OAuth에 대한 토큰을 사용 (RFC 6750 문서 확인)
        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }
    public BaseEntity AuthenticatedUser(String username) {
        return userDetailsService.loadUserByUsernameUseRefreshToken(username);
    }

    private LoginResponseDto getLoginResponseDto(JwtBuilder general, Date date) {
        String accessToken = BEARER_PREFIX + general // payload에 들어갈 정보 조각들
                .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료시간 설정
                .setIssuedAt(date) // 토큰 발행일
                .signWith(key, signatureAlgorithm) // key변수 값과 해당 알고리즘으로 sign
                .compact(); // 토큰 생성

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key,signatureAlgorithm)
                .compact();

        return LoginResponseDto.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }


    /**
     *  일반유저, 오너유저, 총관리자, 관리자 인증 객체 생성 메소드 부분
     */
    // 일반 유저 인증 객체 생성
    public Authentication createUserAuthentication(String username) {
        //
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    //토큰 재발급을 위한 토큰 검색

    public String getAuthenticationByAccessToken(String accessToken) {
        String changeToken = accessToken.substring(7);
        String userPrincipal = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(changeToken).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal);
        return userDetails.getUsername();
    }

    /**
     *  -----------------------------------------------
     *
     *     Header에서 가져온 토큰 검증하는 메소드
     *
     *  -----------------------------------------------
     */

    // 토큰 검증
    // Header에서 토큰 가져오기
    public boolean validateToken(String token) {
        //parser : parsing을 하는 도구. parsing : token에 내재된 자료 구조를 빌드하고 문법을 검사한다.
        // JwtParseBuilder인스턴스를 생성
        // 서명 검증을 위한 키를 지정 setSigningKey()
        // 스레드에 안전한 JwtPaser를 리턴하기 위해 JwtPaserBuilder의 build()메서드를 호출
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {// 전: 권한 없다면 발생 , 후: JWT가 올바르게 구성되지 않았다면 발생
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");

        } catch (ExpiredJwtException e) {// JWT만료
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    //남은 유효시간
    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
    public boolean getRefreshTokenIsTrue(String username) {
        return redisUtil.hasKey(username);
    }
}