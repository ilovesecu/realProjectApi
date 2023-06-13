package com.playground.real_project_api.file.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadParam {
    /*private String clientIP;
    private int memNo;*/
    private String temp = "0";
    private MultipartFile file;
    private String blur = "0";    // 0:blur이미지 미생성
    private String resize = "0";  // 1 : 채팅용 또는 썸네일용 리사이즈 제작
}
