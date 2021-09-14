package com.uxsino.leaderview.model;

import com.uxsino.commons.db.JsonObjectUserType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;

@Data
@TypeDef(name = "jsonb", typeClass = JsonObjectUserType.class)
public class ThreeDModelBaseInfoModel{

    private Long id;

    private String name;


    private String modelType;

    //obj文件路径：除去配置路径剩余的部分
    @Column(columnDefinition = "text")
    private String objPath;

    //mtl：除去配置路径剩余的部分
    @Column(columnDefinition = "text")
    private String mtlPath;

    private MultipartFile file;//接收模型文件

    private String cardImgPath;//接收缩略图

    private String userData;
}
