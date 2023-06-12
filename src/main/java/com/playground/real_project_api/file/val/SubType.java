package com.playground.real_project_api.file.val;

public enum SubType {
    EDITOR_IMAGE("editor_image"),
    AUDIO("audio"),
    IMAGE("image"),
    VIDEO("video"),
    NORMAL("normal");

    private String name;

    SubType(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
