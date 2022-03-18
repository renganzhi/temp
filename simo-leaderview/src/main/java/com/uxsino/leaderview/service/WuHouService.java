package com.uxsino.leaderview.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.utils.ClassPathResourceWalker;
import com.uxsino.commons.utils.DateUtils;
import com.uxsino.leaderview.dao.ITibetCommunityTableDao;
import com.uxsino.leaderview.dao.ITibetOrganizationTableDao;
import com.uxsino.leaderview.dao.ITimeDataDao;
import com.uxsino.leaderview.dao.IWuhouHotelRegisterDao;
import com.uxsino.leaderview.entity.TibetCommunityTable;
import com.uxsino.leaderview.entity.TibetOrganizationTable;
import com.uxsino.leaderview.entity.TimeData;
import com.uxsino.leaderview.model.DataJob;
import com.uxsino.leaderview.utils.MonitorUtils;
import com.uxsino.quartz.lib.Job;
import com.uxsino.quartz.lib.ScheduleManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
@Slf4j
public class WuHouService {

    private static final String Jwt_Secret = "098ec21f7d21ed64620cc1712fac5852c8b9b90d98265acafb8866515cadbef7";
    public static final String appId = "1942fc6b7aab121b22c892c920af8b74b9349f2611ce71d79a8324ce83227279:";
    public static final Integer namespace_id = 1;
    public static final String encoded_data = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lc3BhY2VfaWQiOjF9.dgGuGkLelFnT9ups0kUoBw-AAOFsvUW_fl7HN3KVUWE";

    @Autowired
    private ScheduleManager scheduleManager;

    @Autowired
    ITimeDataDao timeDataDao;

    @Autowired
    ITibetOrganizationTableDao tibetOrganizationTableDao;

    @Autowired
    ITibetCommunityTableDao tibetCommunityTableDao;

    @Autowired
    JdbcTemplate template;

    @Autowired
    private IWuhouHotelRegisterDao wuhouHotelRegisterDao;

    @Autowired
    private ITibetCommunityTableDao communityTableDao;

    @Autowired
    private ITibetOrganizationTableDao organizationTableDao;

    /**
     * 初始化文件名称
     */
    private static final String FILEPATH = "classpath*:sql/simo_leaderview_jx_community_table.sql";
    //涉藏小区初始化sql
    private static final String XIAOQUPATH = "classpath*:sql/jx_orga_table1.sql";
    //涉藏机构初始化sql
    private static final String JIGOUPATH = "classpath*:sql/jx_orga_table2.sql";

    private Logger logger = LoggerFactory.getLogger(WuHouService.class);


    /**
     * 获取中台数据的接口
     * @param formId 表单ID
     * @param pagination 分页参数
     * @param query 查询参数
     * @param ifFormInfo 是获取表头信息还是表单数据
     * @param ifNew 是否是新版接口
     * @return
     * @throws IOException
     */
    public String getData(String formId, String pagination, String query, Boolean ifFormInfo, Boolean ifNew) throws IOException {
        //社会治理-重点人员管控-社区服刑人员	89   街道：query[622]
        //社会治理-重点人员管控-吸毒	90
        //社会治理-重点人员管控-刑释	91
        //社会治理-重点人员管控-易肇事肇祸	92
        //社会治理-重点人员管控-重症精神	93
        //社会治理-重点事件管理-重点人员	94
        //社会治理-重点事件管理-重点事件	95
        //formId = "89";//社会治理-重点人员管控-社区服刑人员
        //y07-01 商铺性质分析 √
        //y09-01 场所打点    √
        //y11-01 涉藏商铺数量
        //y12-01 藏族来源地分析
        //y13-01 群租房入住人员归属地分析
        //老版接口地址
        String prefix = "http://wunlzt.cdwh.gov.cn/api/v4/forms/";
        //新版接口地址
        String prefixV3 = "http://wunlzt.cdwh.gov.cn/apis/daas/pro/3/components/";
        String prefixV33 = "http://wunlzt.cdwh.gov.cn/apis/daas/pro/3/components/y07-01/data?per_page=10000&page=1";
        if(formId.contains("-") || ifNew){
            prefix = prefixV3;
        }
        String url;
        if(ifFormInfo){
            url = prefix + formId;
        }else {
            if(ifNew) {
                url = prefix + formId +"/data";
            }else {
                url = prefix + formId + "/responses/search.json";
            }
            //在pagination或者query中自己添加?
            if(!Strings.isNullOrEmpty(pagination) || !Strings.isNullOrEmpty(query) ){
                url += "?";
            }
            if(!ObjectUtils.isEmpty(pagination)){
                url += pagination;
            }
            if(!ObjectUtils.isEmpty(query)){
                if(!ObjectUtils.isEmpty(pagination)) {
                    url += "&" + query;
                }else {
                    url += query;
                }
            }
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        //新版接口调用
        if(ifNew){
            String token = getToken();
//            httpGet.setHeader("token",
//                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDY3MDgyMDMsImV4cCI6MTY0Njc5NDYwMywidGlkIjozLCJqdGkiOiI2MjczZmQxNDllOGIxMWVjODgzNzAyNDJhYzFhMDAwNyIsInAiOlszXX0.KkMrNEs4CaTNKaGB6jYW02hZd2taSytJvW1efs7OSbs");
            httpGet.setHeader("token", token);
            httpGet.setHeader("Content-Type","multipart/form-data");
        }else {
            httpGet.setHeader("Authorization","1942fc6b7aab121b22c892c920af8b74b9349f2611ce71d79a8324ce83227279:" +
                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lc3BhY2VfaWQiOjF9" +
                    ".dgGuGkLelFnT9ups0kUoBw-AAOFsvUW_fl7HN3KVUWE");
        }

        String result = "";

        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            log.error("中台接口请求表单{}失败{}",formId,e.getMessage());
            e.printStackTrace();
        }
        HttpEntity httpEntity = closeableHttpResponse.getEntity();
        result = EntityUtils.toString(httpEntity);//响应内容
        System.out.println("请求结果是：" + result);

        /*Header[] headers = closeableHttpResponse.getHeaders("X-SLP-Total-Count");
        String value = headers[0].getValue();*/

        return result;
    }

    /**
     * 获取中台数据的接口,url为/pro/1的接口
     * @param formId 表单ID
     * @param pagination 分页参数
     * @param query 查询参数
     * @param ifFormInfo 是获取表头信息还是表单数据
     * @param ifNew 是否是新版接口
     * @return
     * @throws IOException
     */
    public String getData1(String formId, String pagination, String query, Boolean ifFormInfo, Boolean ifNew) throws IOException {

        //formId = "89";//社会治理-重点人员管控-社区服刑人员
        //y07-01 商铺性质分析 √
        //y09-01 场所打点    √
        //y11-01 涉藏商铺数量
        //y12-01 藏族来源地分析
        //y13-01 群租房入住人员归属地分析

        //老版接口地址
        String prefix = "http://wunlzt.cdwh.gov.cn/api/v4/forms/";
        //新版接口地址
        String prefixV1 = "http://wunlzt.cdwh.gov.cn/apis/daas/pro/1/components/";
        String prefixV11 = "http://wunlzt.cdwh.gov.cn/apis/daas/pro/1/components/y07-01/data?per_page=10000&page=1";
        if(formId.contains("-") || ifNew){
            prefix = prefixV1;
        }
        String url;
        if(ifFormInfo){
            url = prefix + formId;
        }else {
            if(ifNew) {
                url = prefix + formId +"/data";
            }else {
                url = prefix + formId + "/responses/search.json";
            }
            //在pagination或者query中自己添加?
            if(!Strings.isNullOrEmpty(pagination) || !Strings.isNullOrEmpty(query) ){
                url += "?";
            }
            if(!ObjectUtils.isEmpty(pagination)){
                url += pagination;
            }
            if(!ObjectUtils.isEmpty(query)){
                if(!ObjectUtils.isEmpty(pagination)) {
                    url += "&" + query;
                }else {
                    url += query;
                }
            }
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        //新版接口调用
        if(ifNew){
            String token = getToken();
//            httpGet.setHeader("token",
//                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDY3MDgyMDMsImV4cCI6MTY0Njc5NDYwMywidGlkIjozLCJqdGkiOiI2MjczZmQxNDllOGIxMWVjODgzNzAyNDJhYzFhMDAwNyIsInAiOlszXX0.KkMrNEs4CaTNKaGB6jYW02hZd2taSytJvW1efs7OSbs");
            httpGet.setHeader("token", token);
            httpGet.setHeader("Content-Type","multipart/form-data");
        }else {
            httpGet.setHeader("Authorization","1942fc6b7aab121b22c892c920af8b74b9349f2611ce71d79a8324ce83227279:" +
                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lc3BhY2VfaWQiOjF9" +
                    ".dgGuGkLelFnT9ups0kUoBw-AAOFsvUW_fl7HN3KVUWE");
        }

        String result = "";

        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            log.error("中台接口请求表单{}失败{}",formId,e.getMessage());
            e.printStackTrace();
        }
        HttpEntity httpEntity = closeableHttpResponse.getEntity();
        result = EntityUtils.toString(httpEntity);//响应内容
        System.out.println("请求结果是：" + result);

        /*Header[] headers = closeableHttpResponse.getHeaders("X-SLP-Total-Count");
        String value = headers[0].getValue();*/

        return result;
    }

