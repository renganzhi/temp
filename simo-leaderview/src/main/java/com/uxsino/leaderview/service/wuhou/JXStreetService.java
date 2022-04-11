package com.uxsino.leaderview.service.wuhou;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.*;

import static com.uxsino.leaderview.service.wuhou.WuHouService.getPieResult;

/**
 * 智慧武侯 —— 浆洗街街道大屏
 */
@Service
@Slf4j
public class JXStreetService {

    @Autowired
    WuHouService wuHouService;

    /**
     *1、浆洗街——区情——浆洗街区域概况
     *接口URL： /apis/daas/pro/3/components/y20-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJX1(String year){

        String res = null;
        try {
            res = wuHouService.getData("y20-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONArray needDataArray = new JSONArray();
        //年份下拉列表
        List<Map<String, String>> list = new ArrayList<>();

        if(ObjectUtils.isEmpty(data)){
            log.error("中台接口返回为空");
            return new JsonModel(true, new JSONObject());
        }
        for(int i = 0; i < data.size(); i++){
            JSONObject obj = data.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            map.put("name",obj.getString("year"));
            map.put("value",obj.getString("year"));
            list.add(map);

            if(obj.getString("year").equals(year)){
                needDataArray.add(obj);
            }
        }

        if(Strings.isNullOrEmpty(year)){
            return new JsonModel(true,list);
        }else {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("数据年份","year");
            map.put("土地面积（平方千米）","land_area");
            map.put("常住人口（万人）","permanent_residents");
            map.put("户籍人口（万人）","household_population");
            map.put("辖区绿化面积（平方米）","green_area");
            map.put("全年空气质量优良天数（天）","air_quality");
            map.put("驻区单位（所）","resident_unit");
            map.put("介绍文案","introductory_copy");
            JSONObject result = getPieResult(map,needDataArray);
            return new JsonModel(true,result);
        }

    }

    /**
     *2、浆洗街——经济——领导走访情况
     *接口URL： /apis/daas/pro/3/components/y21-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJX2(){

        String res = null;
        try {
            res = wuHouService.getData("y21-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("日期","visit_date");
        map.put("企业名称","company_name");
        map.put("走访领导","Visit_leader");
        map.put("详情","details");

        JSONObject result = getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     *3、浆洗街——经济——3种税收历史
     *接口URL： /apis/daas/pro/3/components/y27-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJX3(){

        String res = null;
        try {
            res = wuHouService.getData("y27-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("月份","month");
        map.put("全口径税收（万元）","full_bore_tax");
        map.put("一般公共预算（万元）","general_public_budget");
        map.put("地方税收（万元）","local_tax");
        JSONObject result = getPieResult(map,data);
        return new JsonModel(true,result);

    }

    /**
     *4、浆洗街——经济——3种税收全年总和
     *接口URL： /apis/daas/pro/3/components/y28-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJX4(String year){

        String res = null;
        try {
            res = wuHouService.getData("y28-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONArray needDataArray = new JSONArray();
        //年份下拉列表
        List<Map<String, String>> list = new ArrayList<>();

        if(ObjectUtils.isEmpty(data)){
            log.error("中台接口返回为空");
            return new JsonModel(true, "中台接口返回数据为空", new JSONObject());
        }

        for(int i = 0; i < data.size(); i++){
            JSONObject obj = data.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            map.put("name",obj.getString("year"));
            map.put("value",obj.getString("year"));
            list.add(map);

            if(obj.getString("year").equals(year)){
                needDataArray.add(obj);
            }
        }

        if(Strings.isNullOrEmpty(year)){
                return new JsonModel(true,list);
        }else {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("全口径税收全年总和（万元）","full_bore_tax");
            map.put("一般公共预算全年总和（万元）","general_public_budget");
            map.put("地方税收全年总和（万元）","local_tax");
            JSONObject result = getPieResult(map,needDataArray);
            return new JsonModel(true,result);
        }

    }

    /**
     *5、浆洗街街道——驻藏机构打点
     *接口URL： /apis/daas/pro/3/components/y22-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJX5(Integer id){

        String res = null;
        try {
            res = wuHouService.getData("y22-01","per_page=10000&page=1",null,false,true);
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

        if(ObjectUtils.isEmpty(id)) {
            return new JsonModel(true, data);
        }else {
            JSONObject result = new JSONObject();
            for(int i = 0;i < data.size(); i++){
                JSONObject obj = data.getJSONObject(i);
                if(id.equals(obj.getInteger("id"))){
                    result = obj;
                }
            }

            return new JsonModel(true,result);
        }

    }

    /**
     *6、浆洗街街道——驻藏小区打点
     *接口URL： /apis/daas/pro/3/components/y23-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJX6(Integer id){

        String res = null;
        try {
            res = wuHouService.getData("y23-01","per_page=10000&page=1",null,false,true);
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

        if(ObjectUtils.isEmpty(id)) {
            return new JsonModel(true, data);
        }else {
            JSONObject result = new JSONObject();
            for(int i = 0;i < data.size(); i++){
                JSONObject obj = data.getJSONObject(i);
                if(id.equals(obj.getInteger("id"))){
                    result = obj;
                }
            }

            return new JsonModel(true,result);
        }
    }

    /**
     *7、浆洗街街道——经济基础数据
     *接口URL： /apis/daas/pro/3/components/y29-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJX7(String year, String type){

        String res = null;
        try {
            res = wuHouService.getData("y29-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONArray needDataArray = new JSONArray();
        //年份下拉列表
        List<Map<String, String>> list = new ArrayList<>();

        if(ObjectUtils.isEmpty(data)){
            log.error("中台接口返回为空");
            return new JsonModel(true, "中台接口返回数据为空", new JSONObject());
        }

        for(int i = 0; i < data.size(); i++){
            JSONObject obj = data.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            map.put("name",obj.getString("year"));
            map.put("value",obj.getString("year"));
            list.add(map);

            if(obj.getString("year").equals(year)){
                needDataArray.add(obj);
            }
        }

        if(Strings.isNullOrEmpty(year)){
            return new JsonModel(true,list);
        }else {

            LinkedHashMap<String, LinkedHashMap<String, String>> typeMap = new LinkedHashMap<>();

            //纳税行业分布——饼图
            LinkedHashMap<String, String> NSHYFBMap = new LinkedHashMap<>();
            //内外资投资——文本
            LinkedHashMap<String, String> NWZTZMap = new LinkedHashMap<>();
            //辖区“四上”企业数量——柱状图
            LinkedHashMap<String, String> XQSSQYMap = new LinkedHashMap<>();
            //企业纳税规模分布——饼图
            LinkedHashMap<String, String> NSGMFBMap = new LinkedHashMap<>();
            //重大项目情况——文本
            LinkedHashMap<String, String> ZDXMQKMap = new LinkedHashMap<>();

            typeMap.put("纳税行业分布",NSHYFBMap);
            typeMap.put("内外资投资",NWZTZMap);
            typeMap.put("辖区“四上”企业数量",XQSSQYMap);
            typeMap.put("企业纳税规模分布",NSGMFBMap);
            typeMap.put("重大项目情况",ZDXMQKMap);
            //"construction_industry": "84070.17", //辖区建筑业企业纳税额（万元）
            //"leasing_business_services": "63238.55", //辖区租赁和商务服务业企业纳税额（万元）
            //"real_estate": "104811.54", //辖区房地产业企业纳税额（万元）
            //"wholesale_retail_trade": "69480.61", //辖区批发和零售业企业纳税额（万元）
            //"other_industry": "172987.76", //辖区其他行业企业纳税额（万元）
            //"funded": null, //内资全年累计认定（万元）
            //"foreign_investment": null, //外资全年累计认定（万美元）
            //"regulated_industry": "3", //辖区规上工业企业数量
            //"qualified_construction_industry": "81", //辖区有资质建筑业企业数量
            //"regulated_service_industry": "48", //辖区规上服务业企业数量
            //"limited_to_commerce": "52", //辖区限上商贸业企业数量
            //"more_than_100_million": "8", //辖区企业纳税规模1亿以上数量
            //"10_million_to_100_million": "77", //辖区企业纳税规模1千万~1亿数量
            //"3_million_to_10_million": "103", //辖区企业纳税规模300万~1千万数量
            //"1_million_to_3_million": "282", //辖区企业纳税规模100万~300万数量
            //"below_1_million": "9753", //辖区企业纳税规模100万以下数量
            //"industry_expert": null, //工业专家特新项目（个）
            //"other_high_energy": null, //其他高能级项目（个）
            //"producer_services": null, //生产性服务业项目（个）
            //"major_industries": null, //重大产业项目（个）
            NSHYFBMap.put("辖区建筑业企业纳税额（万元）","construction_industry");
            NSHYFBMap.put("辖区租赁和商务服务业企业纳税额（万元）","leasing_business_services");
            NSHYFBMap.put("辖区房地产业企业纳税额（万元）","real_estate");
            NSHYFBMap.put("辖区批发和零售业企业纳税额（万元）","wholesale_retail_trade");
            NSHYFBMap.put("辖区其他行业企业纳税额（万元）","other_industry");
            NWZTZMap.put("内资全年累计认定（万元）","funded");
            NWZTZMap.put("外资全年累计认定（万美元）","foreign_investment");
            XQSSQYMap.put("辖区规上工业企业数量","regulated_industry");
            XQSSQYMap.put("辖区有资质建筑业企业数量","qualified_construction_industry");
            XQSSQYMap.put("辖区规上服务业企业数量","regulated_service_industry");
            XQSSQYMap.put("辖区限上商贸业企业数量","limited_to_commerce");
            NSGMFBMap.put("辖区企业纳税规模1亿以上数量","more_than_100_million");
            NSGMFBMap.put("辖区企业纳税规模1千万~1亿数量","10_million_to_100_million");
            NSGMFBMap.put("辖区企业纳税规模300万~1千万数量","3_million_to_10_million");
            NSGMFBMap.put("辖区企业纳税规模100万~300万数量","1_million_to_3_million");
            NSGMFBMap.put("辖区企业纳税规模100万以下数量","below_1_million");
            ZDXMQKMap.put("工业专家特新项目（个）","industry_expert");
            ZDXMQKMap.put("其他高能级项目（个）","other_high_energy");
            ZDXMQKMap.put("生产性服务业项目（个）","producer_services");
            ZDXMQKMap.put("重大产业项目（个）","major_industries");

//            JSONObject result = new JSONObject();
            if("内外资投资".equals(type) || "重大项目情况".equals(type)) {
                return new JsonModel(true,getPieResult(typeMap.get(type), needDataArray));
            }else {
//                result = getOneToPie(columns, data, needList, keyAndNameMap);
                return new JsonModel(true,data);
            }
        }
    }

    /**
     *3、浆洗街——经济——辖区企业总纳税额（万元）
     *接口URL： /apis/daas/pro/3/components/y30-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJX8(){

        String res = null;
        try {
            res = wuHouService.getData("y30-01","per_page=10000&page=1",null,false,true);
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

        JSONObject dataObj = data.getJSONObject(0);
        JSONObject result = new JSONObject();
        result.put("name","辖区企业总纳税额（万元）");
        result.put("info",dataObj.get("sum"));
        result.put("value",dataObj.get("sum"));

        return new JsonModel(true,result);

    }

}
