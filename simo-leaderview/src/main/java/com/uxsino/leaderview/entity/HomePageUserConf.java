package com.uxsino.leaderview.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 每个用户的页面配置：可见性，展示排序
 */
@Entity
@Data
@Table(name = "simo_mc_home_page_user_conf")
public class HomePageUserConf {
    //用户id
    private Long userId;
    //页面id
    private Long pageId;
    //可见性
    private boolean visible = false;
    // 大屏页面序号，用于排序和展示
    private int pageIndex;

    public HomePageUserConf(Long userId, Long pageId, boolean visible, int pageIndex) {
        this.userId = userId;
        this.pageId = pageId;
        this.visible = visible;
        this.pageIndex = pageIndex;
    }

}
