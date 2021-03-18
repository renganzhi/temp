package com.uxsino.leaderview.model.business;

/**
 * @description 业务系统运行状态
 * @date 2019年1月4日
 */
public enum RunStatus {
                       Unknow(0,"未监控"),
                       Good(1,"正常"),
                       Warning(2,"告警"),
                       Unavailable(3,"不可用");
    private int value;

    private String text;

    RunStatus(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

}
