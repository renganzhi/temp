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


}
