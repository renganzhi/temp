package com.uxsino.leaderview.model.alert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "告警等级信息参数")
public class AlertLevelQuery {

    /**
     * 告警等级值（取值范围1~10，值越大表示告警越严重）
     */
    @ApiModelProperty(value = "告警等级值")
    private Integer level;

    /**
     * 告警等级显示名称
     */
    @ApiModelProperty(value = "告警等级显示名称")
    private String name;

    /**
     * 告警等级状态
     */
    @ApiModelProperty(value = "告警等级状态")
    private AlertLevelStatus status;

}
