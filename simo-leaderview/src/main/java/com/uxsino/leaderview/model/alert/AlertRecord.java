package com.uxsino.leaderview.model.alert;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.rule.model.IpAlertRuleType;
import com.uxsino.commons.model.RunStatus;
import com.uxsino.commons.utils.DateUtils;
import com.uxsino.leaderview.model.AlertType;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description 组装的告警信息
 *
 * @date 2017年4月13日
 */
@Data
public class AlertRecord {

    /**
     * 告警信息编号
     */
    private Long alertId;

    /**
     * 告警编码，由相关告警规则生成的加密串
     */
    private String alertCode;

    /**
     * 网元ID
     */
    private String objectId;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 网元名称
     */
    private String objectName;

    /**
     * 网元类型
     */
    private String objectType;

    /**
     * 网元子类型
     */
    private String subType;

    /**告警日志所用的采集器*/
    private String collectorId;

    /**告警日志的日志级别*/
    private String logLevel;

    /**告警日志的日志类型*/
    private String logType;

    /**告警日志的日志阈值*/
    private String thresholdValue;

    /**告警日志的日志来源*/
    private String logHost;

    /**
     * 告警等级值（取值范围1~10，值越大表示告警越严重）
     */
    private Integer level;

    /**
     * 告警级别名称
     */
    private String levelName;

    /**
     * 告警处理状态
     */
    private AlertHandleStatus handleStatus;

    /**
     * 处理告警的用户编号
     */
    private Long handleUserId;

    /**
     * 处理告警的用户名
     */
    private String handleUserName;

    /**
     * 告警处理时间
     */
    private Date handleDate;

    /**
     * 告警处理方式
     */
    private AlertHandleWay handleWay;

    /**
     * 告警处理描述
     */
    private String description;

    /**
     * 告警合并条数（告警累计次数）
     */
    private Integer number;

    /**
     * 告警持续次数（告警恢复时清零）
     */
    private Integer times;

    /**
     * 首次告警时间
     */
    private Date firstAlertDate;

    /**
     * 最近告警时间
     */
    private Date recentAlertDate;

    /**
     * 告警信息描述
     */
    private String recentAlertBrief;

    /**
     * 告警工单ID
     */
    private String processInstanceId;

    /**
     * 告警分类
     */
    private AlertType alertType;

    /**
     * 告警持续时间（分钟，目前用于业务告警）
     */
    private Integer lastMins;

    /**
     * 峰值（目前用于业务告警）
     */
    private String peak;

    /**
     * 告警触发时间（目前用于业务告警）
     */
    private Date touchOffDate;

    /**
     * 服务器IP（目前用于业务告警）
     */
    private String serverIp;

    /**
     * 业务指标（目前用于业务告警）
     */
    private String indicatorName;

    /**
     * 业务告警策略标识（目前用于业务告警）
     */
    private String rule;

    /**
     * 最近告警时间字符串
     */
    private String recentAlertDateStr;

    /**
     * 告警处理时间字符串
     */
    private String handleDateStr;

    /**
     *实时告警时间字符串
     */
    private String realAlertDateStr;

    /**
     * 是否被删除
     */
    private Boolean isDeleted;

    /**
     * 陷阱类型
     */
    private String trapType;

    /**
     * 企业名
     */
    private String enterprise;

    /**
     * 资源监控状态，true：监控中，false：未监控
     */
    private Boolean monitoring;

    /**
     * 对象的运行状态
     */
    private RunStatus runStatus;

    /**
     * 资源所在主机编号，独立软件需要安装在主机上才可使用，如数据库等。
     */
    private String hostId;

    /**
     * 区分硬件和软件
     */
    private boolean isHardware;

    /**
     * 硬件监控状态，true：监控中，false：未监控
     */
    private Boolean isHardObjMonitored;

    /**
     * 硬件的运行状态
     */
    private RunStatus hardRunStatus;

    /**
     * 是否由资源统一管理
     */
    private Boolean sourceManage;

    /**
     * 策略规则Id
     */
    private Long ruleId;

    /**
     * 指标ID
     */
    private String indicatorId;

    /**终端变更记录*/
    private String terminalChangeRecord;

    /**终端Mac地址*/
    private String terminalMac;

    private String ruleType;

    /**
     * ip告警类型
     */
    private IpAlertRuleType ipAlertRuleType;

    /**
     * 是否为可用性告警
     */
    private Boolean isAvailability;

