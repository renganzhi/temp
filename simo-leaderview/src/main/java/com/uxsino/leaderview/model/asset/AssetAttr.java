package com.uxsino.leaderview.model.asset;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@ApiModel(value="AssetAttr",description="资产属性对象")
@EqualsAndHashCode(callSuper = false)
public class AssetAttr {
	@ApiModelProperty(value = "属性ID")
	@Id
	@Column(unique=true, nullable=false)
	private    String       id;                  //属性表ID
	//private    Integer      groupNum;            //分组编号
	//private    String       groupName;           //分组名   
	@ApiModelProperty(value = "字段名称")
	private    String       fieldName;           //字段名称
	@ApiModelProperty(value = "字段标识")
	@Column(unique=true, nullable=false)
	private    String       fieldKey;         //字段标识
	@Type(type = "jsonb")
	private    JSONObject    fieldDefinition;           //字段类型定义
	@ApiModelProperty(value = "字段大小")
	private    Integer         fieldSize;           //字段大小
	//@Column(nullable=false)
	//private    Integer      groupSortNum;        //组内排序号
	@ApiModelProperty(value = "字段类型")
	private    String       fieldType;          //显示设置
	@ApiModelProperty(value = "是否必须")
	private    Boolean      isNeces=false;       //是否必须
	@ApiModelProperty(value = "系统预设")
	private    Boolean      isPreinstall;        // 系统预设
	@ApiModelProperty(value = "是否删除 0正常，-1删除")
	private    Integer      isDelete=0;          //是否删除 0正常，-1删除
	@ApiModelProperty(value = "字段类型定义jsonb")
	@Transient
	@Column(columnDefinition="TEXT")
	private    String        fieldDefinitionstr;   
	@ApiModelProperty(value = "是否可编辑,默认true 可编辑")
	private    Boolean      editable=true; 
	@ApiModelProperty(value = "是否可删除,默认true 可删除")
	private    Boolean      cutable=true; 
	private    String        alias;
}
