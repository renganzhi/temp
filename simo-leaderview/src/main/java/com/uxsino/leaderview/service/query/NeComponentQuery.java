package com.uxsino.leaderview.service.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 网络设备链路数据记录表
 *
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeComponentQuery {

    /** 自增长ID */
    private Long id;

    /** 不包括的部件id */
    private List<String> neCompIdNotIn;

    /**
     * 所属网元
     */
    private List<String> neIds;

    /**
     * 所属网元名称
     */
    private String neName;

    /** 部件类型  */
    private String indicatorName;

    /**
     * 部件标识
     */
    private String identifier;//部件显示名称字段

    /** 部件名称*/
    private String componentName;

    /**
     * 是否关注
     */
    private boolean visible;

    /**
     * 检索部件名称或ip
     */
    private String keyword;

}
