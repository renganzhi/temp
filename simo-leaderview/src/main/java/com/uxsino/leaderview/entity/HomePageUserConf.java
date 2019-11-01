package com.uxsino.leaderview.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 每个用户的页面配置：可见性，展示排序
 */
@Entity
@Data
@Table(name = "simo_mc_home_page_user_conf")
public class HomePageUserConf {
    /** 自增长ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //用户id
    private Long userId;
    //页面id
    private Long pageId;
    //可见性
    private String visible;
    // 大屏页面序号，用于排序和展示
    private int pageIndex;

}
