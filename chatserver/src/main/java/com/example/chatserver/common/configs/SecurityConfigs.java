package com.example.chatserver.common.configs;


import com.example.chatserver.common.auth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// 필터를 직접 커스텀 하는 곳
@Configuration
public class SecurityConfigs {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfigs(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }


    /*
    스프링 컨테이너가 관리하는 싱글톤 객체, myFilter란 메소드가 반환하는 객체는 싱글톤으로 만든다.

    내가 커스텀해서 만든 스프링 필터 체인을 통해 사용자 요청응 검증하겠다.
    */

    @Bean
    public SecurityFilterChain myFilter(HttpSecurity httpSecurity) throws Exception {


        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) //csrf 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) //HTTP Basic 비활성화(토큰기반 인증처리를 할거기 때문에 굳이 안씀)
//                특정 url패턴에 대해서는 Authentication객체 요구하지 않음.(인증처리 제외)
                .authorizeHttpRequests(a -> a.requestMatchers("/member/create", "/member/doLogin", "connect/**").permitAll() // 허용
                        .anyRequest().authenticated()) // 나머지는 인증 처리
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션방식을 사용하지 않겠다라는 의미
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*")); //모든 HTTP메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); //모든 헤더값 허용
        configuration.setAllowCredentials(true); //자격증명허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //모든 url에 패턴에 대해 cors 허용 설정
        return source;
    }

    // 암호화를 위한 라이브러리를 싱글톤 객체로 만들어놓고 사용함
    // 멤버서비스에서 의존성 주입받기만 하면됨
    @Bean
    public PasswordEncoder makePassword() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
