package com.uxsino.leaderview.model.monitor;

/**
 * **********************************************************
 * 链路的状态
 * 
 * 
 * @version iSoc Service Platform, 2015-3-5
 ***********************************************************
 */
public enum NetworkLinkStatus {
                               Unkown(0,"未知"),
                               Enable(1,"正常"),
                               Alert(2,"告警"),
                               Unconnection(3,"断开"),
                               Cancelmonitor(4,"取消监控"),
                               Delected(5,"已删除");

    /*链路状态的枚举值*/
    private int value;

    /*链路状态的枚举名称*/
    private String name;

    /**
     * 
     * 创建一个 NetworkLinkStatus 对象实例.
     * 
     * @param value
     */
    private NetworkLinkStatus(int value, String name) {
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
    public static NetworkLinkStatus fromValue(int value) {
        for (NetworkLinkStatus manageStatus : NetworkLinkStatus.values()) {
            if (manageStatus.value == value) {
                return manageStatus;
            }
        }
        throw new IllegalArgumentException("0-4 is a range of parameter('value')");
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
