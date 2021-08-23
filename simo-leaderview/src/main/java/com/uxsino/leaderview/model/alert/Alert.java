package com.uxsino.leaderview.model.alert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @description 资源告警信息实体类
 *
 * @date 2017年4月13日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "资源告警")
public class Alert extends CurrencyAlert {

    /**
     * 告警对象ID（资源ID）
     */
    @Column(nullable = false)
    @ApiModelProperty(value = "告警对象ID")
    @NotBlank(message = "告警对象ID不能为空")
    private String objectId;

    /**
     * 指标ID
     */
    @ApiModelProperty(value = "指标ID")
    private String indicatorId;

    /**
     * 告警来源
     */
    @Column(length = 30)
//    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "告警来源")
    private String alertOrigin;

    /**
     * 部件标识（数量检测、组合策略可能有多个部件）
     */
    @Column(columnDefinition = "TEXT")
    @ApiModelProperty(value = "部件标识")
    private String componentId;

    /**
     * 属性ID
     */
    @Column(columnDefinition = "TEXT")
    @ApiModelProperty(value = "属性ID")
    private String fieldId;

    /**
     * 告警策略规则ID
     */
    @ApiModelProperty(value = "告警策略规则ID", hidden = true)
    private Long ruleId;

    /**
     * 属性配置ID（用于计算健康度）
     */
    @ApiModelProperty(value = "属性配置ID", hidden = true)
    private Long fieldConfId;

}
