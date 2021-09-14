package com.uxsino.leaderview.model;

public enum EnableFlagEnum {

    OK("OK"), // 正常状态
    DELETED("DELETED"); // 删除状态

    private String value;

    private EnableFlagEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
