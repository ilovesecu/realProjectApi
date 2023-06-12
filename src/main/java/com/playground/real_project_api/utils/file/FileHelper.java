package com.playground.real_project_api.utils.file;

import com.playground.real_project_api.file.val.SubType;
import com.playground.real_project_api.file.vo.FileExtensionChkRes;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MediaTypeRegistry;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
@Component
public class FileHelper {
    
    /********************************************************************************************** 
     * @Method 설명 : 파일 확장자 검사 
     * @작성일 : 2023-06-12 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public FileExtensionChkRes fileTypePermitCheck(InputStream inputStream, SubType subType, String originFileName) throws IOException {
        FileExtensionChkRes fileExtensionChkRes = new FileExtensionChkRes();
        try{
            String mimeType = new Tika().detect(inputStream);
            /* Tika supported ext check */
            //https://cwiki.apache.org/confluence/display/TIKA/Troubleshooting+Tika#TroubleshootingTika-MimeTypeMissing
            //https://stackoverflow.com/questions/70158982/retrive-bad-mimetype-with-tika-application-x-tika-ooxml
            //https://bbbicb.tistory.com/54
            // Get your Tika Config, eg
            TikaConfig config = TikaConfig.getDefaultConfig();
            // Get the registry
            MediaTypeRegistry registry = config.getMediaTypeRegistry();
            // List
            /*for (MediaType t : registry.getTypes()) {
                String typeStr = t.toString();
                System.out.println(typeStr);
            }*/
            String[] splited = mimeType.split("/");
            String fileType = splited[0];
            String extType = splited[1];
            fileExtensionChkRes.setFileType(fileType);
            fileExtensionChkRes.setExtType(extType);
            fileExtensionChkRes.setFullType(mimeType);
            switch (subType){
                case EDITOR_IMAGE:
                case IMAGE:
                    fileExtensionChkRes.setResult(this.allowImageType(fileExtensionChkRes.getExtType()));
                    break;
                case NORMAL:
                    /*if(Arrays.stream(offMarkTypeList).anyMatch(mimeType::equals))
                        this.convertUnsupportedTikaExtension(inputStream, originFileName, fileExtensionChkRes);
                    fileExtensionChkRes.setResult(this.allowTextFileType(fileExtensionChkRes.getExtType()));*/
                    break;
                default:
                    log.error("fileTypePermitCheck type is not defined");
                    fileExtensionChkRes.setResult(false);
            }
        }catch(Exception e){
            log.error("[fileTypePermitCheck] error - e",e);
            fileExtensionChkRes.setResult(false);
        }finally {
            inputStream.close();
        }
        return fileExtensionChkRes;
    }


    /********************************************************************************************** 
     * @Method 설명 : 이미지 업로드 허용 확장자 
     * @작성일 : 2023-06-12 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public boolean allowImageType(String extension){
        if(extension == null) return false;
        return "jpg|jpeg|gif|png".contains(extension);
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 문서 파일 업로드 허용 확장자
     * @작성일 : 2023-06-12 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public boolean allowTextFileType(String extension){
        if(extension == null) return false;
        return "doc|docx|hwp|txt|xml|pdf|xls|xlsx|ppt|pptx|zip".contains(extension);
    }
}