    /**
     * 部件标识
     */
    private String componentId;

    /**
     * 第三方来源
     */
    private String accessSource;

    /**
     * 分享人
     */
    private JSONObject shareUser;

    /**
     * 属性ID
     */
    private String fieldId;

    /**
     * 属性配置ID
     */
    private Long fieldConfId;

    /**
     * 告警来源
     */
    private String alertOrigin;

    public AlertRecord() {

    }

    /**
     *资源告警构造器
     */
    public AlertRecord(Long alertId, String alertCode, String objectId, Integer level, String recentAlertBrief,
                       AlertHandleStatus handleStatus, Integer number, Integer times, Date firstAlertDate, Date recentAlertDate,
                       String processInstanceId, Date handleDate, Boolean isDeleted, Long ruleId, String indicatorId,
                       String componentId, String fieldId, Long fieldConfId, String alertOrigin) {
        this.alertId = alertId;
        this.alertCode = alertCode;
        this.objectId = objectId;
        this.level = level;
        this.recentAlertBrief = recentAlertBrief;
        this.handleStatus = handleStatus;
        this.number = number;
        this.times = times;
        this.firstAlertDate = firstAlertDate;
        this.recentAlertDate = recentAlertDate;
        this.processInstanceId = processInstanceId;
        this.handleDate = handleDate;
        this.isDeleted = isDeleted;
        this.ruleId = ruleId;
        this.indicatorId = indicatorId;
        this.componentId = componentId;
        this.fieldId = fieldId;
        this.fieldConfId = fieldConfId;
        this.alertOrigin = alertOrigin;
    }

    /**
     *查询计算指标状态所需信息的告警构造器
     */
    public AlertRecord(String alertCode, String objectId, Integer level, AlertHandleStatus handleStatus,
                       String processInstanceId, Boolean isDeleted, Long ruleId, String indicatorId, String componentId) {
        this.alertCode = alertCode;
        this.objectId = objectId;
        this.level = level;
        this.handleStatus = handleStatus;
        this.processInstanceId = processInstanceId;
        this.isDeleted = isDeleted;
        this.ruleId = ruleId;
        this.indicatorId = indicatorId;
        this.componentId = componentId;
    }

    /**
     *终端告警构造器
     */
    public AlertRecord(Long alertId, String alertCode, Integer level, String recentAlertBrief,
                       AlertHandleStatus handleStatus, Integer number, Integer times, Date firstAlertDate, Date recentAlertDate,
                       String processInstanceId, Date handleDate, String terminalChangeRecord, Boolean isDeleted, Long ruleId,
                       String terminalIp, String terminalMac, String ruleType) {
        this.alertId = alertId;
        this.alertCode = alertCode;
        this.level = level;
        this.recentAlertBrief = recentAlertBrief;
        this.handleStatus = handleStatus;
        this.number = number;
        this.times = times;
        this.firstAlertDate = firstAlertDate;
        this.recentAlertDate = recentAlertDate;
        this.processInstanceId = processInstanceId;
        this.handleDate = handleDate;
        this.terminalChangeRecord = terminalChangeRecord;
        this.isDeleted = isDeleted;
        this.ruleId = ruleId;
        this.ip = terminalIp;
        this.terminalMac = terminalMac;
        this.ruleType = ruleType;
    }

    /**
     * 链路告警
     */
    public AlertRecord(Long alertId, String alertCode, String objectId, Integer level, String recentAlertBrief,
                       AlertHandleStatus handleStatus, Integer number, Integer times, Date firstAlertDate, Date recentAlertDate,
                       String processInstanceId, Date handleDate, Boolean isDeleted) {
        this.alertId = alertId;
        this.alertCode = alertCode;
        this.objectId = objectId;
        this.level = level;
        this.recentAlertBrief = recentAlertBrief;
        this.handleStatus = handleStatus;
        this.number = number;
        this.times = times;
        this.firstAlertDate = firstAlertDate;
        this.recentAlertDate = recentAlertDate;
        this.processInstanceId = processInstanceId;
        this.handleDate = handleDate;
        this.isDeleted = isDeleted;
    }

