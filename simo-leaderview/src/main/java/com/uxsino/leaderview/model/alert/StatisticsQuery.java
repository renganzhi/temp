package com.uxsino.leaderview.model.alert;

import com.uxsino.commons.db.criteria.Criteria;
import com.uxsino.leaderview.model.AlertType;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @description 告警统计的参数对象
 *
 * @date 2017年4月13日
 */
@Data
public class StatisticsQuery {
    /**
     * 资源ID
     */
    private String objId;

    /**
     * 多个资源ID用,分隔
     */
    private String objIds;

    /**
     * 告警级别
     */
    private String alertLevel;

    /**
     * 多个告警等级，用逗号隔开
     */
    private List<Integer> alertLevels;

    /**
     * （起始）告警时间
     */
    private Date startAlertDate;

    /**
     * （截止）告警时间
     */
    private Date endAlertDate;

    /**
     * 多个接收告警通知的用户ID用,分隔（*3.0版本修改：不查询通知到登录用户的告警。用户只能查看域权限下的对象告警。）
     */
    private String notifyUserIds;

    /**
     * 告警分类
     */
    private AlertType alertType;

    /**
     * 服务器IP（目前用于业务告警）
     */
    private String serverIp;

    /**
     * 日志类型
     */
    private String logType;

    /**日志产生主机*/
    private String logHost;

    /**日志级别*/
    private String logLevel;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 陷阱类型
     */
    private String trapType;

    /**
     * 企业名
     */
    private String enterprise;

    /**
     * 是否被删除
     */
    private Boolean isDeleted = false;

    /**
     * 告警处理状态
     */
    @Enumerated(EnumType.STRING)
    private AlertHandleStatus handleStatus;

    /**
     * IP告警类型
     */
    private IpAlertRuleType ipAlertRuleType;

    /**
     * 分组统计字段
     */
    private String groupField;

    /**
     * 参数设置
     */
    private Map<String, Object> params = new HashMap<String, Object>();

    public StatisticsQuery() {

    }

    public StatisticsQuery(String objId, String objIds, String alertLevel, Date startAlertDate, Date endAlertDate,
                           String notifyUserIds, AlertType alertType) {
        this.objId = objId;
        this.objIds = objIds;
        this.alertLevel = alertLevel;
        this.startAlertDate = startAlertDate;
        this.endAlertDate = endAlertDate;
        this.notifyUserIds = notifyUserIds;
        this.alertType = alertType;
    }

    public StatisticsQuery(String objId, String objIds, String alertLevel, String notifyUserIds, AlertType alertType) {
        this.objId = objId;
        this.objIds = objIds;
        this.alertLevel = alertLevel;
        this.notifyUserIds = notifyUserIds;
        this.alertType = alertType;
    }

    public StatisticsQuery(String objIds, List<Integer> alertLevels, AlertType alertType, AlertHandleStatus handleStatus) {
        this.objIds = objIds;
        this.alertLevels = alertLevels;
        this.alertType = alertType;
        this.handleStatus = handleStatus;
    }

    /**
     * 统计告警信息
     * @return
     */
    public String getStatisticsResultSQL() {
        StringBuilder sql = new StringBuilder(
            "select new com.uxsino.alert.model.StatisticsResult (count(a.id) as alertCount,"
                    + " round(sum(extract(epoch from (coalesce(a.handleDate, now()) - a.firstAlertDate))/60)) as alertTotalTime,"
                    + " round(max(extract(epoch from (coalesce(a.handleDate, now()) - a.firstAlertDate))/60)) as alertMaxTime,"
                    + " round(avg(extract(epoch from (coalesce(a.handleDate, now()) - a.firstAlertDate))/60)) as alertAvgTime)"
                    + " from " + alertType + " a where 1=1 ");
        addAlertDateFilter(sql);
        addAlertFilter(sql);
        return sql.toString();
    }

