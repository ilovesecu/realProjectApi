package com.playground.real_project_api.auth.jwt.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    //로그인 시도 메소드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도중");
        //1. username, password받기
        //2. 정상인지 로그인 시도 해보기(가장 간단한 방버이 AuthenticationManager로 로그인 시도를 하면 PrincipalDetailsService의 loadUserByUsername이 자동으로 실행)
        //3. PrincipalDetails가 반환되면 PrincipalDetails를 세션에 담는다. (세션에 있어야 유저별 권한 관리할 수 있음. 굳이 권한관리 안하면 세션에 안담아도 됨)
        //4. JWT 토큰 만들어서 응답
        return super.attemptAuthentication(request, response);
    }
}
