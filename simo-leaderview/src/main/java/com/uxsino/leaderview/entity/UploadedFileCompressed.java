package com.uxsino.leaderview.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 *
 * @description 上传的文件
 * @date 2019年8月5日
 */
@Entity
@Table(name = "simo_uploaded_file_compressed")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadedFileCompressed {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private byte[] compressedFileStream;
}
