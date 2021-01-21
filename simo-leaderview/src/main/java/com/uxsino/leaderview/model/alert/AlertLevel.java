package com.uxsino.leaderview.model.alert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @description 告警等级信息实体类
 * 
 * @date 2017年4月13日
 */
@Data
@Entity
@Table(name = "simo_alert_level")
@ApiModel(description = "告警等级信息")
public class AlertLevel {

    /**
     * 告警等级编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "告警等级编号")
    private Long id;

    /**
     * 告警等级值（取值范围1~10，值越大表示告警越严重）
     */
    @Column(nullable = false)
    @ApiModelProperty(value = "告警等级值")
    @Min(value = 1, message = "告警等级值范围须在1-10之间")
    @Max(value = 10, message = "告警等级值范围须在1-10之间")
    private Integer level;

    /**
     * 告警等级显示名称
     */
    @Column(nullable = false, length = 20)
    @ApiModelProperty(value = "告警等级显示名称")
    @Size(min = 2, max = 2, message = "告警等级显示名称长度须为2个字符")
    private String name;

    /**
     * 告警等级色标（十六进制的颜色代码，如#FFFF00）
     */
    @Column(nullable = false, length = 10)
    @ApiModelProperty(value = "告警等级色标")
    private String color;

    /**
     * 告警提示音文件路径
     */
    @Column(nullable = false, length = 500)
    @ApiModelProperty(value = "告警提示音文件路径")
    private String music;

    /**
     * 告警等级状态
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "告警等级状态")
    private AlertLevelStatus status;

    /**
     * 升级到某个level值
     */
    @ApiModelProperty(value = "升级到某个level值")
    @Min(value = 1, message = "告警等级值范围须在1-10之间")
    @Max(value = 10, message = "告警等级值范围须在1-10之间")
    private Integer upToLevel;

    /**
     * 告警升级条件：告警累计次数（/次）
     */
    @ApiModelProperty(value = "告警升级条件：告警累计次数（/次）")
    @Min(value = 1, message = "告警累计次数须为正整数")
    @Max(value = Integer.MAX_VALUE, message = "告警累计次数最大值为2147483647")
    private Integer upByNumber;

    /**
     * 告警升级条件：告警持续时间（/分钟）
     */
    @ApiModelProperty(value = "告警升级条件：告警持续时间（/分钟）")
    @Min(value = 1, message = "告警持续时间须为正整数")
    @Max(value = Integer.MAX_VALUE, message = "告警持续时间最大值为2147483647")
    private Integer upByTime;

    /**
     * 健康度得分
     */
    @ApiModelProperty(value = "健康度得分")
    @Min(value = 0, message = "健康度得分范围须在0-100之间")
    @Max(value = 100, message = "健康度得分范围须在0-100之间")
    private Integer health;

}
