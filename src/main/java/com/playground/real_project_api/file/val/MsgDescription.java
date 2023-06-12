package com.playground.real_project_api.file.val;

/**********************************************************************************************
 * @FileName : MsgDescription.java
 * @Date : 2023-06-12
 * @작성자 : 정승주
 * @설명 : FilesUploadRsp 에서 사용되는 하나의 요청에 대한 전반적인 메시지
 **********************************************************************************************/
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
