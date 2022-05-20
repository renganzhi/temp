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
            if("涉藏高校".equals(obj.getString("type"))){
                JSONObject obj2 = array.getJSONObject(0);
                array = obj2.getJSONArray("items");
            }
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
        columns = (List<String>) obj.get("columns");
        result.put("columns",columns);
        return new JsonModel(true,result);

    }

    //涉藏概况文本框下钻列表接口的参数param和对应书数据需要的map和columns
//    public LinkedHashMap<String, String> getMapfromType(String param){
    public JSONObject getMapfromType(String param){
//        LinkedHashMap<String, String> targetMap = new LinkedHashMap<>();
        LinkedHashMap<String, String> targetMap = new LinkedHashMap<>();

        JSONObject result = new JSONObject();
        //涉藏机构 都没有 //小区院落 都没有  //涉藏商铺 只有民警，没有网格员 //藏餐茶吧 只有民警，没有网格员
        LinkedHashMap<String, String> map1 = new LinkedHashMap<>();
        //小区院落 都没有
        LinkedHashMap<String, String> map2 = new LinkedHashMap<>();
        //锅庄舞场 1
        LinkedHashMap<String, String> map3 = new LinkedHashMap<>();
        //娱乐场所 1
        LinkedHashMap<String, String> map4 = new LinkedHashMap<>();
        //涉藏商铺 只有民警，没有网格员
        LinkedHashMap<String, String> map5 = new LinkedHashMap<>();
        //藏餐茶吧 只有民警，没有网格员
        LinkedHashMap<String, String> map6 = new LinkedHashMap<>();
        //民宿旅社
        LinkedHashMap<String, String> map7 = new LinkedHashMap<>();
        //"company_name": "西南民族大学",
        //"company_address": "洗面桥横街21号",
        //"company_manager": "李燎原",
        //涉藏机构
        map1.put("名称","company_name");
        map1.put("地址","company_address");
        map1.put("负责人","company_manager");
        map1.put("职务","job_title");
        map1.put("负责人电话","manager_phone");
        map1.put("办公室主任","office_manager");
        map1.put("主任电话","office_manager_phone");
        map1.put("片区民警姓名","pqmjxm");
        map1.put("片区民警联系电话","pqmjlxdh");
        map1.put("片区网格员姓名","pqwgyxm");
        map1.put("片区网格员联系电话","pqmjlxdh");
        //"name": "甘孜干休所", //场所名称
        //"address": "成都市武侯区广福桥20号", //场所地址
        //"community": "广福桥社区", //所属社区
        //小区院落
        map2.put("名称","name");
        map2.put("地址","address");
        map2.put("所在社区","community");
        map2.put("涉藏租住人口数","household_population");
        map2.put("物业","company_name");
        map2.put("物业联系方式","company_phone");
        map2.put("片区民警姓名","pqmjxm");
        map2.put("片区民警联系电话","pqmjlxdh");
        map2.put("片区网格员姓名","pqwgyxm");
        map2.put("片区网格员联系电话","pqmjlxdh");

        //锅庄舞场
        map3.put("场所名称","name");
        map3.put("地址","address");
        map3.put("容纳人数","rnrs");
        map3.put("参与人数","person_number");
        map3.put("观众数","gzs");
        map3.put("组织者","manager_name");
        map3.put("活动时段","time");
        map3.put("所属社区","community");
        map3.put("片区民警姓名","pqmjxm");
        map3.put("片区民警联系电话","pqmjlxdh");
        map3.put("片区网格员姓名","pqwgyxm");
        map3.put("片区网格员联系电话","pqmjlxdh");
        map3.put("主要风险","zyfx");

        //娱乐场所
        map4.put("场所名称","csmc");
        map4.put("地址","address");
        map4.put("面积","area");
        map4.put("负责人姓名","fzrxm");
        map4.put("负责人电话","fzrlxdh");
        map4.put("所属社区","community");
        map4.put("经营范围","jyfw");
        map4.put("员工数量","ygsl");
        map4.put("安全风险等级","aqfxdj");
        map4.put("片区民警姓名","pqmjxm");
        map4.put("片区民警联系电话","pqmjlxdh");
        map4.put("片区网格员姓名","pqwgyxm");
        map4.put("片区网格员联系电话","pqmjlxdh");
        map4.put("燃气使用","rqsy");

        //涉藏商铺
        map5.put("名称","csmc");
        map5.put("地址","address");
        map5.put("所属社区","community");
        map5.put("类别","jyfw");
        map5.put("商铺面积","area");
        map5.put("店主姓名","fzrxm");

        map5.put("店主民族","fzrmz");
        map5.put("联系方式","fzrlxdh");
        map5.put("员工数量","ygsl");
        map5.put("安全风险等级","aqfxdj");
        map5.put("片区民警姓名","pqmjxm");
        map5.put("片区民警联系电话","pqmjlxdh");
        map5.put("片区网格员姓名","pqwgyxm");
        map5.put("片区网格员联系电话","pqmjlxdh");

        //藏餐茶吧
        map6.put("场所名称","csmc");
        map6.put("地址","address");
        map6.put("面积","area");
        map6.put("负责人姓名","fzrxm");
        map6.put("负责人电话","fzrlxdh");
        map6.put("所属社区","community");
        map6.put("经营范围","jyfw");
        map6.put("员工数量","ygsl");
        map6.put("安全风险等级","aqfxdj");
        map6.put("片区民警姓名","pqmjxm");
        map6.put("片区民警联系电话","pqmjlxdh");
        map6.put("片区网格员姓名","pqwgyxm");
        map6.put("片区网格员联系电话","pqmjlxdh");
        map6.put("燃气使用","rqsy");

        map7.put("名称","name");
        map7.put("地址","address");
        map7.put("负责人姓名","manager_name");
        map7.put("负责人电话","manager_phone");

        LinkedHashMap<String, LinkedHashMap<String, String>> typeMap = new LinkedHashMap<>();
        typeMap.put("涉藏机构",map1);
        typeMap.put("小区院落",map2);
        typeMap.put("锅庄舞场",map3);
        typeMap.put("娱乐场所",map4);
        typeMap.put("涉藏商店",map5);
        typeMap.put("藏餐茶吧",map6);
        typeMap.put("民宿旅馆",map7);

        targetMap = typeMap.get(param);

        //columns
        List<String> columns = new ArrayList<>();
        List<String> columns1 = Arrays.asList("名称","地址","负责人");
        List<String> columns2 = Arrays.asList("名称","地址","所在社区");
        List<String> columns3 = Arrays.asList("场所名称","地址","活动时段");
        List<String> columns4 = Arrays.asList("场所名称","地址","所属社区");
        List<String> columns5 = Arrays.asList("名称","地址","所属社区");
        List<String> columns6 = Arrays.asList("场所名称","地址","所属社区");
        List<String> columns7 = Arrays.asList("名称","地址");
        LinkedHashMap<String, List<String>> columnsTypeMap = new LinkedHashMap<>();
        columnsTypeMap.put("涉藏机构",columns1);
        columnsTypeMap.put("小区院落",columns2);
        columnsTypeMap.put("锅庄舞场",columns3);
        columnsTypeMap.put("娱乐场所",columns4);
        columnsTypeMap.put("涉藏商店",columns5);
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
            res = wuHouService.getData("y155-02","per_page=10000&page=1",null,false,true);
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

    /**
     *7、涉藏处突-涉藏人口数
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y142-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT7(){

        String res = null;
        try {
            res = wuHouService.getData("y168-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //    "whq_rks": "14670", //武侯区涉藏人口数
        //    "jxj_rks": "7134", //浆洗街街道涉藏人口数
        //    "jxj_czrks": "1105", //浆洗街街道常住涉藏人口数
        //    "jxj_ldrks": "6029" //浆洗街街道流动涉藏人口数
        map.put("武侯区涉藏人口数","whq_rks");
        map.put("浆洗街街道涉藏人口数","jxj_rks");
        map.put("浆洗街街道常住涉藏人口数","jxj_czrks");
        map.put("浆洗街街道流动涉藏人口数","jxj_ldrks");
        JSONObject result = getPieResult(map,data);
//        result.put("type","getSZCT7");

        return new JsonModel(true,result);

    }

    /**
     * 8、涉藏处突-实时预警
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y142-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT8(){

        String res = null;
        try {
            res = wuHouService.getData("y167-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //    "time": "2022-04-19 17:25:14.053", //时间
        //    "address": "xxx地址", //地址
        //    "type": "xxx类别", //类别
        //    "content": "修改xx内容", //内容
        //    "事件来源": "智能感知源"
        map.put("时间","time");
        map.put("地址","address");
        map.put("类别","type");
        map.put("内容","content");

        JSONObject result = getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 9、涉藏处突-涉藏高校
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y142-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT9(){

        String res = null;
        try {
            res = wuHouService.getData("y161-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        //两个top柱状图需要的数据
        JSONArray histogramArray = new JSONArray();
        JSONArray targetData = new JSONArray();
        LinkedHashMap<String,JSONArray> xqAndDataMap = new LinkedHashMap<>();
        JSONArray targetData2 = new JSONArray();

        //西南民族大学情况对象
        JSONObject dataObj = data.getJSONObject(0);
        JSONArray items1 = dataObj.getJSONArray("items");
        for(int i = 0; i < items1.size(); i++){
            JSONObject obj = items1.getJSONObject(i);
            JSONArray items2 = obj.getJSONArray("items");
//            targetData.add(items2);
            xqAndDataMap.put(obj.getString("xq_name"),items2);
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //    "ssmz": "土家族", //少数民族
        //    "ssrs": "74" //学生人数
        map.put("少数民族","ssmz");
        map.put("学生人数","ssrs");

        for (Map.Entry<String,JSONArray> entry : xqAndDataMap.entrySet()){
            JSONArray neededData = entry.getValue();
            JSONObject neededObj = getPieResult(map,neededData);
            neededObj.put("校区",entry.getKey());
            histogramArray.add(neededObj);
        }

        /*for (int i = 0; i < targetData.size(); i++){
            JSONArray neededData = targetData.getJSONArray(i);
            JSONObject neededObj = getPieResult(map,neededData);
//            neededObj.put("校区",);
            histogramArray.add(neededObj);
        }*/
        //"zcjzgsl": "2123", //在册教职工数量
        //"zcwzjzgsl": "8", //在册维族教职工数量
        //"wjrs": "8" //外教人数
        //"txjzgsl": "877", //退休教职工数量
        //外层文本框
        LinkedHashMap<String, String> map2 = new LinkedHashMap<>();
        map2.put("在册教职工数量","zcjzgsl");
        map2.put("在册维族教职工数量","zcwzjzgsl");
        map2.put("外教人数","wjrs");
        map2.put("退休教职工数量","txjzgsl");

        JSONObject textData = getPieResult(map2,data);
        JSONObject result = new JSONObject();
        result.put("histogramData",histogramArray);
        result.put("textData",textData);

        return new JsonModel(true,result);

    }

    /**
     * 10、涉藏处突-指挥体系-涉藏指挥部
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y169-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JSONObject getSZCT10(){

        String res = null;
        try {
            res = wuHouService.getData("y169-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
//            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        //需要处理的数据
        JSONArray targetData = new JSONArray();
        //指挥长数据
        JSONArray zhzArray = new JSONArray();
        JSONObject zhzObj = new JSONObject();
        JSONObject zhz2Obj = new JSONObject();
        //副指挥长数据
        JSONArray fzhzArray = new JSONArray();
        JSONObject fzhzObj = new JSONObject();
        JSONObject fzhz2Obj = new JSONObject();

        for (int i = 0 ;i < data.size(); i ++){
            JSONObject obj = data.getJSONObject(i);
            if("涉藏指挥部".equals(obj.getString("type"))){
                targetData = obj.getJSONArray("items");
                for (int j = 0; j < targetData.size();j++){
                    JSONObject obj2 = targetData.getJSONObject(j);
                    if("指挥长".equals(obj2.getString("zhbzw"))){
                        zhzArray = obj2.getJSONArray("items");
                    }
                    if("副总指挥长".equals(obj2.getString("zhbzw"))){
                        fzhzArray = obj2.getJSONArray("items");
                    }
                }
            }
        }

        //指挥长、副指挥长 所有字段的map,用于获取弹窗所需字段
        LinkedHashMap<String, String> zhzMap = new LinkedHashMap<>();
        //指挥长、副指挥长 名称、职务字段的map,用于首页表格
        LinkedHashMap<String, String> zhz2Map = new LinkedHashMap<>();
        //副指挥长所有字段的map,用于获取弹窗所需字段
//        LinkedHashMap<String, String> fzhzMap = new LinkedHashMap<>();
        //副指挥长名称、职务字段的map,用于首页表格
//        LinkedHashMap<String, String> fzhz2Map = new LinkedHashMap<>();
        //    "zhbzw": "指挥长", //指挥部职务
        //    "name": "王亚滨", //姓名
        //    "zw": "区委常委、政法委书记", //职务
        //    "photo_link": "https://zhwh.cdyoue.com/FsXYA9tnQJi8dbJJdUGwr-96C7Yv", //照片
        //    "mobilephone": "13881838181" //手机
        zhzMap.put("指挥部职务","zhbzw");
        zhzMap.put("姓名","name");
        zhzMap.put("职务","zw");
        zhzMap.put("照片","photo_link");
        zhzMap.put("手机","mobilephone");

        zhz2Map.put("姓名","name");
        zhz2Map.put("职务","zw");
        //指挥长所有字段结果
        zhzObj = getPieResult(zhzMap,zhzArray);
        //指挥长姓名、职务字段结果
        zhz2Obj = getPieResult(zhz2Map,zhzArray);
        //副指挥长所有字段结果
        fzhzObj = getPieResult(zhzMap,fzhzArray);
        //副指挥长姓名、职务字段结果
        fzhz2Obj = getPieResult(zhz2Map,fzhzArray);

        JSONObject result = new JSONObject();
        result.put("指挥长",zhzObj);
        result.put("指挥长姓名职务",zhz2Obj);
        result.put("副总指挥长",fzhzObj);
        result.put("副总指挥长姓名职务",fzhz2Obj);
        return result;

    }

    /**
     * 13、涉藏处突-专家工作组
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y169-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT11(){

        String res = null;
        try {
            res = wuHouService.getData("y169-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        //需要处理的数据
        JSONArray targetData = new JSONArray();

        for (int i = 0 ;i < data.size(); i ++){
            JSONObject obj = data.getJSONObject(i);
            if("涉藏工作组".equals(obj.getString("type"))){
                targetData = obj.getJSONArray("items");
            }
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //工作职责  牵头单位    责任部门
        //    "name": "现场处置组",
        //
        //    "gzzz": "负责协调公安、街道街面守护控制力量第一时间赶到现场，控制现场、掌握情报；组织常备处突力量开展处突工作。",
        //
        //    "qtdw": "区公安分局",
        //
        //    "zrbm": "区委政法委、区综合执法局，各街道"

        map.put("名称","name");
        map.put("工作职责","gzzz");
        map.put("牵头单位","qtdw");
        map.put("责任部门","zrbm");

        JSONObject result = getPieResult(map,targetData);
        List<String> columns = Arrays.asList("名称","牵头单位");
        result.put("columns",columns);

        return new JsonModel(true,result);

    }

    /**
     * 12、涉藏处突-藏区医院
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y169-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT12(){

        String res = null;
        try {
            res = wuHouService.getData("y174-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //"sjjzsj": "2022-05-17", //数据截止时间
        //"hotel_name": "三六三医院", //医院名称
        //"hotel_address": "成都市武侯区倒桑树街108号", //医院地址
        //"cws": "500", //床位数
        //"bwkkzxm": "廖育蕾", //保卫科科长姓名
        //"bwkkzlxdh": "18980798377", //保卫科科长联系电话
        //"zybrs": null, //在院病人数
        //"zyszbrs": "193", //在院涉藏病人数
        //"bwbzrs": "5", //病危病重人数
        map.put("医院名称","hotel_name");
        map.put("医院地址","hotel_address");
        map.put("床位数","cws");
        map.put("保卫科科长姓名","bwkkzxm");
        map.put("保卫科科长联系电话","bwkkzlxdh");
        map.put("在院病人数","zybrs");
        map.put("在院涉藏病人数","zyszbrs");
        map.put("病危病重人数","bwbzrs");

        JSONObject result = getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 、涉藏处突-
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y169-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT13(){

        String res = null;
        try {
            res = wuHouService.getData("y169-01","per_page=10000&page=1",null,false,true);
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
     * 、涉藏处突-
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y169-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT14(){

        String res = null;
        try {
            res = wuHouService.getData("y169-01","per_page=10000&page=1",null,false,true);
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
     * 、涉藏处突-
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y169-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getSZCT15(){

        String res = null;
        try {
            res = wuHouService.getData("y169-01","per_page=10000&page=1",null,false,true);
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
