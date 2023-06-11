package com.playground.real_project_api.file.val;

public enum MsgDescription {
    SUCEESS("업로드 성공"),
    DB_ERROR("DB 연동실패"),
    FAIL("업로드 실패");


    private String description;

    MsgDescription(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
