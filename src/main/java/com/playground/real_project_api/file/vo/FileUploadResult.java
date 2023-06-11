package com.playground.real_project_api.file.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String resultVal;
    private String message; // (Optional)

    private String encFileName;
    private String blurEncFileName;
    private String blur = "0";    // 0:blur이미지 미생성
    private String resize = "0";  //1 : 채팅용 또는 썸네일용 리사이즈 제작
    private String temp = "0";
    @JsonIgnore
    private String fileFullPath;
}
