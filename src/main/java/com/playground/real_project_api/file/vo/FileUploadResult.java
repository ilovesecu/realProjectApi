package com.playground.real_project_api.file.vo;

import lombok.Data;

/**********************************************************************************************
 * @FileName : FileUploadResult.java
 * @Date : 2023-06-09
 * @작성자 : ilovepc
 * @설명 : 각각 파일 업로드 결과를 저장 - 하나의 요청에 여러 Result가 존재할 수 있음.
 **********************************************************************************************/
@Data
public class FileUploadResult {
    private int code;
    private String rtnResult;

}
