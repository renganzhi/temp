package com.uxsino.leaderview.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 主页模板信息
 */
@Entity
@Data
@Table(name = "simo_mc_home_template")
@NoArgsConstructor
@AllArgsConstructor
public class HomeTemplate {

    /** 自增长ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 角色ID，用于后续权限判定 */
    private String roleIds;

    /** 模板名称 */
    private String name;

    /** 最后更新时间 */
    private Date lastUpdateTime;

    /** 页面视图配置 */
    @Column(columnDefinition = "TEXT")
    private String viewConf;

    /** 模板展示内容的缩略图,存放图片的64位编码的字符串 */
    @Column(columnDefinition = "TEXT")
    private String viewImage;

    /** 画布配置 */
    @Column(columnDefinition = "TEXT")
    private String paintObj;

    public HomeTemplate(Long id, String name, String viewImage, Date lastUpdateTime) {
        this.id = id;
        this.name = name;
        this.viewImage = viewImage;
        this.lastUpdateTime = lastUpdateTime;
    }

}
