package com.uxsino.leaderview.model.business;

import com.uxsino.commons.model.DataSourceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description 业务系统查询参数
 * @date 2019年9月6日
 */
@Data
@ApiModel(description = "业务系统查询参数")
public class BusinessQuery {
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 运行状态
     */
    @ApiModelProperty(value = "运行状态：未监控、正常、告警、不可用")
    private RunStatus runStatus;

    /**
     * 业务ID列表
     */
    @ApiModelProperty(value = "业务ID列表")
    private List<String> ids;

    /**
     * 不包含的业务ID列表
     */
    @ApiModelProperty(value = "不包含的业务ID列表")
    private List<String> notInIds;

    /**
     * 统计时段
     */
    @ApiModelProperty(value = "统计时段：最近7天、最近2周、最近1个月")
    private Period period = Period._7days;

    /**
     * 排序方式
     */
    @ApiModelProperty(value = "排序方式：按创建时间排序、按健康度排序、按告警数排序、按可用率排序、自定义排序")
    private SortWay sortWay;

    /**
     * 数据来源类型
     */
    @ApiModelProperty(value = "数据来源类型：全部、我的、分享的")
    private DataSourceType dataSourceType;

}
