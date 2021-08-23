package com.uxsino.leaderview.model;

/**
 * @ClassName AlertType
 * @Description TODO
 * @Author <a href="mailto:royrxc@gmail.com">Ran</a>
 * @Daate 2021/8/18 17:18
 **/
public enum AlertType {
    Alert("simo_alert","资源告警"),
    NELinkAlert("simo_alert_network_link","链路告警"),
    SysLogAlert("simo_alert_syslog","Syslog告警"),
    ThirdPartyAlert("simo_alert_thirdparty","第三方告警"),
    BusinessAlert("simo_alert_business","业务告警"),
    SnmpTrapAlert("simo_alert_snmptrap","SnmpTrap告警"),
    SystemAlert("simo_alert_system","系统告警"),
    TerminalAlert("simo_alert_terminal","终端告警"),
    IpAlert("simo_alert_ip","IP告警");

    private String value;

    private String text;

    AlertType(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }
}
