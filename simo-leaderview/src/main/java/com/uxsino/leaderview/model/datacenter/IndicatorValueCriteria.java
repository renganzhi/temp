package com.uxsino.leaderview.model.datacenter;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.db.model.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 指标历史数据检索
 */
@Data
@ApiModel(description = "指标数据查询参数类")
@EqualsAndHashCode(callSuper = false)
public class IndicatorValueCriteria extends PageModel {
    private static final Logger LOG = LoggerFactory.getLogger(IndicatorValueCriteria.class);

    @ApiModelProperty(value = "历史数据默认时间间隔秒数")
    private static final Long DEFAULT_INTERVAL = 24 * 60 * 60L;// 历史数据默认时间间隔秒数

    private boolean pagination = true;

    @ApiModelProperty(value = "资源ID")
    @NotBlank(message = "资源ID不能为空")
    private String neId;

    @ApiModelProperty(value = "指标ID")
    @NotBlank(message = "指标ID不能为空")
    private String indicatorId;

    @ApiModelProperty(value = "部件唯一标识")
    private String identifier;

    @ApiModelProperty(value = "检测时间")
    private String tm;

    /*@ApiModelProperty(value = "搜索属性值")
    private String search;
    
    @ApiModelProperty(value = "搜索属性， search为空时：会根据属性过滤掉该属性值为空的记录")
    private String searchField;*/

    @ApiModelProperty(value = "and查询的搜索属性，只支持单值，如： {\"cpu_used\":\"1\",\"cpu_free\": \"3\"}")
    private String fieldSearchMust;

    @ApiModelProperty(value = "or查询的搜索属性，如： {\"cpu_used\":\"1, 2\",\"cpu_free\": \"3\"}")
    private String fieldSearchShould;

    @ApiModelProperty(value = "根据该属性过滤掉该属性值为空的记录")
    private String fieldsNotNull;

    @ApiModelProperty(value = "是否进行单位转换，默认要单位转换")
    private boolean convertUnit = true;

    @ApiModelProperty(value = "起始时间")
    private String fetchDateFrom;

    @ApiModelProperty(value = "结束时间")
    private String fetchDateTo;

    @ApiModelProperty(value = "自定义数据查询时间长度（秒）")
    private Long cusInterval;// 自定义数据查询时间长度（秒）
    
    @ApiModelProperty(value = "用来接收排序参数格式如： {\"v.cpuUsage\":\"asc\",\"tm\": \"desc\"}的字符串")
    private String sortStr;

    // ------------------------------------------------------------------------ 以上是接收的参数，以下是封装的参数

    @ApiModelProperty(value = "指标名称")
    private List<String> indicatorName;// 指标名称，如：["cpu_usage_avg","network_cpu"]

    // 交给前端去处理 添加后缀.keyword
    /* @ApiModelProperty(value = "需要精准查询的参数")
    private List<String> keyWord;*/// 不传该值，则数据中心那边默认资源id和序列精准查询，其他字段模糊查询
                                    // keyword说明：精准查询的参数类型只支持String，不支持int，double，布尔值

    private JSONObject must; // 封装查询条件"and 操作"，如：{"tm":[value,value],"v":value}

    private JSONObject mustNot; // 封装查询条件 "不等于"，如：{"tm":[value,value],"v":value}

    private JSONObject should; // 封装查询条件"or 操作"，如：{"tm":[value,value],"v":value}

    private String sourceType = "exclude"; // null, "all", "include", "exclude",null或 all表示全部返回，
                                           // include只返回指定数据，exclude排除

    private List<String> sourceField = Arrays.asList("tid,type".split(",")); // 指定sourceType控制的字段，如： ["cost","batch"]

    private JSONObject sort; // 排序，如： {"v.cpuUsage":"asc","tm": "desc"}

    private Boolean isNested;

    private JSONObject nested;

    @ApiModelProperty(value = "数据来源")
    private String dataSource = "monitor";// 默认数据来源设置为monitor

    // 数据中心那边把scope整理到must里了
    /*private JSONObject scope;*/ // 范围查询：{"tm":{"gte":"2020-04-10 10:18:50","lte": "2020-05-12
    // 11:40:04"},"fetchDate":{"gte":"2020-04-10 10:18:50","lte": "2020-05-12 11:40:04"}}

    @ApiModelProperty(value = "采样的表 的百分数")
    private Float sampling;

    //对指标名进行小写处理
    public void setIndicatorName(List<String> indName) {
        indName = indName.stream().map(n->n.toLowerCase()).collect(Collectors.toList());
        this.indicatorName = indName;
    }

    public void setSearchTime() {
        Long interval = cusInterval == null ? DEFAULT_INTERVAL : cusInterval;
        JSONObject tmJson = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateFrom = null;
        Date dateTo = null;
        if (fetchDateFrom == null && fetchDateTo == null) {
            dateTo = new Date();
            dateFrom = new Date(System.currentTimeMillis() - interval * 1000);
        } else if (fetchDateFrom != null && fetchDateTo == null) {
            try {
                dateTo = new Date(sdf.parse(fetchDateFrom).getTime() + interval * 1000);
                dateFrom = sdf.parse(fetchDateFrom);
            } catch (ParseException e) {
                LOG.error("fetchDateFrom 转日期错误：",e);
            }
        } else if (fetchDateFrom == null && fetchDateTo != null) {
            try {
                dateFrom = new Date(sdf.parse(fetchDateTo).getTime() - interval * 1000);
                dateTo = sdf.parse(fetchDateTo);
            } catch (ParseException e) {
                LOG.error("fetchDateTo 转日期错误：",e);
            }
        }else if(fetchDateFrom != null && fetchDateTo != null){
            try {
                dateFrom = sdf.parse(fetchDateFrom);
                dateTo = sdf.parse(fetchDateTo);
            }catch (ParseException e){
                LOG.error("fetchDateFrom和fetchDateTo日期转换错误：",e);
            }
        }
        if(dateFrom != null) {
            tmJson.put("gte", dateFrom);
        }
        if(dateTo != null) {
            tmJson.put("lte", dateTo);
        }
        must = must == null ? new JSONObject() : must;
        must.put("tm", tmJson);
    }

    /*public void setSearchTime() {
        Long interval = cusInterval == null ? DEFAULT_INTERVAL : cusInterval;
        JSONObject tmJson = new JSONObject();
        try {
            if (must != null && !must.isEmpty() && must.containsKey("tm")) {
                tmJson = must.getJSONObject("tm");
                if (tmJson.containsKey("gte") && !tmJson.containsKey("lte")) {
                    Date fetchDateTo = new Date(tmJson.getDate("gte").getTime() + interval * 1000);
                    tmJson.put("lte", fetchDateTo);
                } else if (!tmJson.containsKey("gte") && tmJson.containsKey("lte")) {
                    Date fetchDateFrom = new Date(tmJson.getDate("lte").getTime() - interval * 1000);
                    tmJson.put("gte", fetchDateFrom);
                }
            } else {
                Date fetchDateTo = new Date();
                Date fetchDateFrom = new Date(System.currentTimeMillis() - interval * 1000);
                tmJson.put("gte", fetchDateFrom);
                tmJson.put("lte", fetchDateTo);
            }
            must = must == null ? new JSONObject() : must;
            must.put("tm", tmJson);
        } catch (Exception e) {
            LOG.error("IndicatorValueCriteria.setSearchTime报错：", e);
        }
    }*/
}
