package com.playground.real_project_api.file.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playground.real_project_api.file.val.MsgDescription;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**********************************************************************************************
 * @FileName : FileUploadRsp.java 
 * @Date : 2023-06-09 
 * @작성자 : ilovepc
 * @설명 : 파일 업로드 결과 - 요청 당 하나만 생성 - Multi 업로드에 대한 것도 한개만 생성하여 반환
 **********************************************************************************************/
@Data
public class FilesUploadRsp {
    private int errorCount = 0; //업로드 실패 수
    private int totalCount = 0; //총 업로드 수

    private int code; //해당 요청의 성공/실패 코드
    private String message = MsgDescription.SUCEESS.getDescription(); //업로드 성공 여부 메시지
    private List<FileUploadResult> fileUploadResultList = new ArrayList<>();
    
    public void addFileUploadResult(FileUploadResult fileUploadResult){
        this.fileUploadResultList.add(fileUploadResult);
    }
    public void addErrorCnt(){
        this.errorCount += 1;
    }
}
