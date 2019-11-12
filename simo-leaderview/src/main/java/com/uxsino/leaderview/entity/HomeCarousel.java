package com.uxsino.leaderview.entity;

/**
 * 大屏展示轮播配置
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.uxsino.leaderview.model.RefreshType;

import lombok.Data;

@Entity
@Data
@Table(name = "simo_mc_home_carousel")
public class HomeCarousel {

    /** 自增长ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 轮播间隔时间
     */
    private String intervalTime;

    /**
     * 切换效果
     */
    private String specialEffects;

    /**
     * 数据刷新方式
     */
    private RefreshType refreshType;

    /**
     * 数据刷新周期
     */
    private String refreshTime;

    /**
     * 作用用户
     */
    private Long userId;
}
