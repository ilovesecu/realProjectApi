package com.playground.real_project_api.auth;

import com.playground.real_project_api.auth.member.RpmMember;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


/********************************************************************************************** 
 * @FileName : PrincipalDetails.java 
 * @Date : 2023-06-26 
 * @작성자 : 정승주 
 * @설명 : 해당 객체는 로그인 성공 시 Authentication 객체에 들어가고 Authentication 객체는 Security Session에 들어간다.
 **********************************************************************************************/ 
@Data
public class PrincipalDetails implements UserDetails {

    private RpmMember rpmMember;

    public PrincipalDetails(RpmMember rpmMember){this.rpmMember = rpmMember;}

    //해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return null;
            }
        });
        return null;
    }

    @Override
    public String getPassword() {
        return rpmMember.getRpmAuthPassword().getPasswords();
    }

    @Override
    public String getUsername() {
        return rpmMember.getUserId();
    }

    @Override //계정이 만료 안되었니?
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override //계정이 안잠겼니?
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override //비밀번호기간이 안지났니?
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override //계정이 활성화 되어있니?
    public boolean isEnabled() {
        //사이트에서 1년 동안 로그인하지 않으면 휴면으로 하려고 한다면 (물론 User객체에 최종 로그인 시간이 있어야함)
        //현재시간 - 최종로그인시간 = 1년초과 시 return false; 하면 된다.
        return true;
    }
}
