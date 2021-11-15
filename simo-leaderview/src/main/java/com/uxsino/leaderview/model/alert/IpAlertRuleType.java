package com.uxsino.leaderview.model.alert;

public enum IpAlertRuleType {
    subnet_usage("子网使用率"),
    ip_conflict("IP冲突"),
    not_base_ip("非基准IP"),
    source_ip_change("上联设备变化"),
    source_if_change("上联设备端口变化"),
    ip_not_online("基准IP不在线");

    private String text;

    private IpAlertRuleType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
