package com.uxsino.leaderview.model.asset;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * 树结构资产查询，资产类型或库房结构
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "树结构资产查询参数")
public class AssetTreeVo extends Criteria<AssetInstance> {


	public AssetTreeVo() {
		super(AssetInstance.class);
	}
}
