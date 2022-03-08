package com.uxsino.leaderview.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 浆洗街道涉藏小区排查表
 */
@Entity
@Data
@Table(name = "simo_leaderview_jx_community_table")
@NoArgsConstructor
@AllArgsConstructor
public class TibetCommunityTable {

    @Id
    @Column(name = "id")
    private Long id;

    //小区(院落)名称
    private String name;

    //小区(院落)详细地址
    private String address;

    //经纬度
    private String latAndLong;

    //实有房屋数
    private Long house;

    //实有人口数
    private Long population;

    //派出所民(辅)警姓名及电话
    private String policeAndPhone;

    //网格员姓名及电话
    private String gridMemberAndPhone;

    //综治、执法队员姓名及电话
    private String inspectorAndPhone;

    //机关科(队)匹配警力人数
    private Long policeNumber;


}
