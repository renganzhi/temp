package com.uxsino.leaderview.model.monitor;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.db.model.IntervalType;
import com.uxsino.commons.db.model.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指标数据查询参数类
 */
@Data
@ApiModel(description = "指标数据查询参数类")
@EqualsAndHashCode(callSuper = false)
public class IndicatorValueQO extends PageModel {
    private static final Logger logger = LoggerFactory.getLogger(IndicatorValueQO.class);

    @ApiModelProperty(value = "历史数据默认时间间隔秒数")
    private static final Long DEFAULT_INTERVAL = 24 * 60 * 60L;// 历史数据默认时间间隔秒数

    private boolean pagination = false;

    @ApiModelProperty(value = "资源ID")
    @NotBlank(message = "资源ID不能为空")
    private List<String> neIds;

    @ApiModelProperty(value = "指标名称")
    @NotBlank(message = "指标名不能为空")
    private List<String> indicatorNames;// 指标名称，如：["cpu_usage_avg","network_cpu"]

    @ApiModelProperty(value = "指标类型")
    private Map<String, String> indicatorTypes;//key为指标名，value为指标类型。查询聚合值时不能为空

    @ApiModelProperty(value = "是否进行单位转换，默认不要单位转换")
    private boolean convertUnit;

    @ApiModelProperty(value = "自定义数据查询时间长度（秒）")
    private Long cusInterval;// 自定义数据查询时间长度（秒）

    @ApiModelProperty(value = "起始时间")
    private Date dateFrom;

    @ApiModelProperty(value = "结束时间")
    private Date dateTo;

    @ApiModelProperty(value = "统计时间间隔")
    private Long interval;

    @ApiModelProperty(value = "统计时间间隔类型，包括周，天，小时，分钟")
    private IntervalType intervalType;

    private List<String> identifiers;//部件ids，支持多个

    private Map<String, List<String>> fields;//key为指标名，value为属性。单值属性，需要传result

    private JSONObject sort; // 排序，如： {"v.cpuUsage":"asc","tm": "desc"}

    private boolean current = false;//是否查询实时指标表，默认为false，查询历史指标

    public void setSearchTime() {
        Long intervalTmp = cusInterval == null ? DEFAULT_INTERVAL : cusInterval;
        if (dateFrom == null && dateTo == null) {
            dateTo = new Date();
            dateFrom = new Date(System.currentTimeMillis() - intervalTmp * 1000);
        } else if (dateFrom != null && dateTo == null) {
            dateTo = new Date(dateFrom.getTime() + intervalTmp * 1000);
        } else if (dateFrom == null && dateTo != null) {
            dateFrom = new Date(dateTo.getTime() - intervalTmp * 1000);
        }
    }

    public String jpl(){
        StringBuilder jpql = new StringBuilder();
        if (neIds != null && !neIds.isEmpty()) {
            jpql.append(" and o.neId IN (:neIds)");
        }
        if (indicatorNames != null && !indicatorNames.isEmpty()) {
            jpql.append(" and o.indicatorName IN (:indicatorNames)");
        }
        if (dateFrom != null) {
            jpql.append(" and o.fetchDate>= (:dateFrom) ");
        }
        if (dateTo != null) {
            jpql.append(" and o.fetchDate<= (:dateTo) ");
        }
        if (sort != null && !sort.isEmpty()) {
            jpql.append(" order by ");
            int index = 0;
            int endIndex = sort.size() - 1;
            for (String key : sort.keySet()) {
                String v = sort.getString(key);
                if (endIndex == index) {
                    jpql.append(key + " " + v + " ");
                } else {
                    jpql.append(key + " " + v + ", ");
                }
                index++;
            }
        } else {//默认按批次倒序
            jpql.append(" order by o.batch desc");
        }
        return jpql.toString();
    }

    public Map<String, Object> params(){
        Map<String, Object> params = new HashMap<>();
        if (neIds != null && !neIds.isEmpty()) {
            params.put("neIds", neIds);
        }
        if (indicatorNames != null && !indicatorNames.isEmpty()) {
            params.put("indicatorNames", indicatorNames);
        }
        if (dateFrom != null) {
            params.put("dateFrom", dateFrom);
        }
        if (dateTo != null) {
            params.put("dateTo", dateTo);
        }
        return params;
    }
}
