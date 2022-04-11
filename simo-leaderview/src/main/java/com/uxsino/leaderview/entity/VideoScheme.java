package com.uxsino.leaderview.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 视频融合方案表
 */
@Entity
@Table(name = "simo_leaderview_video_scheme")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoScheme {

    /** 自增长ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 方案名称
     */
    private String name;

    /**
     * 视频融合方案内容
     */
    @Column(columnDefinition = "TEXT")
    private String videoArry;
}
