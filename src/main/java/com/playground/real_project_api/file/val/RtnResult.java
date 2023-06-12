package com.playground.real_project_api.file.val;

/********************************************************************************************** 
 * @FileName : RtnResult.java 
 * @Date : 2023-06-12 
 * @작성자 : 정승주 
 * @설명 :
 **********************************************************************************************/ 
public enum RtnResult {
    SUCCESS("success", 2001, "업로드 성공"),
    DENIED_PARAM("fail",1990, "접근 오류_필수파라미터 누락"),
    DENIED_MEMBER("fail", 1980, "회원정보 오류(DB매칭실패)"),
    DENIED_EXTENSION("fail", 1970, "허용되지 않는 확장자 입니다"), //NOT ALLOW
    ERROR_FILE_FORMAT("error", 1890,"파일 포맷 오류"),
    ERROR_SYSTEM_FAULT("error", 1880, "시스템 오류");

    private String result;
    private Integer code;
    private String description;

    RtnResult(String result, Integer code, String description){
        this.result = result;
        this.code = code;
        this.description=description;
    }

    public String getResult(){return this.result;}
    public Integer getCode(){return this.code;}
    public String getDescription(){return this.description;}
}
