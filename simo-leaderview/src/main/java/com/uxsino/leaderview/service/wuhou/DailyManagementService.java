package com.uxsino.leaderview.service.wuhou;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.model.JsonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.LinkedHashMap;

import static com.uxsino.leaderview.service.wuhou.WuHouService.getPieResult;

/**
 * 智慧武侯16:3 日常管理模块（首页）
 */
@Service
@Slf4j
public class DailyManagementService {

    @Autowired
    WuHouService wuHouService;

    /**
     *1、日常管理——一网通办
     *接口URL： /apis/daas/pro/3/components/y14-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getRC1(){

        String res = null;
        try {
            res = wuHouService.getData("y14-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("每日受理（件）","daily_acceptance");
        map.put("网上受理（件）","online_acceptance");
        map.put("当日办理（件）","handle_today");
        map.put("零跑动（项）","zero_run");
        map.put("跑一次（项）","run_once");
        map.put("一次以上（项）","more_than_once");
        JSONObject result = getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     *2、区领导值班情况
     * 接口URL： /apis/daas/pro/3/components/y15-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getRC2(){

        String res = null;
        try {
            res = wuHouService.getData("y15-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        if(ObjectUtils.isEmpty(data)){
            log.error("中台接口返回为空");
            return new JsonModel(true, "中台接口返回数据为空", new JSONObject());
        }
        JSONObject obj = data.getJSONObject(0);
        obj.put("type","区领导");

        JSONObject result = new JSONObject();
        result.put("dataArray",data);

        return new JsonModel(true,result);

    }

    /**
     *22、日常管理-区值班领导姓名
     * 接口URL： /apis/daas/pro/3/components/y15-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getRC22(String formId){

        String res = null;
        try {
            res = wuHouService.getData(formId,"per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(true,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        if(ObjectUtils.isEmpty(data)){
            return new JsonModel(true, "中台接口返回数据为空", new JSONObject());
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("姓名","name");
        JSONObject result = getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     *3、日常管理——疫情监控
     * 接口URL： /apis/daas/pro/3/components/y16-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getRC3(){

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
        map.put("第一剂接种人数（剂）","dyjjzrs");
        //map.put("图片","photo");
        map.put("第二剂接种人数（剂）","dejjzrs");
        map.put("第三剂接种人数（剂）","dsjjzrs");
        map.put("日期时间","date");
        JSONObject result = getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     *4、日常管理——两地四区指数
     * 接口URL： /apis/daas/pro/3/components/y17-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getRC4(){

        String res = null;
        try {
            res = wuHouService.getData("y17-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("AAAA级旅游景区（个）","lyjq");
        map.put("博物馆数量（个）","bwgsl");
        map.put("中小学幼儿园（个）","zxxyey");
        map.put("卫生机构（个）","wsjg");
        map.put("国家级重点文物保护单位（个）","gjjzdwwbhdw");
        map.put("在建工地（个）","zjgd");

        JSONObject result = getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     *5、日常管理——城区事件
     * 接口URL： /apis/daas/pro/3/components/y18-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getRC5(){

        String res = null;
        try {
            res = wuHouService.getData("y18-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("state","EVENTSTATUS");
        map.put("name","ANSWER_DATE");
        map.put("value","SJDDL");

        JSONObject jsonObject = new JSONObject();

        jsonObject = getPieResult(map,data);
        JSONObject result = new JSONObject();
        JSONArray rows = jsonObject.getJSONArray("rows");
        JSONArray rowsArray = new JSONArray();
        rowsArray.add(rows);
        result.put("dataArray",rowsArray);

        return new JsonModel(true,result);

    }

    /**
     *6、日常管理——城区人口
     * 接口URL： /apis/daas/pro/3/components/y19-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getRC6(){

        String res = null;
        try {
            res = wuHouService.getData("y19-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("name","type");
        map.put("value","total");
        map.put("unit","unit");

        JSONObject jsonObject = new JSONObject();

        jsonObject = getPieResult(map,data);
        JSONObject result = new JSONObject();
        JSONArray rows = jsonObject.getJSONArray("rows");
        JSONArray row12 = new JSONArray();
        JSONArray row34 = new JSONArray();
        row12.add(rows.get(0));
        row12.add(rows.get(1));
        row34.add(rows.get(2));
        row34.add(rows.get(3));

        JSONArray rowsArray = new JSONArray();
        rowsArray.add(row12);
        rowsArray.add(row34);
        result.put("dataArray",rowsArray);

        return new JsonModel(true,result);

    }

    /**
     *
     * 接口URL： /apis/daas/pro/3/components/y19-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getRC7(){

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

    /**
     *
     * 接口URL： /apis/daas/pro/3/components/y19-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getRC8(){

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
