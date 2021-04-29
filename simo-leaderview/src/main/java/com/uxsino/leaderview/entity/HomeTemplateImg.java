package com.uxsino.leaderview.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 
 * @description 模板中的图片文件
 * @date 2019年8月9日
 */
@Entity
@Table(name = "simo_mc_home_template_img")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeTemplateImg {
    /**
     * 模板的图片ID，固定ID的方式，不会自增长
     */
    @Id
    @Column(name = "id")
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
    * 文件内容
    */
    private byte[] fileStream;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "compressed_id", referencedColumnName = "id")
    private HomeTemplateImgCompressed homeTemplateImgCompressed;

}
