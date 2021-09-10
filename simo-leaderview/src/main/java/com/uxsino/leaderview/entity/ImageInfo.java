package com.uxsino.leaderview.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "simo_mc_img_info")
public class ImageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String format;//图片格式

    private String path;//图片路径

    @Column(columnDefinition = "int default 1")
    private Integer status = 1;//状态-1 删除，1正常

}
