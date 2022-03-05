package com.uxsino.leaderview.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 民宿信息(WuhouHomestay)实体类
 *
 * @author qjq
 * @since 2022-01-27 11:11:31
 */
@ApiModel(value = "WuhouHomestay对象", description = "民宿信息")
@Entity
@Data
@Table(name = "wuhou_homestay")
@NoArgsConstructor
@AllArgsConstructor
public class WuhouHomestay implements Serializable {
    private static final long serialVersionUID = -37738082660901758L;
    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String placeName;
    /**
     * 房屋类型(1租赁房屋2旅馆3民宿)
     */
    @ApiModelProperty(value = "房屋类型")
    private Integer placeType;
    /**
     * 业主（房东）姓名
     */
    @ApiModelProperty(value = "业主（房东）姓名")
    private String landlordName;
    /**
     * 业主（房东）联系电话
     */
    @ApiModelProperty(value = "业主（房东）联系电话")
    private String landlordPhone;
    /**
     * 经营者姓名
     */
    @ApiModelProperty(value = "经营者姓名")
    private String operatorName;
    /**
     * 经营者联系电话
     */
    @ApiModelProperty(value = "经营者联系电话")
    private String operatorPhone;
    /**
     * 标准地址
     */
    @ApiModelProperty(value = "标准地址")
    private String address;
    /**
     * 房间数
     */
    @ApiModelProperty(value = "房间数")
    private Integer roomNum;
    /**
     * 床铺数
     */
    @ApiModelProperty(value = "床铺数")
    private Integer bedNum;
    /**
     * 社区民警
     */
    @ApiModelProperty(value = "社区民警")
    private String communityPolice;
    /**
     * 网格员电话
     */
    @ApiModelProperty(value = "网格员")
    private String gridMember;
    /**
     * 微消站电话
     */
    @ApiModelProperty(value = "微消站")
    private String fireStation;
    /**
     * 社区民警电话
     */
    @ApiModelProperty(value = "社区民警电话")
    private String communityPolicePhone;
    /**
     * 网格员电话
     */
    @ApiModelProperty(value = "网格员电话")
    private String gridMemberPhone;
    /**
     * 微消站电话
     */
    @ApiModelProperty(value = "微消站电话")
    private String fireStationPhone;
    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String detailAddress;
    /**
     * 民宿介绍
     */
    @ApiModelProperty(value = "民宿介绍")
    private String introduce;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private String latitude;
    /**
     * 图片url
     */
    @ApiModelProperty(value = "图片url")
    private String photoUrl;
    /**
     * 二维码url
     */
    @ApiModelProperty(value = "二维码url")
    private String qrCodeUrl;


    @ApiModelProperty(value = "记录创建人id")
    private Long createUser;

    @ApiModelProperty(value = "记录创建时间")
    private Date createTime;

    @ApiModelProperty(value = "记录修改人id")
    private Long updateUser;

    @ApiModelProperty(value = "记录修改时间")
    private Date updateTime;
    /**
     * 注销说明
     */
    @ApiModelProperty(value = "注销说明")
    private String cancelInstruction;

    @ApiModelProperty(value = "营业状态(1:营业,2:暂停营业)")
    private Integer status;

    @ApiModelProperty(value = "是否已删除（0：正常；1：已删除）")
    private Integer isDeleted;
}
