package com.uxsino.leaderview.model.business;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 业务指标
 * @date 2019年1月6日
 */
public enum Indicator {
    health("健康度","100","",true,false,true),
    available_rate("可用率","100","%",false,false,false),
    down_times("不可用次数","0","",false,false,false),
    alert_count("告警数","0","",false,false,false),
    resp_time("响应时间",null,"ms",true,false,true),
    storage_capacity("存储容量",null,"%",true,false,true),
    db_capacity("数据库容量",null,"%",true,false,true),
    netIf_capacity("带宽容量",null,"%",true,false,true),
    cpu_capacity("CPU容量",null,"%",true,false,true),
    busy_rate("繁忙度","0","%",true,false,true),
    cpu_usage("CPU使用率",null,"%",false,true,true),
    memory_usage("内存利用率",null,"%",false,true,true),
    disk_io("磁盘IO",null,"次/秒",false,true,true),
    busy_resp_time("响应时间",null,"ms",false,true,true),
    if_out_utilization("出流量带宽利用率",null,"%",false,true,true),
    if_in_utilization("入流量带宽利用率",null,"%",false,true,true),
    tcp_link_num("TCP连接数",null,"",false,true,true),
    MTTR("MTTR","0","",false,false,false),
    MTBF("MTBF","0","",false,false,false);
    private String text;

    private String defaultVal;

    private String unit;

    private boolean isMonitorRule;

    private boolean isBusyRule;

    private boolean isInstant;

    private Indicator(String text, String defaultVal, String unit, boolean isMonitorRule, boolean isBusyRule,
                      boolean isInstant) {
        this.text = text;
        this.defaultVal = defaultVal;
        this.unit = unit;
        this.isMonitorRule = isMonitorRule;
        this.isBusyRule = isBusyRule;
        this.isInstant = isInstant;
    }

    public String getText() {
        return this.text;
    }

    public String getDefaultVal() {
        if (this.defaultVal == null) {
            return "--";
        }
        return this.defaultVal;
    }

    public Double getDefDoubleVal() {
        if (this.defaultVal == null) {
            return null;
        }
        return Double.valueOf(this.defaultVal);
    }

    public String getUnit() {
        return this.unit;
    }

    public boolean isMonitorRule() {
        return this.isMonitorRule;
    }

    public boolean isBusyRule() {
        return this.isBusyRule;
    }

    public boolean isInstant() {
        return this.isInstant;
    }

    /**
     * 获取业务策略的指标
     * @return
     */
    public static List<Indicator> getMonitorIndicator() {
        List<Indicator> indList = new ArrayList<>();
        for (Indicator ind : Indicator.values()) {
            if (ind.isMonitorRule) {
                indList.add(ind);
            }
        }
        return indList;
    }

    /**
     * 获取繁忙度策略的指标
     * @return
     */
    public static List<Indicator> getBusyIndicator() {
        List<Indicator> indList = new ArrayList<>();
        for (Indicator ind : Indicator.values()) {
            if (ind.isBusyRule) {
                indList.add(ind);
            }
        }
        return indList;
    }

    /**
     * 获取瞬时值的指标
     * @return
     */
    public static List<Indicator> getInstantIndicator() {
        List<Indicator> indList = new ArrayList<>();
        for (Indicator ind : Indicator.values()) {
            if (ind.isInstant) {
                indList.add(ind);
            }
        }
        return indList;
    }

    /**
     * 获取性能指标
     * @return
     */
    public static List<Indicator> getPerformanceIndicator() {
        return Lists.newArrayList(new Indicator[] { Indicator.health, Indicator.busy_rate, Indicator.resp_time,
                Indicator.disk_io });
    }

    /**
     * 获取容量指标
     * @return
     */
    public static List<Indicator> getCapacityIndicator() {
        return Lists.newArrayList(new Indicator[] { Indicator.storage_capacity, Indicator.db_capacity,
                Indicator.netIf_capacity, Indicator.cpu_capacity });
    }

    /**
     * 获取大屏展示的指标
     * @return
     */
    public static List<Indicator> getLeaderviewIndicator() {
        List<Indicator> busyList = getBusyIndicator();
        List<Indicator> capacityList = getCapacityIndicator();
        List<Indicator> list = Lists.newArrayList();
        for (Indicator ind : Indicator.values()){
            if (busyList.contains(ind) || capacityList.contains(ind)) continue;
            list.add(ind);
        }
        return list;
    }

}
