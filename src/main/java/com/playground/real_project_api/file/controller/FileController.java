package com.playground.real_project_api.file.controller;

import com.playground.real_project_api.file.service.FileService;
import com.playground.real_project_api.file.val.SubType;
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

    @PostMapping(value = "/{type}/{subType}/upload")
    public Object fileUpload(@PathVariable(value = "type", required = true) String type,
                             @PathVariable(value = "subType", required = true)String subType,
                             FileUploadParam fileUploadParam,
                             HttpServletRequest request){
        //type : ImageType의 키 / 업로드될 폴더명
        //subType : 사용자가 지정한 파일 타입
        fileUploadParam.setClientIP(request.getRemoteAddr()); //IP 수집

        if(subType.equals(SubType.IMAGE.getName())){
            fileService.uploadImage(type, SubType.IMAGE, fileUploadParam);
        }

        return null;
    }
}