    /**
     *第三方告警构造器
     */
    public AlertRecord(Long alertId, String alertCode, String objectId, Integer level, String recentAlertBrief,
                       AlertHandleStatus handleStatus, Integer number, Integer times, Date firstAlertDate, Date recentAlertDate,
                       String processInstanceId, Date handleDate, Boolean isDeleted, String objectName, String accessSource) {
        this.alertId = alertId;
        this.alertCode = alertCode;
        this.objectId = objectId;
        this.level = level;
        this.recentAlertBrief = recentAlertBrief;
        this.handleStatus = handleStatus;
        this.number = number;
        this.times = times;
        this.firstAlertDate = firstAlertDate;
        this.recentAlertDate = recentAlertDate;
        this.processInstanceId = processInstanceId;
        this.handleDate = handleDate;
        this.isDeleted = isDeleted;
        this.objectName = objectName;
        this.accessSource = accessSource;
    }

    /**
     * syslog告警构造器
     */
    public AlertRecord(Long alertId, String alertCode, String objectId, Integer level, String recentAlertBrief,
                       AlertHandleStatus handleStatus, Integer number, Integer times, Date firstAlertDate, Date recentAlertDate,
                       String processInstanceId, Date handleDate, String collectorId, String ip, String logLevel, String logType,
                       String objectName, String thresholdValue, String logHost, Boolean isDeleted) {
        this.alertId = alertId;
        this.alertCode = alertCode;
        this.objectId = objectId;
        this.level = level;
        this.recentAlertBrief = recentAlertBrief;
        this.handleStatus = handleStatus;
        this.number = number;
        this.times = times;
        this.firstAlertDate = firstAlertDate;
        this.recentAlertDate = recentAlertDate;
        this.processInstanceId = processInstanceId;
        this.handleDate = handleDate;
        this.collectorId = collectorId;
        this.ip = ip;
        this.logLevel = logLevel;
        this.logType = logType;
        this.objectName = objectName;
        this.thresholdValue = thresholdValue;
        this.logHost = logHost;
        this.isDeleted = isDeleted;
    }

    /**
     * snmptrap告警构造器
     */
    public AlertRecord(Long alertId, String alertCode, String objectId, Integer level, String recentAlertBrief,
                       AlertHandleStatus handleStatus, Integer number, Integer times, Date firstAlertDate, Date recentAlertDate,
                       String processInstanceId, Date handleDate, String collectorId, String ip, String trapType, String enterprise,
                       Boolean isDeleted) {
        this.alertId = alertId;
        this.alertCode = alertCode;
        this.objectId = objectId;
        this.level = level;
        this.recentAlertBrief = recentAlertBrief;
        this.handleStatus = handleStatus;
        this.number = number;
        this.times = times;
        this.firstAlertDate = firstAlertDate;
        this.recentAlertDate = recentAlertDate;
        this.processInstanceId = processInstanceId;
        this.handleDate = handleDate;
        this.collectorId = collectorId;
        this.ip = ip;
        this.trapType = trapType;
        this.enterprise = enterprise;
        this.isDeleted = isDeleted;
    }

    /**
     * 业务告警构造器
     */
    public AlertRecord(Long alertId, String alertCode, String objectId, Integer level, String recentAlertBrief,
                       AlertHandleStatus handleStatus, Integer number, Integer times, Date firstAlertDate, Date recentAlertDate,
                       String processInstanceId, Date handleDate, String indicatorName, String rule, Integer lastMins,
                       Boolean isDeleted) {
        this.alertId = alertId;
        this.alertCode = alertCode;
        this.objectId = objectId;
        this.level = level;
        this.recentAlertBrief = recentAlertBrief;
        this.handleStatus = handleStatus;
        this.number = number;
        this.times = times;
        this.firstAlertDate = firstAlertDate;
        this.recentAlertDate = recentAlertDate;
        this.processInstanceId = processInstanceId;
        this.handleDate = handleDate;
        this.indicatorName = indicatorName;
        this.rule = rule;
        this.lastMins = lastMins;
        this.isDeleted = isDeleted;
    }

    /**
     * ip告警构造器
     */
    public AlertRecord(Long alertId, String alertCode, String objectId, Integer level, String recentAlertBrief,
                       AlertHandleStatus handleStatus, Integer number, Integer times, Date firstAlertDate, Date recentAlertDate,
                       String processInstanceId, Date handleDate, Boolean isDeleted, String ip, IpAlertRuleType ipAlertRuleType) {
        this.alertId = alertId;
        this.alertCode = alertCode;
        this.objectId = objectId;
        this.level = level;
        this.recentAlertBrief = recentAlertBrief;
        this.handleStatus = handleStatus;
        this.number = number;
        this.times = times;
        this.firstAlertDate = firstAlertDate;
        this.recentAlertDate = recentAlertDate;
        this.processInstanceId = processInstanceId;
        this.handleDate = handleDate;
        this.isDeleted = isDeleted;
        this.ip = ip;
        this.ipAlertRuleType = ipAlertRuleType;
    }

