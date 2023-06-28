package com.playground.real_project_api.auth.member;

import com.playground.real_project_api.proc.RealProjectMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**********************************************************************************************
 * @FileName : PrincipalDetailsService.java
 * @Date : 2023-06-26
 * @작성자 : 정승주
 * @설명 :
 *
 *
 * 시큐리티 설정(SecurityConfig.java)에 loginProcessingUrl("/login");을 걸어놨기 때문에
 * /login 으로 요청이 오면 자동으로 UserDetailsService타입으로 IoC되어 있는 클래스의     loadUserByUsername() 함수가 호출 된다. (이건 규칙이다.)
 * 이때 클라이언트로 넘어오는 파라미터 값이 username, password로 되어있어야 로그인이 된다. 만약 다른 파라미터명을 사용하고 싶다면 시큐리티 설정(SecurityConfig.java)의 configure메소드에 .usernameParameter(~), .passwordParameter(~) 를 설정해주어야한다.
 * /login security 사용하면 기본 로그인 주소인데 formLogin을 disabled 시켜놔서 작동하지 않음 주의!
 **********************************************************************************************/

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final RealProjectMemberMapper realProjectMemberMapper;

    //loadUserByUsername 메소드는 무슨 역할을 하는 녀석이길래 자동으로 호출까지 해줄까?
    //바로 클라이언트가 보낸 아이디가(로그인을 시도하려는 아이디) 실제로 DB에 저장되어 있는 녀석인지 알아보는 메소드이다.
    //loadUserByUsername 메소드가 반환될 때 Authentication객체 내부에 반환되는 PrincipalDetails(UserDetails)가 쏙 들어간다.
    // 그리고 시큐리티 세션에 Authentication이 쏙 들어간다. 그래서 UserDetailsService를 구현하는 것이다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RpmMember rpmMember = realProjectMemberMapper.getFullUserWithUserId(username);
        System.out.println(rpmMember);
        return null;
    }
}
