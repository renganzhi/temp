package com.uxsino.leaderview.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @description 模板中压缩后的图片文件(用于小卡片预览和鹰眼)
 * @date 2019年8月9日
 */
@Entity
@Table(name = "simo_mc_home_template_img_compressed")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeTemplateImgCompressed {
    /**
     * 模板的图片ID，固定ID的方式，与HomeTemplateImg表对应图片保持一致
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 文件内容，这里储存的是被压缩后的图片内容
     */
    private byte[] compressedFileStream;
}
