package com.uxsino.leaderview.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @description 上传的文件
 * @date 2019年8月5日
 */
@Entity
@Table(name = "simo_uploaded_file")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadedFile {
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
    * 文件内容
    */
    private byte[] fileStream;

    /**
     * 上传文件的用户ID
     */
    private Long userId;

}
