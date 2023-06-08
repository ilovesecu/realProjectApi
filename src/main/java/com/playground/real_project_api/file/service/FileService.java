package com.playground.real_project_api.file.service;

import com.playground.real_project_api.file.vo.FileUploadParam;
import com.playground.real_project_api.proc.RealProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final RealProjectMapper realProjectMapper;

    /**********************************************************************************************
     * @Method 설명 : 이미지 파일 업로드
     * @작성일 : 2023-06-09
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public void uploadImage(String type, String subType, FileUploadParam fileUploadParam){
        this.uploadFile();
    }

    /**********************************************************************************************
     * @Method 설명 : 파일 업로드 공통 메서드
     * @작성일 : 2023-06-09
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public void uploadFile(){

    }
}
