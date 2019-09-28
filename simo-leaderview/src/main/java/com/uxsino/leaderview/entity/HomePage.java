package com.uxsino.leaderview.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设置主页显示内容
 */
@Entity
@Data
@Table(name = "simo_mc_home_page")
@NoArgsConstructor
@AllArgsConstructor
public class HomePage {

    /** 自增长ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 用户ID
    private Long userId;
    // 角色ID，用于后续权限判定
    private String roleIds;
    // 大屏页面序号，用于排序和展示
    private int pageIndex;
    // 大屏页面名称
    private String name;
    // 是否激活，即是否参与轮播
    private boolean visible = true;
    // 最后更新时间
    private Date lastUpdateTime;
    // 页面视图配置
    @Column(columnDefinition = "TEXT")
    private String viewConf;
    // 大屏画布
    @Column(columnDefinition = "TEXT")
    private String paintObj;
    // 组合功能
    @Column(columnDefinition = "TEXT")
    private String composeObj;


    /**
     * 主页展示内容的缩略图,存放图片的64位编码的字符串
     */
    @Column(columnDefinition = "TEXT")
    private String viewImage;

    public HomePage(Long id, Long userId, String roleIds, int pageIndex, String name, boolean visible,
                    Date lastUpdateTime, String viewImage) {
        this.id = id;
        this.userId = userId;
        this.roleIds = roleIds;
        this.pageIndex = pageIndex;
        this.name = name;
        this.visible = visible;
        this.lastUpdateTime = lastUpdateTime;
        this.viewImage = viewImage;
    }

    public HomePage(Long id, Long userId, String roleIds, int pageIndex, String name, boolean visible,
                    Date lastUpdateTime) {
        this.id = id;
        this.userId = userId;
        this.roleIds = roleIds;
        this.pageIndex = pageIndex;
        this.name = name;
        this.visible = visible;
        this.lastUpdateTime = lastUpdateTime;
    }

}