    public AlertRecord(Integer level, Integer number) {
        this.level = level;
        this.number = number;
    }

    public String getRuleTypeText() {
        return ipAlertRuleType == null ? null : ipAlertRuleType.getText();
    }

    /**
     * 获取告警处理状态名称
     * @return
     */
    public String getHandleStatusName() {
        if (handleStatus != null) {
            return handleStatus.getText();
        }
        return null;
    }

    /**
     * 获取告警处理方式名称
     * @return
     */
    public String getHandleWayName() {
        if (handleWay != null) {
            return handleWay.getText();
        }
        return null;
    }

    /**
     * 获取首次告警时间字符串 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getFirstAlertDateStr() {
        if (firstAlertDate != null) {
            return DateUtils.formatCommonDate(firstAlertDate);
        }
        return null;
    }

    /**
     * 获取最近告警时间字符串 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getTouchOffDateStr() {
        if (touchOffDate != null) {
            return DateUtils.formatCommonDate(touchOffDate);
        }
        return null;
    }

    /**
     * 获取告警触发时间字符串 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getRecentAlertDateStr() {
        if (StringUtils.isBlank(recentAlertDateStr) && null != recentAlertDate) {
            recentAlertDateStr = DateUtils.formatCommonDate(recentAlertDate);
        }
        return recentAlertDateStr;
    }

    /**
     * 获取告警处理时间字符串 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getHandleDateStr() {
        if (StringUtils.isBlank(handleDateStr) && handleDate != null) {
            handleDateStr = DateUtils.formatCommonDate(handleDate);
        }
        return handleDateStr;
    }

    /**
     * 【实时告警】左侧时间显示
     * @return
     */
    public String getRealAlertDateStr() {
        if (StringUtils.isBlank(realAlertDateStr) && recentAlertDate != null) {
            Calendar sysDate = Calendar.getInstance();
            Calendar realDate = Calendar.getInstance();
            realDate.setTime(recentAlertDate);
            String formatStr = "HH:mm";
            if (sysDate.get(Calendar.YEAR) != realDate.get(Calendar.YEAR)) {
                formatStr = "yyyy-MM-dd " + formatStr;
            } else if (sysDate.get(Calendar.MONTH) != realDate.get(Calendar.MONTH)) {
                formatStr = "MM-dd " + formatStr;
            } else if (sysDate.get(Calendar.DATE) != realDate.get(Calendar.DATE)) {
                formatStr = "MM-dd " + formatStr;
            }
            realAlertDateStr = new SimpleDateFormat(formatStr).format(recentAlertDate);
        }
        return realAlertDateStr;
    }

    /**
     * 是否包含关键字
     * @param keyword 关键字
     * @return
     */
    public boolean containsKeyword(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return true;
        }
        String flds = "";
        switch (alertType) {
        case Alert:
            flds = recentAlertBrief + ip + objectName;
            break;
        case NELinkAlert:
            flds = recentAlertBrief + objectName;
            break;
        case SysLogAlert:
            flds = recentAlertBrief + logHost + ip;
            break;
        case SnmpTrapAlert:
            flds = recentAlertBrief + ip + trapType;
            break;
        case ThirdPartyAlert:
            flds = recentAlertBrief + objectName;
            break;
        case SystemAlert:
            flds = recentAlertBrief + objectId;
            break;
        case TerminalAlert:
            flds = recentAlertBrief + ip + terminalMac;
            break;
        case BusinessAlert:
            flds = recentAlertBrief + objectName;
            break;
        case IpAlert:
            flds = recentAlertBrief + ip;
            break;
        default:
            break;
        }
        return flds.toLowerCase().contains(keyword.toLowerCase());
    }

    public CurrencyAlert parseCurrencyAlert() {
        return new CurrencyAlert(alertId, alertCode, level, handleStatus, handleUserId, handleDate, handleWay,
            description, number, times, firstAlertDate, recentAlertDate, recentAlertBrief, processInstanceId,
            isDeleted);
    }

}
