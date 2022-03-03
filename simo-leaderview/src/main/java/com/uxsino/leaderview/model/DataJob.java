package com.uxsino.leaderview.model;


import lombok.Data;

@Data
public class DataJob {

    //接口名称
    private String name;
    //cron表达式
    private String cron;
    //组件类型
    private String group;
    //调用接口需要的参数和获取的结果
    private String conf;
}