    /**
     * 调用中台一些url特殊且固定的接口
     * @param url
     * @param ifNew
     * @return
     * @throws IOException
     */
    public String getData2(String url, Boolean ifNew) throws IOException {

        //formId = "89";//社会治理-重点人员管控-社区服刑人员
        //y07-01 商铺性质分析 √
        //y09-01 场所打点    √
        //y11-01 涉藏商铺数量
        //y12-01 藏族来源地分析
        //y13-01 群租房入住人员归属地分析

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        //新版接口调用
        if(ifNew){
            String token = getToken();
//            httpGet.setHeader("token",
//                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDY3MDgyMDMsImV4cCI6MTY0Njc5NDYwMywidGlkIjozLCJqdGkiOiI2MjczZmQxNDllOGIxMWVjODgzNzAyNDJhYzFhMDAwNyIsInAiOlszXX0.KkMrNEs4CaTNKaGB6jYW02hZd2taSytJvW1efs7OSbs");
            httpGet.setHeader("token", token);
            httpGet.setHeader("Content-Type","multipart/form-data");
        }else {
            httpGet.setHeader("Authorization","1942fc6b7aab121b22c892c920af8b74b9349f2611ce71d79a8324ce83227279:" +
                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lc3BhY2VfaWQiOjF9" +
                    ".dgGuGkLelFnT9ups0kUoBw-AAOFsvUW_fl7HN3KVUWE");
        }

        String result = "";

        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            log.error("中台接口请求表单{}失败{}",e.getMessage());
            e.printStackTrace();
        }
        HttpEntity httpEntity = closeableHttpResponse.getEntity();
        result = EntityUtils.toString(httpEntity);//响应内容
        System.out.println("请求结果是：" + result);

