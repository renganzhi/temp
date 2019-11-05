package com.uxsino.leaderview.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "simo_mc_home_page_vo")
public class HomePageVo {
    /** 自增长ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pageId;
    private int pageIndex;
    private Long userId;
    private boolean visible;
    private String name;
    private Date lastUpdateTime;

    public HomePageVo() {
    }

    public HomePageVo(Long id, Long pageId, int pageIndex, Long userId, boolean visible, String name, Date lastUpdateTime) {
        this.id = id;
        this.pageId = pageId;
        this.pageIndex = pageIndex;
        this.userId = userId;
        this.visible = visible;
        this.name = name;
        this.lastUpdateTime = lastUpdateTime;
    }
}
