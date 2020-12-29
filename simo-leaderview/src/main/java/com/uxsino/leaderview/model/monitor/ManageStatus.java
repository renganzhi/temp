package com.uxsino.leaderview.model.monitor;

/**
 * **********************************************************
 * 管理对象的管理状态的枚举对象
 * 
 * 
 * @version iSoc Service Platform, 2015-3-5
 ***********************************************************
 */
public enum ManageStatus {
                          Unknow(0,"未知"),
                          // New(1,"新添加"), // 新添加，但未添加到管理域中,管理对象尚未激活，不能进行监管控
                          Online(2,"在线"),	// 管理对象在线,可正常进行监控，监控状态下，部分属性不可编辑
                          Offline(3,"下线"),	// 管理对象下线,设备不可进行监控。当前对象属性不可编辑
                          Maintain(4,"维护"),	// 管理对象正在维护,设备不可进行监控。可编辑对象属性
                          Delected(5,"已销毁");	// 管理对象已被销毁,不可进行监控，不可查看，不可修改

    /*管理状态的枚举值*/
    private int value;

    /*管理状态的枚举名称*/
    private String name;

    /**
     * 
     * 创建一个 ManageStatus 对象实例.
     * 
     * @param value
     */
    private ManageStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 
     * 由枚举值生成枚举对象
     * 
     * @param value
     * @return
     */
    public static ManageStatus fromValue(int value) {
        for (ManageStatus manageStatus : ManageStatus.values()) {
            if (manageStatus.value == value) {
                return manageStatus;
            }
        }
        throw new IllegalArgumentException("0-5 is a range of parameter('value')");
    }

    /**
     * 
     * 取得枚举对象的值
     * 
     * @return
     */
    public int getValue() {
        return this.value;
    }

    /**
     * 
     * 取得枚举对象的名称
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }
}