    /**
     * 按告警对象统计告警信息
     * @return
     */
    public String getObjStatisticsResultSQL() {
        StringBuilder sql = new StringBuilder(
            "select new com.uxsino.alert.model.StatisticsResult (a.objectId as scopeValue, count(a.id) as alertCount,"
                    + " round(sum(extract(epoch from (coalesce(a.handleDate, now()) - a.firstAlertDate))/60)) as alertTotalTime,"
                    + " round(max(extract(epoch from (coalesce(a.handleDate, now()) - a.firstAlertDate))/60)) as alertMaxTime,"
                    + " round(avg(extract(epoch from (coalesce(a.handleDate, now()) - a.firstAlertDate))/60)) as alertAvgTime)"
                    + " from " + alertType + " a where 1=1 ");
        addAlertDateFilter(sql);
        addAlertFilter(sql);
        sql.append(" group by a.objectId");
        sql.append(" order by alertCount desc");
        return sql.toString();
    }

    public String getObjAndLevelStatSQL() {
        StringBuilder sql = new StringBuilder(
                "select new com.uxsino.alert.model.StatisticsResult (a.objectId as scopeValue, a.level as alertLevel, count(a.id) as alertCount)"
                        + " from " + alertType + " a where 1=1 and a.handleStatus = :handleStatus ");
        params.put("handleStatus", AlertHandleStatus.UNTREATED);
        addAlertFilter(sql);
        sql.append(" group by a.objectId, a.level");
        return sql.toString();
    }

    /**
     * 查询业务告警类型、资源ID映射
     * @return
     */
    public String getBAltTypeMapObjIdSQL() {
        StringBuilder sql = new StringBuilder("select distinct new map(a.businessAlertType, a.objectId) from "
                + alertType + " a where 1=1 ");
        addAlertDateFilter(sql);
        addAlertFilter(sql);
        return sql.toString();
    }

    /**
     * 查询IP告警类型、子网ID映射
     * @return
     */
    public String geIpAltTypeMapObjIdSQL() {
        StringBuilder sql = new StringBuilder("select distinct new map(a.ruleType, a.objectId) from "
                + alertType + " a where 1=1 ");
        addAlertDateFilter(sql);
        addAlertFilter(sql);
        return sql.toString();
    }

    /**
     * 查询终端告警等级、MAC地址映射
     * @return
     */
    public String getTerminalAlertLevelMacMapSQL() {
        StringBuilder sql = new StringBuilder("select distinct new map(a.level, a.terminalMac) from " + alertType
                + " a where 1=1 ");
        addAlertDateFilter(sql);
        addAlertFilter(sql);
        return sql.toString();
    }

    /**
     * 查询告警级别、资源ID映射
     * @return
     */
    public String getAlertLevelMapObjIdSQL() {
        StringBuilder sql = new StringBuilder("select distinct new map(a.level, a.objectId) from " + alertType
                + " a where 1=1 ");
        addAlertDateFilter(sql);
        addAlertFilter(sql);
        return sql.toString();
    }

    /**
     * 拼接Alert表过滤条件
     * @param sb sql拼接语句
     * @return
     */
    private void addAlertFilter(StringBuilder sb) {
        if (null != objId) {
            sb.append(" and a.objectId = :objId");
            params.put("objId", objId);
        }
        if (StringUtils.isNotBlank(objIds)) {
            sb.append(" and a.objectId in (:objIds)");
            params.put("objIds", Arrays.asList(objIds.split(",")));
        }
        if (AlertType.BusinessAlert == alertType) {
            if (StringUtils.isNotBlank(serverIp)) {
                sb.append(" and lower(a.serverIp) like :serverIp");
                params.put("serverIp", Criteria.escapeLike(serverIp.toLowerCase()));
            }
        }
        if (AlertType.SysLogAlert == alertType) {
            if (StringUtils.isNotBlank(logHost)) {
                sb.append(" and lower(a.logHost) like :logHost");
                params.put("logHost", Criteria.escapeLike(logHost.toLowerCase()));
            }
        }
        if (AlertType.SnmpTrapAlert == alertType) {
            if (StringUtils.isNotBlank(ip)) {
                sb.append(" and lower(a.ip) like :ip");
                params.put("ip", Criteria.escapeLike(ip.toLowerCase()));
            }
        }
        if (AlertType.IpAlert.equals(alertType)) {
            if (ipAlertRuleType != null) {
                sb.append(" and a.ruleType = :ipAlertRuleType");
                params.put("ipAlertRuleType", ipAlertRuleType);
            }
        }
        if (StringUtils.isNotBlank(alertLevel)) {
            sb.append(" and a.level = :alertLevel");
            params.put("alertLevel", Integer.valueOf(alertLevel));
        }
        if (null != isDeleted) {
            sb.append(" and a.isDeleted = :isDeleted");
            params.put("isDeleted", isDeleted);
        }
        if (CollectionUtils.isNotEmpty(alertLevels)) {
            sb.append(" and a.level in :alertLevels");
            params.put("alertLevels", alertLevels);
        }
        if (Objects.nonNull(handleStatus)) {
            sb.append(" and a.handleStatus = :handleStatus");
            params.put("handleStatus", handleStatus);
        }
    }

