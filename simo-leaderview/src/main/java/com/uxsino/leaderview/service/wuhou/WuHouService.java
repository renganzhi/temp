package com.uxsino.leaderview.service.wuhou;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
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
import com.uxsino.leaderview.model.HcnetPoint;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    //浆洗街道地图天网打点需要的点位名称
    public static List<String> HcnetTotalNames = new ArrayList<>();

    //民族区域治理6个点位的名称
    public static List<String> HcnetNationNames = new ArrayList<>();

    //涉藏处突大屏天网打点需要的点位名称
    public static List<String> SZCTNames = new ArrayList<>();

    //浆洗街道天网打点
    public static List<HcnetPoint> HcnetTotalList = new ArrayList<>();

    //民族区域治理6个点位
    public static List<HcnetPoint> HcnetNationList = new ArrayList<>();

    //涉藏处突大屏打点
    public static List<HcnetPoint> SZCTList = new ArrayList<>();

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
     * @param nameAndKeyMap 数据名称和对应field的map
     * @param data 中台返回的data集合
     * @return
     */
    public static JSONObject getPieResult(HashMap<String,String> nameAndKeyMap, JSONArray data){
        JSONObject result = new JSONObject();
        /*if(ObjectUtils.isEmpty(data)){
            log.error("中台接口返回为空");
            return result;
        }*/
        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        for(Map.Entry<String,String> entry : nameAndKeyMap.entrySet()){
            if("id".equals(entry.getKey()))continue;
            columns.add(entry.getKey());
        }
        if(!ObjectUtils.isEmpty(data)){

        for(int i = 0; i < data.size() ; i++){
            JSONObject dataObj = (JSONObject) data.get(i);
            LinkedHashMap<Object,Object> rowMap = new LinkedHashMap<>();
            JSONObject row = new JSONObject();
            Boolean abnormal = false;
            //对异常数据做整行数据标红处理
            if("异常".equals(dataObj.getString("checkStatus"))){
                abnormal = true;
            }
            for(Map.Entry<String,String> entry : nameAndKeyMap.entrySet()){
                if("详情".equals(entry.getKey()) || "走访详情".equals(entry.getKey())){
                    row.put(entry.getKey(),entry.getKey());
                    rowMap.put(entry.getKey(),entry.getKey());
                }else if(abnormal){
                    JSONObject abnormalData = new JSONObject();
                    abnormalData.put("color","red");
                    abnormalData.put("value",dataObj.get(entry.getValue()));
                    row.put(entry.getKey(), abnormalData);
                    rowMap.put(entry.getKey(), abnormalData);
                }
                else {
                    if("上报时间".equals(entry.getKey())){
                        row.put(entry.getKey(), new SimpleDateFormat("yyyy-MM-dd").format((DateUtils.convertStringToDate((String) dataObj.get(entry.getValue())))));
                        rowMap.put(entry.getKey(), new SimpleDateFormat("yyyy-MM-dd").format((DateUtils.convertStringToDate((String) dataObj.get(entry.getValue())))));
                    }else {
                        row.put(entry.getKey(), dataObj.get(entry.getValue()));
                        rowMap.put(entry.getKey(), dataObj.get(entry.getValue()));
                    }
                }
            }
//            rows.add(row);
            //为了保证弹窗数据的顺序，将row改成map
            rows.add(rowMap);
        }
        }else {
            log.error("中台接口返回为空");
        }

        result.put("rows",rows);
        result.put("columns",columns);
        return result;
    }

    /**
     * 将只有一行的数据转换为饼图类型的数据
     * @param columns  行
     * @param data  中台接口返回的只有一行的JSONArray
     * @param needList  需要的枚举key值集合
     * @param keyAndNameMap key和中文name转换需要的map
     * @return
     */
    public static JSONObject getOneToPie(JSONArray columns, JSONArray data, List<String> needList, HashMap<String,String> keyAndNameMap){
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        if(ObjectUtils.isEmpty(data)){
            log.error("中台接口返回为空");
            return result;
        }

        JSONObject dataObj = data.getJSONObject(0);
        for(Map.Entry<String, Object> entry : dataObj.entrySet()){
            if(!needList.contains(entry.getKey())) continue;
            JSONObject row = new JSONObject();
            row.put(columns.getString(0),keyAndNameMap.get(entry.getKey()));
            row.put(columns.getString(1),entry.getValue());
            rows.add(row);
        }

        result.put("rows",rows);
        result.put("columns",columns);
        return result;
    }

    public static JSONObject getOneToPie(JSONArray columns, JSONArray data, HashMap<String,String> nameAndKeyMap){
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        if(ObjectUtils.isEmpty(data)){
            log.error("中台接口返回为空");
            return result;
        }
        JSONObject dataObj = data.getJSONObject(0);
        for(Map.Entry<String, String> entry : nameAndKeyMap.entrySet()){
            JSONObject row = new JSONObject();
            row.put(columns.getString(0),entry.getKey());
            row.put(columns.getString(1),dataObj.get(entry.getValue()));
            rows.add(row);
        }

        result.put("rows",rows);
        result.put("columns",columns);
        return result;
    }

    /**
     * 将中台给的数据封装为下拉项式文本框接口需要的数据，result内含一个用于下拉列表的List和用于获取fieldName相应结果的Map
     * @param fieldName  属性字段，通过它获取属性中文名
     * @param data
     * @return
     */
    public JSONObject getTextResult(String fieldName, JSONArray data){
        JSONObject result = new JSONObject();
        if(ObjectUtils.isEmpty(data)){
            log.error("中台接口返回为空");
            return result;
        }
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

        result.put("list",list);
        result.put("nameAndObjMap",nameAndObjMap);

        return result;
    }

    /**
     * 将getTextResult()方法处理成text形式需要的数据封装为JsonModel
     * @param param  传入的参数
     * @param valueName  数据结果的字段
     * @param obj
     * @return
     */
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

    /**
     * 将没有列名，只有一行数据的表格转化为可选文本框
     * 如：data": [
     *     {
     *         "nz": 1814300,
     *         "wz": 47500
     *     }
     * ]
     * @param param
     * @param object
     * @return
     */
    public JsonModel getPieToText(String param, JSONObject object){


        if(ObjectUtils.isEmpty(object)){
            return new JsonModel(true, "中台接口返回数据为空", new JSONObject());
        }
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
            if("武侯区介绍文案".equals(param)){
                value = "      " + value;
            }
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

        confObj.put("value",value);
        job.setConf(confObj.toString());

        String name = job.getName();
        Long id = Long.valueOf(Arrays.asList(name.split("_")).get(1));
        TimeData data = timeDataDao.findOne(id);
        data.setValue(value);
        //将结果存入库中
        timeDataDao.save(data);

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

    public static String GetCameraPreviewURL(Integer pageNo, Integer pageSize) {

        // ////////////////////////////////// 请自行修改以下变量值  ////////////////////////////////////
        //          var appkey = "24183731";                           //综合安防管理平台提供的appkey，必填
        //          var secret = setEncrypt("babYTegRFvTRymoWubNS");   //综合安防管理平台提供的secret，必填
        //          var ip = "172.16.152.136";                           //综合安防管理平台IP地址，必填
        //          var playMode = 0;                                  //初始播放模式：0-预览，1-回放
        //          var port = 443;                                    //综合安防管理平台端口，若启用HTTPS协议，默认443
        //          var snapDir = "D:\\SnapDir";                       //抓图存储路径
        //          var videoDir = "D:\\VideoDir";                     //紧急录像或录像剪辑存储路径
        //          var layout = "1x1";                                //playMode指定模式的布局
        //          var enableHTTPS = 1;                               //是否启用HTTPS协议与综合安防管理平台交互，这里总是填1
        //          var encryptedFields = 'secret';             //加密字段，默认加密领域为secret
        //          var showToolbar = 0;                               //是否显示工具栏，0-不显示，非0-显示
        //          var showSmart = 0;                                 //是否显示智能信息（如配置移动侦测后画面上的线框），0-不显示，非0-显示
        //          var buttonIDs = "0,16,256,257,258,259,260,512,513,514,515,516,517,768,769";  //自定义工具条按钮
        //          ////////////////////////////////// 请自行修改以上变量值  ////////////////////////////////////

        /**
         * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
         */
        ArtemisConfig.host = "172.16.152.136:443"; // 平台的ip端口
        ArtemisConfig.appKey = "24183731";  // 密钥appkey
        ArtemisConfig.appSecret = "babYTegRFvTRymoWubNS";// 密钥appSecret

        /**
         * STEP2：设置OpenAPI接口的上下文
         */
        final String ARTEMIS_PATH = "/artemis";

        /**
         * STEP3：设置接口的URI地址
         */
        final String previewURLsApi = ARTEMIS_PATH + "/api/resource/v1/cameras";
//        final String previewURLsApi = ARTEMIS_PATH + "/api/resource/v1/regions";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", previewURLsApi);//根据现场环境部署确认是http还是https
            }
        };

        /**
         * STEP4：设置参数提交方式
         */
        String contentType = "application/json";

        /**
         * STEP5：组装请求参数
         */
        JSONObject jsonBody = new JSONObject();
        /*jsonBody.put("cameraIndexCode", "748d84750e3a4a5bbad3cd4af9ed5101");
        jsonBody.put("streamType", 0);
        jsonBody.put("protocol", "rtsp");
        jsonBody.put("transmode", 1);
        jsonBody.put("expand", "streamform=ps");*/
        //{
        //    "pageNo": 1,
        //    "pageSize": 20,
        //    "treeCode": "0"
        //}
        //{"indexCode":"2aeb2e2f19dc4270a68159f08b9e7ca5","treeCode":"0","name":"天网接入点位","parentIndexCode":"9499dc10a3d34cb3a16131cc5ed3a0a7"},
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        jsonBody.put("treeCode", "0");
        String body = jsonBody.toJSONString();
        /**
         * STEP6：调用接口
         */
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType , null);// post请求application/json类型参数
        return result;
    }

    public void initHcnet(){

        Integer pageSize = 200;
        String result = GetCameraPreviewURL(1,pageSize);
        JSONObject object = JSONObject.parseObject(result);
        if(ObjectUtils.isEmpty(object)){
            return;
        }

        JSONObject data = object.getJSONObject("data");

        if(ObjectUtils.isEmpty(data)){
            log.error("海康摄像头code获取接口500");
            return;
        }
        Integer total = data.getInteger("total");
        JSONArray list = new JSONArray();

        //浆洗街道地图天网打点需要的点位名称
        //武侯祠横街2号附10号、武侯祠正门右边、蜀峰花园对面路口、东城根街南延线武侯祠大门旁、永记面馆、蜀汉街8号、武侯祠横街7号、金色柠檬酒店、洗横路口
        HcnetTotalNames.add("D-07071061-ZX-(球)体育路四川电视台");
        HcnetTotalNames.add("Y-07071646-YA-(球)武侯祠大街245号2");
        HcnetTotalNames.add("D-07071033-ZX-(球)老房子酒楼门口");
        HcnetTotalNames.add("D-07071032-ZX-(球)体院学院附属医院");
        HcnetTotalNames.add("D-07071001-ZX-(球)永记面馆");
        HcnetTotalNames.add("D-07071075-ZX-(球)锦南春酒家门前");
        HcnetTotalNames.add("D-07071014-ZX-(球)南郊公园大门右侧");
        HcnetTotalNames.add("D-07070003-ZX-(球)成都市龙江路小学分校");
        HcnetTotalNames.add("D-07071223-ZX-(球)武侯祠东街8号门口");
        HcnetTotalNames.add("Y-07071664-YA-(球)武侯祠大街238号附1号外2");
        HcnetTotalNames.add("D-07071089-ZX-(球)武侯祠正门右边");
        HcnetTotalNames.add("D-07071012-ZX-(球)东城根街南延线武侯祠大门旁");
        HcnetTotalNames.add("D-07071003-ZX-(球)锦里门口");
        HcnetTotalNames.add("D-07071120-ZX-(球)武侯祠大街与武侯祠横街路口");
        HcnetTotalNames.add("D-07071027-ZX-(球)蜀峰花园对面路口");
        HcnetTotalNames.add("D-07071121-ZX-(球)武侯祠横街2号附10号");
        HcnetTotalNames.add("D-07071091-ZX-(球)武侯祠横街3号(对面)");
        HcnetTotalNames.add("D-07071238-ZX-(球)武侯祠横街1号门口");
        HcnetTotalNames.add("D-07071065-ZX-(球)武侯祠东街1号门口");
        HcnetTotalNames.add("D-07071004-ZX-(球)洗横路口");
        HcnetTotalNames.add("D-07071064-ZX-(人)武侯祠横街4号附2号R");
        HcnetTotalNames.add("D-07071149-ZX-(球)浆洗街-武侯祠横街4号附2号");
        HcnetTotalNames.add("D-07071044-ZX-(球)锦尚酒店门口旁");
        HcnetTotalNames.add("D-07071043-ZX-(球)金色柠檬酒店");
        HcnetTotalNames.add("D-07072003-ZX-(球)蜀汉街8号");
        HcnetTotalNames.add("D-07072002-ZX-(球)荣园餐馆");
        HcnetTotalNames.add("D-07071194-ZX-(人)蜀汉东街8号2#");
        HcnetTotalNames.add("D-07071215-XW-(球)武侯祠横街12号");
        HcnetTotalNames.add("D-07071018-ZX-(球)蜀汉东街8号");
        HcnetTotalNames.add("D-07071026-ZX-(球)武侯祠横街7号");
        HcnetTotalNames.add("D-07071038-ZX-(球)山珍老鸭汤门口");

        //浆洗街道大屏民族区域自治和城市之眼
        //耍都
        HcnetNationNames.add("D-07071025-ZX-(球)春江花园中心广场");
        HcnetNationNames.add("D-07071071-ZX-(球)武侯祠大街耍都门口");
        //金科双楠
        HcnetNationNames.add("D-07121157-ZX-(球)广福路寿安堂中医院对面");
        HcnetNationNames.add("D-07121093-ZX-(球)广福路与置信南街交叉口");
        //罗马假日
        HcnetNationNames.add("D-07121038-ZX-(球)罗马假日肯德基");
        HcnetNationNames.add("D-07121026-ZX-(球)罗马广场A1B1之间");
        //另外四个
        HcnetNationNames.add("D-07121014-ZX-(球)四川统计局门口");
        HcnetNationNames.add("D-07121109-ZX-(球)红牌楼商业城");
        HcnetNationNames.add("D-07071003-ZX-(球)锦里门口");
        HcnetNationNames.add("D-07088008-ZX-(球)高升桥路口");
        //涉藏处突大屏
        HcnetNationNames.add("D-07071121-ZX-(球)武侯祠横街2号附10号");
        HcnetNationNames.add("D-07071089-ZX-(球)武侯祠正门右边");
        HcnetNationNames.add("D-07071027-ZX-(球)蜀峰花园对面路口");
        HcnetNationNames.add("D-07071012-ZX-(球)东城根街南延线武侯祠大门旁");
        HcnetNationNames.add("D-07071001-ZX-(球)永记面馆");
        HcnetNationNames.add("D-07072003-ZX-(球)蜀汉街8号");
        HcnetNationNames.add("D-07071026-ZX-(球)武侯祠横街7号");
        HcnetNationNames.add("D-07071043-ZX-(球)金色柠檬酒店");
        HcnetNationNames.add("D-07071004-ZX-(球)洗横路口");

        //SZCTNames
        //D-07071150-ZX-(球)浆洗街公厕
        //D-07071151-ZX-(球)肥猪市公厕
        //D-07071153-ZX-(球)凉水井公厕
        //D-07071154-ZX-(球)新倒桑树公厕
        //D-07071155-ZX-(球)六一一公厕
        //D-07071157-ZX-(球)体院公厕
        //D-07071158-ZX-(球)电器厂公厕
        //D-07071160-ZX-(人)高升桥小游园公厕R
        //D-07071249-ZX-(球)成都购物中心与高升桥地铁D出口之间1#
        //D-07071250-ZX-(球)成都购物中心与高升桥地铁D出口之间2#
        //D-07071223-ZX-(球)武侯祠东街8号门口
        //D-07071224-ZX-(球)洗面桥横街22号附12号门口
        //D-07071226-ZX-(球)洗面桥横街22号附2号门口1#
        //D-07071227-ZX-(球)洗面桥横街22号附2号门口2#
        //D-07071228-ZX-(球)洗面桥横街24号附14号门口
        //D-07071215-XW-(球)武侯祠横街12号
        //D-07071186-XW-(人)武侯祠大街19号附143号
        //D-07071221-ZX-(球)武侯祠大街252号附1号2#
        //D-07071222-ZX-(球)瑞信阳光商务酒店1#
        //D-07071180-XW-(人)一环路南四段16号西南民族大学门口右侧1#
        //D-07071235-XW-(人)一环路南四段10号门口左
        //D-07071236-XW-(人)一环路南四段10号门口右
        //D-07071229-ZX-(球)倒桑树街76号门口
        //D-07071230-XW-(人)倒桑树街108号门口内3号门
        //D-07071232-XW-(人)三六三医院1号门口左
        //D-07071233-XW-(人)三六三医院1号门口右
        //D-07071234-ZX-(球)通祠路39号停车场出入口
        //D-07071177-ZX-(球)外滩1号-1
        //D-07071178-XW-(人)外滩1号-2
        //D-07071182-XW-(人)体育学院大门口左侧1#
        //D-07071162-ZX-(球)浆洗街15号附4号门口-1
        //D-07071163-ZX-(球)武警医院门口左侧1#
        //D-07071166-ZX-(球)一环路南四段4号1#
        //D-07071168-ZX-(球)一环路四段与武侯祠横街路口2#
        //D-07071172-ZX-(球)一环路西一段19号(成都体育学院门口右侧)
        //D-07071176-ZX-(球)南浦东路29号对面
        //D-07071191-ZX-(人)蜀汉东街7号附7-1号1#
        //D-07071192-ZX-(人)蜀汉东街7号附7-1号2#
        //D-07071193-ZX-(人)蜀汉东街8号1#
        //D-07071194-ZX-(人)蜀汉东街8号2#
        //D-07071213-ZX-(人)武侯祠横街18号内
        //D-07071214-ZX-(人)武侯祠横街18号外
        //D-07071216-ZX-(人)蜀汉东街7号附1号对面1#
        //D-07071209-ZX-(人)南郊公园大门进右边
        //D-07071210-ZX-(人)南郊公园大门出右边
        //D-07071239-ZX-(人)南郊公园大门进左边
        //D-07071240-ZX-(人)南郊公园大门出左边
        //D-07071201-ZX-(人)武侯祠横街1号附16号1#
        //D-07071202-ZX-(人)武侯祠横街1号附16号2#
        //D-07071187-ZX-(人)武侯祠横街13号1#
        //D-07071188-ZX-(人)武侯祠横街13号2#
        //D-07071199-ZX-(人)武侯祠横街新2号附3号1#
        //D-07071200-ZX-(人)武侯祠横街新2号附3号2#
        //D-07071225-ZX-(人)洗面桥横街22号附2号门口
        //D-07071241-ZX-(人)锦里戏台通道1#
        //D-07071242-ZX-(人)锦里戏台通道2#
        //D-07071243-ZX-(人)锦里戏台通道3#
        //D-07071244-ZX-(人)锦里戏台通道4#
        //D-07071245-ZX-(人)锦里小吃街通道门口1#
        //D-07071246-ZX-(人)锦里小吃街通道门口2#
        //D-07071247-ZX-(人)锦里小吃街通道门口3#
        //D-07071248-ZX-(人)锦里小吃街通道门口4#
        //D-07071189-ZX-(人)武侯祠横街18号1#
        //D-07071190-ZX-(人)武侯祠横街18号2#
        //D-07071195-ZX-(人)武侯祠大街252号1#
        //D-07071196-ZX-(人)武侯祠大街252号2#
        //D-07071212-ZX-(人)武侯祠横街17号锦江苑门口外
        //D-07071217-ZX-(球)蜀汉东街7号附1号对面2#
        //D-07071218-ZX-(人)武侯祠大街262号门口内
        //D-07071219-ZX-(人)武侯祠大街262号门口外
        //D-07071204-ZX-(人)洗面桥横街23号附5号2#
        //D-07071205-ZX-(人)洗面桥横街14号附9号1#
        //D-07071206-ZX-(人)洗面桥横街14号附9号2#
        //D-07071203-ZX-(人)洗面桥横街23号附5号1#
        //D-07071220-ZX-(人)武侯祠大街252号附1号1#
        //D-07071211-ZX-(人)武侯祠横街17号锦江苑门口内
        //D-07071181-ZX-(人)一环路南四段16号西南民族大学门口右侧2#
        //D-07071171-ZX-(球)蜀汉街2号附18号(成都银行)
        //D-07071238-ZX-(球)武侯祠横街1号门口
        //D-07071116-ZX-(球)浆洗-染靛街船尾
        //D-07071118-ZX-(球)武侯祠大街建国巷路口
        //D-07071131-ZX-(球)彩虹桥小广场
        //D-07071114-ZX-(球)浆洗街与染靛街口1#
        //D-07071115-ZX-(球)浆洗街与染靛街口2#
        //D-07071121-ZX-(球)武侯祠横街2号附10号
        //D-07071122-ZX-(球)武侯祠东街1号附1号
        //D-07071128-ZX-(球)一环路南四段与蜀汉路路口1#
        //D-07071129-ZX-(球)一环路南四段与蜀汉路路口2#
        //D-07071138-ZX-(球)成都市锦里小学后门
        //D-07071119-ZX-(球)建国巷1号警务室对面
        //D-07071147-ZX-(球)四川慈爱医院左侧
        //D-07071123-ZX-(球)洗面桥横街21号
        //D-07071117-ZX-(球)染靛街与通祠路口
        //D-07071126-ZX-(球)一环路南四段28号商检局家属区门口2#
        //D-07071127-ZX-(球)一环路南四段28号商检局家属区门口3#
        //D-07071130-ZX-(球)武侯祠横街口与武侯祠东街交汇处
        //D-07071120-ZX-(球)武侯祠大街与武侯祠横街路口
        //D-07071133-ZX-(球)悠悠三国大本营2#
        //D-07071134-ZX-(球)莲花府邸对面
        //D-07071142-ZX-(球)锦此一家1#
        //D-07071143-ZX-(球)锦此一家2#
        //D-07071145-ZX-(球)浆洗-空军第二幼儿园
        //D-07071132-ZX-(球)南浦西路3号门口(锦江锦官桥处)
        //D-07071149-ZX-(球)浆洗街-武侯祠横街4号附2号
        //D-07071144-ZX-(球)浆洗-西南民族大学幼儿园
        //D-07071030-ZX-(球)363医院大门左侧
        //D-07071092-ZX-(球)洗面桥横街22号
        //D-07071001-ZX-(球)永记面馆
        //D-07071069-ZX-(球)赛思特酒店门口
        //D-07071078-ZX-(球)江北老灶火锅
        //D-07071003-ZX-(球)锦里门口
        //D-07071058-ZX-(球)洗面桥巷益民菜市
        //D-07071027-ZX-(球)蜀峰花园对面路口
        //D-07071065-ZX-(球)武侯祠东街1号门口
        //D-07071082-ZX-(球)锦里古往今来门口
        //D-07071002-ZX-(球)西藏医院(对面)
        //D-07072001-ZX-(球)武侯横街16号(经贸学校)
        //D-07071081-ZX-(球)诸葛庐大门对面
        //D-0707101002-ZX-(球)一环路-高升桥
        //D-07071007-ZX-(球)洗面桥街29号四川咨询产业大厦对面
        //D-07071053-ZX-(球)蜀汉街4号
        //D-07071100-ZX-(球)武侯祠大街19号附87号3楼
        //D-07075001-ZX-(球)爱尔眼科
        //D-07070003-ZX-(球)成都市龙江路小学分校
        //D-07071061-ZX-(球)体育路四川电视台
        //D-07071004-ZX-(球)洗横路口
        //D-07071091-ZX-(球)武侯祠横街3号(对面)
        //D-07071089-ZX-(球)武侯祠正门右边
        //D-07071084-ZX-(球)酒吧一条街汉世门口
        //D-07071052-ZX-(球)武侯名园
        //D-07071044-ZX-(球)锦尚酒店门口旁
        //D-07071073-ZX-(球)倒桑树凤鸣水店
        //D-07071080-ZX-(球)锦里戏台假山处
        //D-07071005-ZX-(球)洗面桥巷与洗面桥横街路口
        //D-07071008-ZX-(球)一环路西南民族学院大门
        //D-07071039-ZX-(球)康定酒店(对面)
        //D-07071034-ZX-(球)太成宾馆右侧
        //D-07071040-ZX-(球)瑞信商务酒店
        //D-07071087-ZX-(球)一环路四段与武侯祠横街路口1#
        //D-07071012-ZX-(球)东城根街南延线武侯祠大门旁
        //D-07071014-ZX-(球)南郊公园大门右侧
        //D-07071026-ZX-(球)武侯祠横街7号
        //D-07071038-ZX-(球)山珍老鸭汤门口
        //D-07071043-ZX-(球)金色柠檬酒店
        //D-07071057-ZX-(球)洗面桥横街10号门口
        //D-07071064-ZX-(人)武侯祠横街4号附2号R
        //D-07071074-ZX-(人)洗面桥横街14号R
        //D-07071079-ZX-(球)悠游三国大本营侧
        //D-07071083-ZX-(球)戏台背后皮影表演处
        //D-07071086-ZX-(球)洗面桥横街20号
        //D-07071095-ZX-(球)成都市西北中学
        //D-07070001-ZX-(球)成都市凉水井街小学(正门)
        //D-07071020-ZX-(人)南浦东路花园内R
        //D-07071025-ZX-(球)春江花园中心广场
        //D-07071063-ZX-(球)彩虹桥头小广场入口
        //D-07074001-ZX-(球)船头
        //D-07077001-ZX-(球)凉水井市场
        //D-07078001-ZX-(球)彩虹桥路口
        //D-07071013-ZX-(球)如意旅馆门口右侧
        //D-07071021-ZX-(球)南浦东路人行道旁
        //D-07071023-ZX-(球)武侯祠大街253号
        //D-07071032-ZX-(球)体院学院附属医院
        //D-07071033-ZX-(球)老房子酒楼门口
        //D-07071051-ZX-(球)武侯祠大街35号
        //D-07071054-ZX-(球)611所大门左侧
        //D-07071071-ZX-(球)武侯祠大街耍都门口
        //D-07071075-ZX-(球)锦南春酒家门前
        //D-07071076-ZX-(球)南甫西路
        //D-07078004-ZX-(球)千禧酒店
        //D-07071046-ZX-(球)南浦西路1号(彩虹花园)
        //D-07078003-ZX-(球)百花大桥
        //D-07071015-ZX-(球)体院路2号
        //D-07071042-ZX-(球)倒桑树街108号附4号
        //D-07071070-ZX-(球)倒桑树街143号
        //D-07071018-ZX-(球)蜀汉东街8号
        //D-07071019-ZX-(球)活力美健身器材门口
        //D-07071022-ZX-(球)体育学院大门口右侧
        //D-07071028-ZX-(球)朱记肥肠粉门前左侧
        //D-07071036-ZX-(球)蜀汉酒店门口路边
        //D-07071072-ZX-(球)洗面桥街5号
        //D-07072003-ZX-(球)蜀汉街8号
        //D-07073001-ZX-(球)区政府
        //D-07071059-ZX-(球)通祠路6号
        //D-07071066-ZX-(球)建国巷4号
        //D-07071085-ZX-(球)洗面桥横街33号
        //D-07071009-ZX-(球)一环路与高升桥东路路口海关大门口
        //D-07071010-ZX-(球)武候祠大街购书中心对面
        //D-07071016-ZX-(球)肥猪市路口
        //D-07071017-ZX-(球)西北中学北门
        //D-07071029-ZX-(球)武警医院门口左侧
        //D-07071045-ZX-(球)一环路南四段4号
        //D-07071049-ZX-(人)浆洗街33号体彩中心门口R
        //D-07071055-ZX-(球)洗面桥8号对面花台
        //D-07071062-ZX-(球)洗面桥文化小广场
        //D-07071024-ZX-(球)建国巷1号
        //D-07071037-ZX-(球)蜀汉酒店后门左侧
        //D-07071056-ZX-(球)浆洗街敬老院门口
        //D-07071060-ZX-(人)浆洗街15号R
        //D-07071090-ZX-(球)武侯祠大街264号
        //D-07071094-ZX-(球)洗面桥横街14号附15号
        //D-07071067-ZX-(球)一环路南4段32号附1号
        //D-07072002-ZX-(球)荣园餐馆
        //D-0707100702-ZX-(球)四川咨询产业大厦对面
        //D-07071011-ZX-(球)大石东路社区左侧
        //D-07074004-ZX-(球)洗面桥巷菜市
        //D-07071098-ZX-(球)武侯区政府出门左侧1
        //D-07071097-ZX-(球)武侯区政府出门左侧2
        //D-07071099-ZX-(球)武侯区政府停车场围墙
        //D-07071035-ZX-(球)文汉宾馆路边
        //D-07071048-ZX-(球)新知书城门口
        //D-07071096-ZX-(球)武侯区政府侧门
        //D-07100902-ZX-(球)商检局大门口
        //D-07071111-ZX-(球)武侯祠大街19号附142号3楼
        //D-07071112-ZX-(球)武侯祠大街19号附146号2楼
        //D-07071164-XW-(球)洗面桥29号1#
        //D-07071165-XW-(球)洗面桥29号2#
        //D-07071167-XW-(球)一环路与洗面桥巷
        //D-07071125-ZX-(球)环路南四段28号商检局家属区门口1#
        //D-07071237-ZX-(球)武侯祠横街1号门口对面+1
        //D-07071197-ZX-(人)武侯祠大街248号附1号1#R
        //D-07071198-ZX-(人)武侯祠大街248号附1号2#R
        //D-07071169-ZX-(球)洗面桥街31号附2号外
        //D-07071183-ZX-(球)洗面桥街25号
        //D-07071184-ZX-(球)武侯祠横街18号对面
        //D-07071170-ZX-(球)武侯祠大街231号附1号锦里内
        //D-07071252-ZX-(人)蜀汉街2号1#R
        //D-07071253-ZX-(人)蜀汉街2号2#R
        //D-07071254-ZX-(人)蜀汉街2号3#R
        //D-07071255-ZX-(人)蜀汉街2号4#R
        //D-07071041-ZX-(球)千禧酒店门口
        //Y-07071601-YA-(球)成体体操主场馆前门通道左
        //Y-07071602-YA-(球)成体体操主场馆前门通道右
        //Y-07071603-YA-(球)成体体操主场馆后门通道左
        //Y-07071604-YA-(球)成体体操主场馆后门通道右
        //Y-07071605-YA-(球)成体体操主场馆与训练馆楼内通道1
        //Y-07071606-YA-(球)成体体操主场馆与训练馆楼内通道2
        //Y-07071607-YA-(球)成体3F训练馆内前侧
        //Y-07071608-YA-(球)成体3F训练馆内后侧
        //Y-07071609-YA-(球)成体3F器械训练馆内前侧
        //Y-07071610-YA-(球)成体3F器械训练馆内后侧
        //Y-07071611-YA-(球)成体体操主场馆与训练馆1F人行通道
        //Y-07071612-YA-(球)成体体育学院大门口绿化带1
        //Y-07071613-YA-(球)成体体育学院大门口绿化带2
        //Y-07071614-YA-(球)成体桂冠路与银杏路路口1
        //Y-07071615-YA-(球)成体桂冠路与银杏路路口2
        //Y-07071616-YA-(球)成体体育路与励志路路口1
        //Y-07071617-YA-(球)成体体育路与励志路路口2
        //Y-07071618-YA-(球)成体体育学院东校门内体育路1
        //Y-07071619-YA-(球)成体体育学院东校门内体育路2
        //Y-07071620-YA-(球)成体体育学院东校门外体育路2号1
        //Y-07071621-YA-(球)成体体育学院东校门外体育路2号2
        //Y-07071622-YA-(球)成体训练场与球类馆通道转角1
        //Y-07071623-YA-(球)成体训练场与球类馆通道转角2
        //Y-07071624-YA-(球)成体训练场入口处
        //Y-07071625-YA-(球)成体训练馆与通道转角1
        //Y-07071626-YA-(球)成体训练馆与通道转角2
        //Y-07071627-YA-(球)成体比赛馆与训练馆中间通道1
        //Y-07071628-YA-(球)成体比赛馆与训练馆中间通道2
        //Y-07071629-YA-(球)成体比赛馆与通道墙边
        //Y-07071630-YA-(球)成体比赛馆与训练馆角1
        //Y-07071631-YA-(球)成体比赛馆与训练馆角2
        //Y-07071632-YA-(球)成体观众入口与男生宿舍公寓
        //Y-07071633-YA-(球)成体男生公寓1
        //Y-07071634-YA-(球)成体男生公寓2
        //Y-07071635-YA-(球)成体球类馆与停车场1
        //Y-07071636-YA-(球)成体球类馆与停车场2
        //Y-07071637-YA-(球)成体训练馆与比赛馆通道1
        //Y-07071638-YA-(球)成体训练馆与比赛馆通道2
        //Y-07071639-YA-(球)成体比赛场馆角落1
        //Y-07071640-YA-(球)成体比赛场馆角落2
        //Y-07071641-YA-(球)成体比赛场馆角落3
        //Y-07071642-YA-(球)成体比赛场馆角落4
        //Y-07071643-YA-(球)体院路2号1
        //Y-07071644-YA-(枪)体院路2号2
        //Y-07071645-YA-(球)武侯祠大街245号1
        //Y-07071646-YA-(球)武侯祠大街245号2
        //Y-07071647-YA-(枪)武侯祠大街245号3
        //Y-07071648-YA-(枪)武侯祠大街245号4
        //Y-07071649-YA-(枪)老房子酒楼路口1
        //Y-07071650-YA-(枪)老房子酒楼路口2
        //Y-07071651-YA-(枪)老房子酒楼路口3
        //Y-07071652-YA-(球)体育附属医院门外1
        //Y-07071653-YA-(枪)体育附属医院门外2
        //Y-07071654-YA-(枪)体育附属医院门外3
        //Y-07071655-YA-(球)武侯祠大街259号门外1
        //Y-07071656-YA-(球)武侯祠大街259号门外2
        //Y-07071657-YA-(枪)武侯祠大街259号门外3
        //Y-07071658-YA-(枪)武侯祠大街259号门外4
        //Y-07071659-YA-(枪)武侯祠大街271号4附4号1
        //Y-07071660-YA-(枪)武侯祠大街271号4附4号2
        //Y-07071661-YA-(球)武侯祠停车场外1
        //Y-07071662-YA-(球)武侯祠停车场外2
        //Y-07071663-YA-(球)武侯祠大街238号附1号外1
        //Y-07071664-YA-(球)武侯祠大街238号附1号外2
        //Y-07071665-YA-(球)武侯祠横街1号大门口1
        //Y-07071666-YA-(球)武侯祠横街1号大门口2
        //Y-07071667-YA-(球)龙江路小学分校路口1
        //Y-07071668-YA-(球)龙江路小学分校路口2
        //D-07071256-ZX-(球)南河公寓
        SZCTNames.add("D-07071150-ZX-(球)浆洗街公厕");
        SZCTNames.add("D-07071151-ZX-(球)肥猪市公厕");
        SZCTNames.add("D-07071153-ZX-(球)凉水井公厕");
        SZCTNames.add("D-07071154-ZX-(球)新倒桑树公厕");
        SZCTNames.add("D-07071155-ZX-(球)六一一公厕");
        SZCTNames.add("D-07071157-ZX-(球)体院公厕");
        SZCTNames.add("D-07071158-ZX-(球)电器厂公厕");
        SZCTNames.add("D-07071160-ZX-(人)高升桥小游园公厕R");
        SZCTNames.add("D-07071249-ZX-(球)成都购物中心与高升桥地铁D出口");
        SZCTNames.add("D-07071250-ZX-(球)成都购物中心与高升桥地铁D出口");
        SZCTNames.add("D-07071223-ZX-(球)武侯祠东街8号门口");
        SZCTNames.add("D-07071224-ZX-(球)洗面桥横街22号附12号门口");
        SZCTNames.add("D-07071226-ZX-(球)洗面桥横街22号附2号门口1#");
        SZCTNames.add("D-07071227-ZX-(球)洗面桥横街22号附2号门口2#");
        SZCTNames.add("D-07071228-ZX-(球)洗面桥横街24号附14号门口");
        SZCTNames.add("D-07071215-XW-(球)武侯祠横街12号");
        SZCTNames.add("D-07071186-XW-(人)武侯祠大街19号附143号");
        SZCTNames.add("D-07071221-ZX-(球)武侯祠大街252号附1号2#");
        SZCTNames.add("D-07071222-ZX-(球)瑞信阳光商务酒店1#");
        SZCTNames.add("D-07071180-XW-(人)一环路南四段16号西南民族大学");
        SZCTNames.add("D-07071235-XW-(人)一环路南四段10号门口左");
        SZCTNames.add("D-07071236-XW-(人)一环路南四段10号门口右");
        SZCTNames.add("D-07071229-ZX-(球)倒桑树街76号门口");
        SZCTNames.add("D-07071230-XW-(人)倒桑树街108号门口内3号门");
        SZCTNames.add("D-07071232-XW-(人)三六三医院1号门口左");
        SZCTNames.add("D-07071233-XW-(人)三六三医院1号门口右");
        SZCTNames.add("D-07071234-ZX-(球)通祠路39号停车场出入口");
        SZCTNames.add("D-07071177-ZX-(球)外滩1号-1");
        SZCTNames.add("D-07071178-XW-(人)外滩1号-2");
        SZCTNames.add("D-07071182-XW-(人)体育学院大门口左侧1#");
        SZCTNames.add("D-07071162-ZX-(球)浆洗街15号附4号门口-1");
        SZCTNames.add("D-07071163-ZX-(球)武警医院门口左侧1#");
        SZCTNames.add("D-07071166-ZX-(球)一环路南四段4号1#");
        SZCTNames.add("D-07071168-ZX-(球)一环路四段与武侯祠横街路口2#");
        SZCTNames.add("D-07071172-ZX-(球)一环路西一段19号(成都体育学");
        SZCTNames.add("D-07071176-ZX-(球)南浦东路29号对面");
        SZCTNames.add("D-07071191-ZX-(人)蜀汉东街7号附7-1号1#");
        SZCTNames.add("D-07071192-ZX-(人)蜀汉东街7号附7-1号2#");
        SZCTNames.add("D-07071193-ZX-(人)蜀汉东街8号1#");
        SZCTNames.add("D-07071194-ZX-(人)蜀汉东街8号2#");
        SZCTNames.add("D-07071213-ZX-(人)武侯祠横街18号内");
        SZCTNames.add("D-07071214-ZX-(人)武侯祠横街18号外");
        SZCTNames.add("D-07071216-ZX-(人)蜀汉东街7号附1号对面1#");
        SZCTNames.add("D-07071209-ZX-(人)南郊公园大门进右边");
        SZCTNames.add("D-07071210-ZX-(人)南郊公园大门出右边");
        SZCTNames.add("D-07071239-ZX-(人)南郊公园大门进左边");
        SZCTNames.add("D-07071240-ZX-(人)南郊公园大门出左边");
        SZCTNames.add("D-07071201-ZX-(人)武侯祠横街1号附16号1#");
        SZCTNames.add("D-07071202-ZX-(人)武侯祠横街1号附16号2#");
        SZCTNames.add("D-07071187-ZX-(人)武侯祠横街13号1#");
        SZCTNames.add("D-07071188-ZX-(人)武侯祠横街13号2#");
        SZCTNames.add("D-07071199-ZX-(人)武侯祠横街新2号附3号1#");
        SZCTNames.add("D-07071200-ZX-(人)武侯祠横街新2号附3号2#");
        SZCTNames.add("D-07071225-ZX-(人)洗面桥横街22号附2号门口");
        SZCTNames.add("D-07071241-ZX-(人)锦里戏台通道1#");
        SZCTNames.add("D-07071242-ZX-(人)锦里戏台通道2#");
        SZCTNames.add("D-07071243-ZX-(人)锦里戏台通道3#");
        SZCTNames.add("D-07071244-ZX-(人)锦里戏台通道4#");
        SZCTNames.add("D-07071245-ZX-(人)锦里小吃街通道门口1#");
        SZCTNames.add("D-07071246-ZX-(人)锦里小吃街通道门口2#");
        SZCTNames.add("D-07071247-ZX-(人)锦里小吃街通道门口3#");
        SZCTNames.add("D-07071248-ZX-(人)锦里小吃街通道门口4#");
        SZCTNames.add("D-07071189-ZX-(人)武侯祠横街18号1#");
        SZCTNames.add("D-07071190-ZX-(人)武侯祠横街18号2#");
        SZCTNames.add("D-07071195-ZX-(人)武侯祠大街252号1#");
        SZCTNames.add("D-07071196-ZX-(人)武侯祠大街252号2#");
        SZCTNames.add("D-07071212-ZX-(人)武侯祠横街17号锦江苑门口外");
        SZCTNames.add("D-07071217-ZX-(球)蜀汉东街7号附1号对面2#");
        SZCTNames.add("D-07071218-ZX-(人)武侯祠大街262号门口内");
        SZCTNames.add("D-07071219-ZX-(人)武侯祠大街262号门口外");
        SZCTNames.add("D-07071204-ZX-(人)洗面桥横街23号附5号2#");
        SZCTNames.add("D-07071205-ZX-(人)洗面桥横街14号附9号1#");
        SZCTNames.add("D-07071206-ZX-(人)洗面桥横街14号附9号2#");
        SZCTNames.add("D-07071203-ZX-(人)洗面桥横街23号附5号1#");
        SZCTNames.add("D-07071220-ZX-(人)武侯祠大街252号附1号1#");
        SZCTNames.add("D-07071211-ZX-(人)武侯祠横街17号锦江苑门口内");
        SZCTNames.add("D-07071181-ZX-(人)一环路南四段16号西南民族大学");
        SZCTNames.add("D-07071171-ZX-(球)蜀汉街2号附18号(成都银行)");
        SZCTNames.add("D-07071238-ZX-(球)武侯祠横街1号门口");
        SZCTNames.add("D-07071116-ZX-(球)浆洗-染靛街船尾");
        SZCTNames.add("D-07071118-ZX-(球)武侯祠大街建国巷路口");
        SZCTNames.add("D-07071131-ZX-(球)彩虹桥小广场");
        SZCTNames.add("D-07071114-ZX-(球)浆洗街与染靛街口1#");
        SZCTNames.add("D-07071115-ZX-(球)浆洗街与染靛街口2#");
        SZCTNames.add("D-07071121-ZX-(球)武侯祠横街2号附10号");
        SZCTNames.add("D-07071122-ZX-(球)武侯祠东街1号附1号");
        SZCTNames.add("D-07071128-ZX-(球)一环路南四段与蜀汉路路口1#");
        SZCTNames.add("D-07071129-ZX-(球)一环路南四段与蜀汉路路口2#");
        SZCTNames.add("D-07071138-ZX-(球)成都市锦里小学后门");
        SZCTNames.add("D-07071119-ZX-(球)建国巷1号警务室对面");
        SZCTNames.add("D-07071147-ZX-(球)四川慈爱医院左侧");
        SZCTNames.add("D-07071123-ZX-(球)洗面桥横街21号");
        SZCTNames.add("D-07071117-ZX-(球)染靛街与通祠路口");
        SZCTNames.add("D-07071126-ZX-(球)一环路南四段28号商检局家属区");
        SZCTNames.add("D-07071127-ZX-(球)一环路南四段28号商检局家属区");
        SZCTNames.add("D-07071130-ZX-(球)武侯祠横街口与武侯祠东街交汇处");
        SZCTNames.add("D-07071120-ZX-(球)武侯祠大街与武侯祠横街路口");
        SZCTNames.add("D-07071133-ZX-(球)悠悠三国大本营2#");
        SZCTNames.add("D-07071134-ZX-(球)莲花府邸对面");
        SZCTNames.add("D-07071142-ZX-(球)锦此一家1#");
        SZCTNames.add("D-07071143-ZX-(球)锦此一家2#");
        SZCTNames.add("D-07071145-ZX-(球)浆洗-空军第二幼儿园");
        SZCTNames.add("D-07071132-ZX-(球)南浦西路3号门口(锦江锦官桥处");
        SZCTNames.add("D-07071149-ZX-(球)浆洗街-武侯祠横街4号附2号");
        SZCTNames.add("D-07071144-ZX-(球)浆洗-西南民族大学幼儿园");
        SZCTNames.add("D-07071030-ZX-(球)363医院大门左侧");
        SZCTNames.add("D-07071092-ZX-(球)洗面桥横街22号");
        SZCTNames.add("D-07071001-ZX-(球)永记面馆");
        SZCTNames.add("D-07071069-ZX-(球)赛思特酒店门口");
        SZCTNames.add("D-07071078-ZX-(球)江北老灶火锅");
        SZCTNames.add("D-07071003-ZX-(球)锦里门口");
        SZCTNames.add("D-07071058-ZX-(球)洗面桥巷益民菜市");
        SZCTNames.add("D-07071027-ZX-(球)蜀峰花园对面路口");
        SZCTNames.add("D-07071065-ZX-(球)武侯祠东街1号门口");
        SZCTNames.add("D-07071082-ZX-(球)锦里古往今来门口");
        SZCTNames.add("D-07071002-ZX-(球)西藏医院(对面)");
        SZCTNames.add("D-07072001-ZX-(球)武侯横街16号(经贸学校)");
        SZCTNames.add("D-07071081-ZX-(球)诸葛庐大门对面");
        SZCTNames.add("D-0707101002-ZX-(球)一环路-高升桥");
        SZCTNames.add("D-07071007-ZX-(球)洗面桥街29号四川咨询产业大厦");
        SZCTNames.add("D-07071053-ZX-(球)蜀汉街4号");
        SZCTNames.add("D-07071100-ZX-(球)武侯祠大街19号附87号3楼");
        SZCTNames.add("D-07075001-ZX-(球)爱尔眼科");
        SZCTNames.add("D-07070003-ZX-(球)成都市龙江路小学分校");
        SZCTNames.add("D-07071061-ZX-(球)体育路四川电视台");
        SZCTNames.add("D-07071004-ZX-(球)洗横路口");
        SZCTNames.add("D-07071091-ZX-(球)武侯祠横街3号(对面)");
        SZCTNames.add("D-07071089-ZX-(球)武侯祠正门右边");
        SZCTNames.add("D-07071084-ZX-(球)酒吧一条街汉世门口");
        SZCTNames.add("D-07071052-ZX-(球)武侯名园");
        SZCTNames.add("D-07071044-ZX-(球)锦尚酒店门口旁");
        SZCTNames.add("D-07071073-ZX-(球)倒桑树凤鸣水店");
        SZCTNames.add("D-07071080-ZX-(球)锦里戏台假山处");
        SZCTNames.add("D-07071005-ZX-(球)洗面桥巷与洗面桥横街路口");
        SZCTNames.add("D-07071008-ZX-(球)一环路西南民族学院大门");
        SZCTNames.add("D-07071039-ZX-(球)康定酒店(对面)");
        SZCTNames.add("D-07071034-ZX-(球)太成宾馆右侧");
        SZCTNames.add("D-07071040-ZX-(球)瑞信商务酒店");
        SZCTNames.add("D-07071087-ZX-(球)一环路四段与武侯祠横街路口1#");
        SZCTNames.add("D-07071012-ZX-(球)东城根街南延线武侯祠大门旁");
        SZCTNames.add("D-07071014-ZX-(球)南郊公园大门右侧");
        SZCTNames.add("D-07071026-ZX-(球)武侯祠横街7号");
        SZCTNames.add("D-07071038-ZX-(球)山珍老鸭汤门口");
        SZCTNames.add("D-07071043-ZX-(球)金色柠檬酒店");
        SZCTNames.add("D-07071057-ZX-(球)洗面桥横街10号门口");
        SZCTNames.add("D-07071064-ZX-(人)武侯祠横街4号附2号R");
        SZCTNames.add("D-07071074-ZX-(人)洗面桥横街14号R");
        SZCTNames.add("D-07071079-ZX-(球)悠游三国大本营侧");
        SZCTNames.add("D-07071083-ZX-(球)戏台背后皮影表演处");
        SZCTNames.add("D-07071086-ZX-(球)洗面桥横街20号");
        SZCTNames.add("D-07071095-ZX-(球)成都市西北中学");
        SZCTNames.add("D-07070001-ZX-(球)成都市凉水井街小学(正门)");
        SZCTNames.add("D-07071020-ZX-(人)南浦东路花园内R");
        SZCTNames.add("D-07071025-ZX-(球)春江花园中心广场");
        SZCTNames.add("D-07071063-ZX-(球)彩虹桥头小广场入口");
        SZCTNames.add("D-07074001-ZX-(球)船头");
        SZCTNames.add("D-07077001-ZX-(球)凉水井市场");
        SZCTNames.add("D-07078001-ZX-(球)彩虹桥路口");
        SZCTNames.add("D-07071013-ZX-(球)如意旅馆门口右侧");
        SZCTNames.add("D-07071021-ZX-(球)南浦东路人行道旁");
        SZCTNames.add("D-07071023-ZX-(球)武侯祠大街253号");
        SZCTNames.add("D-07071032-ZX-(球)体院学院附属医院");
        SZCTNames.add("D-07071033-ZX-(球)老房子酒楼门口");
        SZCTNames.add("D-07071051-ZX-(球)武侯祠大街35号");
        SZCTNames.add("D-07071054-ZX-(球)611所大门左侧");
        SZCTNames.add("D-07071071-ZX-(球)武侯祠大街耍都门口");
        SZCTNames.add("D-07071075-ZX-(球)锦南春酒家门前");
        SZCTNames.add("D-07071076-ZX-(球)南甫西路");
        SZCTNames.add("D-07078004-ZX-(球)千禧酒店");
        SZCTNames.add("D-07071046-ZX-(球)南浦西路1号(彩虹花园)");
        SZCTNames.add("D-07078003-ZX-(球)百花大桥");
        SZCTNames.add("D-07071015-ZX-(球)体院路2号");
        SZCTNames.add("D-07071042-ZX-(球)倒桑树街108号附4号");
        SZCTNames.add("D-07071070-ZX-(球)倒桑树街143号");
        SZCTNames.add("D-07071018-ZX-(球)蜀汉东街8号");
        SZCTNames.add("D-07071019-ZX-(球)活力美健身器材门口");
        SZCTNames.add("D-07071022-ZX-(球)体育学院大门口右侧");
        SZCTNames.add("D-07071028-ZX-(球)朱记肥肠粉门前左侧");
        SZCTNames.add("D-07071036-ZX-(球)蜀汉酒店门口路边");
        SZCTNames.add("D-07071072-ZX-(球)洗面桥街5号");
        SZCTNames.add("D-07072003-ZX-(球)蜀汉街8号");
        SZCTNames.add("D-07073001-ZX-(球)区政府");
        SZCTNames.add("D-07071059-ZX-(球)通祠路6号");
        SZCTNames.add("D-07071066-ZX-(球)建国巷4号");
        SZCTNames.add("D-07071085-ZX-(球)洗面桥横街33号");
        SZCTNames.add("D-07071009-ZX-(球)一环路与高升桥东路路口海关大门");
        SZCTNames.add("D-07071010-ZX-(球)武候祠大街购书中心对面");
        SZCTNames.add("D-07071016-ZX-(球)肥猪市路口");
        SZCTNames.add("D-07071017-ZX-(球)西北中学北门");
        SZCTNames.add("D-07071029-ZX-(球)武警医院门口左侧");
        SZCTNames.add("D-07071045-ZX-(球)一环路南四段4号");
        SZCTNames.add("D-07071049-ZX-(人)浆洗街33号体彩中心门口R");
        SZCTNames.add("D-07071055-ZX-(球)洗面桥8号对面花台");
        SZCTNames.add("D-07071062-ZX-(球)洗面桥文化小广场");
        SZCTNames.add("D-07071024-ZX-(球)建国巷1号");
        SZCTNames.add("D-07071037-ZX-(球)蜀汉酒店后门左侧");
        SZCTNames.add("D-07071056-ZX-(球)浆洗街敬老院门口");
        SZCTNames.add("D-07071060-ZX-(人)浆洗街15号R");
        SZCTNames.add("D-07071090-ZX-(球)武侯祠大街264号");
        SZCTNames.add("D-07071094-ZX-(球)洗面桥横街14号附15号");
        SZCTNames.add("D-07071067-ZX-(球)一环路南4段32号附1号");
        SZCTNames.add("D-07072002-ZX-(球)荣园餐馆");
        SZCTNames.add("D-0707100702-ZX-(球)四川咨询产业大厦对面");
        SZCTNames.add("D-07071011-ZX-(球)大石东路社区左侧");
        SZCTNames.add("D-07074004-ZX-(球)洗面桥巷菜市");
        SZCTNames.add("D-07071098-ZX-(球)武侯区政府出门左侧1");
        SZCTNames.add("D-07071097-ZX-(球)武侯区政府出门左侧2");
        SZCTNames.add("D-07071099-ZX-(球)武侯区政府停车场围墙");
        SZCTNames.add("D-07071035-ZX-(球)文汉宾馆路边");
        SZCTNames.add("D-07071048-ZX-(球)新知书城门口");
        SZCTNames.add("D-07071096-ZX-(球)武侯区政府侧门");
        SZCTNames.add("D-07100902-ZX-(球)商检局大门口");
        SZCTNames.add("D-07071111-ZX-(球)武侯祠大街19号附142号3楼");
        SZCTNames.add("D-07071112-ZX-(球)武侯祠大街19号附146号2楼");
        SZCTNames.add("D-07071164-XW-(球)洗面桥29号1#");
        SZCTNames.add("D-07071165-XW-(球)洗面桥29号2#");
        SZCTNames.add("D-07071167-XW-(球)一环路与洗面桥巷");
        SZCTNames.add("D-07071125-ZX-(球)环路南四段28号商检局家属区门");
        SZCTNames.add("D-07071237-ZX-(球)武侯祠横街1号门口对面+1");
        SZCTNames.add("D-07071197-ZX-(人)武侯祠大街248号附1号1#R");
        SZCTNames.add("D-07071198-ZX-(人)武侯祠大街248号附1号2#R");
        SZCTNames.add("D-07071169-ZX-(球)洗面桥街31号附2号外");
        SZCTNames.add("D-07071183-ZX-(球)洗面桥街25号");
        SZCTNames.add("D-07071184-ZX-(球)武侯祠横街18号对面");
        SZCTNames.add("D-07071170-ZX-(球)武侯祠大街231号附1号锦里内");
        SZCTNames.add("D-07071252-ZX-(人)蜀汉街2号1#R");
        SZCTNames.add("D-07071253-ZX-(人)蜀汉街2号2#R");
        SZCTNames.add("D-07071254-ZX-(人)蜀汉街2号3#R");
        SZCTNames.add("D-07071255-ZX-(人)蜀汉街2号4#R");
        SZCTNames.add("D-07071041-ZX-(球)千禧酒店门口");
        SZCTNames.add("Y-07071601-YA-(球)成体体操主场馆前门通道左");
        SZCTNames.add("Y-07071602-YA-(球)成体体操主场馆前门通道右");
        SZCTNames.add("Y-07071603-YA-(球)成体体操主场馆后门通道左");
        SZCTNames.add("Y-07071604-YA-(球)成体体操主场馆后门通道右");
        SZCTNames.add("Y-07071605-YA-(球)成体体操主场馆与训练馆楼内通道");
        SZCTNames.add("Y-07071606-YA-(球)成体体操主场馆与训练馆楼内通道");
        SZCTNames.add("Y-07071607-YA-(球)成体3F训练馆内前侧");
        SZCTNames.add("Y-07071608-YA-(球)成体3F训练馆内后侧");
        SZCTNames.add("Y-07071609-YA-(球)成体3F器械训练馆内前侧");
        SZCTNames.add("Y-07071610-YA-(球)成体3F器械训练馆内后侧");
        SZCTNames.add("Y-07071611-YA-(球)成体体操主场馆与训练馆1F人行");
        SZCTNames.add("Y-07071612-YA-(球)成体体育学院大门口绿化带1");
        SZCTNames.add("Y-07071613-YA-(球)成体体育学院大门口绿化带2");
        SZCTNames.add("Y-07071614-YA-(球)成体桂冠路与银杏路路口1");
        SZCTNames.add("Y-07071615-YA-(球)成体桂冠路与银杏路路口2");
        SZCTNames.add("Y-07071616-YA-(球)成体体育路与励志路路口1");
        SZCTNames.add("Y-07071617-YA-(球)成体体育路与励志路路口2");
        SZCTNames.add("Y-07071618-YA-(球)成体体育学院东校门内体育路1");
        SZCTNames.add("Y-07071619-YA-(球)成体体育学院东校门内体育路2");
        SZCTNames.add("Y-07071620-YA-(球)成体体育学院东校门外体育路2号");
        SZCTNames.add("Y-07071621-YA-(球)成体体育学院东校门外体育路2号");
        SZCTNames.add("Y-07071622-YA-(球)成体训练场与球类馆通道转角1");
        SZCTNames.add("Y-07071623-YA-(球)成体训练场与球类馆通道转角2");
        SZCTNames.add("Y-07071624-YA-(球)成体训练场入口处");
        SZCTNames.add("Y-07071625-YA-(球)成体训练馆与通道转角1");
        SZCTNames.add("Y-07071626-YA-(球)成体训练馆与通道转角2");
        SZCTNames.add("Y-07071627-YA-(球)成体比赛馆与训练馆中间通道1");
        SZCTNames.add("Y-07071628-YA-(球)成体比赛馆与训练馆中间通道2");
        SZCTNames.add("Y-07071629-YA-(球)成体比赛馆与通道墙边");
        SZCTNames.add("Y-07071630-YA-(球)成体比赛馆与训练馆角1");
        SZCTNames.add("Y-07071631-YA-(球)成体比赛馆与训练馆角2");
        SZCTNames.add("Y-07071632-YA-(球)成体观众入口与男生宿舍公寓");
        SZCTNames.add("Y-07071633-YA-(球)成体男生公寓1");
        SZCTNames.add("Y-07071634-YA-(球)成体男生公寓2");
        SZCTNames.add("Y-07071635-YA-(球)成体球类馆与停车场1");
        SZCTNames.add("Y-07071636-YA-(球)成体球类馆与停车场2");
        SZCTNames.add("Y-07071637-YA-(球)成体训练馆与比赛馆通道1");
        SZCTNames.add("Y-07071638-YA-(球)成体训练馆与比赛馆通道2");
        SZCTNames.add("Y-07071639-YA-(球)成体比赛场馆角落1");
        SZCTNames.add("Y-07071640-YA-(球)成体比赛场馆角落2");
        SZCTNames.add("Y-07071641-YA-(球)成体比赛场馆角落3");
        SZCTNames.add("Y-07071642-YA-(球)成体比赛场馆角落4");
        SZCTNames.add("Y-07071643-YA-(球)体院路2号1");
        SZCTNames.add("Y-07071644-YA-(枪)体院路2号2");
        SZCTNames.add("Y-07071645-YA-(球)武侯祠大街245号1");
        SZCTNames.add("Y-07071646-YA-(球)武侯祠大街245号2");
        SZCTNames.add("Y-07071647-YA-(枪)武侯祠大街245号3");
        SZCTNames.add("Y-07071648-YA-(枪)武侯祠大街245号4");
        SZCTNames.add("Y-07071649-YA-(枪)老房子酒楼路口1");
        SZCTNames.add("Y-07071650-YA-(枪)老房子酒楼路口2");
        SZCTNames.add("Y-07071651-YA-(枪)老房子酒楼路口3");
        SZCTNames.add("Y-07071652-YA-(球)体育附属医院门外1");
        SZCTNames.add("Y-07071653-YA-(枪)体育附属医院门外2");
        SZCTNames.add("Y-07071654-YA-(枪)体育附属医院门外3");
        SZCTNames.add("Y-07071655-YA-(球)武侯祠大街259号门外1");
        SZCTNames.add("Y-07071656-YA-(球)武侯祠大街259号门外2");
        SZCTNames.add("Y-07071657-YA-(枪)武侯祠大街259号门外3");
        SZCTNames.add("Y-07071658-YA-(枪)武侯祠大街259号门外4");
        SZCTNames.add("Y-07071659-YA-(枪)武侯祠大街271号4附4号1");
        SZCTNames.add("Y-07071660-YA-(枪)武侯祠大街271号4附4号2");
        SZCTNames.add("Y-07071661-YA-(球)武侯祠停车场外1");
        SZCTNames.add("Y-07071662-YA-(球)武侯祠停车场外2");
        SZCTNames.add("Y-07071663-YA-(球)武侯祠大街238号附1号外1");
        SZCTNames.add("Y-07071664-YA-(球)武侯祠大街238号附1号外2");
        SZCTNames.add("Y-07071665-YA-(球)武侯祠横街1号大门口1");
        SZCTNames.add("Y-07071666-YA-(球)武侯祠横街1号大门口2");
        SZCTNames.add("Y-07071667-YA-(球)龙江路小学分校路口1");
        SZCTNames.add("Y-07071668-YA-(球)龙江路小学分校路口2");


        //无法获取
//        HcnetNationNames.add("D-07121111-ZX-(球)广福路二环路口");
//        HcnetNationNames.add("D-07121170-ZX-(球)罗马假日广场中央大道十字路口");

        for(int i = 0 ;i < total/pageSize + 1; i++){
            String result2 = GetCameraPreviewURL(i+1,pageSize);
            object = JSONObject.parseObject(result2);
            data = object.getJSONObject("data");
            list = data.getJSONArray("list");
            doConvert(HcnetTotalNames,list, HcnetTotalList);
            doConvert(HcnetNationNames,list, HcnetNationList);
            //涉藏处突大屏打点
            doConvert(SZCTNames,list, SZCTList);
        }

    }

    public static void doConvert(List<String> names, JSONArray list, List<HcnetPoint> totalList){

        for (int i = 0; i < list.size(); i++){
            JSONObject obj = list.getJSONObject(i);
            if(names.contains(obj.getString("name"))){
                totalList.add(HcnetPoint.JsonObjectToPoint(obj));
            }
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
                template.execute(sql);
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
        dataArry.put("nameArray",nameArry);
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
        dataArry.put("dataArray",dataArray);
        res.put("dataArray",dataArry);
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

        return new JsonModel(true,result);
    }

    public JsonModel getOrgaDot() {

        String data = null;
        try {
            data = getData("y132-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }
        JSONObject object = JSONObject.parseObject(data);
        JSONArray jsonArray = object.getJSONArray("data");

        //群租房打点和网约房打点同步
        /*//装经度的set
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
        }*/
        JSONObject result = new JSONObject();
        result.put("dataArray", jsonArray);

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

        return new JsonModel(true,result);
    }

    //——————————————————————————————————————————————————智慧社区-群租房版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————智慧社区-群租房版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————智慧社区-群租房版块—————————————————————————————————————————————————————————

    /**
     * 1、群租房：入住人员对比
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

        return new JsonModel(true,result);

    }

    /**
     * 2、群租房：
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
        map.put("","");

        JSONObject result = new JSONObject();

        result = getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 3、群租房：群租房入住人员归属地分析
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQZF3(){

        String res = null;
        try {
            res = getData("y13-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        result.put("rowsArray", data);

        return new JsonModel(true,result);

    }
    //——————————————————————————————————————————————————智慧社区-未办证住所版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————智慧社区-未办证住所版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————智慧社区-未办证住所版块—————————————————————————————————————————————————————————

    /**
     * 1、群租房：全区群租房地图打点
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getWBZ1(){

        String res = null;
        try {
            res = getData("y49-02","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        JSONObject result = new JSONObject();
        /*LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("所属街道","street");
        map.put("名称","room_name");
        map.put("地址","address");
        map.put("经度","longitude");
        map.put("纬度","latitude");
        map.put("业主（房东）姓名","owner_name");
        map.put("业主（房东）联系电话","owner_phone");
        map.put("经营者姓名","manager_name");
        map.put("经营者联系电话","maager_phone");
        map.put("房间数","room_number");
        map.put("床铺数","bed_number");
        map.put("网格员姓名","gridman_name");
        map.put("网格员联系电话","gridman_phone");
        map.put("社区民警姓名","police_name");
        map.put("社区民警联系电话","police_phone");
        map.put("区域微型消防站联络员姓名","liaison_name");
        map.put("区域微型消防站联络电话","liaison_phone");

        result = getPieResult(map,data);*/
        result.put("dataArray", data);

        return new JsonModel(true,result);

    }

    /**
     * 5、群租房：街道群租房分布
     * 接口URL： {{baseUrl}}/apis/daas/pro/3/components/y13-01/data?per_page=100&page=1
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getWBZ2(){

        String res = null;
        try {
            res = getData("y50-01","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");
        data = JSON.parseArray(JSON.toJSONString(data.stream().filter(obj -> !((JSONObject) obj).getString("street").equals("总店铺数")).collect(Collectors.toList())));

        JSONObject result = new JSONObject();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("街道","street");
        map.put("数量","count");
        result = getPieResult(map,data);

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
        JSONObject result = getPieResult(map,data);

        String url = "/leaderview/WuHou163/getJJ3?param=用地性质:";

        result.put("url",url);
        
        

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

        String query = "ydxz=" + param.split(":")[1];
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

        JSONObject result = new JSONObject();

        result.put("dataArray", data);

        

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
        map.put("来源","address");
        map.put("时间","time");
        map.put("明细","details");
        //详情需要替换的行
        map.put("详情","明细");

        List<String> removeList = Arrays.asList("明细");

        JSONObject result = new JSONObject();

        result = getPieResult(map,data);

        JSONArray columns = result.getJSONArray("columns");
        columns.removeAll(removeList);

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
//        map.put("警综管辖","policing");
//        map.put("数量","num");
        map.put("name","policing");
        map.put("value","num");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);

        String url = "/leaderview/WuHou163/getSJ3?param=name:";
        result.put("url",url);


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
        String query = "policing=" + param.split(":")[1];
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
//        map.put("高频词","term");
//        map.put("高频词出现次数","num");
        map.put("name","term");
        map.put("value","num");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        
        

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
        JSONObject obj = new JSONObject();
        data.add(obj);

//        JSONObject result = getTextResult("source",data);

        return new JsonModel(true,data);

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
        JSONObject result = getTextResult("event_type",data);

        return new JsonModel(true,result);

    }

    /**
     *9、 事件类型分类-事件列表
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-06-3/data?source=12345
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent9(String param, Integer pageSize){
        String query = "source=" + param;
        String res = null;
        try {
            res = getData1("w02-06-3","per_page=" + pageSize + "&page=1",query,false,true);
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
    public JsonModel getEvent10(String param){
        if(param.contains("大联动微治理")){
            String[] split = param.split(":");
            param = split[0] + "&" + split[1];
        }
        String query = "id=" + param ;
        String res = null;
        try {
            res = getData1("w02-06-4","per_page=10000&page=1",query,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("id","id");
        map.put("来源","source");
//        map.put("handle_reply","handle_reply");
        map.put("责任单位","responsible_unit");
        map.put("事件内容","event_content");
        map.put("上报时间","event_time");
        map.put("事件类型","event_type");
        map.put("街道","street");
        map.put("社区","community");
//        map.put("place_of_complaint","place_of_complaint");
        map.put("办理状态","handle_state");
        map.put("办理详情","handle_detail");
//        map.put("event_sub_type","event_sub_type");
        map.put("办理时间","handle_time");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);
        
        

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
        
        

        return new JsonModel(true,result);

    }

    /**
     * 14、110非警推送-数据分析（左）
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w02-09-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getEvent14(){

        String res = null;
        try {
            res = getData1("w02-11-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("月份","date");
        map.put("数量","sum");
        JSONObject result = new JSONObject();

        result = getPieResult(map,data);

        return new JsonModel(true,result);

    }

    //—————————————————————————————————————————生态板块——————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————
    //—————————————————————————————————————————————————————————————————————————————————————————————————————————————————

    /**
     * 1、天气
     * 返回内容过多，根据实际需求来确定需要的数据
     * 接口URL： {{baseUrl}}/apis/weather/v1/weather?cityNm=成都
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getHB11(){

        String res = null;
        try {
            res = getData2("http://wunlzt.cdwh.gov.cn/apis/weather/v1/weather?cityNm=成都",true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONObject data = object.getJSONObject("data");
        JSONObject realTime = data.getJSONObject("realTime");
        JSONArray array = new JSONArray();
        array.add(realTime);

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //    "week": "星期五",
        //    "wtId": "2",
        //    "wtNm": "多云", //天气
        //    "wtIcon": "01",
        //    "wtTemp": "4", //温度℃
        //    "wtHumi": "83", //湿度%
        //    "wtWindId": "8",
        //    "wtWindNm": "北风", //风向
        //    "wtWinp": "0", //风力 单位:级
        //    "wtWins": "0", //风速 单位:km/h
        //    "wtAqi": "35", //pm2.5 aqi
        //    "wtVisibility": "4.00", //能见度km
        //    "wtRainfall": "0.00", //降雨量mm
        //    "wtPressurel": "966" //气压hpa
        map.put("今日天气","wtNm");
        map.put("温度℃","wtTemp");
        map.put("湿度%","wtHumi");
        map.put("风力","wtWinp");
        map.put("aqi","wtAqi");
        JSONObject result = getPieResult(map,array);

        return new JsonModel(true,result);

    }

    /**
     * 1.2、天气预报
     * 返回内容过多，根据实际需求来确定需要的数据
     * 接口URL： {{baseUrl}}/apis/weather/v1/weather?cityNm=成都
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getHB12(){

        String res = null;
        try {
            res = getData2("http://wunlzt.cdwh.gov.cn/apis/weather/v1/weather?cityNm=成都",true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONObject data = object.getJSONObject("data");
        JSONArray array = data.getJSONArray("futureDay");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //"futureDay": [
        //
        //    {
        //        "dateYmd": "2022-01-29",
        //        "week": "星期六",
        //        "wtBlueSkyId": "0",
        //        "wtId1": "2",
        //        "wtId2": "3",
        //        "wtNm1": "多云", //天气(白天)
        //        "wtNm2": "阴", //天气(夜间)
        //        "wtIcon1": "01",
        //        "wtIcon2": "02",
        //        "wtTemp1": "10", //温度(白天)
        //        "wtTemp2": "3", //温度(夜间)
        map.put("时间","dateYmd");
        map.put("最高℃","wtTemp1");
        map.put("最低℃","wtTemp2");
        JSONObject result = getPieResult(map,array);
        
        

        return new JsonModel(true,result);

    }

    /**
     * 2、域内河流
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-02-1/data?monitoring_type
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getHB2(){

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
        JSONObject result = getTextResult("monitoring_type",data);

        return new JsonModel(true,result);

    }

    /**
     * 需求中不需要了
     * 3、域内河流-监测点-类型-列表
     * 返回内容过多，根据实际需求来确定需要的数据
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-02-2/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getHB3(){

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

        JSONObject result = getPieResult(map,data);

        return new JsonModel(true,result);

    }

    /**
     * 4、浆洗街街道五项绿地面积
     * 返回内容过多，根据实际需求来确定需要的数据
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-03-3/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getHB4(){

        String res = null;
        try {
            res = getData1("w03-03-3","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("绿地类型","green_type");
        map.put("绿地面积","sum");
        JSONObject result = getPieResult(map,data);
        result.put("unit","平方米");

        return new JsonModel(true,result);
    }

    /**
     * 5、环境/绿地（五项绿地）
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-03-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getHB5(){

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
        map.put("绿地类型","green_type");
        map.put("绿地面积","sum");
        JSONObject result = getPieResult(map,data);
        result.put("unit","平方米");

        return new JsonModel(true,result);

    }

    /**
     * 6、环境/垃圾
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-04-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getHB6(){

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
//        map.put("id","id");
        map.put("月份","sdate");
//        map.put("垃圾处理总量（吨）","total_processing");
        map.put("可回收垃圾处理量（吨）","recyclable_trash");
        map.put("厨余垃圾处理量（吨）","kitchen_waste");
        map.put("其他垃圾处理量（吨）","other_garbage");
//        map.put("有害垃圾处理量（吨）","hazardous_waste");
        JSONObject result = getPieResult(map,data);
        
        

        return new JsonModel(true,result);

    }

    /**
     * 7、大气-成都市aqi
     * 接口URL： {{baseUrl}}/apis/weather/v1/aqi
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getHB7(){

        String res = null;
        try {
            res = getData2("http://wunlzt.cdwh.gov.cn/apis/weather/v1/aqi",true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONObject dataObj = object.getJSONObject("data");
        JSONArray data = new JSONArray();
        data.add(dataObj);

        //该接口的所有数据
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("aqi","AQI");
        map.put("二氧化硫","F_SQ2IAQI");
        map.put("二氧化氮","F_NO2IAQI");
        map.put("可吸入颗粒物（PM10）","F_PM10IAQI");
        map.put("一氧化碳","F_CO1IAQI");
//        map.put("其他垃圾处理量（吨）","other_garbage");
        map.put("臭氧","F_O3IAQI");
        map.put("细颗粒物（PM2.5）","F_PM25IAQI");
//        map.put("—","F_FIRSTPOLLUTANTNAME");
        map.put("时间","F_DATATIME");
//        map.put("F_MONITORSTATIONID","F_MONITORSTATIONID");
//        map.put("color","color");
        map.put("等级","Level");
        map.put("空气质量（优/良）","str");
//        map.put("经纬度","Point");
        JSONObject result = getPieResult(map,data);


        return new JsonModel(true,result);

    }

    /**
     * 8、大气-成都市 近30天AQI
     * 接口URL： {{baseUrl}}/apis/weather/v1/aqiday
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getHB8(){

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

        List<String> columns = Arrays.asList("日期","数值");
        JSONArray rows = new JSONArray();
        for (int i = 0; i<aqi.size();i++){
            JSONObject row = new JSONObject();
            JSONObject aqiDay = aqi.getJSONObject(i);
            Object date = day.get(i);
            row.put("日期",date);
            //如果没有获取到当天的aqi,则跳过
            if (ObjectUtils.isEmpty(aqiDay.get("y")))continue;
            row.put("数值",aqiDay.get("y"));
            rows.add(row);
        }
        JSONObject result = new JSONObject();
        result.put("rows",rows);
        result.put("columns",columns);

        return new JsonModel(true,result);

    }

    /**
     * 9、环境/绿地（道路绿地）
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w03-03-2/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getHB9(){

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
        result.put("unit","平方米");



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
        map.put("公司","company_name");
        map.put("警报类型","alarm_type");
        map.put("警报时间","alarm_time");
        JSONObject result = getPieResult(map,data);
        String url = "/leaderview/WuHou163/getAQ2?param=id:";
        result.put("url",url);

        
        

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

        JSONObject result = getTextResult("alarm_type",data);

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
        map.put("单位名称","company_name");
//        map.put("数量","num" );
        JSONObject result = new JSONObject();

        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        for(Map.Entry<String,String> entry : map.entrySet()){
            if("id".equals(entry.getKey()))continue;
            columns.add(entry.getKey());
        }

        for(int i = 0; i < data.size() ; i++){
            if(rows.size() == 5) break;
            JSONObject dataObj = (JSONObject) data.get(i);
            if(ObjectUtils.isEmpty(dataObj.get("company_name"))) continue;
            JSONObject row = new JSONObject();
            for(Map.Entry<String,String> entry : map.entrySet()){
                row.put(entry.getKey(),dataObj.get(entry.getValue()));
            }
            rows.add(row);
        }

        result.put("rows",rows);
        result.put("columns",columns);

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
        
        

        return new JsonModel(true,result);

    }

    /**
     * 7、隐患排查情况-年（当年）
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ7(Boolean ifNew){

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
        map.put("已处理","finished");
        map.put("处理中","processing" );
        //如果是武侯柱状图新组件，则不需要总量字段

        map.put("总数", "total");


        JSONObject result = new JSONObject();

        //如果是true，则封装为武侯柱状图
        if(!ifNew) {
            result = getPieResult(map, data);
        }else {
            JSONArray columns = new JSONArray();
            columns.add("排查情况");
            columns.add("数量");

            result = getOneToPie(columns, data, map);
        }

        return new JsonModel(true,result);

    }

    /**
     * 8、隐患排查情况-月(当月)
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-2/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getAQ8(Boolean ifNew){

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
        map.put("已处理","finished");
        map.put("处理中","processing" );
        //如果是武侯柱状图新组件，则不需要总量字段

        map.put("总数", "total");


        JSONObject result = new JSONObject();

        //如果是true，则封装为武侯柱状图
        if(!ifNew) {
            result = getPieResult(map, data);
        }else {
            JSONArray columns = new JSONArray();
            columns.add("排查情况");
            columns.add("数量");

            result = getOneToPie(columns, data, map);
        }

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

        String url = "/leaderview/WuHou163/getAQ10?param=id:";

        result.put("url",url);
        
        

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

        String query = "id=" + param.split(":")[1];
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

        return new JsonModel(true,result);

    }

    /**
     * 11、隐患趋势变化-年
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-5/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JSONObject getAQ11(){

        String res = null;
        try {
            res = getData1("w04-02-5","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("时间","time");
        map.put("数量","num");
        JSONObject result = getPieResult(map,data);

        return result;

    }

    /**
     * 12、隐患趋势变化-月
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-6/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JSONObject getAQ12(){

        String res = null;
        try {
            res = getData1("w04-02-6","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("时间","time");
        map.put("数量","num");
        JSONObject result = getPieResult(map,data);
        
        

        return result;

    }

    /**
     * 13、隐患趋势变化-天
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w04-02-6-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JSONObject getAQ13(){

        String res = null;
        try {
            res = getData1("w04-02-6-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("时间","time");
        map.put("数量","num");
        JSONObject result = getPieResult(map,data);

        return result;

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
//        map.put("数量","num");
        JSONObject result = new JSONObject();

        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        for(Map.Entry<String,String> entry : map.entrySet()){
            if("id".equals(entry.getKey()))continue;
            columns.add(entry.getKey());
        }

        for(int i = 0; i < data.size() ; i++){
            if(rows.size() == 5) break;
            JSONObject dataObj = (JSONObject) data.get(i);
            if(ObjectUtils.isEmpty(dataObj.get("unit_name"))) continue;
            JSONObject row = new JSONObject();
            for(Map.Entry<String,String> entry : map.entrySet()){
                row.put(entry.getKey(),dataObj.get(entry.getValue()));
            }
            rows.add(row);
        }

        result.put("rows",rows);
        result.put("columns",columns);
        

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
    public JSONObject getQQ1(){

        String res = null;
        try {
            res = getData1("w05-01-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            //return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("社区","community");
        map.put("常住人口","population");
        JSONObject result = getPieResult(map,data);
        
        

        return result;

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

        return new JsonModel(true,result);

    }

    /**
     * 2.2、数字武侯
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w05-02-1/data?id=6826
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQQ22(){

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

        JSONObject result = new JSONObject();
        result.put("dataArray",data);

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
        
        

        return new JsonModel(true,result);

    }

    /**
     * 4、区情板块-流动人口数量排行
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w05-04-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JSONObject getQQ4(){

        String res = null;
        try {
            res = getData1("w05-04-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            //return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("社区","community");
        map.put("流动人口数量","inflow");
        JSONObject result = getPieResult(map,data);
        
        

        return result;

    }

    /**
     * 5、区情板块-经济人口介绍文案
     * 接口URL： {{baseUrl}}/apis/daas/pro/1/components/w05-04-1/data
     * 请求方式： GET
     * Content-Type： multipart/form-data
     * @return
     */
    public JsonModel getQQ5(){

        String res = null;
        try {
            res = getData1("w05-07-1","per_page=10000&page=1",null,false,true);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            //return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject object = JSONObject.parseObject(res);
        JSONArray data = object.getJSONArray("data");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("街道","street");
        map.put("社区","community");
        map.put("常住人口（万）","czrk");
        map.put("生产总值（亿）","sczz");
        map.put("服务业增加值（亿）","fwyzjz");
        map.put("固定资产投资增长（%）","gdzctzzz");
        map.put("一般公共预算收入（亿）","ybggyssr");
        map.put("人均可支配收入增长（%）","rjkzpsrzz");
        map.put("武侯区介绍文案","whqjswa");

        JSONObject result = getPieResult(map,data);

        return new JsonModel(true,result);

    }



    /**
     * 可视化-百度链接
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

        return new JsonModel(true,result);
    }




}
