package com.uxsino.leaderview.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class HcnetPoint {

    //摄像头名称
    String name;

    //设备编号
    String deviceIndexCode;

    //经度
    String longitude;

    //维度
    String latitude;

    public static HcnetPoint JsonObjectToPoint(JSONObject jsonObject){

        HcnetPoint hcnetPoint = new HcnetPoint();

        hcnetPoint.setName(jsonObject.getString("name"));
        hcnetPoint.setDeviceIndexCode(jsonObject.getString("cameraIndexCode"));
        hcnetPoint.setLongitude(jsonObject.getString("longitude"));
        hcnetPoint.setLatitude(jsonObject.getString("latitude"));

        return hcnetPoint;
    }
}
