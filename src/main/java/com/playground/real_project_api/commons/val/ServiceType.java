package com.playground.real_project_api.commons.val;

public enum ServiceType {
    PROJECT_MEME("meme");

    private String projectName;

    ServiceType(String projectName){
        this.projectName=projectName;
    }

    public String getProjectName() {
        return projectName;
    }
}
