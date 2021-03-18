package com.uxsino.leaderview.model.business;

/**
 * @description 业务系统管理状态
 * @date 2019年1月4日
 */
public enum ManageStatus {
                          Online(0,"在线"),
                          Offline(1,"下线"),
                          Delected(2,"已删除");
    private int value;

    private String text;

    private ManageStatus(int value, String text) {
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
