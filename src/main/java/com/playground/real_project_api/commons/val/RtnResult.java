package com.playground.real_project_api.commons.val;

public enum RtnResult {
    SUCCESS("success", 1, "업로드 성공"),
    DENIED_PARAM("fail",0, "접근 오류_필수파라미터 누락"),
    DENIED_MEMBER("fail", -1, "회원정보 오류(DB매칭실패)"),
    ERROR_FILE_FORMAT("error", -2,"파일 포맷 오류"),
    ERROR_SYSTEM_FAULT("error", -3, "시스템 오류");

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
