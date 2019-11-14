package com.uxsino.leaderview.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 *
 * @description 保存的视频文件的基本信息
 * @date 2019年11月14日
 */
@Entity
@Table(name = "simo_mc_home_video_file")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoFile {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件扩展名
     */
    private String extension;

    /**
     * 文件地址
     */
    private String filePath;

    /**
     * 上传文件的用户ID
     */
    private Long userId;
}
