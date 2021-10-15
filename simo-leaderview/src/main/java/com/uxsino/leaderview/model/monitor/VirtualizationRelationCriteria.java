package com.uxsino.leaderview.model.monitor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 虚拟化信息关联表查询类
 * 
 * @version simo2.0 Service Platform 2019-11-01
 * @author chenyl
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
public class VirtualizationRelationCriteria{

    private Long id;

    private List<Long> ids;

    private String vmId; // 所属资源Id

    private String neIds; // 资源ID包含

    private String name; // 名称

    private String ip; // IP

    private String type; // 类型(esxi主机/虚拟主机/数据存储)

    private List<String> types;// 多个类型

    private String status; // 状态

    private Boolean monitoring; // 监控状态，true：监控中，false：未监控

    private List<String> identifiers; // 部件ID

    private boolean isAccurate = true; // 是否是精确查询,默认是精确查询

    private boolean pagination = false;

    private int pageSize;

}
