package com.uxsino.leaderview.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.utils.DateUtils;
import com.uxsino.leaderview.model.GetDataJob;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
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


    public String getData(String formId, String pagination, String query, Boolean ifFormInfo) throws IOException {
        //社会治理-重点人员管控-社区服刑人员	89   街道：query[622]
        //社会治理-重点人员管控-吸毒	90
        //社会治理-重点人员管控-刑释	91
        //社会治理-重点人员管控-易肇事肇祸	92
        //社会治理-重点人员管控-重症精神	93
        //社会治理-重点事件管理-重点人员	94
        //社会治理-重点事件管理-重点事件	95
        //formId = "89";//社会治理-重点人员管控-社区服刑人员
        String prefix = "https://wunlzt.cdwh.gov.cn/api/v4/forms/";
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

        String result = "";

        CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = closeableHttpResponse.getEntity();
        result = EntityUtils.toString(httpEntity);//响应内容
        System.out.println("请求结果是：" + result);

        /*Header[] headers = closeableHttpResponse.getHeaders("X-SLP-Total-Count");
        String value = headers[0].getValue();*/



        return result;
    }

    public CloseableHttpResponse getResponse(String formId, String pagination, String query, Boolean ifFormInfo){
        //社会治理-重点人员管控-社区服刑人员	89   街道：query[622]
        //社会治理-重点人员管控-吸毒	90
        //社会治理-重点人员管控-刑释	91
        //社会治理-重点人员管控-易肇事肇祸	92
        //社会治理-重点人员管控-重症精神	93
        //社会治理-重点事件管理-重点人员	94
        //社会治理-重点事件管理-重点事件	95
        //formId = "89";//社会治理-重点人员管控-社区服刑人员
        String prefix = "https://wunlzt.cdwh.gov.cn/api/v4/forms/";
        //query = "query[622]=浆洗街";


        String url;
        if(ifFormInfo){
            url = prefix + formId;
        }else {
            url = prefix + formId + "/responses";
            //在pagination或者query中自己添加?
            /*if(!ObjectUtils.isEmpty(pagination) || !ObjectUtils.isEmpty(query)){
                url += "?";
            }*/
            if(!Strings.isNullOrEmpty(pagination)){
                url += pagination;
            }
            if(!Strings.isNullOrEmpty(query)){
                url += query;
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
            e.printStackTrace();
        }

        return closeableHttpResponse;
    }

    /**
     * 将获取的数据封装为表格
     * @param formId 表单ID
     * @param column 需要展示的列，不传则展示所有的列
     * @return
     */
    public JsonModel getFormDataForTable(String formId, String column, String query){
        String formInfo;
        try {
            formInfo = this.getData(formId,"?per_page=100",query,false);
        } catch (Exception e) {
            log.error("中台接口报错");
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
        JSONArray dataArray = JSONArray.parseArray(formInfo);
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
            rows.add(row);
        }

        result.put("columns",columns);
        result.put("rows",rows);

        return  new JsonModel(true,result);
    }

    /**
     * 走访情况弹窗信息接口
     * @param formId 重点人员走访情况表单ID 106
     * @return
     */
    public JsonModel getFormDataForWindow(String formId, String params){
        String formInfo = "";
        List<String> list = Arrays.asList(params.split("、"));
        //做一个表头title和value的map,再遍历它通过表头从idNameMap中获取表头对应的fieldId来组装query
        HashMap<String,String> nameValueMap = new HashMap<>();
        for (String s : list) {
            String[] strings = s.split(":");
            nameValueMap.put(strings[0],strings[1]);
        }
        String query = "/search.json?";
        HashMap<String,Integer> NameIdMap = new HashMap<>();
        try {
            NameIdMap = this.getFormNameIdMap(formId);
        } catch (IOException e) {
            log.error("表单title和fieldId的Map获取失败");
            e.printStackTrace();
        }
//        String queryP = "/search.json?" + pagination + "&query[622]=浆洗街";
        for(Map.Entry<String,String> entry: nameValueMap.entrySet()){
            query += "query[" + NameIdMap.get(entry.getKey()) + "]=" + entry.getValue() + "&";
        }
        query = query.substring(0,query.lastIndexOf("&"));

        //获取指定走访情况数据
        try {
            formInfo = this.getData(formId,"",query,false);
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

        for(int i = 0; i < dataArray.size(); i++){
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
        String formInfo = this.getData(formId,"","",true);
        JSONObject jsonObject = JSONObject.parseObject(formInfo);
        JSONArray fields = jsonObject.getJSONArray("fields");

        JSONArray res = new JSONArray();
        for(int i = 0;i < fields.size(); i++){
            JSONObject obj = fields.getJSONObject(i);
            Integer id = obj.getInteger("id");
            String title = obj.getString("title");
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
        String formInfo = this.getData(formId,"","",true);
        JSONObject jsonObject = JSONObject.parseObject(formInfo);
        JSONArray fields = jsonObject.getJSONArray("fields");

        HashMap<Integer,String> idTitleMap = new HashMap<>();
        for(int i = 0;i < fields.size(); i++){
            JSONObject obj = fields.getJSONObject(i);
            Integer id = obj.getInteger("id");
            String title = obj.getString("title");
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
        String formInfo = this.getData(formId,"","",true);
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

    public void getDataByTime(GetDataJob job) throws SchedulerException {
        try {
            //这个方法中带有更新功能,但是更新功能无法更新job里面的实现,所以仍然需要进行重新创建
            scheduleManager.start(new Job<GetDataJob>() {
                @Override
                public void run() {
                    System.out.println(job.getConf() + DateUtils.formatCommonDate(new Date()));
                }
            }.name(job.getName()).target(job).group(job.getGroup()).cron(job.getCron()));
        } catch (Exception e) {
            log.error("开启计划定时任务异常：{}", e.getMessage());
        }

        //scheduleManager.delete(job.getName(),job.getGroup());

    }

    /**
     * 获取浆洗街道文本数据
     * @param formId 表单ID
     * @return
     */
    public JSONObject getJXData(String formId,String query) throws IOException {
        String formInfo;
        formInfo = this.getData(formId,null,query,false);
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
     * 定时更新各类统计数据
     */
    public void RefreshData(){

        //定时更新饼图类数据

        //定时更新折线图类数据

        //更新更新柱状图类数据


    }
}
