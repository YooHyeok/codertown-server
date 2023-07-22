package io.codertown.support.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // TODO Auto-generated method stub
        http.csrf().disable(); // csrf기능 OFF
        http.httpBasic().disable() // HttpBasicAuth 기반 로그인 기능 OFF
                .authorizeHttpRequests() // 인가 정책 시작
                .antMatchers("/**/*").permitAll() // 모든 경로 접근 허가
                .anyRequest().authenticated(); //어떠한 요청에도 인증 허용
//		.antMatchers("/login**", "/web-respirces/**", "/actuator/**").permitAll()
//		.antMatchers("/admit/**").hasRole("ADMIN")
//		.antMatchers("/user/**").hasRole("USER")

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //세션 제어 (JWT토큰 대체)
        return http.build();
    }
}
