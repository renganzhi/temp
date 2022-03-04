package com.uxsino.leaderview.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "simo_leaderview_time_data")
@Data
public class TimeData {

    @Id
    private Long id;
    //接口名称
    private String name;
    //cron表达式
    private String cron;
    //组件类型
    private String type;
    //调用接口需要的参数
    private String conf;
    //接口需要的结果
    @Column(columnDefinition = "TEXT")
    private String value;

}
