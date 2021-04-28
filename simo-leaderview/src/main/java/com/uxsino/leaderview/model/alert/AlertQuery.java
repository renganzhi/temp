package com.uxsino.leaderview.model.alert;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.db.criteria.Criteria;
import com.uxsino.commons.enums.AlertOrigin;
import com.uxsino.commons.model.AlertType;
import com.uxsino.commons.model.DataSourceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @description 告警查询的参数对象
 * 
 * @date 2017年4月13日
 */
@Data
@ApiModel(description = "告警查询的参数对象")
public class AlertQuery {

    /**
     * 告警信息编号
     */
    @ApiModelProperty(value = "告警信息编号")
    private Long alertId;

    /**
     * 告警日期记录信息编号
     */
    @ApiModelProperty(value = "告警日期记录信息编号")
    private Long alertDateId;

    /**
     * 告警编码，由相关告警规则生成的加密串
     */
    @ApiModelProperty(value = "告警编码")
    private String alertCode;

    /**
     * 告警编码列表
     */
    @ApiModelProperty(value = "告警编码列表")
    private List<String> alertCodeIn;

    /**
     * 过滤的告警编码列表
     */
    @ApiModelProperty(value = "过滤的告警编码列表")
    private List<String> alertCodeNotIn;

    /**
     * 网元ID
     */
    @ApiModelProperty(value = "网元ID")
    private String objectId;

    /**
     * 多个网元ID用,分隔
     */
    @ApiModelProperty(value = "多个网元ID用,分隔")
    private String objectIds;

    /**
     * IP地址（模糊匹配）
     */
    @ApiModelProperty(value = "IP地址（模糊匹配）")
    private String ip;

    /**
     * IP地址（全匹配）
     */
    @ApiModelProperty(value = "IP地址（全匹配）")
    private String ipEq;

    /**
     * 网元名称
     */
    @ApiModelProperty(value = "网元名称")
    private String objectName;

    /**
     * 网元类型
     */
    @ApiModelProperty(value = "网元类型")
    private String objectType;

    /**
     * 日志类型
     */
    @ApiModelProperty(value = "日志类型")
    private String logType;

    /**日志产生主机*/
    @ApiModelProperty(value = "日志产生主机")
    private String logHost;

    /**日志级别*/
    @ApiModelProperty(value = "日志级别")
    private String logLevel;

    /**采集器*/
    @ApiModelProperty(value = "采集器")
    private String collectorId;

    /**
     * 网元子类型
     */
    @ApiModelProperty(value = "网元子类型")
    private String subType;

    /**
     * 告警等级值（取值范围1~10，值越大表示告警越严重）
     */
    @ApiModelProperty(value = "告警等级值")
    private Integer level;

    /**
     * 多个告警等级值（取值范围1~10，值越大表示告警越严重）
     */
    @ApiModelProperty(value = "多个告警等级值")
    private List<Integer> levels;

    /**
     * 最近告警信息描述
     */
    @ApiModelProperty(value = "最近告警信息描述")
    private String recentAlertBrief;

    /**
     * 最近告警信息描述
     */
    @ApiModelProperty(value = "最近告警信息描述")
    private String recentAlertDate;

