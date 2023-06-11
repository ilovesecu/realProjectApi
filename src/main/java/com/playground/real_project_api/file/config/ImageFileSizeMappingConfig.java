package com.playground.real_project_api.file.config;

import com.playground.real_project_api.commons.val.ServiceType;

import java.util.HashMap;
import java.util.Map;

/**********************************************************************************************
 * @FileName : ImageFileSizeMappingConfig.java 
 * @Date : 2023-06-11 
 * @작성자 : ilovepc
 * @설명 : 이미지 타입들의 리사이즈를 결정하는 클래스
 **********************************************************************************************/ 
public class ImageFileSizeMappingConfig {
    private static Map<String,Integer> projectMemeSizeMap = new HashMap<>();

    static {
        projectMemeSizeMap.put("w",720);
        projectMemeSizeMap.put("b",320);
        projectMemeSizeMap.put("m",160);
    }

    /**********************************************************************************************
     * @Method 설명 : 서비스 별 사이즈 정보 저장된 맵 반환
     * @작성일 : 2023-06-11
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public static Map<String,Integer> getSizeMap(ServiceType serviceType){
        switch (serviceType){
            case PROJECT_MEME:
                return projectMemeSizeMap;
            default:
                return null;
        }
    }


}
