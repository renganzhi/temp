package com.uxsino.leaderview.service.wuhou;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.model.JsonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;

import static com.uxsino.leaderview.service.wuhou.WuHouService.getPieResult;

/**
 * 智慧武侯16:3 融合指挥模块
 */
@Service
@Slf4j
public class RHZHService {
    @Autowired
    WuHouService wuHouService;

    /**
     * 1、领导班子通讯录
     * 接口URL： /apis/daas/pro/3/components/y31-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getRH1(){

        String res = null;
        try {
            res = wuHouService.getData("y31-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("姓名","name");
        map.put("职务","job_title");
        map.put("座机","landline_number");
        map.put("手机","phone");

        JSONObject result = getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     *
     * 接口URL： /apis/daas/pro/3/components/y19-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getRH(){

        String res = null;
        try {
            res = wuHouService.getData("y16-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("","");
        map.put("","");

        JSONObject result = getPieResult(map,data);

        return new JsonModel(true,result);

    }



}
