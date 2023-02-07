package com.team.final8teamproject.security.config;

import com.team.final8teamproject.security.exception.CustomAccessDeniedHandler;
import com.team.final8teamproject.security.exception.CustomAuthenticationEntryPoint;
import com.team.final8teamproject.security.jwt.JwtAuthFilter;
import com.team.final8teamproject.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(prePostEnabled = true) // @Secured 어노테이션 활성화
@EnableScheduling // @Scheduled 어노테이션 활성화
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 가장 먼저 시큐리티를 사용하기 위해선 선언해준다.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.authorizeRequests()
        http.authorizeHttpRequests()//요청에 대한 권한을 지정할 수 있다.
                .requestMatchers("/users/**").permitAll()
                .requestMatchers("/h2-console").permitAll()
                .anyRequest().authenticated()//인증이 되어야 한다는 이야기이다.
                //.anonymous() : 인증되지 않은 사용자도 접근할 수 있다.
                // JWT 인증/인가를 사용하기 위한 설정
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
                // 401 Error 처리, Authorization 즉, 인증과정에서 실패할 시 처리
                http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
                // 403 Error 처리, 인증과는 별개로 추가적인 권한이 충족되지 않는 경우
                http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

//                .formLogin().failureHandler();
//                http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl());
//        http.formLogin().loginPage("/api/user/login-page").permitAll();
        // 이 부분에서 login 관련 문제 발생
        // jwt 로그인 방식에서는 세션 로그인 방식을 막아줘야 한다.
//        http.exceptionHandling().accessDeniedPage("/api/user/forbidden");
        return http.build();
    }
}