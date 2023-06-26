package com.playground.real_project_api.auth.member;

import lombok.Data;

@Data
public class RpmMember {
    private int userNo;
    private String userId;
    private int loginType; //0: id-pw, 1:social

    private RpmAuthPassword rpmAuthPassword; //패스워드 정보
}
