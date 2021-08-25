package com.uxsino.leaderview.model.asset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCond {
	/**
	 * 对应实体字段名字
	 */
	@ApiModelProperty(value="查询的条件的字段",name="fieldKey",required=false)
	private String fieldKey;
	/**
	 * 查询条件
	 */
	@ApiModelProperty(value="查询的字段的判断条件,如eq,lt",name="cond",required=false)
	private String cond;
	/**
	 * 条件值
	 */
	@ApiModelProperty(value="查询字段的值",name="fieldValue",required=false)
	private Object fieldValue;

}
