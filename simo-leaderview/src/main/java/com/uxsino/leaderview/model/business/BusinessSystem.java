package com.uxsino.leaderview.model.business;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @description 业务系统
 * @date 2019年1月4日
 */
@Entity
@Data
@ApiModel(description = "业务系统")
public class BusinessSystem {
    /**
     * ID
     */
    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "ID")
    @Size(max = 255, message = "ID最大长度255")
    private String id;

    /**
     * 名称
     */
    @Column(nullable = false)
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    @Size(max = 255, message = "名称最大长度255")
    private String name;

    /**
     * 责任人ID
     */
    @ApiModelProperty(value = "责任人ID", hidden = true)
    private Long liableUserId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Size(max = 255, message = "备注最大长度255")
    private String description;

    /**
     * 管理状态
     */
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "管理状态", hidden = true)
    private ManageStatus manageStatus;

    /**
     * 运行状态
     */
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "运行状态：未监控、正常、告警、不可用", hidden = true)
    private RunStatus runStatus;

    /**
     * 容量配置，格式如：{"storage":{"neComponent":[{"neId":"c3141355-dbe4-4ef2-87b2-4bb9793503f2","component":["/home","run"]},{"neId":"4be47ac6-d4d1-4b5a-acf0-476780390af4","component":["C:\\","D:\\"]}],"calcBy":"max"},"db":{"neComponent":[{"neId":"050cd153-a2ff-4ca9-bfe6-5d67d49bdf65","component":["SYSCATSPACE","USERSPACE1"]}],"calcBy":"avg"},"netIf":{"neComponent":[{"neId":"a7d72564-80f3-4dda-8afb-a3308765d64e","component":["5","8"]}],"calcBy":"min"},"cpu":{"neComponent":[{"neId":"da7f96f7-ea71-4eb5-8bcf-7fa29a30e008","component":["cpu1","cpu2"]},{"neId":"202dd14d-7a2a-4ff7-b83d-8469a393f265","component":["cpu2","cpu4"]}],"calcBy":"max"}}
     */
    @Type(type = "jsonb")
    @ApiModelProperty(value = "容量配置", hidden = true)
    private JSONObject capacityConf;

    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID", hidden = true)
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createDate;

    /**
     * 修改人ID
     */
    @ApiModelProperty(value = "修改人ID", hidden = true)
    private Long updateUserId;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", hidden = true)
    private Date updateDate;

    /**
     * 最近一次状态由“不可用”变成可用的时间（用于计算“运行时长”）
     */
    @ApiModelProperty(value = "最近一次状态由“不可用”变成可用的时间", hidden = true)
    private Date availableDate;

    /**
     * 接手人ID
     */
    @ApiModelProperty(value = "接手人ID", hidden = true)
    private Long handoverId;

    /**
     * 接手时间
     */
    @ApiModelProperty(value = "接手时间", hidden = true)
    private Date handoverDate;

    /**
     * 分享配置，格式如：{"roles":[1,2,3],"uids":[8,9,11]}
     */
    @Type(type = "jsonb")
    @ApiModelProperty(value = "分享配置", hidden = true)
    private JSONObject shareConf;

}
