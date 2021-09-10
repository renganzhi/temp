package com.uxsino.leaderview.entity;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.db.JsonObjectUserType;
import com.uxsino.commons.db.base.BaseEntity;
import com.uxsino.commons.utils.StringUtils;
import com.uxsino.leaderview.model.ModelType;
import com.uxsino.leaderview.model.ThreeDModelBaseInfoModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@Data
@Table(name = "simo_leaderview_model_base_info")
@TypeDef(name = "jsonb", typeClass = JsonObjectUserType.class)
@AllArgsConstructor
@NoArgsConstructor
public class ThreeDModelBaseInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    private String modelType;

    //缩略图路径
    @Column(columnDefinition = "text")
    private String cardImg;

    //obj文件路径：除去配置路径剩余的部分
    @Column(columnDefinition = "text")
    private String objPath;

    //mtl：除去配置路径剩余的部分
    @Column(columnDefinition = "text")
    private String mtlPath;

    //@ApiModelProperty(value = "容量配置", hidden = true)
    @Column(columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private JSONObject userData;

    @Column(columnDefinition = "text default 'custom'")
    private String source = "custom";//模型来源default/custom


    public ThreeDModelBaseInfo(ThreeDModelBaseInfoModel model){
        this.id = model.getId();
        this.name = model.getName();
        this.modelType = model.getModelType();
        this.objPath = model.getObjPath();
        this.mtlPath = model.getMtlPath();
        if (!StringUtils.isEmpty(model.getUserData())){
            this.userData = JSONObject.parseObject(model.getUserData());
        }
    }


    /**
     * 模型数据合法性检查
     * @return
     */
    public Boolean validityCheck(){
        //Assert.isTrue(!StringUtils.isEmpty(this.cardImg),"缩略图为空");
        Assert.isTrue(!StringUtils.isEmpty(this.name),"名称为空");
        Assert.isTrue(!StringUtils.isEmpty(this.mtlPath),"找不到gltf文件路径，请检查模型文件");
        Assert.isTrue(!StringUtils.isEmpty(this.objPath),"找不到bin文件路径，请检查模型文件");
        //Assert.isTrue(!StringUtils.isEmpty(this.modelType),"模型类型为空");
        //Assert.isTrue(ModelType.contains(this.modelType),"无效模型类型参数");
        return true;
    }

}
