package com.playground.real_project_api.file.config;

import java.util.*;

/**********************************************************************************************
 * @FileName : FileDirMappingConfig.java
 * @Date : 2023-06-11
 * @작성자 : ilovepc
 * @설명 : 파일 타입별 업로드 폴더 맵핑 설정 클래스
 **********************************************************************************************/
public class FileDirMappingConfig {
    /** 타입별 photo folder */
    private static final Map<String,String> FILE_TYPE_MAPPING = new HashMap<>();
    /** 파입 업로드/다운로드 가능한 타입 목록 */
    public static List<String> FILE_UPDOWN_LIST;

    //클래스가 초기화될 때 실행되고, main() 보다 먼저 수행됩니다.
    static {
        //////////////////////////////////////
        // 공통 타입
        //////////////////////////////////////
        FILE_TYPE_MAPPING.put("temp"       ,"project_tmp");    // 임시저장용


        //////////////////////////////////////
        // REAL MEME - 하나의 ServiceType에서도 여러 업로드 경로를 사용할 수 있으므로 여기서는 ServiceType을 사용하지 말고 String으로 구분
        //////////////////////////////////////
        FILE_TYPE_MAPPING.put("meme"       ,"project_meme");    // meme project용 폴더


        //////////////////////////////////////
        // 파입업로드/다운로드 가능한 타입
        //////////////////////////////////////
        FILE_UPDOWN_LIST = Arrays.asList("project_meme","");
    }
    //Type에 해당하는 폴더명을 반환
    public static String get(String type){return FILE_TYPE_MAPPING.get(type);}


}
