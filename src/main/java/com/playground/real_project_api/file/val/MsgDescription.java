package com.playground.real_project_api.file.val;

public enum MsgDescription {
    SUCEESS("업로드 성공"),
    DB_ERROR("DB 연동실패"),
    PARAM_NOT_FOUND("필수 파라미터 누락"),
    FAIL("업로드 실패");


    private String message;

    MsgDescription(String description){
        this.message = description;
    }

    public String getMessage() {
        return message;
    }
}
