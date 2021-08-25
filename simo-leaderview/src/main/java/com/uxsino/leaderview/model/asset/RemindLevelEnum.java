package com.uxsino.leaderview.model.asset;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

/**
 * 资产提醒等级
 *
 */
@Getter
public enum RemindLevelEnum {
             ONELEVEL(1,"一级--一般"),
             SECONDLEVEL(2,"二级--严重"),
             THREELEVEL(3,"三级--紧急");

    private Integer level;

    private String value;

    private RemindLevelEnum(Integer level, String value) {
        this.level = level;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public Integer getLevel() {
        return this.level;
    }

    public static JSONArray toJson() {
        JSONArray array = new JSONArray();
        for (RemindLevelEnum status : RemindLevelEnum.values()) {
            JSONObject json = new JSONObject();
            json.put("name", status.toString());
            json.put("value", status.getValue());
            json.put("level", status.getLevel());
            array.add(json);
        }
        return array;
    }
}
