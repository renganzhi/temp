package com.uxsino.leaderview.model;


/**
 * 主页轮播时，数据刷新方式
 * @author Wang Sihao
 * @create 2019/6/14
 */
public enum RefreshType {

    /**
     * 手动刷新方式
     */
    Manual(0,"手动刷新"),

    /**
     * 自动刷新方式
     */
    Automatic(1,"自动刷新");

    private int value;

    private String text;

    RefreshType(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
