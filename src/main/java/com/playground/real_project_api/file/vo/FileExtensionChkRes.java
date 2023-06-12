package com.playground.real_project_api.file.vo;

import lombok.Data;

/********************************************************************************************** 
 * @FileName : FileExtensionChkRes.java 
 * @Date : 2023-06-12
 * @작성자 : 정승주 
 * @설명 : 파일 확장자 체크 결과
 **********************************************************************************************/ 

@Data
public class FileExtensionChkRes {
    private boolean result = false; //확장자 체크 결과
    private String fileType=""; // image, audio 등
    private String extType=""; // png, jpeg, mp3 등
    private String fullType=""; // image/png , image/jpeg 등등
    private String originExtType = ""; //사용자가 업로드한 이름에서 추출한 확장자명 (변조 가능성 有)
}
