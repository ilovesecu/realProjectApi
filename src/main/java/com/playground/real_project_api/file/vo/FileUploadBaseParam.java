package com.playground.real_project_api.file.vo;

import lombok.Data;

import java.util.List;

@Data
public class FileUploadBaseParam {
    private String clientIP;
    private int memNo;
    private List<FileUploadParam> fileUploadParams;
}
