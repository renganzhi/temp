package com.uxsino.leaderview.model;

/**
 * 用其表示某一个页面的三种状态：1.属于当前用户的页面 2.被其他用户分享的页面 3.与当前用户无关的页面
 */
public enum ShareState {
    IS_BELONGS_CURRENT(0,"属于当前用户的页面"),
    IS_BE_SHARED(1,"被其他用户分享的页面"),
    INDEPENDENT(2,"与当前用户无关的页面");


    /*管理状态的枚举值*/
    private int value;

    /*管理状态的枚举名称*/
    private String name;
    private ShareState(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