    /**
     * 拼接AlertDate表过滤条件
     * @param sb sql拼接语句
     */
    private void addAlertDateFilter(StringBuilder sb) {
        if (startAlertDate == null && endAlertDate == null && StringUtils.isBlank(logType)
                && StringUtils.isBlank(logLevel) && StringUtils.isBlank(enterprise) && StringUtils.isBlank(trapType)) {
            return;
        }
        sb.append(" and a.id in (select ad.alertId from AlertDate ad where 1=1 ");
        if (null != startAlertDate) {
            sb.append(" and ad.alertDate >= :startAlertDate");
            params.put("startAlertDate", startAlertDate);
        }
        if (null != endAlertDate) {
            sb.append(" and ad.alertDate <= :endAlertDate");
            params.put("endAlertDate", endAlertDate);
        }
        if (null != alertType) {
            sb.append(" and ad.alertType = :alertType");
            params.put("alertType", alertType);
        }
        if (AlertType.SysLogAlert.equals(alertType)) {
            if (StringUtils.isNotBlank(logType)) {
                sb.append(" and ad.probeId = :logType");
                params.put("logType", logType);
            }
            if (StringUtils.isNotBlank(logLevel)) {
                sb.append(" and ad.value = :logLevel");
                params.put("logLevel", logLevel);
            }
        }
        if (AlertType.SnmpTrapAlert.equals(alertType)) {
            if (StringUtils.isNotBlank(enterprise)) {
                sb.append(" and lower(ad.probeId) like :enterprise");
                params.put("enterprise", Criteria.escapeLike(enterprise.toLowerCase()));
            }
            if (StringUtils.isNotBlank(trapType)) {
                sb.append(" and lower(ad.value) like :trapType");
                params.put("trapType", Criteria.escapeLike(trapType.toLowerCase()));
            }
        }
        sb.append(")");
    }

    /**
     * 根据指定group字段统计告警信息
     * @return
     */
    public String getLevelStatisticsResultSQL() {
        StringBuilder sql = new StringBuilder("select new com.uxsino.alert.model.StatisticsResult (''||a.");
        sql.append(getGroupField());
        sql.append(" as scopeValue, count(a.id) as alertCount,"
                + " round(sum(extract(epoch from (coalesce(a.handleDate, now()) - a.firstAlertDate))/60)) as alertTotalTime,"
                + " round(max(extract(epoch from (coalesce(a.handleDate, now()) - a.firstAlertDate))/60)) as alertMaxTime,"
                + " round(avg(extract(epoch from (coalesce(a.handleDate, now()) - a.firstAlertDate))/60)) as alertAvgTime)"
                + " from " + alertType + " a where 1=1 ");
        addAlertDateFilter(sql);
        addAlertFilter(sql);
        sql.append(" group by a.");
        sql.append(getGroupField());
        return sql.toString();
    }

    public String getGroupField() {
        if (!StringUtils.isNotBlank(this.groupField)) {
            return "level";
        }
        try {
            Field group = CurrencyAlert.class.getDeclaredField(this.groupField);
            return group.getName();
        } catch (NoSuchFieldException | SecurityException e) {
            return "level";
        }
    }

    /**
     * 统计发生过告警的资源数
     * @return
     */
    public String getCountAlertObjSQL() {
        StringBuilder sql = new StringBuilder("select count(distinct a.objectId) from " + alertType + " a where 1=1 ");
        addAlertDateFilter(sql);
        addAlertFilter(sql);
        return sql.toString();
    }

}
