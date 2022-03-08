package com.uxsino.leaderview.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 浆洗街道辖区涉藏驻蓉机构一览表
 */
@Entity
@Data
@Table(name = "simo_leaderview_jx_orga_table")
@NoArgsConstructor
@AllArgsConstructor
public class TibetOrganizationTable {

    @Id
    @Column(name = "id")
    private Long id;

    //单位名称
    private String orgaName;

    //单位地址
    private String address;

    //经纬度
    private String latAndLong;

    //单位负责人
    private String orgaLeader;

    //职务
    private String position;

    //负责人联系电话
    private String leaderPhone;

    //联系人（办公室主任）
    private String officeDirector;

    //办公室主任联系电话
    private String directorPhone;

    //备注
    private String comment;


}
