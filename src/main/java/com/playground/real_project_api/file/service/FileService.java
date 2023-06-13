package com.playground.real_project_api.file.service;

import com.playground.real_project_api.commons.val.ServiceType;
import com.playground.real_project_api.file.config.FileDirMappingConfig;
import com.playground.real_project_api.file.val.MsgDescription;
import com.playground.real_project_api.file.val.RtnResult;
import com.playground.real_project_api.file.val.SubType;
import com.playground.real_project_api.file.vo.*;
import com.playground.real_project_api.proc.RealProjectMapper;
import com.playground.real_project_api.utils.EncryptionHelper;
import com.playground.real_project_api.utils.file.FileHelper;
import com.playground.real_project_api.utils.random.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    @Value("${file.root.directory}")
    private String uploadRootDir;

    public static final int RANDOM_STRING_LENGTH = 5; //주의:함부로 바꾸면 이전 파일 이름 체계가 엉망
    private final RealProjectMapper realProjectMapper;
    private final FileHelper fileHelper;
    
    /**********************************************************************************************
     * @Method 설명 : 이미지 파일 업로드
     * @작성일 : 2023-06-09
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public FilesUploadRsp uploadImage(String type, SubType subType, FileUploadBaseParam fileUploadBaseParam){
        int memNo = fileUploadBaseParam.getMemNo();
        FilesUploadRsp filesUploadRsp = new FilesUploadRsp();
        List<FileUploadParam> fileUploadParams = fileUploadBaseParam.getFileUploadParams();

        if(fileUploadParams == null || fileUploadParams.size()==0){
            filesUploadRsp.setCode(RtnResult.DENIED_PARAM.getCode());
            filesUploadRsp.setMessage(MsgDescription.PARAM_NOT_FOUND.getMessage());
            return filesUploadRsp;
        }

        //파일 업로드 폴더
        Map<String,String>dateMap = this.getDateMap();
        filesUploadRsp.setTotalCount(fileUploadParams.size());
        String upDir = this.makeDirWithDate(dateMap, type); //업로드 경로

        for(FileUploadParam fileUploadParam : fileUploadParams){
            try{
                MultipartFile imageFile = fileUploadParam.getFile();
                FileUploadResult fileUploadResult = new FileUploadResult();

                //File 예외처리 - custom validate 써보기
                if(imageFile == null){
                    fileUploadResult.setCode(RtnResult.DENIED_PARAM.getCode());
                    fileUploadResult.setMessage(MsgDescription.PARAM_NOT_FOUND.getMessage());
                    return filesUploadRsp;
                }

                String originFileName = imageFile.getOriginalFilename(); //사용자가 업로드한 원본 이름
                //확장자 확인(사용자가 업로드한 그대로)
                String[] splitFileName = originFileName.split("\\.");
                String extFileName = "jpg";
                if (splitFileName.length > 1) {
                    int i = splitFileName.length - 1;
                    extFileName = splitFileName[i].trim().toLowerCase();
                }
                //Tika 확장자 검사
                FileExtensionChkRes fileExtensionChkRes = fileHelper.fileTypePermitCheck(imageFile.getInputStream(), subType, originFileName);
                if(!fileExtensionChkRes.isResult()){
                    fileUploadResult.setCode(RtnResult.DENIED_EXTENSION.getCode());
                    fileUploadResult.setMessage(RtnResult.DENIED_EXTENSION.getDescription());
                    fileUploadResult.setResultVal(RtnResult.DENIED_EXTENSION.getResult());
                    filesUploadRsp.addFileUploadResult(fileUploadResult);
                    filesUploadRsp.addErrorCnt();
                    continue;
                }
                //임시 저장 여부
                String tempDir = "";
                if("1".equals(fileUploadParam.getTemp())){
                    tempDir = FileDirMappingConfig.getDir("temp");
                }
                upDir = upDir.replace("${temp}", tempDir+File.separator);
                System.out.println(upDir);

                //서버 저장 파일명
                String resultDecimal = RandomStringGenerator.getRandomStringDigit(RANDOM_STRING_LENGTH);
                String serverFileName = this.getNewFileName(memNo, dateMap, fileExtensionChkRes.getExtType(), resultDecimal, type);
                //암호화
                String encFileName = EncryptionHelper.encryptAES256(serverFileName);
                //원본파일 업로드

                this.uploadFile(upDir,serverFileName, imageFile.getInputStream());
            }catch (Exception e){

            }

        }
        return filesUploadRsp;
    }

    /**********************************************************************************************
     * @Method 설명 : 파일 업로드 공통 메서드
     * @작성일 : 2023-06-09
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public void uploadFile(String uploadDir, String fileName, InputStream inputStream) throws IOException {
        try{
            Path uploadDirFullPath = Paths.get(uploadDir);
            if(!Files.exists(uploadDirFullPath, LinkOption.NOFOLLOW_LINKS)) Files.createDirectories(uploadDirFullPath); //NOFOLLOW_LINKS : 심볼릭 링크는 따라가지 않는다.
            Path uploadFileUllPath = Paths.get(uploadDirFullPath + File.separator + StringUtils.cleanPath(fileName));
            Files.copy(inputStream, uploadFileUllPath, StandardCopyOption.REPLACE_EXISTING); //파일 복사(업로드), REPLACE_EXISTING : 같은 파일명이 존재하면 덮어쓰기
        }catch (Exception e){

        }finally {
            inputStream.close();
        }
    }

    /**********************************************************************************************
     * @Method 설명 : 업로드 경로 (시간 디렉토리)
     * @작성일 : 2023-06-13
     * @작성자 : 정승주
     * @변경이력 : 임시저장 인지는 반복문에서 결정되므로 ${temp}로 설정해두고 반복문 안에서 최종적으로 replace 하도록 한다.
     **********************************************************************************************/
    private String makeDirWithDate(Map<String,String>dateMap, String type){
        return uploadRootDir + File.separator + "${temp}" + FileDirMappingConfig.getDir(type)
                + File.separator + dateMap.get("yyyy")
                + File.separator + dateMap.get("MM")
                + File.separator + dateMap.get("dd")
                + File.separator + dateMap.get("hhVal");
    }


    /**********************************************************************************************
     * @Method 설명 : 서비에 저장될 파일명 생성 (중복 제거) → 해당 이름으로 파일이 서버에 저장됨.
     * @작성일 : 2023-06-12
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    private String getNewFileName(int memNo, Map<String,String> dateMap, String exType, String resultDecimal, String type){
        return memNo + "_"
                + dateMap.get("yyyy")
                + dateMap.get("MM")
                + dateMap.get("dd")
                + dateMap.get("HH")
                + dateMap.get("mm")
                + dateMap.get("ss")
                + resultDecimal + "_" + type + "." + exType;
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
