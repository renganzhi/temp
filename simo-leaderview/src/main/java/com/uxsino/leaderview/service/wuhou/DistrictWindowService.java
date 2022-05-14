package com.uxsino.leaderview.service.wuhou;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.model.JsonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DistrictWindowService {


    @Autowired
    WuHouService wuHouService;

    /**
     *1、城区人口-
     *接口URL： /apis/daas/pro/3/components/y104-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel GetPopulation1(String param){
        String res = null;
        try {
            res = wuHouService.getData("y104-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        data = JSON.parseArray(JSON.toJSONString(data.stream().filter(obj -> !((JSONObject) obj).getString("street").equals("全区数")).collect(Collectors.toList())));
        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //    "street": "簇锦街道", //所属街道
        //    "population": 162883, //实有人口数
        //    "county_inflow": 27781, //跨县流入
        //    "city_inflow": 53110, //跨市流入
        //    "province_inflow": 16385, //跨省流入
        //    "zlr": 97276 //总流入
        map.put("所属街道","street");
        map.put("实有人口","population");
        map.put("流入人口","zlr");

        result = wuHouService.getPieResult(map,data);
        result.put("unit","人");

        return new JsonModel(true,result);

    }

    /**
     *1、教育资源-街道教师分布柱状图
     *接口URL： /apis/daas/pro/3/components/y144-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel GetEducation1(String param){
        String res = null;
        try {
            res = wuHouService.getData("y144-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        data = JSON.parseArray(JSON.toJSONString(data.stream().filter(obj -> !((JSONObject) obj).getString("street").equals("全区数")).collect(Collectors.toList())));
        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        if(ObjectUtils.isEmpty(param)) {
            map.put("街道","street");
            map.put("教师数","zrjs");

            result = wuHouService.getPieResult(map,data);
            result.put("unit","人");
            String url = "/leaderview/Window/GetEducation1?param=街道:";
            result.put("url",url);
        }else {
            JSONArray targetData = new JSONArray();
            for(int i = 0; i < data.size(); i++){
                JSONObject obj = data.getJSONObject(i);
                if(param.split(":")[1].equals(obj.getString("street"))){
                    targetData = obj.getJSONArray("items");
                }
            }

            //"zxs": "374", //在校生
            //"bxlx": "幼儿园", //办学类型
            //"xxdz": "成都市武侯区南桥四路590号", //学校地址
            //"xxmc": "成都市武侯区第七幼儿园", //学校名称
            //"zrjs": "34", //专任教师
            //"street": "华兴街道", //街道
            map.put("学校地址","xxdz");
            map.put("学校名称","xxmc");
            map.put("办学类型","bxlx");
            map.put("专任教师","zrjs");
            map.put("街道","street");

            result = wuHouService.getPieResult(map,targetData);
            List<String> columns = Arrays.asList("学校名称","专任教师");
            result.put("columns",columns);
        }

        return new JsonModel(true,result);

    }

    /**
     *2、教育资源-街道学生分布柱状图
     *接口URL： /apis/daas/pro/3/components/y56-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel GetEducation2(String param){
        String res = null;
        try {
            res = wuHouService.getData("y144-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        data = JSON.parseArray(JSON.toJSONString(data.stream().filter(obj -> !((JSONObject) obj).getString("street").equals("全区数")).collect(Collectors.toList())));
        JSONObject result = new JSONObject();

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if(ObjectUtils.isEmpty(param)) {
            map.put("街道","street");
            map.put("学生数","zxs");

            result = wuHouService.getPieResult(map,data);
            result.put("unit","人");
            String url = "/leaderview/Window/GetEducation2?param=街道:";
            result.put("url",url);
        }else {
            JSONArray targetData = new JSONArray();
            for(int i = 0; i < data.size(); i++){
                JSONObject obj = data.getJSONObject(i);
                if(param.split(":")[1].equals(obj.getString("street"))){
                    targetData = obj.getJSONArray("items");
                }
            }

            map.put("学校地址","xxdz");
            map.put("学校名称","xxmc");
            map.put("办学类型","bxlx");
            map.put("在校生","zxs");
            map.put("街道","street");

            result = wuHouService.getPieResult(map,targetData);
            List<String> columns = Arrays.asList("学校名称","在校生");
            result.put("columns",columns);
        }

        return new JsonModel(true,result);

    }

    /**
     *、教育资源-
     *接口URL： /apis/daas/pro/3/components/y56-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel GetEducation3(String param){
        String res = null;
        try {
            res = wuHouService.getData("y145-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        data = JSON.parseArray(JSON.toJSONString(data.stream().filter(obj -> !((JSONObject) obj).getString("street").equals("全区数")).collect(Collectors.toList())));
        JSONObject result = new JSONObject();

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if(ObjectUtils.isEmpty(param)) {
            //"street": "华兴街道", //街道
            //"cnt": 8, //数量
            map.put("街道","street");
            map.put("学校数","cnt");

            result = wuHouService.getPieResult(map,data);
            result.put("unit","所");
            String url = "/leaderview/Window/GetEducation3?param=街道:";
            result.put("url",url);
        }else {
            JSONArray targetData = new JSONArray();
            //整个街道的对象
            JSONObject Street = new JSONObject();
            //街道包含的所有类型学校的数组
            JSONArray allSchool = new JSONArray();
            //某一类型所有学校的对象
            JSONObject oneTypeSchool = new JSONObject();
            //包含某一类型所有学校的数组
            JSONArray schools = new JSONArray();
            //一所学校的对象
            JSONObject school = new JSONObject();

            for(int i = 0; i < data.size(); i++){
                Street = data.getJSONObject(i);
                if(param.split(":")[1].equals(Street.getString("street"))){
                    allSchool = Street.getJSONArray("items");
                    for(int j = 0; j < allSchool.size(); j++){
                        oneTypeSchool = allSchool.getJSONObject(j);
                        schools = oneTypeSchool.getJSONArray("items");
                        for (int k = 0 ;k < schools.size(); k++){
                            school = schools.getJSONObject(k);
                            //需要封装到结果的对象
                            JSONObject targetObj = new JSONObject();
                            targetObj.put("xxmc",school.getString("xxmc"));
                            targetObj.put("bxlx",oneTypeSchool.getString("bxlx"));
                            targetObj.put("street",Street.getString("street"));
                            targetData.add(targetObj);
                        }
                    }
                }
            }

//            map.put("学校地址","xxdz");
            map.put("学校名称","xxmc");
            map.put("办学类型","bxlx");
//            map.put("在校生","zxs");
            map.put("街道","street");

            result = wuHouService.getPieResult(map,targetData);
//            List<String> columns = Arrays.asList("学校名称","办学类型");
//            result.put("columns",columns);
        }

        return new JsonModel(true,result);

    }

    /**
     *、教育资源-
     *接口URL： /apis/daas/pro/3/components/y56-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel GetEducation4(String param){
        String res = null;
        try {
            res = wuHouService.getData("y104-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        data = JSON.parseArray(JSON.toJSONString(data.stream().filter(obj -> !((JSONObject) obj).getString("street").equals("全区数")).collect(Collectors.toList())));
        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("","");
        map.put("","");
        map.put("","");

        result = wuHouService.getPieResult(map,data);
        result.put("unit","");

        return new JsonModel(true,result);

    }

    /**
     *、教育资源-
     *接口URL： /apis/daas/pro/3/components/y56-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel GetEducation5(String param){
        String res = null;
        try {
            res = wuHouService.getData("y104-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        data = JSON.parseArray(JSON.toJSONString(data.stream().filter(obj -> !((JSONObject) obj).getString("street").equals("全区数")).collect(Collectors.toList())));
        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("","");
        map.put("","");
        map.put("","");

        result = wuHouService.getPieResult(map,data);
        result.put("unit","");

        return new JsonModel(true,result);

    }

}
