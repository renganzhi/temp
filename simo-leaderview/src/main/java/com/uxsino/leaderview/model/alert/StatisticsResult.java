package com.uxsino.leaderview.model.alert;

import lombok.Data;

import java.util.List;

/**
 * @description 告警统计的结果信息
 * 
 * @date 2017年4月13日
 */
@Data
public class StatisticsResult {
    /**
     * 统计口径（按资源、按资源类型、按告警类型、按告警级别）对应的字段值
     */
    private String scopeValue;

    /**
     * 资源数
     */
    private Integer objCount;

    /**
     * 告警等级
     */
    private Integer alertLevel;

    /**
     * 告警次数
     */
    private Long alertCount;

    /**
     * 告警总历时（分钟）
     */
    private Long alertTotalTime;

    /**
     * 告警最大历时（分钟）
     */
    private Integer alertMaxTime;

    /**
     * 告警平均历时（分钟，浮点型，因hibernate与postgresql数字类型转换问题，该字段作临时存放）
     */
    private Double doubleAvgTime;

    /**
     * 告警平均历时（分钟）
     */
    private Integer alertAvgTime;

    /**
     * 不同告警级别的统计结果
     */
    private List<StatisticsResult> levelResultList;

    public StatisticsResult() {

    }

    public StatisticsResult(Long alertCount, Long alertTotalTime, Integer alertMaxTime, Double doubleAvgTime) {
        this.alertCount = null == alertCount ? 0L : alertCount;
        this.alertTotalTime = null == alertTotalTime ? 0L : alertTotalTime;
        this.alertMaxTime = null == alertMaxTime ? 0 : alertMaxTime;
        this.doubleAvgTime = doubleAvgTime;
        this.alertAvgTime = null != doubleAvgTime ? doubleAvgTime.intValue() : 0;
    }

    public StatisticsResult(Integer scopeValue, Long alertCount, Long alertTotalTime, Integer alertMaxTime,
                            Double doubleAvgTime) {
        this.scopeValue = scopeValue.toString();
        this.alertCount = null == alertCount ? 0L : alertCount;
        this.alertTotalTime = null == alertTotalTime ? 0L : alertTotalTime;
        this.alertMaxTime = null == alertMaxTime ? 0 : alertMaxTime;
        this.doubleAvgTime = doubleAvgTime;
        this.alertAvgTime = null != doubleAvgTime ? doubleAvgTime.intValue() : 0;
    }

    public StatisticsResult(Long scopeValue, Long alertCount, Long alertTotalTime, Integer alertMaxTime,
                            Double doubleAvgTime) {
        this.scopeValue = scopeValue.toString();
        this.alertCount = null == alertCount ? 0L : alertCount;
        this.alertTotalTime = null == alertTotalTime ? 0L : alertTotalTime;
        this.alertMaxTime = null == alertMaxTime ? 0 : alertMaxTime;
        this.doubleAvgTime = doubleAvgTime;
        this.alertAvgTime = null != doubleAvgTime ? doubleAvgTime.intValue() : 0;
    }

    public StatisticsResult(String scopeValue, Integer alertLevel, Long alertCount) {
        this.scopeValue = scopeValue;
        this.alertLevel = alertLevel;
        this.alertCount = alertCount;
    }
}
