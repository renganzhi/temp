package com.uxsino.leaderview.service.wuhou;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.dao.ICustomDotSchemeDao;
import com.uxsino.leaderview.entity.CustomDotScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.uxsino.leaderview.service.wuhou.WuHouService.getPieResult;

/**
 * 智慧武侯 —— 智慧社区大屏
 */
@Service
@Slf4j
public class ZHSQService {

    @Autowired
    WuHouService wuHouService;

    @Autowired
    ICustomDotSchemeDao customDotSchemeDao;

    /**
     * 、智慧社区-
     *  接口URL： /apis/daas/pro/3/components/y21-01/data?per_page=100&page=1
     *  请求方式： GET
     *  Content-Type： multipart/form-data
     * @param param 所选人员类型的接口id
     * @return
     */
    public JsonModel getZHSQ1(String param){
        //社区矫正 y01-01
        //吸毒 y02-01
        //刑释 y03-01
        //易肇事肇祸 y04-01
        //重症精神 y05-01
        //重点人员 y06-01

        String res = null;
        try {
            res = wuHouService.getData(param,"per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        result.put("name","总数");
        result.put("value",data.size());
        result.put("info",data.size());

        return new JsonModel(true,result);
    }

    /**
     *1、智慧社区-社区服刑人员街道管控数据
     *接口URL： /apis/daas/pro/3/components/y01-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSQJZ1(){

        String res = null;
        try {
            res = wuHouService.getData("y01-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        Map<String, List<Object>> streetMap = data.stream().collect(Collectors.groupingBy(obj -> ((JSONObject)obj).getString("street")));
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        List<String> columns = Arrays.asList("街道","数量");
        for(Map.Entry<String,List<Object>> entry : streetMap.entrySet()){
            JSONObject row = new JSONObject();
            row.put("街道",entry.getKey());
            row.put("数量",entry.getValue().size());
            row.put("详情","详情");
            rows.add(row);
        }

        result.put("rows",rows);
        result.put("columns",columns);
        result.put("url","/leaderview/ZHSQ/getSQJZ2?param=街道:");

        return new JsonModel(true,result);

    }

    /**
     * 2、智慧社区-社区服刑人员-街道人员表格
     * 目前中台不支持参数查询,需要和中台沟通开放
     * 接口URL： /apis/daas/pro/3/components/y01-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @param param 街道名称
     * @return
     */
    public JsonModel getSQJZ2(String param){

        String query = "street=" + param;
        String res = null;
        try {
            res = wuHouService.getData("y01-01","per_page=40&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("身份证号码","idcard");
        map.put("具体罪名","jtzm");
        map.put("每月服务次数","myfwcs");
        map.put("走访详情","走访详情");

        JSONObject result = getPieResult(map,data);
        //走访记录接口，待中台开发
        result.put("url","");
        return new JsonModel(true,result);

    }

    /**
     *1、智慧社区-社区康复-吸毒人员分布数据
     *接口URL： /apis/daas/pro/3/components/y02-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSQKF1(){

        String res = null;
        try {
            res = wuHouService.getData("y02-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        Map<String, List<Object>> streetMap = data.stream().collect(Collectors.groupingBy(obj -> ((JSONObject)obj).getString("street")));
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        List<String> columns = Arrays.asList("街道","数量");
        for(Map.Entry<String,List<Object>> entry : streetMap.entrySet()){
            JSONObject row = new JSONObject();
            row.put("街道",entry.getKey());
            row.put("数量",entry.getValue().size());
            rows.add(row);
        }

        result.put("rows",rows);
        result.put("columns",columns);
        result.put("url","/leaderview/ZHSQ/getSQKF2?param=街道:");


        return new JsonModel(true,result);

    }

    /**
     *2、智慧社区-社区康复-街道吸毒人员
     *接口URL： /apis/daas/pro/3/components/y02-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSQKF2(String param){
        String query = "street=" + param;
        String res = null;
        try {
            res = wuHouService.getData("y02-01","per_page=10000&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //"ifno": "审核通过", //是否通过审核
        //"street": "浆洗街街道", //乡镇（街道）
        //"community": "少陵路社区", //村（社区）
        //"wg": "第三网格", //网格
        //"name": "刘桂琼", //姓名
        //"idcard": "511027********2840", //身份证号码
        //"xdyy": "人生受挫", //吸毒原因
        //"gkqk": "在控", //管控情况
        //"myfwcs": "1", //每月服务次数
        map.put("姓名","name");
        map.put("街道","street");
        map.put("社区","community");
        map.put("吸毒原因","xdyy");
        map.put("管控情况","gkqk");
        map.put("每月服务次数","myfwcs");

        JSONObject result = getPieResult(map,data);
        return new JsonModel(true,result);

    }

    /**
     *1、涉藏处突-社区民警列表
     *接口URL： /apis/daas/pro/3/components/y56-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT1(String param){

        String res = null;
        try {
            res = wuHouService.getData("y56-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        //需要展示的数据
        JSONArray targetData = new JSONArray();

        for(int i = 0;i < data.size();i++){
            JSONObject streetObj = data.getJSONObject(i);
            if("浆洗街街道".equals(streetObj.getString("ssjd"))){
                JSONArray streetArray = streetObj.getJSONArray("items");
                for(int j = 0;j < streetArray.size();j++){
                    if(param.equals(streetArray.getJSONObject(j).getString("sssq"))){
                        targetData = streetArray.getJSONObject(j).getJSONArray("items");
                    }
                }
            }
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        //"area_id": "072281",
        //"area_police": "高军红",
        //"police_phone": "17708194219",
        //"police_id": null,
        map.put("民警","area_police");
        map.put("手机","police_phone");
        map.put("管控区","house_number");
        map.put("责任区","area_name");
        map.put("社区","sssq");
        map.put("区域Id","area_id");
        map.put("民警Id","police_id");

        JSONObject result = getPieResult(map,targetData);

        List<String> columns = Arrays.asList("民警","手机","责任区");
        result.put("columns",columns);
        return new JsonModel(true,result);

    }

    /**
     *2、涉藏处突-涉藏概况-文本下钻
     *接口URL： /apis/daas/pro/3/components/y56-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT2(){

        String res = null;
        try {
            res = wuHouService.getData("y102-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        //企业类型和对应的打点信息映射
        LinkedHashMap<String,JSONArray> typeAndDotMap = new LinkedHashMap<>();
        for(int i = 0;i < data.size();i++){
            JSONObject obj = data.getJSONObject(i);
            JSONArray array = obj.getJSONArray("items");
            Collections.shuffle(array);
            typeAndDotMap.put(obj.getString("type"), array);
        }

        JSONObject result = wuHouService.getTextResult("type",data);
        result.put("typeAndDotMap",typeAndDotMap);
        return new JsonModel(true,result);

        /*JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        JSONArray targetData = new JSONArray();
        for(int i = 0;i < data.size();i++){
            JSONObject obj = data.getJSONObject(i);
            if("浆洗街街道".equals(obj.getString("street"))){
                targetData = obj.getJSONArray("items");
            }
        }
        //企业类型和对应的打点信息映射
        LinkedHashMap<String,JSONArray> typeAndDotMap = new LinkedHashMap<>();
        for(int i = 0;i < targetData.size();i++){
            JSONObject obj = targetData.getJSONObject(i);
            typeAndDotMap.put(obj.getString("company_type"),obj.getJSONArray("items"));
        }

        JSONObject result = wuHouService.getTextResult("company_type",targetData);
        result.put("typeAndDotMap",typeAndDotMap);
        return new JsonModel(true,result);*/

    }

    /**
     *、涉藏处突-
     *接口URL： /apis/daas/pro/3/components/y56-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT3(){

        String res = null;
        try {
            res = wuHouService.getData("y102-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        //企业类型和对应的打点信息映射
        LinkedHashMap<String,JSONArray> typeAndDotMap = new LinkedHashMap<>();
        for(int i = 0;i < data.size();i++){
            JSONObject obj = data.getJSONObject(i);
            JSONArray array = obj.getJSONArray("items");
            Collections.shuffle(array);
            typeAndDotMap.put(obj.getString("type"), array);
        }

        JSONObject result = wuHouService.getTextResult("type",data);
        result.put("typeAndDotMap",typeAndDotMap);
        return new JsonModel(true,result);

    }

    /**
     *5、涉藏处突-涉藏概况下钻列表
     *接口URL： /apis/daas/pro/3/components/y56-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT5(String param){

        String res = null;
        try {
            res = wuHouService.getData("y102-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        //需要展示的数据
        JSONArray targetData = new JSONArray();

        for(int i = 0;i < data.size();i++){
            JSONObject obj = data.getJSONObject(i);
            if(param.equals(obj.getString("type"))) {
                targetData = obj.getJSONArray("items");
                Collections.shuffle(targetData);
            }
        }

        JSONObject obj= getMapfromType(param);
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) obj.get("map");
        JSONObject result = getPieResult(map,targetData);

        List<String> columns = new ArrayList<>();
        List<String> columns1 = Arrays.asList("名称","地址","负责人");
        List<String> columns2 = Arrays.asList("名称","地址","所在社区");
        List<String> columns3 = Arrays.asList("场所名称","地址","时段");
        LinkedHashMap<String, List<String>> typeMap = new LinkedHashMap<>();
        typeMap.put("涉藏机构",columns1);
        typeMap.put("小区院落",columns2);
        typeMap.put("锅庄舞场",columns3);

//        columns = typeMap.get(param);
        columns = (List<String>) obj.get("columns");
        result.put("columns",columns);
        return new JsonModel(true,result);

    }

    //涉藏概况文本框下钻列表接口的参数param和对应的map
//    public LinkedHashMap<String, String> getMapfromType(String param){
    public JSONObject getMapfromType(String param){
//        LinkedHashMap<String, String> targetMap = new LinkedHashMap<>();
        LinkedHashMap<String, String> targetMap = new LinkedHashMap<>();

        JSONObject result = new JSONObject();
        //涉藏机构
        LinkedHashMap<String, String> map1 = new LinkedHashMap<>();
        //小区院落
        LinkedHashMap<String, String> map2 = new LinkedHashMap<>();
        //锅庄舞场
        LinkedHashMap<String, String> map3 = new LinkedHashMap<>();
        //娱乐场所
        LinkedHashMap<String, String> map4 = new LinkedHashMap<>();
        //涉藏商铺
        LinkedHashMap<String, String> map5 = new LinkedHashMap<>();
        //藏餐茶吧
        LinkedHashMap<String, String> map6 = new LinkedHashMap<>();
        //民宿旅社
        LinkedHashMap<String, String> map7 = new LinkedHashMap<>();
        //"company_name": "西南民族大学",
        //"company_address": "洗面桥横街21号",
        //"company_manager": "李燎原",
        map1.put("名称","company_name");
        map1.put("地址","company_address");
        map1.put("负责人","company_manager");
        //"name": "甘孜干休所", //场所名称
        //"address": "成都市武侯区广福桥20号", //场所地址
        //"community": "广福桥社区", //所属社区
        map2.put("名称","name");
        map2.put("地址","address");
        map2.put("所在社区","community");
        //"name": "成都A区·锦外", //场所名称
        //"address": "成都市武侯区一环路南四段17号", //场所地址
        //"time": "7.30----9.30",
        map3.put("场所名称","name");
        map3.put("地址","address");
        map3.put("时段","time");

        //"name": "GOON酒吧", //场所名称
        //"license": null, //有无证照
        //"address": "成都市武侯区高升桥东路6号罗马假日广场16-301", //场所地址
        //"community": "广福桥社区", //所属社区
        map4.put("场所名称","name");
        map4.put("地址","address");
        map4.put("所属社区","community");

        //
        map5.put("名称","name");
        map5.put("地址","address");
        map5.put("所属社区","community");

        map6.put("名称","name");
        map6.put("地址","address");
        map6.put("所属社区","community");

        map7.put("名称","name");
        map7.put("地址","address");


        LinkedHashMap<String, LinkedHashMap<String, String>> typeMap = new LinkedHashMap<>();
        typeMap.put("涉藏机构",map1);
        typeMap.put("小区院落",map2);
        typeMap.put("锅庄舞场",map3);
        typeMap.put("娱乐场所",map4);
        typeMap.put("涉藏商铺",map5);
        typeMap.put("藏餐茶吧",map6);
        typeMap.put("民宿旅馆",map7);

        targetMap = typeMap.get(param);

        //columns
        List<String> columns = new ArrayList<>();
        List<String> columns1 = Arrays.asList("名称","地址","负责人");
        List<String> columns2 = Arrays.asList("名称","地址","所在社区");
        List<String> columns3 = Arrays.asList("场所名称","地址","时段");
        List<String> columns4 = Arrays.asList("场所名称","地址","所属社区");
        List<String> columns5 = Arrays.asList("名称","地址","所属社区");
        List<String> columns6 = Arrays.asList("名称","地址","所属社区");
        List<String> columns7 = Arrays.asList("名称","地址");
        LinkedHashMap<String, List<String>> columnsTypeMap = new LinkedHashMap<>();
        columnsTypeMap.put("涉藏机构",columns1);
        columnsTypeMap.put("小区院落",columns2);
        columnsTypeMap.put("锅庄舞场",columns3);
        columnsTypeMap.put("娱乐场所",columns4);
        columnsTypeMap.put("涉藏商铺",columns5);
        columnsTypeMap.put("藏餐茶吧",columns6);
        columnsTypeMap.put("民宿旅馆",columns7);

        columns = columnsTypeMap.get(param);

        result.put("map",targetMap);
        result.put("columns",columns);

//        return typeMap.get(param);
        return result;
    }

    /**
     *6、涉藏处突-涉藏概况下钻列表
     *接口URL： /apis/daas/pro/3/components/y56-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT6(String param){

        String res = null;
        try {
            res = wuHouService.getData("y102-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        //需要展示的数据
        JSONArray targetData = new JSONArray();

        for(int i = 0;i < data.size();i++){
            JSONObject obj = data.getJSONObject(i);
            if(param.equals(obj.getString("type"))) {
                targetData = obj.getJSONArray("items");
                Collections.shuffle(targetData);
            }
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        JSONObject result = getPieResult(map,targetData);

        List<String> columns = new ArrayList<>();
        List<String> columns1 = Arrays.asList("名称","地址","负责人");
        List<String> columns2 = Arrays.asList("名称","地址","所在社区");
        List<String> columns3 = Arrays.asList("场所名称","地址","时段");
        LinkedHashMap<String, List<String>> typeMap = new LinkedHashMap<>();
        typeMap.put("涉藏机构",columns1);
        typeMap.put("小区院落",columns2);
        typeMap.put("锅庄舞场",columns3);

        columns = typeMap.get(param);
        result.put("columns",columns);
        return new JsonModel(true,result);

    }

    public JsonModel getCustomDot(){
        List<CustomDotScheme> list = customDotSchemeDao.findAll();
        return new JsonModel(true,list);
    }

    public JsonModel saveCustomDot(CustomDotScheme customDotScheme){
        customDotSchemeDao.save(customDotScheme);
        return new JsonModel(true);
    }

    public JsonModel updateCustomDot(CustomDotScheme customDotScheme){
        customDotSchemeDao.update(customDotScheme.getId(),customDotScheme);
        return new JsonModel(true);
    }

    public JsonModel deleteCustomDot(CustomDotScheme customDotScheme){
        customDotSchemeDao.delete(customDotScheme);
        return new JsonModel(true);
    }

    /**
     * 4、涉藏处突-小区院落打点
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y141-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getCommunityDot(){

        String res = null;
        try {
            res = wuHouService.getData("y141-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        result.put("dataArray", data);

        return new JsonModel(true,result);

    }

    /**
     * 5、涉藏处突-社区网格打点
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y142-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getGridDot(){

        String res = null;
        try {
            res = wuHouService.getData("y142-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        result.put("dataArray", data);

        return new JsonModel(true,result);

    }

    /**
     * 6、涉藏处突-网格区打点信息
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y142-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT4(){

        String res = null;
        try {
            res = wuHouService.getData("y55-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        JSONArray targetData = new JSONArray();

        for(int i = 0;i < data.size(); i++){
            JSONObject obj = data.getJSONObject(i);
            if("浆洗街街道".equals(obj.getString("ssjd"))){
                targetData.add(obj);
            }
        }
        JSONObject result = new JSONObject();
        result.put("dataArray", targetData);

        return new JsonModel(true,result);

    }

}