    /**
     * 告警处理状态
     */
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "告警处理状态")
    private AlertHandleStatus handleStatus;

    /**
     * 需要的告警状态
     */
    @ApiModelProperty(value = "需要的告警状态")
    private AlertHandleStatus[] handleStatusIn;

    /**
     * 不需要的告警状态
     */
    @ApiModelProperty(value = "不需要的告警状态")
    private AlertHandleStatus[] handleStatusNotIn;

    /**
     * 处理告警的用户编号
     */
    @ApiModelProperty(value = "处理告警的用户编号")
    private Long handleUserId;

    /**
     * 起始告警时间
     */
    @ApiModelProperty(value = "起始告警时间")
    private String startAlertDate;

    /**
     * 截止告警时间
     */
    @ApiModelProperty(value = "截止告警时间")
    private String endAlertDate;

    /**
     * 告警工单ID
     */
    @ApiModelProperty(value = "告警工单ID")
    private String processInstanceId;

    /**
     * 域ID
     */
    @ApiModelProperty(value = "域ID")
    private Long[] domainIds;

    /**
     * 多个接收告警通知的用户ID用,分隔（*3.0版本修改：不查询通知到登录用户的告警。用户只能查看域权限下的对象告警。）
     */
    @ApiModelProperty(value = "多个接收告警通知的用户ID用,分隔")
    private String notifyUserIds;

    /**
     * 排序字段名
     */
    @ApiModelProperty(value = "排序字段名")
    private String orderField;

    /**
     * 排序条件：升序（asc）、降序（desc）
     */
    @ApiModelProperty(value = "排序条件")
    private String orderCondition;

    /**
     * 告警分类
     */
    @ApiModelProperty(value = "告警分类")
    private AlertType alertType;

    /**
     * 服务器IP（目前用于业务告警）
     */
    @ApiModelProperty(value = "服务器IP")
    private String serverIp;

    /**
     * 资源分组ID
     */
    @ApiModelProperty(value = "资源分组ID")
    private Long neGroupId;

    /**
     * 是否被删除
     */
    @ApiModelProperty(value = "是否被删除")
    private Boolean isDeleted = false;

    /**
     * 陷阱类型
     */
    @ApiModelProperty(value = "陷阱类型")
    private String trapType;

    /**
     * 企业名
     */
    @ApiModelProperty(value = "企业名")
    private String enterprise;

    /**
     * 是否由资源统一管理
     */
    @ApiModelProperty(value = "是否由资源统一管理")
    private Boolean sourceManage;

    /**
     * 多个指标用,分隔
     */
    @ApiModelProperty(value = "多个指标用,分隔")
    private String indicatorIds;

    /**
     * 过滤的多个指标用,分隔
     */
    @ApiModelProperty(value = "过滤的多个指标用,分隔")
    private List<String> indicatorIdNotIn;

    /**
     * 告警来源
     */
    @ApiModelProperty(value = "告警来源")
    private AlertOrigin alertOrigin;

    /**
     * 部件标识
     */
    @ApiModelProperty(value = "部件标识")
    private String componentId;

    /**
     * 多个部件标识
     */
    @ApiModelProperty(value = "多个部件标识")
    private String componentIds;

    /**
     * 终端MAC地址
     */
    @ApiModelProperty(value = "终端MAC地址")
    private String terminalMac;

    /**
     * 终端页面操作变更
     */
    @ApiModelProperty(value = "终端页面操作变更")
    private String ruleType;

    /**
     * 查询结果是否被用于计算指标状态
     */
    @ApiModelProperty(value = "查询结果是否被用于计算指标状态")
    private Boolean toCalculateIndStatus = false;

    /**
     * 是否为可用性告警
     */
    @ApiModelProperty(value = "是否为可用性告警")
    private Boolean isAvailability;

    /**
     * 数据来源类型
     */
    @ApiModelProperty(value = "数据来源类型")
    private DataSourceType dataSourceType = DataSourceType.all;

    /**
     * 关键字
     */
    @ApiModelProperty(value = "关键字")
    private String keyword;

    /**
     * 局域网ID
     */
    @ApiModelProperty(value = "局域网ID")
    private String lanId;

    /**
     * 局域网名称
     */
    @ApiModelProperty(value = "局域网名称")
    private String lanName;

    /**
     * 子网ID
     */
    @ApiModelProperty(value = "子网ID")
    private String subnetId;

    /**
     * 子网名称
     */
    @ApiModelProperty(value = "子网名称")
    private String subnetName;

    /**
     * 查询条数
     */
    @ApiModelProperty(value = "查询条数")
    private int limit = 0;

    /**
     * 参数设置
     */
    @ApiModelProperty(value = "参数设置")
    private Map<String, Object> params = new HashMap<String, Object>();

    /**
     * 告警对象信息
     */
    @ApiModelProperty(value = "告警对象信息")
    private Map<String, JSONObject> objectInfo = new HashMap<>();

    /**
     * 按条件查询告警信息
     * @return jpql语句
     */
    public String getAlertListJPQL(Map<String, String> orderBy) {
        StringBuilder sb = null;
        if (AlertType.SysLogAlert.equals(alertType)) {
            sb = new StringBuilder(
                "select new com.uxsino.alert.model.AlertRecord (a.id as alertId, a.alertCode as alertCode, a.objectId as objectId,"
                        + " a.level as level, a.recentAlertBrief as recentAlertBrief, a.handleStatus as handleStatus,"
                        + " a.number as number, a.times as times, a.firstAlertDate as firstAlertDate, a.recentAlertDate as recentAlertDate,"
                        + " a.processInstanceId as processInstanceId, a.handleDate as handleDate,"
                        + " a.collectorId as collectorId, a.ip as ip, a.logLevel as logLevel, a.logType as logType, a.objectName as objectName, "
                        + " a.thresholdValue as thresholdValue, a.logHost as logHost, a.isDeleted as isDeleted) from "
                        + alertType + " a where 1=1 ");
        } else if (AlertType.SnmpTrapAlert.equals(alertType)) {
            sb = new StringBuilder(
                "select new com.uxsino.alert.model.AlertRecord (a.id as alertId, a.alertCode as alertCode, a.objectId as objectId,"
                        + " a.level as level, a.recentAlertBrief as recentAlertBrief, a.handleStatus as handleStatus,"
                        + " a.number as number, a.times as times, a.firstAlertDate as firstAlertDate, a.recentAlertDate as recentAlertDate,"
                        + " a.processInstanceId as processInstanceId, a.handleDate as handleDate,"
                        + " a.collectorId as collectorId, a.ip as ip, a.trapType as trapType, a.enterprise as enterprise,"
                        + " a.isDeleted as isDeleted) from " + alertType + " a where 1=1 ");
        } else if (AlertType.BusinessAlert.equals(alertType)) {
            sb = new StringBuilder(
                "select new com.uxsino.alert.model.AlertRecord (a.id as alertId, a.alertCode as alertCode, a.objectId as objectId,"
                        + " a.level as level, a.recentAlertBrief as recentAlertBrief, a.handleStatus as handleStatus,"
                        + " a.number as number, a.times as times, a.firstAlertDate as firstAlertDate, a.recentAlertDate as recentAlertDate,"
                        + " a.processInstanceId as processInstanceId, a.handleDate as handleDate,"
                        + " a.indicatorName as indicatorName, a.rule as rule,"
                        + " round(extract(epoch from (coalesce(a.handleDate, now()) - a.firstAlertDate))/60) as lastMins,"
                        + " a.isDeleted as isDeleted) from " + alertType + " a where 1=1 ");
        } else if (AlertType.SystemAlert.equals(alertType)) {
            sb = new StringBuilder(
                "select new com.uxsino.alert.model.AlertRecord (a.id as alertId, a.alertCode as alertCode, a.objectId as objectId,"
                        + " a.level as level, a.recentAlertBrief as recentAlertBrief, a.handleStatus as handleStatus,"
                        + " a.number as number, a.times as times, a.firstAlertDate as firstAlertDate, a.recentAlertDate as recentAlertDate,"
                        + " a.processInstanceId as processInstanceId, a.handleDate as handleDate,"
                        + " a.isDeleted as isDeleted) from " + alertType + " a where 1=1 ");
        } else if (AlertType.Alert.equals(alertType)) {
            if (toCalculateIndStatus) {
                sb = new StringBuilder(
                    "select new com.uxsino.alert.model.AlertRecord (a.alertCode as alertCode, a.objectId as objectId,"
                            + " a.level as level,a.handleStatus as handleStatus, a.processInstanceId as processInstanceId,"
                            + "a.isDeleted as isDeleted, a.ruleId as ruleId, a.indicatorId as indicatorId,"
                            + "  a.componentId as componentId) from " + alertType + " a where 1=1 ");
            } else {
                sb = new StringBuilder(
                    "select new com.uxsino.alert.model.AlertRecord (a.id as alertId, a.alertCode as alertCode, a.objectId as objectId,"
                            + " a.level as level, a.recentAlertBrief as recentAlertBrief, a.handleStatus as handleStatus,"
                            + " a.number as number, a.times as times, a.firstAlertDate as firstAlertDate, a.recentAlertDate as recentAlertDate,"
                            + " a.processInstanceId as processInstanceId, a.handleDate as handleDate, a.isDeleted as isDeleted,"
                            + " a.ruleId as ruleId, a.indicatorId as indicatorId, a.componentId as componentId, a.fieldId as fieldId, a.fieldConfId as fieldConfId) from "
                            + alertType + " a where 1=1 ");
            }
        } else if (AlertType.TerminalAlert.equals(alertType)) {
            sb = new StringBuilder(
                "select new com.uxsino.alert.model.AlertRecord (a.id as alertId, a.alertCode as alertCode,"
                        + " a.level as level, a.recentAlertBrief as recentAlertBrief, a.handleStatus as handleStatus,"
                        + " a.number as number, a.times as times, a.firstAlertDate as firstAlertDate, a.recentAlertDate as recentAlertDate,"
                        + " a.processInstanceId as processInstanceId, a.handleDate as handleDate,a.changeRecord as terminalChangeRecord,"
                        + " a.isDeleted as isDeleted, a.ruleId as ruleId, a.terminalIp as ip, a.terminalMac as terminalMac, a.ruleType as ruleType) from "
                        + alertType + " a where 1=1 ");
        } else if (AlertType.ThirdPartyAlert.equals(alertType)) {
            sb = new StringBuilder(
                "select new com.uxsino.alert.model.AlertRecord (a.id as alertId, a.alertCode as alertCode, a.objectId as objectId,"
                        + " a.level as level, a.recentAlertBrief as recentAlertBrief, a.handleStatus as handleStatus,"
                        + " a.number as number, a.times as times, a.firstAlertDate as firstAlertDate, a.recentAlertDate as recentAlertDate,"
                        + " a.processInstanceId as processInstanceId, a.handleDate as handleDate, a.isDeleted as isDeleted,"
                        + " a.objectName as objectName, a.accessSource as accessSource) from " + alertType
                        + " a where 1=1 ");
        } else if (AlertType.IpAlert.equals(alertType)) {
            sb = new StringBuilder(
                "select new com.uxsino.alert.model.AlertRecord (a.id as alertId, a.alertCode as alertCode, a.objectId as objectId,"
                        + " a.level as level, a.recentAlertBrief as recentAlertBrief, a.handleStatus as handleStatus,"
                        + " a.number as number, a.times as times, a.firstAlertDate as firstAlertDate, a.recentAlertDate as recentAlertDate,"
                        + " a.processInstanceId as processInstanceId, a.handleDate as handleDate,"
                        + " a.isDeleted as isDeleted, a.ip as ip, a.ruleType as ipAlertRuleType) from " + alertType
                        + " a where 1=1 ");
        } else {
            sb = new StringBuilder(
                "select new com.uxsino.alert.model.AlertRecord (a.id as alertId, a.alertCode as alertCode, a.objectId as objectId,"
                        + " a.level as level, a.recentAlertBrief as recentAlertBrief, a.handleStatus as handleStatus,"
                        + " a.number as number, a.times as times, a.firstAlertDate as firstAlertDate, a.recentAlertDate as recentAlertDate,"
                        + " a.processInstanceId as processInstanceId, a.handleDate as handleDate,"
                        + " a.isDeleted as isDeleted) from " + alertType + " a where 1=1 ");
        }
        addAlertDateFilter(sb);
        addAlertFilter(sb);
        if (null != orderBy && !orderBy.isEmpty()) {
            sb.append(" order by ");
            for (Entry<String, String> entry : orderBy.entrySet()) {
                sb.append(entry.getKey() + " " + entry.getValue() + ",");
            }
            // 去掉最后一个,号
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 按条件查询告警信息数
     * @return jpql语句
     */
    public String getAlertCountJPQL() {
        StringBuilder sb = new StringBuilder("select COUNT(a.id) from " + alertType + " a where 1=1 ");
        addAlertDateFilter(sb);
        addAlertFilter(sb);
        return sb.toString();
    }

    /**
     * 查询未处理的告警等级数
     * @return jpql语句
     */
    /*public String findUnHandleAlertLevelCount() {
        String jpql = new String(
            "select new com.uxsino.alert.model.AlertRecord(a.level as level, COUNT(a.id) as number) from " + alertType
                    + " a where a.handlerStatus = '" + AlertHandleStatus.UNTREATED
                    + "' and a.isDeleted = false group by a.level");
        return jpql;
    }*/

    /**
     * 查询所有告警的告警等级数
     * @return jpql语句
     */
    public String findAlertLevelCount() {
        String jpql = new String(
            "select new com.uxsino.alert.model.AlertRecord(a.level as level, COUNT(a.id) as number) from " + alertType
                    + " a where 1=1 and a.isDeleted = false");
        if (handleStatus != null) {
            jpql += (" and a.handlerStatus = '" + handleStatus + "'");
        }
        jpql += " group by a.level";
        return jpql;
    }

    /**
     * 拼接AlertDate表查询条件
     * @param sb 原jpql语句
     */
    private void addAlertDateFilter(StringBuilder sb) {
        if (StringUtils.isBlank(startAlertDate) && StringUtils.isBlank(endAlertDate)) {
            return;
        }
        sb.append(" and a.id in (select ad.alertId from AlertDate ad where 1=1 ");
        if (StringUtils.isNotBlank(startAlertDate)) {
            try {
                sb.append(" and ad.alertDate >= :startAlertDate");
                params.put("startAlertDate",
                    DateUtils.parseDate(startAlertDate, com.uxsino.commons.baseclass.Constants.datePatterns));
            } catch (Exception e) {
            }
        }
        if (StringUtils.isNotBlank(endAlertDate)) {
            try {
                sb.append(" and ad.alertDate <= :endAlertDate");
                params.put("endAlertDate",
                    DateUtils.parseDate(endAlertDate, com.uxsino.commons.baseclass.Constants.datePatterns));
            } catch (Exception e) {
            }
        }
        if (null != alertType) {
            sb.append(" and ad.alertType = :alertType");
            params.put("alertType", alertType);
        }
        if (null != alertDateId) {
            sb.append(" and ad.id = :alertDateId");
            params.put("alertDateId", alertDateId);
        }
        sb.append(")");
    }

    /**
     * 拼接Alert表查询条件
     * @param sb 原jpql语句
     * @return 新jpql语句
     */
    private void addAlertFilter(StringBuilder sb) {
        if (null != alertId) {
            sb.append(" and a.id = :alertId");
            params.put("alertId", alertId);
        }
        if (StringUtils.isNotBlank(recentAlertDate)) {
            try {
                sb.append(" and a.recentAlertDate >= :recentAlertDate");
                params.put("recentAlertDate",
                    DateUtils.parseDate(recentAlertDate, com.uxsino.commons.baseclass.Constants.datePatterns));
            } catch (Exception e) {
            }
        }
        if (StringUtils.isNotBlank(recentAlertBrief)) {
            sb.append(" and lower(a.recentAlertBrief) like :recentAlertBrief");
            params.put("recentAlertBrief", Criteria.escapeLike(recentAlertBrief.toLowerCase()));
        }
        if (null != objectId) {
            sb.append(" and a.objectId = :objectId");
            params.put("objectId", objectId);
        }
        if (null != handleStatus) {
            sb.append(" and a.handleStatus = :handleStatus");
            params.put("handleStatus", handleStatus);
        }
        if (null != handleStatusIn && handleStatusIn.length > 0) {
            sb.append(" and a.handleStatus in (:handleStatusIn)");
            params.put("handleStatusIn", Arrays.asList(handleStatusIn));
        }
        if (null != handleStatusNotIn && handleStatusNotIn.length > 0) {
            sb.append(" and a.handleStatus not in (:handleStatusNotIn)");
            params.put("handleStatusNotIn", Arrays.asList(handleStatusNotIn));
        }
        if (null != level) {
            sb.append(" and a.level = :level");
            params.put("level", level);
        }
        if (StringUtils.isNotBlank(alertCode)) {
            sb.append(" and a.alertCode = :alertCode");
            params.put("alertCode", alertCode);
        }
        if (alertCodeIn != null && !alertCodeIn.isEmpty()) {
            sb.append(" and a.alertCode in (:alertCodeIn)");
            params.put("alertCodeIn", alertCodeIn);
        }
        if (alertCodeNotIn != null && !alertCodeNotIn.isEmpty()) {
            sb.append(" and a.alertCode not in (:alertCodeNotIn)");
            params.put("alertCodeNotIn", alertCodeNotIn);
        }
        if (StringUtils.isNotBlank(objectIds)) {
            sb.append(" and a.objectId in (:objectIdIn_1)");
            params.put("objectIdIn_1", Arrays.asList(objectIds.split(",")));
        }
        if (AlertType.BusinessAlert == alertType) {
            if (StringUtils.isNotBlank(serverIp)) {
                sb.append(" and lower(a.serverIp) like :serverIp");
                params.put("serverIp", Criteria.escapeLike(serverIp.toLowerCase()));
            }
        }
        if (AlertType.SysLogAlert == alertType) {
            if (StringUtils.isNotBlank(objectName)) {
                sb.append(" and lower(a.objectName) like :objectName");
                params.put("objectName", Criteria.escapeLike(objectName.toLowerCase()));
            }
            if (StringUtils.isNotBlank(ip)) {
                sb.append(" and lower(a.ip) like :ip1");
                params.put("ip1", Criteria.escapeLike(ip.toLowerCase()));
            }
            if (StringUtils.isNotBlank(logHost)) {
                sb.append(" and lower(a.logHost) like :logHost");
                params.put("logHost", Criteria.escapeLike(logHost.toLowerCase()));
            }
            if (StringUtils.isNotBlank(logType)) {
                sb.append(" and a.logType = :logType");
                params.put("logType", logType);
            }
            if (StringUtils.isNotBlank(logLevel)) {
                sb.append(" and a.logLevel = :logLevel");
                params.put("logLevel", logLevel);
            }
        }
        if (AlertType.SnmpTrapAlert == alertType) {
            if (StringUtils.isNotBlank(ip)) {
                sb.append(" and lower(a.ip) like :ip2");
                params.put("ip2", Criteria.escapeLike(ip.toLowerCase()));
            }
            if (StringUtils.isNotBlank(enterprise)) {
                sb.append(" and lower(a.enterprise) like :enterprise");
                params.put("enterprise", Criteria.escapeLike(enterprise.toLowerCase()));
            }
            if (StringUtils.isNotBlank(trapType)) {
                sb.append(" and lower(a.trapType) like :trapType");
                params.put("trapType", Criteria.escapeLike(trapType.toLowerCase()));
            }
        }
        if (AlertType.ThirdPartyAlert == alertType) {
            if (StringUtils.isNotBlank(objectName)) {
                sb.append(" and lower(a.objectName) like :thirdObjName");
                params.put("thirdObjName", Criteria.escapeLike(objectName.toLowerCase()));
            }
        }
        if (AlertType.SystemAlert == alertType) {
            if (StringUtils.isNotBlank(objectName)) {
                sb.append(" and lower(a.objectId) like :sysObjName");
                params.put("sysObjName", Criteria.escapeLike(objectName.toLowerCase()));
            }
        }
        if (AlertType.TerminalAlert == alertType) {
            if (StringUtils.isNotBlank(terminalMac)) {
                sb.append(" and lower(a.terminalMac) like :terminalMac");
                params.put("terminalMac", Criteria.escapeLike(terminalMac.toLowerCase()));
            }
            if (StringUtils.isNotBlank(ruleType)) {
                sb.append(" and a.ruleType = :ruleType");
                params.put("ruleType", ruleType);
            }
            if (StringUtils.isNotBlank(ip)) {
                sb.append(" and lower(a.terminalIp) like :terminalIp");
                params.put("terminalIp", Criteria.escapeLike(ip.toLowerCase()));
            }
        }
        if (AlertType.IpAlert == alertType) {
            if (StringUtils.isNotBlank(ip)) {
                sb.append(" and lower(a.ip) like :ip3");
                params.put("ip3", Criteria.escapeLike(ip.toLowerCase()));
            }
            if (StringUtils.isNotBlank(ipEq)) {
                sb.append(" and a.ip = :ipEq");
                params.put("ipEq", ipEq);
            }
        }
        if (null != isDeleted) {
            sb.append(" and a.isDeleted = :isDeleted");
            params.put("isDeleted", isDeleted);
        }
        if (StringUtils.isNotBlank(indicatorIds)) {
            sb.append(" and a.indicatorId in (:indicatorIds)");
            params.put("indicatorIds", Arrays.asList(indicatorIds.split(",")));
        }
        if (indicatorIdNotIn != null && !indicatorIdNotIn.isEmpty()) {
            sb.append(" and a.indicatorId not in (:indicatorIdNotIn)");
            params.put("indicatorIdNotIn", indicatorIdNotIn);
        }
        if (alertOrigin != null) {
            sb.append(" and a.alertOrigin = :alertOrigin");
            params.put("alertOrigin", alertOrigin);
        }
        if (StringUtils.isNotBlank(componentId)) {
            sb.append(" and CONCAT(',', a.componentId, ',') like :componentId");
            // sb.append(" and a.componentId = :componentId");
            params.put("componentId", "," + componentId + ",");
        }
        if (StringUtils.isNotBlank(componentIds)) {
            // sb.append(" and a.componentId in (:componentIds)");
            List<String> componentIdList = Arrays.asList(componentIds.split(","));
            sb.append(" and (");
            for (int index = 0; index < componentIdList.size(); index++) {
                String componentIdStr = componentIdList.get(index);
                sb.append(" CONCAT(',', a.componentId, ',') like :componentId");
                sb.append(index);
                sb.append(" or");
                params.put("componentId" + index, "," + componentIdStr + ",");
            }
            sb.setLength(sb.length() - 2);
            sb.append(")");
            // params.put("componentIds", Arrays.asList(componentIds.split(",")));
        }
    }

}
