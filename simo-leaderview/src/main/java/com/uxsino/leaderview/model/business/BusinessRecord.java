package com.uxsino.leaderview.model.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.Date;

@Data
@ApiModel(description = "业务以及指标信息")
public class BusinessRecord {

    @ApiModelProperty(value = "业务系统ID")
    private String businessId;

    @ApiModelProperty(value = "业务名称")
    private String bnsName;

    @ApiModelProperty(value = "业务责任人ID")
    private Long liableUserId;

    @ApiModelProperty(value = "业务备注")
    private String description;

    @ApiModelProperty(value = "业务运行状态：未监控、正常、告警、不可用")
    private String bnsRunStatus;

    @ApiModelProperty(value = "业务创建人ID")
    private Long createUserId;

    @ApiModelProperty(value = "业务创建时间")
    private Date createDate;

    /**
     * 最近一次状态由“不可用”变成可用的时间（用于计算“运行时长”）
     */
    @ApiModelProperty(value = "业务最近一次状态由“不可用”变成可用的时间")
    private Date availableDate;

    @ApiModelProperty(value = "业务接手人ID")
    private Long handoverId;

    /**
     * 分享配置，格式如：{"roles":[1,2,3],"uids":[8,9,11]}
     */
    @Type(type = "jsonb")
    @ApiModelProperty(value = "业务分享配置")
    private JSONObject shareConf;

    /**
     * 指标值（空值处理为默认值）
     */
    private Double indicatorValue;

    /**
     * 指标采集时间
     */
    private Date fetchDate;

    /**
     * 指标状态
     */
    private String runStatus;

    /**
     * 真实指标值
     */
    private Double realIndValue;

    /**
     * 指标名称
     */
    private String indicatorName;

    /**
     * 指标详情
     */
    private JSON indicatorDetail;

    private Indicator indicator;

}
