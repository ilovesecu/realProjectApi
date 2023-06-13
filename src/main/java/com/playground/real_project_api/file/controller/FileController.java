package com.playground.real_project_api.file.controller;

import com.playground.real_project_api.file.service.FileService;
import com.playground.real_project_api.file.val.SubType;
import com.playground.real_project_api.file.vo.FileUploadBaseParam;
import com.playground.real_project_api.file.vo.FileUploadParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/storage")
public class FileController {
    private final FileService fileService;

    /**
     * @param type
     * @param subType
     * @param fileUploadBaseParam
     * @param request
     *
     * =========================================
     * ------------------JSON-------------------
     *  요청 JSON 예시 - @RequestBody 붙이는거 잊지말자.
     *  {
     *   "memNo":1,
     *   "fileUploadParams":[
     *     {
     *       "temp":"1",
     *       "blur":"1",
     *       "resize":"0"
     *     },
     *     {
     *       "temp":"1",
     *       "blur":"1",
     *       "resize":"0"
     *     }
     *    ]
     * }
     * ------------------FORM-------------------
     * 요청 FORM 예시 - @RequestBody 지우는거 잊지말자.
     * memNo:1
     * fileUploadParams[0].temp = "1"
     * fileUploadParams[0].files = multipart/file (binary 데이터)
     * ...
     * =========================================
     *
     * @return
     */
    @PostMapping(value = "/{type}/{subType}/upload")
    public Object fileUpload(@PathVariable(value = "type", required = true) String type,
                             @PathVariable(value = "subType", required = true)String subType,
                             //FileUploadParam fileUploadParam,
                             FileUploadBaseParam fileUploadBaseParam,
                             HttpServletRequest request){
        //type : ImageType의 키 / 업로드될 폴더명
        //subType : 사용자가 지정한 파일 타입
        fileUploadBaseParam.setClientIP(request.getRemoteAddr()); //IP 수집

        if(subType.equals(SubType.IMAGE.getName())){
            return fileService.uploadImage(type, SubType.IMAGE, fileUploadBaseParam);
        }

        return null;
    }
}
