package com.uxsino.leaderview.model.asset;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

/**
 * 资产提醒类型
 *
 */
@Getter
public enum RemindTypeEnum {
                            REPERTORY("REPERTORY","库存不足"),
                            EXPIRED("EXPIRED","许可过期"),
	                        MAINTENANCEOVER("MAINTENANCEOVER","维保到期");
//                            INSTALL("许可分配提醒","剩余分配数"),
//                            MAINTENANCE("过保提醒","剩余维保天数");

    private String value;// 类型名称

    private String content;// 提示信息

    private RemindTypeEnum(String value, String content) {
        this.value = value;
        this.content = content;
    }

    public String getValue() {
        return this.value;
    }

    public String getContent() {
        return this.content;
    }

    public static JSONArray toJson() {
        JSONArray array = new JSONArray();
        for (RemindTypeEnum status : RemindTypeEnum.values()) {
            JSONObject json = new JSONObject();
            json.put("name", status.toString());
            json.put("value", status.getValue());
            json.put("content", status.getContent());
            array.add(json);
        }
        return array;
    }
}
