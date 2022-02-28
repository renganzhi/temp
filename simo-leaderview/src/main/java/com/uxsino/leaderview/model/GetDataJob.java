package com.uxsino.leaderview.model;

import lombok.Data;


@Data
public class GetDataJob {
    private String name;
    private String cron;
    private String group;
    private String conf;

}