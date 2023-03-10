package com.team.final8teamproject.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.final8teamproject.security.dto.SecurityExceptionDto;
import com.team.final8teamproject.security.redis.RedisUtil;
import com.team.final8teamproject.user.dto.SetRedisRefreshToken;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request);
        if(token != null) {
            Object blackList = redisUtil.getBlackList(token);
            if(blackList != null){
                if(blackList.equals("logout")){
                    throw new IllegalArgumentException("Please Login again.");
                }
            }
            if (!jwtUtil.validateToken(token)) {
                response.sendError(401, "만료되었습니다.");
                jwtExceptionHandler(response, "401", HttpStatus.BAD_REQUEST.value());
                return;

            }
            AuthenticatedUserByToken(token);
        }
        filterChain.doFilter(request,response);
    }
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        //jwtUtil에서  username에 알맞은 User객체를 가져와서 Authentication에 넣어준다.
        Authentication authentication = jwtUtil.createUserAuthentication(username);

        //그리고 빈 컨텍스트에 가져온 데이터(User객체와, username) authentication변수를 넣어주고
        context.setAuthentication(authentication);
        //누가 인증하였는지에 대한 정보들을 저장한다.
        SecurityContextHolder.setContext(context);
    }

    private void AuthenticatedUserByToken(String token) {
        Claims accessTokenInfo = jwtUtil.getUserInfoFromToken(token);
        setAuthentication(accessTokenInfo.getSubject());
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}