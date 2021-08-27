package com.uxsino.leaderview.model.asset;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.db.JsonObjectUserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

/**
 * 
 * 资产实例
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonObjectUserType.class)
public class AssetInstance extends BaseEntity {
	
	private static final long serialVersionUID = -2041235894293550977L;
	/**
	 * 状态
	 */
	@Enumerated(EnumType.STRING)
	private AssetStatusEnum status;
	/**
	 * 资产编号
	 */
	private String assetNo;
	
	/**
	 * 资产名称
	 */
	private String assetName;
	
	/**
	 * 资产分类
	 */
	private String category;
	/**
	 * 规格型号
	 */
	private String specification;
	/**
	 * 资产模型ID
	 */
	private String templateId;
	/**
	 * 资产属性
	 */
	@Type(type = "jsonb")
	private JSONObject properties;
	
	/**
	 * 资产属性中文名称
	 */
	@Type(type = "jsonb")
	private JSONObject propertiesCh;
	/**
	 * 库房
	 */
	private String warehouseId;		
	
	/**
	 * 扩展字段，用于策略批量检索 存储数据 模型id+规格型号
	 */
	private String extend_1;

	/*
	 * 图片上传id
	 */
	private String fileId;
	
	/**
	 * 最近一次台账操作id
	 */
	private String lastStandingBookId;

    /**
     * 入库时间
     */
    private Date storageDate;

	/**
	 * 是否已逻辑删除，默认0正常，1逻辑删除
	 */
	@Column(columnDefinition = "int default 0")
	private int isLogicalDelete;

	/**
	 * 选择项使用情况
	 */
	@Transient
	private JSONObject usedIndex;

}
