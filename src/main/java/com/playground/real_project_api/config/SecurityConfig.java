package com.playground.real_project_api.config;

import com.playground.real_project_api.auth.jwt.filter.JwtAuthenticationFilter;
import com.playground.real_project_api.auth.jwt.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfig corsConfig;

    //https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //UsernamePasswordAuthenticationFilter 작동전에 JwtFilter가 작동하도록 설정
        //강의에서는 SecurityContextPersistenceFilter 작동전. 하지만 deprecated
        //http.addFilter(new JwtAuthenticationFilter());
        //http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션을 사용X (STATELESS 전용)
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeHttpRequests((authz) -> authz
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN") //인증 뿐 아니라 역할도 맞아야해.
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().permitAll()//해당 URL이 아닌 곳은 모두 허용
                )
        .addFilter(corsConfig.corsFilter()); //cors정책 설정 (CorsConfig 참고), @CrossOrigin은 인증이없을 때 사용, 인증이있을때는 시큐리티 필터에 등록 -> Authentication Filter 인증보다 앞에 필터를 추가해주어야 한다.
        return http.build();
    }


    // js, css, image 설정은 보안 설정의 영향 밖에 있도록 만들어주는 설정. [static 폴더 내의 파일]
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }

    //authenticationManagerBean 대체
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}
