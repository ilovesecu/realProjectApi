package com.playground.real_project_api.file.service;

import com.playground.real_project_api.commons.val.ServiceType;
import com.playground.real_project_api.file.val.MsgDescription;
import com.playground.real_project_api.file.val.RtnResult;
import com.playground.real_project_api.file.vo.FileUploadParam;
import com.playground.real_project_api.file.vo.FilesUploadRsp;
import com.playground.real_project_api.proc.RealProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    public static final int RANDOM_STRING_LENGTH = 5; //주의:함부로 바꾸면 이전 파일 이름 체계가 엉망
    private final RealProjectMapper realProjectMapper;
    
    /**********************************************************************************************
     * @Method 설명 : 이미지 파일 업로드
     * @작성일 : 2023-06-09
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public FilesUploadRsp uploadImage(String type, String subType, FileUploadParam fileUploadParam){
        FilesUploadRsp filesUploadRsp = new FilesUploadRsp();

        MultipartFile[] imageFiles = fileUploadParam.getFiles();
        //File 예외처리 - custom validate 써보기
        if(imageFiles == null || imageFiles.length ==0){
            filesUploadRsp.setCode(RtnResult.DENIED_PARAM.getCode());
            filesUploadRsp.setMessage(MsgDescription.PARAM_NOT_FOUND.getMessage());
            return filesUploadRsp;
        }



        this.uploadFile();
        return filesUploadRsp;
    }

    /**********************************************************************************************
     * @Method 설명 : 파일 업로드 공통 메서드
     * @작성일 : 2023-06-09
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public void uploadFile(){

    }
    
    /********************************************************************************************** 
     * @Method 설명 : 파일 업로드 폴더를 위한 현재 날짜,시,분,초를 key로 하는 Map 반환
     * @작성일 : 2023-06-11 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    private Map<String,String> getDateMap(){
        Map<String,String> dateMap = new HashMap<>();
        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formatted = sdf.format(date);
        String[] splited = formatted.split("-");
        dateMap.put("yyyy", splited[0]);
        dateMap.put("MM", splited[1]);
        dateMap.put("dd", splited[2]);
        dateMap.put("HH", splited[3]);
        dateMap.put("mm", splited[4]);
        dateMap.put("ss", splited[5]);

        // 1~8:0 / 9~16:1 / 17~0:2
        int hhIntVal = Integer.parseInt(splited[3]);
        if(1 <= hhIntVal && hhIntVal <= 8){
            dateMap.put("hhVal","0");
        }else if(9 <= hhIntVal && hhIntVal <= 16){
            dateMap.put("hhVal","1");
        }else{
            dateMap.put("hhVal","2");
        }

        return dateMap;
    }
}
