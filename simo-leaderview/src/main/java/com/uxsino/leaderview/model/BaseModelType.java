package com.uxsino.leaderview.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 模型基本分类（包含所有模型）
 */
public enum BaseModelType {

    room(1,"机房"),
    draw(2,"绘制"),
    general(3,"通用");
    //pe(4,"动环");
    
    private Integer value;
    private String text;

    /**
     * 创建一个 BaseModelType 对象实例.
     */
    private BaseModelType(Integer value,String text) {
        this.text = text;
        this.value = value;
    }

    /**
     *
     * 由枚举值生成枚举对象
     *
     * @param text
     * @return
     */
    public static BaseModelType fromText(String text) {
        for (BaseModelType baseClass : BaseModelType.values()) {
            if (baseClass.text.equals(text)) {
                return baseClass;
            }
        }
        throw new IllegalArgumentException("无法识别的类型：" + text);
    }

    /**
     *
     * 取得枚举对象的名称
     *
     * @return
     */

    public String getText() {
        return this.text;
    }

    public Integer getValue() {
        return this.value;
    }

    public List<ModelType> getModelType() {
        List<ModelType> list = new ArrayList<>();
        for (ModelType ModelType : ModelType.values()) {
            if (ModelType.getBaseType().equals(this)) {
                list.add(ModelType);
            }
        }
        return list;
    }

    public static boolean contains(String str) {
        for (BaseModelType baseClass : BaseModelType.values()) {
            if (baseClass.name().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据模型类型枚举构造json数据结构
     * @return
     */
    public static JSONObject createJsonObjectStructure(){
        JSONObject ret = new JSONObject();
        for (BaseModelType baseModelType : BaseModelType.values()) {
            ret.put(baseModelType.name(),new JSONArray());
        }
        return ret;
    }
}
