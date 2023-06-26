package com.playground.real_project_api.auth.member;

import lombok.Data;

@Data
public class RpmAuthPassword {
    private int passwordId;
    private int userNo; //FK 회원번호
    private String passwords;
    private String updateDate;
}
