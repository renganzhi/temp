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
    // 大屏画布
    @Column(columnDefinition = "TEXT")
    private String paintObj;
    // 组合功能
    @Column(columnDefinition = "TEXT")
    private String composeObj;
    // 页面视图配置
    @Column(columnDefinition = "TEXT")
    private String viewConf;

    /*模板类型,分为自定义模板和单资源模板
     * custom为空或者null：自定义模板
     * single:单资源模板*/
    private String templateType;

    /*当模板为单资源模板时的资源类型，包含baseneclass、neclass,自定义模板时为空
     * {"baseneclss":"xx","neclass":"xx"}*/
    @Column(columnDefinition = "TEXT")
    private String templateConf;

    // 创建人ID
    private Long createUserId;
    // 接收人ID
    private Long handoverId;
    // 分享人名字，用于大屏显示，数据库中不存放
    private String shareName;
    // 分享配置 格式如{"roles":[1,2,3],"uids":[8,9,11]}
    @Column(columnDefinition = "TEXT")
    private String shareConf;
    // 是否为当前登录用户的页面，用于大屏显示，数据库中不存放
    private String belongCurrentUser;

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

    public HomePage(Long id, Long userId, String roleIds, int pageIndex, String name, boolean visible,
                    Date lastUpdateTime, String viewImage, Long createUserId, Long handoverId, String shareConf) {
        this.id = id;
        this.userId = userId;
        this.roleIds = roleIds;
        this.pageIndex = pageIndex;
        this.name = name;
        this.visible = visible;
        this.lastUpdateTime = lastUpdateTime;
        this.viewImage = viewImage;
        this.createUserId = createUserId;
        this.handoverId = handoverId;
        this.shareConf = shareConf;
    }

    public HomePage(Long userId, String roleIds, int pageIndex, String name, boolean visible, Date lastUpdateTime,
                    String paintObj, String composeObj, String viewConf, Long createUserId, Long handoverId,
                    String shareName, String shareConf, String belongCurrentUser, String viewImage) {
        this.userId = userId;
        this.roleIds = roleIds;
        this.pageIndex = pageIndex;
        this.name = name;
        this.visible = visible;
        this.lastUpdateTime = lastUpdateTime;
        this.paintObj = paintObj;
        this.composeObj = composeObj;
        this.viewConf = viewConf;
        this.createUserId = createUserId;
        this.handoverId = handoverId;
        this.shareName = shareName;
        this.shareConf = shareConf;
        this.belongCurrentUser = belongCurrentUser;
        this.viewImage = viewImage;
    }

    public HomePage(Long id, String composeObj, Date lastUpdateTime, String name, int pageIndex, String paintObj, String viewConf, boolean visible,  Long createUserId, Long handoverId, String shareConf) {
        this.id = id;
        this.pageIndex = pageIndex;
        this.name = name;
        this.visible = visible;
        this.lastUpdateTime = lastUpdateTime;
        this.paintObj = paintObj;
        this.composeObj = composeObj;
        this.viewConf = viewConf;
        this.createUserId = createUserId;
        this.handoverId = handoverId;
        this.shareConf = shareConf;
    }
}
