package com.uxsino.leaderview.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 巡查记录(WuhouPatrolRecord)表实体类
 *
 * @author huanglei
 * @since 2022-01-28 09:05:22
 */
@Data
@Entity
@Table(name = "wuhou_patrol_record")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WuhouPatrolRecord {

    @Id
    @ApiModelProperty(value = "主键id")
    @GeneratedValue(generator = "uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
    private Long id;
    
    @ApiModelProperty(value = "场所id")
    private Long hotelId;
    
    @ApiModelProperty(value = "巡查人id")
    private Long patrolId;

    @ApiModelProperty(value = "巡查人名称")
    private String patrolName;

    @ApiModelProperty(value = "巡查内容")
    private String patrolConent;

    @ApiModelProperty(value = "巡查人手机号")
    private String patrolPhone;
    
    @ApiModelProperty(value = "巡查时间")
    private Date patrolTime;
    
    @ApiModelProperty(value = "巡查照片地址")
    private String patrolPhoto;

    @ApiModelProperty(value = "小程序人员openid")
    private String openId;
    
    @ApiModelProperty(value = "记录创建人id")
    private Long createUser;
    
    @ApiModelProperty(value = "记录创建时间")
    private Date createTime;

    @ApiModelProperty(value = "记录修改人id")
    private Long updateUser;

    @ApiModelProperty(value = "记录修改时间")
    private Date updateTime;
    
    @ApiModelProperty(value = "是否已删除（0：正常；1：已删除）")
    private Integer isDeleted;

    @ApiModelProperty(value = "巡查项目")
    private String patrolProject;

    @ApiModelProperty(value = "检查状态(1正常；2异常；)")
    private Integer checkStatus;

    @ApiModelProperty(value = "巡查角色ID")
    private String patrolRoleId;

    @ApiModelProperty(value = "巡查角色名称")
    private String patrolRoleName;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    /*@Override
    protected Serializable pkVal() {
        return this.id;
    }*/
    
}