        return result;
    }

    public String getToken(){

        String token = null;

        String tokenUrl2 = "http://wunlzt.cdwh.gov.cn/apis/daas/token?appID=819ee3726ceff963a5f6877de2d92c00&appSecret" +
                "=4fd078c9fef8ee0f1311e2d1661619c5";
        CloseableHttpClient httpClient  = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(tokenUrl2);
        httpGet.setHeader("Content-Type","multipart/form-data");

        try {
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            String res = EntityUtils.toString(httpEntity);//响应内容
            System.out.println("请求结果是：" + res);
            JSONObject object = JSONObject.parseObject(res);
            JSONObject data = (JSONObject) object.get("data");
            token = (String) data.get("token");


        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取中台token失败");
        }

        return token;
    }

    /**
     * 通过header获取中台返回数据总条数的方法
     * @param formId
     * @param pagination
     * @param query
     * @param ifFormInfo
     * @return
     */
    public CloseableHttpResponse getResponse(String formId, String pagination, String query, Boolean ifFormInfo){
        //社会治理-重点人员管控-社区服刑人员	89   街道：query[622]
        //社会治理-重点人员管控-吸毒	90
        //社会治理-重点人员管控-刑释	91
        //社会治理-重点人员管控-易肇事肇祸	92
        //社会治理-重点人员管控-重症精神	93
        //社会治理-重点事件管理-重点人员	94
        //社会治理-重点事件管理-重点事件	95
        //formId = "89";//社会治理-重点人员管控-社区服刑人员
        String prefix = "http://wunlzt.cdwh.gov.cn/api/v4/forms/";
        String url;
        if(ifFormInfo){
            url = prefix + formId;
        }else {
            url = prefix + formId + "/responses/search.json";
            //在pagination或者query中自己添加?
            if(!Strings.isNullOrEmpty(pagination) || !Strings.isNullOrEmpty(query) ){
                url += "?";
            }
            if(!ObjectUtils.isEmpty(pagination)){
                url += pagination;
            }
            if(!ObjectUtils.isEmpty(query)){
                if(!ObjectUtils.isEmpty(pagination)) {
                    url += "&" + query;
                }else {
                    url += query;
                }
            }
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization","1942fc6b7aab121b22c892c920af8b74b9349f2611ce71d79a8324ce83227279:" +
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lc3BhY2VfaWQiOjF9.dgGuGkLelFnT9ups0kUoBw-AAOFsvUW_fl7HN3KVUWE");


        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpResponse = httpClient.execute(httpGet);
            /*HttpEntity httpEntity = closeableHttpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);//响应内容
            System.out.println("请求结果是：" + result);

            Header[] headers = closeableHttpResponse.getHeaders("X-SLP-Total-Count");
            String value = headers[0].getValue();*/

        } catch (Exception e) {
            log.error("中台接口请求表单{}失败{}",formId,e.getMessage());
            e.printStackTrace();
        }

        return closeableHttpResponse;
    }

    /**
     * 将中台返回的数据封装成柱状图、条形图、饼图形式的result
     * @param map 数据名称和对应field的map
     * @param data 中台返回的data集合
     * @return
     */
    public static JSONObject getPieResult(HashMap<String,String> map, JSONArray data){
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        for(Map.Entry<String,String> entry : map.entrySet()){
            columns.add(entry.getKey());
        }

        for(int i = 0; i < data.size() ; i++){
            JSONObject dataObj = (JSONObject) data.get(i);
            JSONObject row = new JSONObject();
            for(Map.Entry<String,String> entry : map.entrySet()){
                row.put(entry.getKey(),dataObj.get(entry.getValue()));
            }
            rows.add(row);
        }

        result.put("rows",rows);
        result.put("columns",columns);
        return result;
    }

    /**
     * 将中台给的数据封装为下拉项式文本框接口需要的数据，result内含一个用于下拉列表的List和用于获取fieldName相应结果的Map
     * @param fieldName
     * @param data
     * @return
     */
    public JSONObject getTextResult(String fieldName, JSONArray data){

        HashMap<String,JSONObject> nameAndObjMap = new HashMap<>();
        List<Map<String, String>> list = new ArrayList<>();
        for(int i = 0 ;i < data.size(); i++){
            JSONObject obj = data.getJSONObject(i);
            String name = obj.getString(fieldName);
            nameAndObjMap.put(name,obj);

            //有很多不同的数据，做数据条目的下拉列表
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("name",name);
            map.put("value",name);
            list.add(map);
        }

        JSONObject result = new JSONObject();
        result.put("list",list);
        result.put("nameAndObjMap",nameAndObjMap);

        return result;
    }

    public JsonModel getTextJsonModel(String param, String valueName, JSONObject obj){

        List<String> list = (List<String>) obj.get("list");
        HashMap<String,JSONObject> nameAndObjMap = (HashMap<String, JSONObject>) obj.get("nameAndObjMap");

        //传参则返回具体条目的数据，未传则返回下拉列表
        if(!Strings.isNullOrEmpty(param)){
            JSONObject valueObj = nameAndObjMap.get(param);
            String value = valueObj.getString(valueName);
            String unit = valueObj.getString("unit");
            JSONObject resultObj = new JSONObject();
            resultObj.put("name",param);
            resultObj.put("info",value);
            resultObj.put("unit",unit);

            return new JsonModel(true,resultObj);
        }

        return new JsonModel(true,list);

    }

    public JsonModel getPieToText(String param, JSONObject object){


        JSONArray columns = object.getJSONArray("columns");
        JSONArray rows = object.getJSONArray("rows");
        JSONObject row = rows.getJSONObject(0);

        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String,Object> nameAndObjMap = new HashMap<>();
        for(int i = 0 ;i < columns.size(); i++){
            String name = columns.getString(i);
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("name",name);
            linkedHashMap.put("value",name);
            list.add(linkedHashMap);
            nameAndObjMap.put(name,row.get(name));
        }

        if(!Strings.isNullOrEmpty(param)){
            Object value = nameAndObjMap.get(param);

            JSONObject resultObj = new JSONObject();
            resultObj.put("name",param);
            resultObj.put("info",value);
            resultObj.put("unit","");

            return new JsonModel(true,resultObj);
        }

        return new JsonModel(true,list);
    }

    /**
     * 将获取的数据封装为表格
     * @param formId 表单ID
     * @param column 需要展示的列，不传则展示所有的列
     * @return
     */
    public JsonModel getFormDataForTable(String formId, String column, String query, String url){
        String formInfo;
        try {
            formInfo = this.getData(formId,"",query,false,false);
        } catch (Exception e) {
            log.error("中台接口报错");
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
        JSONArray dataArray = JSONArray.parseArray(formInfo);
        //当是重点事件表时，将集合内数据打乱
        if("95".equals(formId)) {
            Collections.shuffle(dataArray);
        }
        JSONObject result = new JSONObject();
        List<String> columns = new ArrayList<>();
        JSONArray rows = new JSONArray();

        HashMap<Integer,String> idNameMap = new HashMap<>();
        try {
            idNameMap = this.getFormIdNameMap(formId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("表单fieldId和title的Map获取失败");
        }
        //查看详情需要展示所有字段，所以不只需要展示列字段的title，而是展示所有字段的title
        if(ObjectUtils.isEmpty(column)) {
//            idNameMap = this.getFormInfoMap(formId);
            for (Map.Entry<Integer, String> entry : idNameMap.entrySet()) {
                columns.add(entry.getValue());
            }
            //columns.add("查看详情");
        }else {
            List<String> list = Arrays.asList(column.split(","));
            for (String s : list) {
                if(s.contains("/")) {
                    String[] idAndName = s.split("/");
//                idNameMap.put(Integer.valueOf(idAndName[0]),idAndName[1]);
                    columns.add(idAndName[1]);
                }else {
                    columns.add(s);
                }
            }
        }

        //如果弹窗信息需要二次请求接口，就把接口url传给前端
        if(!Strings.isNullOrEmpty(url)){
            result.put("url",url);
            //从query中取出street参数
            if(!ObjectUtils.isEmpty(query) && query.contains("街道")) {
                String[] sArray = query.split("=");
                //街道和人员的map,保证每个街道都有一个人有走访详情
                HashMap<String, JSONObject> streetAndPersonMap = getStreetAndPersonMap();
                rows.add(streetAndPersonMap.get(sArray[1]));
            }
        }

        for(int i = 0; i < dataArray.size(); i++){
            JSONObject obj = dataArray.getJSONObject(i);
            JSONArray entries = obj.getJSONArray("entries");
            JSONObject row = new JSONObject();
            for (int j = 0; j < entries.size(); j++){
                JSONObject singleData = (JSONObject) entries.get(j);
//                if(!idNameMap.containsKey(singleData.get("field_id"))) continue;
                row.put(idNameMap.get(singleData.get("field_id")), singleData.get("value"));
            }
            if(columns.contains("详情")) {
                row.put("详情", "详情");
            }
            if(columns.contains("走访详情")) {
                row.put("走访详情", "走访详情");
            }
            rows.add(row);
        }

        result.put("columns",columns);
        result.put("rows",rows);

        return  new JsonModel(true,result);
    }

    //街道和人员的map,保证每个街道都有一个人有走访详情
    public HashMap<String,JSONObject> getStreetAndPersonMap(){
        HashMap<String,JSONObject> streetAndPersonMap = new HashMap<>();
        JSONObject person1 = new JSONObject();
        person1.put("姓名","毛智勇");
        person1.put("吸毒原因","好奇");
        person1.put("管控情况","在押");
        person1.put("走访详情","走访详情");
        streetAndPersonMap.put("浆洗街街道",person1);
        JSONObject person2 = new JSONObject();
        person2.put("姓名","朱强");
        person2.put("管控情况","社会面戒断不满三年");
        person2.put("走访详情","走访详情");
        streetAndPersonMap.put("玉林街道",person2);
        JSONObject person3 = new JSONObject();
        person3.put("姓名","刘开琼");
        person3.put("管控情况","社会面戒断三年未复吸");
        person3.put("走访详情","走访详情");
        streetAndPersonMap.put("红牌楼街道",person3);
        JSONObject person4 = new JSONObject();
        person4.put("姓名","李平");
        person4.put("管控情况","在押");
        person4.put("走访详情","走访详情");
        streetAndPersonMap.put("金花桥街道",person4);
        JSONObject person5 = new JSONObject();
        person5.put("姓名","周晓梅");
        person5.put("吸毒原因","亲友影响");
        person5.put("管控情况","在控");
        person5.put("走访详情","走访详情");
        streetAndPersonMap.put("望江路街道",person5);


        return streetAndPersonMap;
    }

    /**
     * 走访情况弹窗信息接口
     * @param formId 重点人员走访情况表单ID 106
     * @return
     */
    public JsonModel getFormDataForWindow(String formId, String params){
        String formInfo = "";
        List<String> list = Arrays.asList(params.split(","));
        //做一个表头title和value的map,再遍历它通过表头从idNameMap中获取表头对应的fieldId来组装query
        HashMap<String,String> nameValueMap = new HashMap<>();
        for (String s : list) {
            String[] strings = s.split(":");
            nameValueMap.put(strings[0],strings[1]);
        }
        String query = "";
        HashMap<String,Integer> NameIdMap = new HashMap<>();
        try {
            NameIdMap = this.getFormNameIdMap(formId);
        } catch (IOException e) {
            log.error("表单title和fieldId的Map获取失败");
            e.printStackTrace();
        }
        String pagination = "";
        for(Map.Entry<String,String> entry: nameValueMap.entrySet()){
            query += "query[" + NameIdMap.get(entry.getKey()) + "]=" + entry.getValue() + "&";
        }
        query = query.substring(0,query.lastIndexOf("&"));

        //获取指定走访情况数据
        try {
            formInfo = this.getData(formId,pagination,query,false,false);
        } catch (IOException e) {
            log.error("中台获取表单数据接口报错");
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
        JSONArray dataArray = JSONArray.parseArray(formInfo);
        JSONObject result = new JSONObject();
        List<String> columns = new ArrayList<>();
        JSONArray rows = new JSONArray();

        //获取走访情况表头信息
        HashMap<Integer,String> idNameMap = new HashMap<>();
        try {
            idNameMap = this.getFormIdNameMap(formId);
        } catch (IOException e) {
            log.error("表单fieldId和title的Map获取失败");
            e.printStackTrace();
        }
        for (Map.Entry<Integer, String> entry : idNameMap.entrySet()) {
            columns.add(entry.getValue());
        }

        //走访详情弹窗只需要一条数据
        for(int i = 0; i < dataArray.size(); i++){
//        for(int i = 0; i < 1; i++){
            if(i >=1) break;
            JSONObject obj = dataArray.getJSONObject(i);
            JSONArray entries = obj.getJSONArray("entries");
            JSONObject row = new JSONObject();
            for (int j = 0; j < entries.size(); j++){
                JSONObject singleData = (JSONObject) entries.get(j);
                row.put(idNameMap.get(singleData.get("field_id")), singleData.get("value"));
            }
            rows.add(row);
        }

        result.put("columns",columns);
        result.put("rows",rows);

        return  new JsonModel(true,result);
    }

    /**
     * 获取表单信息下拉列表
     * @param formId 表单ID
     * @return
     */
    public JSONArray getFormInfo(String formId) throws IOException {
        String formInfo = this.getData(formId,"","",true,false);
        JSONObject jsonObject = JSONObject.parseObject(formInfo);
        JSONArray fields = jsonObject.getJSONArray("fields");

        JSONArray res = new JSONArray();
        for(int i = 0;i < fields.size(); i++){
            JSONObject obj = fields.getJSONObject(i);
            Integer id = obj.getInteger("id");
            String title = obj.getString("title");
            if("包案领导".equals(title))title = "标题";
            res.add(MonitorUtils.newResultObj(title,id + "/" + title));
        }
        res.add(MonitorUtils.newResultObj("详情","详情"));

        return  res;
    }

    /**
     * 获取表单中fieldId和title的Map
     * @param formId 表单ID
     * @return
     */
    public HashMap<Integer,String> getFormIdNameMap(String formId) throws IOException {
        String formInfo = this.getData(formId,"","",true,false);
        JSONObject jsonObject = JSONObject.parseObject(formInfo);
        JSONArray fields = jsonObject.getJSONArray("fields");

        HashMap<Integer,String> idTitleMap = new HashMap<>();
        for(int i = 0;i < fields.size(); i++){
            JSONObject obj = fields.getJSONObject(i);
            Integer id = obj.getInteger("id");
            String title = obj.getString("title");
            if("包案领导".equals(title))title = "标题";
            idTitleMap.put(id,title);
        }

        return idTitleMap;
    }

    public JSONObject getFormDataForText(String formId,String query) {

        CloseableHttpResponse response = this.getResponse(formId, "", query, false);
        Header[] headers = response.getHeaders("X-SLP-Total-Count");
        String value = headers[0].getValue();
        JSONObject result = new JSONObject();
        result.put("name","总数");
        result.put("value",value);
        result.put("info",value);

        return result;
    }

    /**
     * 获取表单中title和fieldId的Map
     * @param formId 表单ID
     * @return
     */
    public HashMap<String,Integer> getFormNameIdMap(String formId) throws IOException {
        String formInfo = this.getData(formId,"","",true,false);
        JSONObject jsonObject = JSONObject.parseObject(formInfo);
        JSONArray fields = jsonObject.getJSONArray("fields");

        HashMap<String,Integer> titleIdMap = new HashMap<>();
        for(int i = 0;i < fields.size(); i++){
            JSONObject obj = fields.getJSONObject(i);
            Integer id = obj.getInteger("id");
            String title = obj.getString("title");
            titleIdMap.put(title,id);
        }
        return titleIdMap;
    }

    public void getDataByTime(DataJob job) throws SchedulerException {
        try {
            //这个方法中带有更新功能,但是更新功能无法更新job里面的实现,所以仍然需要进行重新创建
            scheduleManager.start(new Job<DataJob>() {
                @Override
                public void run() {
                    System.out.println(DateUtils.formatCommonDate(new Date()) + "————"+ job.getName() + "接口定时调用开始");
                    switch (job.getGroup()){
                        case "饼图" : getPieData(job);
                    }
                    System.out.println(DateUtils.formatCommonDate(new Date()) + "————"+ job.getName() + "接口定时调用结束");
                }
            }.name(job.getName()).target(job).group(job.getGroup()).cron(job.getCron()));
        } catch (Exception e) {
            log.error("开启计划定时任务异常：{}", e.getMessage());
        }

    }

    /**
     * 创建饼图类型的定时任务,每个任务保存后，创建的信息就可以删了或者保存一下
     */
    public void savePieData(){
        //需要提供的数据
        //枚举全量 有哪些类型
        List<String> types = Arrays.asList("征地拆迁","涉军安置","涉法涉诉");
        //表单ID
        String formId = "94";
        //属性ID
        String fieldId = "679";
        //属性name
        String fieldName = "涉访类别";

        //封装定时获取数据需要的数据
        JSONObject info = new JSONObject();
        info.put("types",types);
        info.put("formId",formId);
        info.put("fieldId",fieldId);
        info.put("fieldName",fieldName);

        String conf = info.toString();

        TimeData data = new TimeData();
        data.setId(1L);
        data.setName("涉访类别详细分析");
        data.setType("饼图");
        // 0 0/5 * * * ?   5分钟一次
        // 0 0 0/1 * * ?   1小时一次
        data.setCron("0 0/5 * * * ? ");
        data.setConf(conf);
        timeDataDao.save(data);

    }

    public JSONObject getPieData(DataJob job){

        String conf = job.getConf();

        JSONObject confObj = JSONObject.parseObject(conf);
        List<String> typeList = (List<String>) confObj.get("types");
        String formId = (String) confObj.get("formId");
        String fieldId = (String) confObj.get("fieldId");
        String fieldName = (String) confObj.get("fieldName");

        Map<Object,Object> nameAndCount = new HashMap<>();
        for(String name : typeList){
            String query = "query[" + fieldId + "]=" + name;
            CloseableHttpResponse response = getResponse(formId, "", query, false);
            Header[] headers = response.getHeaders("X-SLP-Total-Count");
            String count = headers[0].getValue();
            nameAndCount.put(name,count);
        }

        JSONObject json = new JSONObject();
        json.put("columns", new JSONArray(Arrays.asList(fieldName, "数量")));
        JSONArray rows = new JSONArray();
        for(Map.Entry<Object, Object> entry : nameAndCount.entrySet()){
            rows.add(MonitorUtils.newResultObj("涉访类别",entry.getKey(),"数量",
                    entry.getValue()));
        }

        json.put("rows",rows);
        String value = json.toString();
        System.out.println(value);

        confObj.put("value",value);
        job.setConf(confObj.toString());

        String name = job.getName();
        Long id = Long.valueOf(Arrays.asList(name.split("_")).get(1));
        TimeData data = timeDataDao.findOne(id);
        data.setValue(value);
        //将结果存入库中
        timeDataDao.save(data);
        System.out.println("数据库中的value是:" + data.getValue());

        return json;
    }

    public List<DataJob> createJobFromTimeData(List<TimeData> list){

        List<DataJob> jobList = new ArrayList<>();
        for(TimeData data : list){
            DataJob job = new DataJob();
            job.setName(data.getName()+"_"+data.getId());
            job.setGroup(data.getType());
            job.setCron(data.getCron());
            job.setConf(data.getConf());
            jobList.add(job);
        }
        return jobList;

    }

    /**
     * 获取浆洗街道文本数据
     * @param formId 表单ID
     * @return
     */
    public JSONObject getJXData(String formId,String query) throws IOException {
        String formInfo;
        formInfo = this.getData(formId,null,query,false,false);
        HashMap<Integer,String> idNameMap = new HashMap<>();
        try {
            idNameMap = this.getFormIdNameMap(formId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("表单fieldId和title的Map获取失败");
        }
        JSONArray info = new JSONArray();
        JSONArray dataArray = JSONArray.parseArray(formInfo);
        if (dataArray.size() == 0){
            return new JSONObject();
        }
        JSONObject dataObj = (JSONObject) dataArray.get(0);
        JSONArray entries = (JSONArray) dataObj.get("entries");
        HashMap<Integer,String> valueMap = new HashMap<>();
        for (int i = 0;i < entries.size(); i++){
            JSONObject obj = (JSONObject) entries.get(i);
            Integer fieldId = obj.getInteger("field_id");
            info.add(MonitorUtils.newResultObj(idNameMap.get(fieldId),fieldId));
            valueMap.put(fieldId, (String) obj.get("value"));
        }
        JSONObject res = new JSONObject();
        res.put("info",info);
        res.put("value",valueMap);

        return res;
    }

    /**
     * 社区人口排行
     * @param query
     * @return
     */
    public JsonModel getPopulationRanking(String query){
        String formInfo;
        try {
            formInfo = this.getData("110","?per_page=100",query,false,false);
        } catch (Exception e) {
            log.error("中台接口报错");
            e.printStackTrace();
            return new JsonModel(false,e.getMessage(),MonitorUtils.empObj());
        }

        JSONArray dataArray = JSONArray.parseArray(formInfo);
        JSONObject result = new JSONObject();
        List<String> columns = new ArrayList<>();
        JSONArray rows = new JSONArray();
        columns.add("社区");
        columns.add("实有人口数");

        //110人口表 778社区 777街道 779实有人口数
        for(int i = dataArray.size()-1; i >= 0; i--) {
            JSONObject obj = dataArray.getJSONObject(i);
            JSONArray entries = obj.getJSONArray("entries");
            JSONObject row = new JSONObject();
            for (int j = 0; j < entries.size(); j++) {
                JSONObject singleData = (JSONObject) entries.get(j);
                if (singleData.getInteger("field_id") == 778) {
                    row.put("社区", singleData.get("value"));
                }
                if (singleData.getInteger("field_id") == 779) {
                    row.put("实有人口数", singleData.get("value"));
                }
            }
            rows.add(row);
        }

        result.put("columns",columns);
        result.put("rows",rows);

        return new JsonModel(true,result);
    }

    public void initTimeData(){
        Long count = timeDataDao.count();
        try {
            if (count > 0) {
                timeDataDao.deleteAll();
            }
            initFile(FILEPATH);
        } catch (FileNotFoundException e) {
            log.error("未找到定时任务sql的文件[init_time_data.sql]", e);
        } catch (IOException e) {
            log.error("未找到定时任务sql的文件[init_time_data.sql]", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initOrgan(){
        Long count = tibetOrganizationTableDao.count();
        try {
            if (count == 0) {
                initFile(JIGOUPATH);
            }
        } catch (FileNotFoundException e) {
            log.error("未找到定时任务sql的文件[jx_orga_table2.sql]", e);
        } catch (IOException e) {
            log.error("未找到定时任务sql的文件[jx_orga_table2.sql]", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initCommunity(){
        Long count = tibetCommunityTableDao.count();
        try {
            if (count == 0) {
                initFile(XIAOQUPATH);
            }
        } catch (FileNotFoundException e) {
            log.error("未找到定时任务sql的文件[jx_orga_table1.sql]", e);
        } catch (IOException e) {
            log.error("未找到定时任务sql的文件[jx_orga_table1.sql]", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initFile(String filePath) throws IOException {
        new ClassPathResourceWalker(filePath).forEach(file -> {
            InputStream in;
            try {
                in = file.openStream();
            } catch (IOException e) {
                return;
            }
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] cache = new byte[1024];
                int size = -1;
                while ((size = in.read(cache)) != -1) {
                    out.write(cache, 0, size);
                }
                String sql = out.toString("UTF-8");
                System.out.println(sql);
                template.execute(sql);
                System.out.println(filePath + "文件已导入数据库");
            } catch (IOException e) {
                log.error("", e);
            } finally {
                try {
                    in.close();
                } catch (Exception e) {
                    log.error("关闭流失败", e);
                }
            }
        });
    }

    public JsonModel getHousingPersonnelTop() {
        JSONObject res = new JSONObject();
        JSONObject dataArry = new JSONObject();
        JSONArray nameArry = new JSONArray();
        nameArry.add("高频入住人员TOP10");
        nameArry.add("高流动性人员TOP10");
        dataArry.put("nameArry",nameArry);
        JSONArray dataArray = new JSONArray();
        //获取高频入住top10
        JSONObject checkInTopObject = new JSONObject();
        JSONArray checkInColumns = new JSONArray();
        checkInColumns.add("姓名");
        checkInColumns.add("次数");
        JSONArray checkInTopRows = new JSONArray();
        List<String[]> checkInTop = wuhouHotelRegisterDao.getHousingPersonnelTop(10);
        for (int i = 0; i < checkInTop.size(); i++) {
            JSONObject item = new JSONObject();
            item.put(checkInColumns.getString(0),checkInTop.get(i)[0]);
            item.put(checkInColumns.getString(1),checkInTop.get(i)[1]);
            checkInTopRows.add(item);
        }
        checkInTopObject.put("columns",checkInColumns);
        checkInTopObject.put("unit","次");
        checkInTopObject.put("rows",checkInTopRows);
        dataArray.add(checkInTopObject);
        //获取高流动性人群top10
        JSONObject flowTopObject = new JSONObject();
        JSONArray flowColumns = new JSONArray();
        flowColumns.add("姓名");
        flowColumns.add("家");
        JSONArray flowTopRows = new JSONArray();
        List<String[]> flowTop = wuhouHotelRegisterDao.getFlowHousingPersonnelTop(10);
        for (int i = 0; i < flowTop.size(); i++) {
            JSONObject item = new JSONObject();
            item.put(flowColumns.getString(0),flowTop.get(i)[0]);
            item.put(flowColumns.getString(1),flowTop.get(i)[1]);
            flowTopRows.add(item);
        }
        flowTopObject.put("columns",flowColumns);
        flowTopObject.put("unit","家");
        flowTopObject.put("rows",flowTopRows);
        dataArray.add(flowTopObject);
        //封装返回值
        dataArry.put("dataArry",dataArray);
        res.put("dataArry",dataArry);
        return new JsonModel(true,res);

    }

    public JsonModel getTodayWeather(String fieldName) throws IOException {

        //weather  天气，如多云转云
        //temperature_curr 当前温度
        //winp  风力等级
        //humidity 湿度

        String url = "http://api.k780.com/?app=weather" +
                ".today&cityNm=成都&appkey=64621&sign=c02c030e1ee59910ce7a8762611935bc&format=json";
        JSONObject obj = getWeatherData(url);
        JSONObject data = (JSONObject) obj.get("result");
        String value = (String) data.get(fieldName);
        JSONObject result = new JSONObject();
        result.put("name",fieldName);
        result.put("value",value);
        result.put("info",value);


        return new JsonModel(true,result);
    }

    /**
     * 从天气接口获取未来5-7天的温度预报
     * @return
     * @throws IOException
     */
    public JsonModel getFutureWeather() throws IOException{

        String url = "http://api.k780.com/?app=weather.future&cityNm=成都&ag=today,futureDay,lifeIndex," +
                "futureHour&appkey=64621&sign=c02c030e1ee59910ce7a8762611935bc&format=json";
        JSONObject obj = getWeatherData(url);
        JSONArray array = (JSONArray) obj.get("result");
        JSONObject result = new JSONObject();
        List<String> columns = Arrays.asList("日期","最高℃", "最低℃");
        JSONArray rows = new JSONArray();
        for (int i = 0 ; i < array.size(); i++){
            JSONObject data = (JSONObject) array.get(i);
            JSONObject row = new JSONObject();
            String day = (String) data.get("days");
            day = day.substring(5);
            day = day.replace("0","");
            day = day.replace("-",".");
            row.put("日期",day);
            String temperature = (String) data.get("temperature");
            temperature = temperature.replace("℃","");
            String[] temp = temperature.split("/");
            row.put("最高℃",temp[0]);
            row.put("最低℃",temp[1]);
            rows.add(row);
        }
        result.put("rows",rows);
        result.put("columns",columns);

        return new JsonModel(true,result);
    }

    /**
     * 调用api获取天气数据
     * @param url 不同的数据地址不同
     * @return
     * @throws IOException
     */
    public JSONObject getWeatherData(String url) throws IOException{

        String urlZH = "http://api.k780.com/?app=weather.realtime&cityNm=成都&ag=today,futureDay,lifeIndex," +
                "futureHour&appkey=64621&sign=c02c030e1ee59910ce7a8762611935bc&format=json";

        String urlYUBAO = "http://api.k780.com/?app=weather.future&cityNm=成都&ag=today,futureDay,lifeIndex," +
                "futureHour&appkey=64621&sign=c02c030e1ee59910ce7a8762611935bc&format=json";

        URL u= new URL(url);

        InputStream in=u.openStream();
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        try {
            byte buf[]=new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        }  finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[]=out.toByteArray( );
        String string = new String(b,"utf-8");
        System.out.println(new String(b,"utf-8"));
        System.out.println(new String(b,"utf-8"));

        //实时天气的返回结果中的"result"是JONObject，天气预报的是JSONArray，所以统计返回外层的result，拿到后再解析
        JSONObject result = JSONObject.parseObject(string);
        return result;
    }

    public JsonModel getOrgaInfo(){
        List<TibetOrganizationTable> list = organizationTableDao.findAll();
        return new JsonModel(true,list);
    }

    public JsonModel getCommunityInfo(){
        List<TibetCommunityTable> list = communityTableDao.findAll();
        return new JsonModel(true,list);
    }

    public JsonModel getOrganById(Long id){

        TibetOrganizationTable result = organizationTableDao.findOne(id);
        return new JsonModel(true,result);

    }

    public JsonModel getCommunityById(Long id){
        TibetCommunityTable result = communityTableDao.findOne(id);
        return new JsonModel(true,result);
    }

    public JsonModel getStorePie(String type) {

        String data = null;
        try {
            data = getData("y07-01", "per_page=10000&page=1", null, false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }
        JSONObject object = JSONObject.parseObject(data);
        JSONArray jsonArray = object.getJSONArray("data");

//        String type1 = "store_character";
//        String type2 = "store_charge_user_nation";

        //用来装商铺性质的set
        HashSet<String> set = new HashSet<>();
        //用来装商铺性质和数量的map
        HashMap<String,Integer> countMap = new HashMap<>();
        if(!ObjectUtils.isEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                String fieldName = obj.getString(type);
                if (set.contains(fieldName)) {
//                    Integer count = countMap.get(fieldName);
                    Integer count = countMap.get(fieldName);
                    count++;
                    countMap.put(fieldName, count);
                } else {
                    set.add(fieldName);
                    countMap.put(fieldName, 1);
                }
            }
        }

        JSONObject result = new JSONObject();
        List<String> columns = Arrays.asList("商铺性质","数量");
        JSONArray rows = new JSONArray();
        for(Map.Entry<String,Integer> entry : countMap.entrySet()){
            JSONObject row = new JSONObject();
            row.put("商铺性质",entry.getKey());
            row.put("数量",entry.getValue());
            rows.add(row);
        }
        result.put("rows",rows);
        result.put("columns",columns);
        result.put("unit","家");
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);
    }

    public JsonModel getOrgaDot() {

        String data = null;
        try {
            data = getData("y09-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }
        JSONObject object = JSONObject.parseObject(data);
        JSONArray jsonArray = object.getJSONArray("data");

        //装经度的set
        HashSet<String> longSet = new HashSet<>();
        //经度和场所的map
        HashMap<String,JSONArray> longMap = new HashMap<>();

        if(!ObjectUtils.isEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                String longitude = obj.getString("longitude");
                if (longSet.contains(longitude)) {
                    JSONArray array = longMap.get(longitude);
                    array.add(obj);
                } else {
                    longSet.add(longitude);
                    JSONArray array = new JSONArray();
                    array.add(obj);
                    longMap.put(longitude, array);
                }
            }
        }

        JSONArray result = new JSONArray();
        for (Map.Entry<String,JSONArray> entry : longMap.entrySet()){
            JSONObject obj = new JSONObject();
            JSONObject dataObj = (JSONObject) entry.getValue().get(0);
            String longitude = (String) dataObj.get("longitude");
            String latitude = (String) dataObj.get("latitude");
            obj.put("longitude",longitude);
            obj.put("latitude",latitude);
            obj.put("arr",entry.getValue());
            result.add(obj);
        }

        return new JsonModel(true,result);
    }

    public JsonModel getJXStoreCount(){
        String data = null;
        try {
            data = getData("y11-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }
        JSONObject object = JSONObject.parseObject(data);
        JSONArray jsonArray = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        List<String> columns = Arrays.asList("社区","数量");
        JSONArray rows = new JSONArray();

        if(!ObjectUtils.isEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                JSONObject row = new JSONObject();
                row.put("社区", obj.get("community_name"));
                row.put("数量", obj.get("number"));
                rows.add(row);
            }
        }

        result.put("rows",rows);
        result.put("columns",columns);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    public JsonModel getJXTibetSourceCount() {
        String data = null;
        try {
            data = getData("y12-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(data);
        JSONArray jsonArray = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        List<String> columns = Arrays.asList("来源地","数量");
        JSONArray rows = new JSONArray();

        if(!ObjectUtils.isEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                if (i >= 10) break;
                JSONObject obj = (JSONObject) jsonArray.get(i);
                JSONObject row = new JSONObject();
                row.put("来源地", obj.get("district"));
                row.put("数量", obj.get("cnt"));
                rows.add(row);
            }
        }

        result.put("rows",rows);
        result.put("columns",columns);
        result.put("unit","次");
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);
    }

    /**
     * 群租房：入住人员对比
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/y08-01/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF1(){

        String res = null;
        try {
            res = getData("y08-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("人员类别","tern");
        map.put("数量","num");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 群租房：群租房入住人员归属地分析
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF2(){

        String res = null;
        try {
            res = getData("y13-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("省","province");
        map.put("市","city");
        map.put("区县","district");
        map.put("数量","cnt");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }


    //——————————————————————————————————————————————————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————————————————————————————————————————————————————————
    //下面是智慧武侯16：3大屏的方法
    //————————————————————————经济板块——————————————————————————————————————————————————————————————————
    /**
     * 1、土地资源（待上市地块）总数
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w01-01-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ1(){

        String res = null;
        try {
            res = getData1("w01-01-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        Integer count = 0;
        //中台接口是否调用成功
        Boolean ifSuccess = false;
        if(!ObjectUtils.isEmpty(data)) {
            ifSuccess = true;
            JSONObject total = (JSONObject) data.get(0);
            count = (Integer) total.get("total");
        }

        JSONObject result = new JSONObject();
        result.put("name","土地资源总数");
        result.put("info",ifSuccess ? count : "中台接口调用失败");
        result.put("value",ifSuccess ? count : "中台接口调用失败");

        return new JsonModel(true,result);

    }

    /**
     * 2、土地资源（待上市地块）分类汇总
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w01-01-2/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ2(){

        String res = null;
        try {
            res = getData1("w01-01-2","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("用地性质","ydxz");
        map.put("数量","num");
        JSONObject result = result = getPieResult(map,data);

        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 3、土地资源（待上市地块）分类列表明细
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w01-01-3/data?ydxz=商业用地
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ3(String param){

        String query = "ydxz=" + param;
        String res = null;
        try {
            res = getData1("w01-01-3","per_page=10000&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("功能区管委会","gnqgwh");
        map.put("宗地位置","zdwz");
        map.put("净地面积","jdmj");
        map.put("容积率","rjl");
        map.put("业主单位","yzdw");
        map.put("所属街道","street");
        map.put("用地性质","ydxz");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);

        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);
    }


    /**
     * 4、地图坐标 土地资源
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w01-01-4/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ4(){

        String res = null;
        try {
            res = getData1("w01-01-4","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("ID","id");
        map.put("宗地位置","zdwz");
        map.put("坐标","location");

        JSONObject result = getPieResult(map,data);

        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);
    }


    /**
     * 5、地图 土地资源-详情
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w01-01-5/data?id=371
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ5(String param){

        String query = "id=" + param;
        String res = null;
        try {
            res = getData1("w01-01-5","per_page=10000&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("用地性质","ydxz");
        map.put("容积率","rjl");
        map.put("净地面积","jdmj");
        map.put("宗地位置","zdwz");
        map.put("所属街道","street");
        map.put("业主单位","yzdw");
        map.put("功能区管委会","gnqgwh");
        JSONObject result = getPieResult(map,data);

        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);
    }

    /**
     * 6、重大项目情况-项目分类统计
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w01-02-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ6(){

        String res = null;
        try {
            res = getData1("w01-02-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }
        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = getTextResult("project_type",data);

        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);
    }

    /**
     * 7、重大项目情况-项目分类明细
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w01-02-2/data?project_type=其它高能级项目
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ7(String param){

        String query = "project_type=" + param;

        String res = null;
        try {
            res = getData1("w01-02-2","per_page=10000&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("项目名称","project_type");
        map.put("投资额","investment");
        map.put("功能区","functional_area");
        map.put("街道","street");
        map.put("项目类型","project_type");
        JSONObject result = getPieResult(map,data);

        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);
    }

    /**
     * 8、内外资投资情况
     * (中台开发中)
     *接口URL： {{baseUrl}}/apis/daas/pro/1/components/w01-03-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ8(){

        String res = null;
        try {
            res = getData1("w01-03-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("内资","nz");
        map.put("外资","wz");
        JSONObject result = getPieResult(map,data);

        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);
    }

    /**
     * 9、内外资投资情况 月度列表
     * (中台开发中)
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w01-03-2/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ9(String param){

        String res = null;
        try {
            res = getData1("w01-03-2","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("月份","month");
        if("内资".equals(param)) {
            map.put("内资月度认定数", "nzydrds");
            map.put("内资同比增减情况（%）", "increase_or_decrease_rmb");
        }else {
            map.put("外资月度认定数", "wzydrds");
            map.put("外资同比增减情况（%）", "increase_or_decrease_dollar");
        }
        JSONObject result = getPieResult(map,data);

        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);
    }

    /**
     * 10、领导率队赴外开展投资促进活动情况
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w029/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ10(){

        String res = null;
        try {
            res = getData1("w029","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("拜访时间","visit_time");
        map.put("拜访地址","visit_address");
        map.put("拜访企业名称","visit_company_name");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 11、企业发展情况(数据源未定)
     * 接口URL： {{baseUrl}}/wuhouapi/proxyapi/publicapi/rest/project/detail
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ11(String ifToday, String fieldType){
        //ifToday: thisyear、today
        //    "zxnum": 1191, //企业注销数量
        //    "gtzxnum": 800, //个体注销数量
        //    "slnum": 1579, //企业注册数量
        //    "gtslnum": 1181 //个体注册数量
        String res = null;
        try {
            res = getData2("http://wunlzt.cdwh.gov.cn/wuhouapi/proxyapi/publicapi/rest/project/detail",true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONObject custom = object.getJSONObject("custom");
        JSONObject data = custom.getJSONObject(ifToday);
        //具体的数据
        String value = data.getString(fieldType);

        JSONObject result = new JSONObject();
        result.put("name",fieldType);
        result.put("value",value);
        result.put("info",value);

        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 12、经济板块--所有企业行业分类情况
     * （中台修改中）
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w01-06-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ12(){

        String res = null;
        try {
            res = getData1("w01-06-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        return null;
    }

    /**
     * 13、所有企业行业分类情况（未定）
     * 中台修改中
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w004/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getJJ13(){

        String res = null;
        try {
            res = getData1("w004","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        return null;
    }

    //—————————————————————————————————————————事件板块——————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————

    /**
     *1、今日重大活动信息
     * 中台开发中
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-01-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent1(){

        String res = null;
        try {
            res = getData1("w02-01-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("名称","name");
        map.put("地点","address");
        map.put("事件","time");
        map.put("明细","details");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     *2、  110非警推送-警综管辖 分类统计
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-02-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent2(){

        String res = null;
        try {
            res = getData1("w02-02-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("警综管辖","policing");
        map.put("数量","num");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     *3、  110非警推送-明细
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-02-2/data?policing=武侯金花桥所
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent3(String param){
        String query = "policing=" + param;
        String res = null;
        try {
            res = getData1("w02-02-2","per_page=10000&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //"alarm_content": "男：7人讨要工资，需要警察帮助 ", //报警内容
        //"policing": "武侯金花桥所", //警综管辖
        //"alarm_address": "武侯区金兴北路陆坝小学门口等（在建的小学）", //事发地址
        //"alarm_phone": "18227684323", //报警电话
        //"alarm_time": "2021-12-09 17:27:01", //报警时间
        //"alarm_type": "社会联动警情" //类型
        map.put("报警内容","alarm_content");
        map.put("警综管辖","policing");
        map.put("事发地址","alarm_address");
        map.put("报警电话","alarm_phone");
        map.put("报警时间","alarm_time");
        map.put("类型","alarm_type");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     *4、 网络舆情分类分析
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-03-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent4(){

        String res = null;
        try {
            res = getData1("w02-03-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("来源","source");
        map.put("占比（%）","percentage");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     *5、 网络舆情高频词
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-04-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent5(){

        String res = null;
        try {
            res = getData1("w02-04-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //    "term": "肺炎", //高频词
        //    "num": 4335 //高频词出现次数
        map.put("高频词","term");
        map.put("高频词出现次数","num");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     *6、 数据分析（左）无
     * 中台修改中
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-05-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent6(){

        String res = null;
        try {
            res = getData1("w02-05-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("","");
        map.put("","");
        map.put("","");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     *7、 事件类型分类-来源下拉
     * 中台修改中
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-06-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent7(){

        String res = null;
        try {
            res = getData1("w02-06-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("来源","source");
        map.put("数量","num");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     *8、 事件类型分类-事件类型 占比
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-06-2/data?source
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent8(){

        String res = null;
        try {
            res = getData1("w02-06-2","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("事件类型","event_type");
        map.put("数量","num");
        map.put("占比（%）","percentage");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     *9、 事件类型分类-事件列表
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-06-3/data?source=12345
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent9(){

        String res = null;
        try {
            res = getData1("w02-06-3","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("上报时间","event_time");
        map.put("事件内容","event_content");
        map.put("责任单位","responsible_unit");
        map.put("办理状态","handle_state");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     *10、 事件类型分类-事件详情
     * 中台修改中
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-06-4/data?id=15970
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent10(){

        String res = null;
        try {
            res = getData1("w02-06-4","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("来源","source");
        map.put("handle_reply","handle_reply");
        map.put("责任单位","responsible_unit");
        map.put("事件内容","event_content");
        map.put("上报时间","event_time");
        map.put("事件类型","event_type");
        map.put("街道","street");
        map.put("社区","community");
        map.put("place_of_complaint","place_of_complaint");
        map.put("办理状态","handle_state");
        map.put("办理详情","handle_detail");
        map.put("event_sub_type","event_sub_type");
        map.put("办理时间","handle_time");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     *11、 事件类型分类-数据分析
     * 中台修改中
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-07-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent11(){

        String res = null;
        try {
            res = getData1("w02-07-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("日期","date");
        map.put("数量","num");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 12、 地图-热力图坐标
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-08-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent12(){

        String res = null;
        try {
            res = getData1("w02-08-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("坐标","location");
        map.put("数量","val");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 13、地图-派出所点位
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-09-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent13(){

        String res = null;
        try {
            res = getData1("w02-09-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("名称","name");
        map.put("坐标","location");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    //—————————————————————————————————————————生态板块——————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————

    /**
     * 1、天气-天气预报
     * 返回内容过多，根据实际需求来确定需要的数据
     * 接口URL： {{baseUrl}}/apis/weather/v1/weather?cityNm=成都
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getST1(){

        String res = null;
        try {
            res = getData2("http://wunlzt.cdwh.gov.cn/apis/weather/v1/weather?cityNm=成都",true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONObject data = object.getJSONObject("data");

        /*LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("名称","name");
        map.put("坐标","location");
        JSONObject result = getResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);*/

        return new JsonModel(true);

    }

    /**
     * 2、域内河流
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-02-1/data?monitoring_type
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getST2(){

        String query = "monitoring_type";
        String res = null;
        try {
            res = getData1("w03-02-1","per_page=10000&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("监测类型","monitoring_type");
        map.put("数量","num");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 3、域内河流-监测点-类型-列表
     * 返回内容过多，根据实际需求来确定需要的数据
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-02-2/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getST3(){

        String res = null;
        try {
            res = getData1("w03-02-2","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        /*map.put("id","id");
        map.put("名称","name");
        map.put("坐标","location");*/
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 4、地图-监测点
     * 返回内容过多，根据实际需求来确定需要的数据
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-02-3/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getST4(){

        String res = null;
        try {
            res = getData1("w03-02-3","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        /*map.put("id","id");
        map.put("名称","name");
        map.put("坐标","location");*/
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 5、环境/绿地（统计街道面积）
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-03-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getST5(){

        String res = null;
        try {
            res = getData1("w03-03-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("所属街道","street");
        map.put("绿地面积（平方公里）","green_space_area");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 6、环境/垃圾
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-04-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getST6(){

        String res = null;
        try {
            res = getData1("w03-04-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("月份","sdate");
        map.put("垃圾处理总量（吨）","total_processing");
        map.put("可回收垃圾处理量（吨）","recyclable_trash");
        map.put("厨余垃圾处理量（吨）","kitchen_waste");
        map.put("其他垃圾处理量（吨）","other_garbage");
        map.put("有害垃圾处理量（吨）","hazardous_waste");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 7、大气-成都市aqi
     * 接口URL： {{baseUrl}}/apis/weather/v1/aqi
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getST7(String param){

        String res = null;
        try {
            res = getData2("http://wunlzt.cdwh.gov.cn/apis/weather/v1/aqi",true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONObject data = object.getJSONObject("data");

        //该接口的所有数据
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("aqi","AQI");
        map.put("二氧化硫","F_SQ2IAQI");
        map.put("二氧化氮","F_NO2IAQI");
        map.put("可吸入颗粒物（PM10）","F_PM10IAQI");
        map.put("一氧化碳","F_CO1IAQI");
        map.put("其他垃圾处理量（吨）","other_garbage");
        map.put("臭氧","F_O3IAQI");
        map.put("细颗粒物（PM2.5）","F_PM25IAQI");
        map.put("—","F_FIRSTPOLLUTANTNAME");
        map.put("时间","F_DATATIME");
        map.put("F_MONITORSTATIONID","F_MONITORSTATIONID");
        map.put("color","color");
        map.put("等级","Level");
        map.put("str","str");
        map.put("经纬度","Point");
        JSONObject allData = new JSONObject();
        JSONObject result = new JSONObject();
        for (Map.Entry<String,String> entry : map.entrySet()){
            allData.put(entry.getKey(),data.get(entry.getValue()));
        }
        result.put("name",param);
        result.put("info",data.get(param));

        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 8、大气-成都市 近30天AQI
     * 接口URL： {{baseUrl}}/apis/weather/v1/aqiday
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getST8(){

        String res = null;
        try {
            res = getData2("http://wunlzt.cdwh.gov.cn/apis/weather/v1/aqiday",true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONObject data = object.getJSONObject("data");
        JSONArray aqi = data.getJSONArray("aqi");
        JSONArray day = data.getJSONArray("day");

        List<String> columns = Arrays.asList("数值","颜色","日期");
        JSONArray rows = new JSONArray();
        for (int i = 0; i<aqi.size();i++){
            JSONObject row = new JSONObject();
            JSONObject aqiDay = aqi.getJSONObject(i);
            Object date = day.get(i);
            row.put("数值",aqiDay.get("y"));
            row.put("颜色",aqiDay.get("color"));
            row.put("日期",date);
            rows.add(row);
        }
        JSONObject result = new JSONObject();
        result.put("rows",rows);
        result.put("columns",columns);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 9、环境/绿地（道路绿地）
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-03-2/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getST9(){

        String res = null;
        try {
            res = getData1("w03-03-2","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("街道办","street");
        map.put("道路绿地","road_green");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    //—————————————————————————————————————————安全板块——————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————

    /**
     * 1、危险源情况
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-01-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ1(){

        String res = null;
        try {
            res = getData1("w04-01-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("单位名称","company_name");
        map.put("警报类型","alarm_type");
        map.put("警报时间","alarm_time");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 2、危险源详情
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-01-2/data?id=6826
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ2(String param){
        String query = "id=" + param;
        String res = null;
        try {
            res = getData1("w04-01-2","per_page=10000&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("警报时间","alarm_time" );
        map.put("单位名称","company_name");
        map.put("重点重大","important");
        map.put("警报类型","alarm_type");
        map.put("警报信息描述","alarm_information");
        map.put("处置详情","handle_details");
        map.put("处置方式","handle_model");
        map.put("状态","status");
        map.put("响应处置时间","handle_time");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 3、报警类型数量
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-01-3/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ3(){

        String res = null;
        try {
            res = getData1("w04-01-3","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("警报类型","alarm_type");
        map.put("数量","num" );
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 4、企业危险源情况top10
     * 中台开发中
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-01-4/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ4(){

        String res = null;
        try {
            res = getData1("w04-01-4","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("数量","num" );
        map.put("单位名称","company_name");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 5、报警变化趋势-时间
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-01-5/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ5(){

        String res = null;
        try {
            res = getData1("w04-01-5","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("时间","time");
        map.put("数量","num" );
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 6、报警变化趋势-分类
     * 接口URL：{{baseUrl}}/apis/daas/pro/1/components/w04-01-6/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ6(){

        String res = null;
        try {
            res = getData1("w04-01-6","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("警报类型","alarm_type");
        map.put("数量","num" );
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 7、隐患排查情况-年（当年）
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ7(){

        String res = null;
        try {
            res = getData1("w04-02-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("已完成","finished");
        map.put("进行中","processing" );
        map.put("总数","total" );
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 8、隐患排查情况-月(当月)
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-2/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ8(){

        String res = null;
        try {
            res = getData1("w04-02-2","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("已完成","finished");
        map.put("进行中","processing" );
        map.put("总数","total" );
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 9、隐患报警列表
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-3/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ9(){

        String res = null;
        try {
            res = getData1("w04-02-3","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("单位名称","unit_name");
        map.put("整改情况","governance_status");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 10、隐患报警列表-明细
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-4/data?id=6948
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ10(String param){

        String query = "id=" + param;
        String res = null;
        try {
            res = getData1("w04-02-4","per_page=10000&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("place_name","place_name");
        map.put("单位名称","unit_name");
        map.put("餐馆类型","place_type");
        map.put("所属街道","territorial_unit");
        map.put("key_parts","key_parts");
        map.put("hidden_dangers_describe","hidden_dangers_describe");
        map.put("整改情况","governance_status");
        map.put("inspection_date","inspection_date");
        map.put("closing_date","closing_date");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 11、隐患趋势变化-年
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-5/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ11(){

        String res = null;
        try {
            res = getData1("w04-02-5","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("时间","time");
        map.put("数量","num");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 12、隐患趋势变化-月
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-6/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ12(){

        String res = null;
        try {
            res = getData1("w04-02-6","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("时间","time");
        map.put("数量","num");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 13、隐患趋势变化-天
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-6-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ13(){

        String res = null;
        try {
            res = getData1("w04-02-6-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("时间","time");
        map.put("数量","num");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 14、隐患企业统计排行TOP榜单
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-7/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ14(){

        String res = null;
        try {
            res = getData1("w04-02-7","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("单位名称","unit_name");
        map.put("数量","num");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 15、地图-街道办-隐患报警数量 热力
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-03-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ15(){

        String res = null;
        try {
            res = getData1("w04-03-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("街道","street");
        map.put("数量","val");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    //—————————————————————————————————————————区情板块——————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————


    /**
     * 1、区情板块-社区常住人口排行
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w05-01-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQQ1(){

        String res = null;
        try {
            res = getData1("w05-01-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("社区","community");
        map.put("常住人口","population");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 2、数字武侯
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w05-02-1/data?id=6826
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQQ2(){

        String res = null;
        try {
            res = getData1("w05-02-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = getTextResult("name",data);

        /*//name和相应obj的map,用于根据参数name获取对应的数据
        HashMap<String,JSONObject> nameAndObjMap = new HashMap<>();
        List<Map<String, String>> list = new ArrayList<>();
        for(int i = 0 ;i < data.size(); i++){
            JSONObject obj = data.getJSONObject(i);
            String name = obj.getString("name");
            nameAndObjMap.put(name,obj);

            //有很多不同的数据，做数据条目的下拉列表
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("name",name);
            map.put("value",name);
            list.add(map);
        }

        JSONObject result = new JSONObject();
        result.put("list",list);
        result.put("nameAndObjMap",nameAndObjMap);*/

        return new JsonModel(true,result);

    }

    /**
     * 3、行政区划-地图数据
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w05-03-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQQ3(){

        String res = null;
        try {
            res = getData1("w05-03-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("所属类别","category");
        map.put("名称","name");
        map.put("地理位置","address");
        map.put("坐标","location");
        map.put("责任人","person_liable");
        map.put("联系电话","contact_number");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 4、区情板块-流动人口数量排行
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w05-04-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQQ4(){

        String res = null;
        try {
            res = getData1("w05-04-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("社区","community");
        map.put("流动人口数量","inflow");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);

    }

    /**
     * 5、可视化-百度链接
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w111/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getBaiDu(){

        String res = null;
        try {
            res = getData1("w111","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("baidu_url","baidu_url");
        map.put("response_id","response_id");
        map.put("created_at","created_at");
        map.put("user_id","user_id");
        map.put("user_name","user_name");
        map.put("user_identifier","user_identifier");
        JSONObject result = getPieResult(map,data);
        String jsonString = result.toJSONString();
        System.out.println(jsonString);

        return new JsonModel(true,result);
    }

}
