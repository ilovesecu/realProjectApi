package com.playground.real_project_api.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터1");

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setCharacterEncoding("UTF-8");

        //토큰을 만들었다고 가정 (rpm 이라는 토큰)
        if(req.getMethod().equals("POST")){ //POST요청일 때만 동작
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            if(headerAuth != null && headerAuth.equals("rpm")){
                System.out.println("인증완료");
                chain.doFilter(req,res); //필터체인에 등록해준다.
            }else{
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.setContentType("text/plain;charset=UTF-8");
                PrintWriter out = res.getWriter();
                out.print("No Auth Token");
                out.flush();
                out.close();
            }
        }
    }
}
