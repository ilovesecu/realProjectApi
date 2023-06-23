package com.playground.real_project_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //ajax 요청이 왔을 때 처리된 응답을 보내줄지 여부
        config.addAllowedOrigin("*"); //모든 IP 응답 허용
        config.addAllowedHeader("*");
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.GET);
        //config.addAllowedMethod("*");
        source.registerCorsConfiguration("/api/**", config); //api로 들어오는 모든 주소는 해당 config설정을 적용
        return new CorsFilter(source);
    }
}
