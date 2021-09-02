package com.uxsino.leaderview.model.asset;

import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;

/**
 * 自定义字段的可选字段类型
 * @author cuixy
 *
 */
public enum FieldType {
	Text("Text", "文本"), // 文本
	Date("Date", "时间"), // 时间
	Staff("Staff", "人员"), // 人员
	File("File", "文件"), // 文件
	Options("Options", "选项"),
	Depart("Depart", "部门"),
	Table("Table", "表格");
	
	private String type;
	
	private String desc;
	
	private FieldType(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public static FieldType fromType(String type) {
		for (FieldType types : FieldType.values()) {
			if(types.type == type) {
				return types;
			}
		}
		throw new IllegalArgumentException("无此枚举项:" + type);
	}
	
    public static FieldType getFieldType(String type) {
        for (FieldType types : FieldType.values()) {
            if (type.equals(types.getType())) {
                return types;
            }
        }
        return null;
    }
    public static JSONObject getJsonObj() {
    	JSONObject lan=new JSONObject();
    	FieldType[] names=values();
        for (FieldType types : FieldType.values()) {
        	lan.put(types.type, types.desc);
        }
        return lan;
    }
    
    public static FieldType fromText(String str) {
        return Arrays.stream(FieldType.values())
                .filter(member -> member.toString().equalsIgnoreCase(str))
                .findFirst()
                .orElse(null);
    }

	
	public String getType() {
        return this.type;
    }
	
	public String getDesc() {
        return this.desc;
    }
}
