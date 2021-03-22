package com.uxsino.leaderview.model;

public enum ApiFileName {
    MONITOR_FILE_NAME("monitor_home_api.json"),
    BUSINESS_FILE_NAME("business_home_api.json");

    private String value;

    ApiFileName(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
