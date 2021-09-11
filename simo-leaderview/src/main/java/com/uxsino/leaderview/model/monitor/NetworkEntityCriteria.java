package com.uxsino.leaderview.model.monitor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.model.RunStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 网元检索
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "资源记录查询类")
public class NetworkEntityCriteria extends PageModel {

    private boolean pagination = true;

    @ApiModelProperty(value = "查询条件-关键字")
    private String keyword;// 匹配关键字

    @ApiModelProperty(value = "查询条件-资源名称")
    private String name; // 模糊查询

    @ApiModelProperty(value = "查询条件-父类型")
    private BaseNeClass baseNeClass;   // 对象自身的父类型：主机\网络\数据库...

    @ApiModelProperty(value = "查询条件-父类型包含")
    private List<BaseNeClass> baseNeclasses;

    @ApiModelProperty(value = "查询条件-资源类型")
    private NeClass neClass;// 资源类型

    @ApiModelProperty(value = "查询条件-类型包含")
    private List<NeClass> neClasses;// 资源类型

    @ApiModelProperty(value = "查询条件-IP")
    private String ip;

    @ApiModelProperty(value = "查询条件-IP包含")
    private List<String> ips;

    @ApiModelProperty(value = "查询条件-主机ID")
    private String hostId;// 主机ID

    @ApiModelProperty(value = "查询条件-主机ID包含")
    private List<String> hostIdIn;// 主机ID包含

    @ApiModelProperty(value = "查询条件-域ID")
    private Long domainId;// 安全域ID

    @ApiModelProperty(value = "查询条件-域范围包含")
    private List<Long> domainIds;// 多个安全域

    @ApiModelProperty(value = "查询条件-资源ID")
    private String id;// 查询某一个数据

    @ApiModelProperty(value = "查询条件-资源ID包含")
    private List<String> ids;// 包含的ID

    @ApiModelProperty(value = "查询条件-不包含的ID")
    private List<String> idNotIn;// 不包含的ID

    @ApiModelProperty(value = "查询条件-协议")
    private String protocol;// 某一种协议

    @ApiModelProperty(value = "查询条件-协议包含")
    private List<String> protocols;// 多个协议

    @ApiModelProperty(value = "查询条件-资源类型的分组ID")
    private Long groupTypeId;// 资源类型的分组

    @ApiModelProperty(value = "查询条件-资源类型的分组ID包含")
    private List<Long> groupTypeIds;// 资源类型的分组

    @ApiModelProperty(value = "查询条件-匹配自定义资源类型分组 groupType 字段")
    private String groupType;// 匹配自定义资源类型分组 groupType 字段

    @ApiModelProperty(value = "查询条件-是否被监控")
    private Boolean monitoring;// 是否被监控

    @ApiModelProperty(value = "查询条件-是否由资源同一管理")
    private Boolean sourceManage = Boolean.TRUE;// 是否由资源统一管理

    @ApiModelProperty(value = "查询条件-管理状态")
    private ManageStatus manageStatus;// // 对象的管理状态

    @ApiModelProperty(value = "查询条件-管理状态包含")
    private List<ManageStatus> manageStatusIn;// 对象的管理状态

    @ApiModelProperty(value = "查询条件-管理状态,默认不显示删除的资源")
    private List<ManageStatus> manageStatusNotIn = Lists.newArrayList(ManageStatus.Delected);// 对象的管理状态,默认不显示删除的资源

    @ApiModelProperty(value = "查询条件-运行状态")
    private RunStatus runStatus;// 运行状态

    @ApiModelProperty(value = "查询条件-运行状态包含")
    private List<RunStatus> runStatusIn;// 运行状态包含

    @ApiModelProperty(value = "查询条件-特殊的查询条件")
    private String querySpec;

    @ApiModelProperty(value = "查询条件-资源分组ID")
    private Long groupId;

    @ApiModelProperty(value = "查询条件-资源分组ID")
    private List<Long> groupIds;

    @ApiModelProperty(value = "查询条件-是否限定为登录用户的权限，默认为true")
    private boolean limit = true;

    @ApiModelProperty(value = "查询条件-是否查询资源健康度，默认为false")
    private boolean healthReturn;

    @ApiModelProperty(value = "查询条件-排序的字段")
    private String sortField;

    @ApiModelProperty(value = "查询条件-是否升序排列，默认为false")
    private Boolean asc = Boolean.FALSE;

    @ApiModelProperty(value = "查询条件-软硬件区分")
    private Boolean hardOrSoft;// 只查询软件资源或者物理资源

    @ApiModelProperty(value = "分组字段")
    private String groupField;

    @ApiModelProperty(value = "不分页查询时查询结果条数限制")
    private Integer limitNum;

    Map<String, Object> param = Maps.newHashMap();
}
