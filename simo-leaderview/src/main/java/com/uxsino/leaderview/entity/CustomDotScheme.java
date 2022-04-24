package com.uxsino.leaderview.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 视频融合方案表
 */
@Entity
@Table(name = "simo_leaderview_custom_dot_scheme")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomDotScheme {

    /** 自增长ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     */
    private String name;

    /**
     * 详情
     */
    @Column(columnDefinition = "TEXT")
    private String details;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;


}
