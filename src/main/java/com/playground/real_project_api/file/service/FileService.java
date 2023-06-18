package com.playground.real_project_api.file.service;

import com.playground.real_project_api.commons.val.ServiceType;
import com.playground.real_project_api.file.config.FileDirMappingConfig;
import com.playground.real_project_api.file.config.ImageFileSizeMappingConfig;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
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
        String upDir = this.makeDirWithDate(dateMap, type); //업로드 경로 -> \home\storage\${temp}\project_meme\2023\06\13\0~2

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
                upDir = upDir.replace("${temp}", tempDir+File.separator); // \home\storage\project_tmp\project_meme\2023\06\13\0~2 or \home\storage\project_meme\2023\06\13\0~2

                //서버 저장 파일명
                String resultDecimal = RandomStringGenerator.getRandomStringDigit(RANDOM_STRING_LENGTH);
                String serverFileName = this.getNewFileName(memNo, dateMap, fileExtensionChkRes.getExtType(), resultDecimal, type);
                //암호화
                String encFileName = EncryptionHelper.encryptAES256(serverFileName);
                String errMessage = "";
                //원본파일 업로드
                boolean uploadResult = this.uploadFile(upDir,serverFileName, imageFile.getInputStream(), errMessage);
                //이미지 읽기
                BufferedImage inputImage = ImageIO.read(imageFile.getInputStream());
                //리사이즈 제작
                if(uploadResult && fileUploadParam.getResize().equals("1")){
                    Map<String,Integer> sizeMap = ImageFileSizeMappingConfig.getSizeMap(type);
                    for (Map.Entry<String, Integer> entry : sizeMap.entrySet()) {
                        String key = entry.getKey();
                        Integer value = entry.getValue();
                        this.resizeImage(upDir, serverFileName, inputImage, key,value,value);
                    }
                }
                //블러 이미지 제작
                if(uploadResult && fileUploadParam.getBlur().equals("1")){
                    
                }

                if(uploadResult){
                    fileUploadResult.setFileFullPath(upDir+File.separator+serverFileName);
                    fileUploadResult.setEncFileName(encFileName);
                    fileUploadResult.setResultVal(RtnResult.SUCCESS.getResult());
                    fileUploadResult.setCode(RtnResult.SUCCESS.getCode());
                }else{
                    fileUploadResult.setResultVal(RtnResult.FAIL.getResult());
                    fileUploadResult.setCode(RtnResult.FAIL.getCode());
                    fileUploadResult.setMessage(errMessage);
                }

            }catch (Exception e){
                e.printStackTrace();
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
    public boolean uploadFile(String uploadDir, String fileName, InputStream inputStream, String errMessage) throws IOException {
        boolean uploadResult = true;
        try{
            Path uploadDirFullPath = Paths.get(uploadDir);
            if(!Files.exists(uploadDirFullPath, LinkOption.NOFOLLOW_LINKS)) Files.createDirectories(uploadDirFullPath); //NOFOLLOW_LINKS : 심볼릭 링크는 따라가지 않는다.
            Path uploadFileFullPath = Paths.get(uploadDirFullPath + File.separator + StringUtils.cleanPath(fileName));
            long read = Files.copy(inputStream, uploadFileFullPath, StandardCopyOption.REPLACE_EXISTING); //파일 복사(업로드), REPLACE_EXISTING : 같은 파일명이 존재하면 덮어쓰기
            File file = uploadFileFullPath.toFile();
            uploadResult = file.exists() && file.length() == read;
            if(!uploadResult){
                log.error("uploadFile fail ==> 업로드가 정상적으로 이루어지지 않았습니다. (업로드된 파일이 존재하지 않거나, 파일 용량이 맞지않습니다.)");
            }
        }catch (Exception e){
            log.error("uploadFile fail ==> uploadDir:{}, filename:{} e:",uploadDir,fileName);
            uploadResult = false;
        }finally {
            inputStream.close();
        }
        if(!uploadResult) errMessage = "원본 파일 업로드 실패";
        return uploadResult;
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 리사이즈 제작
     * @작성일 : 2023-06-14 
     * @작성자 : 정승주
     * @변경이력 : 성능이 좀 느림.. 읽는 부분은 공통부분이니까 최적화는 가능할듯 -> 스케일링할 떄 Graphics2D말고.. Thumbnails.of 로해볼까?
     **********************************************************************************************/
    private void resizeImage(String uploadDir, String serverFileName, BufferedImage inputImage, String sizeCodeName, int dstW, int dstH) throws IOException {
        Image scaledImage = inputImage.getScaledInstance(dstW, dstH, Image.SCALE_SMOOTH);
        //이미지 저장용 객체 만들기
        BufferedImage outputImage = new BufferedImage(dstW, dstH, BufferedImage.TYPE_INT_RGB);
        //지정된 크기로 스케일링 및 그리기
        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(scaledImage, 0, 0, null);
        graphics2D.dispose();

        String[] splitted=serverFileName.split("\\.");
        String resizeName = splitted[0]+"_"+sizeCodeName+"."+splitted[1];
        File outputFile = new File(uploadDir,resizeName);
        ImageIO.write(outputImage, "PNG",outputFile);
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 블러 이미지 제작
     * @작성일 : 2023-06-18 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    private void blurImage(String uploadDir, String serverFileName, BufferedImage inputImage){

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
