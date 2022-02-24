package com.uxsino.leaderview.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.utils.MonitorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@Slf4j
public class WuHouService {

    private static final String Jwt_Secret = "098ec21f7d21ed64620cc1712fac5852c8b9b90d98265acafb8866515cadbef7";
    public static final String appId = "1942fc6b7aab121b22c892c920af8b74b9349f2611ce71d79a8324ce83227279:";
    public static final Integer namespace_id = 1;
    public static final String encoded_data = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lc3BhY2VfaWQiOjF9.dgGuGkLelFnT9ups0kUoBw-AAOFsvUW_fl7HN3KVUWE";

    /*@Autowired
    private ScheduleManager scheduleManager;*/


    public String getData(String formId, String pagination, String query, Boolean ifFormInfo){
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
            url = prefix + formId + "/responses";
            //在pagination或者query中自己添加?
            /*if(!ObjectUtils.isEmpty(pagination) || !ObjectUtils.isEmpty(query)){
                url += "?";
            }*/
            if(!ObjectUtils.isEmpty(pagination)){
                url += pagination;
            }
            if(!ObjectUtils.isEmpty(query)){
                url += query;
            }
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization","1942fc6b7aab121b22c892c920af8b74b9349f2611ce71d79a8324ce83227279:" +
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lc3BhY2VfaWQiOjF9.dgGuGkLelFnT9ups0kUoBw-AAOFsvUW_fl7HN3KVUWE");

        String result = "";
        try {
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);//响应内容
            System.out.println("请求结果是：" + result);

            /*Header[] headers = closeableHttpResponse.getHeaders("X-SLP-Total-Count");
            String value = headers[0].getValue();*/

        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public JsonModel getFormDataForTable(String formId, String column){
        String formInfo;
        try {
            formInfo = this.getData(formId,"","",false);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
        JSONArray dataArray = JSONArray.parseArray(formInfo);
        JSONObject result = new JSONObject();
        List<String> columns = new ArrayList<>();
//        JSONArray columns = new JSONArray();
        JSONArray rows = new JSONArray();

        HashMap<Integer,String> idNameMap = new HashMap<>();
        idNameMap = this.getFormInfoMap(formId);
        //查看详情需要展示所有字段，所以不只需要展示列字段的title，而是展示所有字段的title
        if(ObjectUtils.isEmpty(column)) {
//            idNameMap = this.getFormInfoMap(formId);
            for (Map.Entry<Integer, String> entry : idNameMap.entrySet()) {
                columns.add(entry.getValue());
            }
            columns.add("查看详情");
        }else {
            List<String> list = Arrays.asList(column.split(","));
            for (String s : list) {
                String[] idAndName = s.split("/");
//                idNameMap.put(Integer.valueOf(idAndName[0]),idAndName[1]);
                columns.add(idAndName[1]);
            }
            columns.add("查看详情");
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
            row.put("查看详情","查看详情");
            rows.add(row);
        }

        result.put("columns",columns);
        result.put("rows",rows);

        return  new JsonModel(true,result);
    }

    /**
     * 走访情况弹窗信息接口
     * @param formId 重点人员走访情况表单ID 106
     * @param column
     * @param query 参数
     * @return
     */
    public JsonModel getFormDataForWindow(String formId, String column, String query){
        String formInfo;
        formInfo = this.getData(formId,"",query,false);
        JSONArray dataArray = JSONArray.parseArray(formInfo);
        JSONObject result = new JSONObject();
        List<String> columns = new ArrayList<>();
//        JSONArray columns = new JSONArray();
        JSONArray rows = new JSONArray();

        HashMap<Integer,String> idNameMap = new HashMap<>();
        idNameMap = this.getFormInfoMap(formId);
        //查看详情需要展示所有字段，所以不只需要展示列字段的title，而是展示所有字段的title
        if(ObjectUtils.isEmpty(column)) {
//            idNameMap = this.getFormInfoMap(formId);
            for (Map.Entry<Integer, String> entry : idNameMap.entrySet()) {
                columns.add(entry.getValue());
            }
            columns.add("查看详情");
        }else {
            List<String> list = Arrays.asList(column.split(","));
            for (String s : list) {
                String[] idAndName = s.split("/");
//                idNameMap.put(Integer.valueOf(idAndName[0]),idAndName[1]);
                columns.add(idAndName[1]);
            }
            columns.add("查看详情");
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
            row.put("查看详情","查看详情");
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
    public JSONArray getFormInfo(String formId){
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

        return  res;
    }

    /**
     * 获取表单中fieldId和title的Map
     * @param formId 表单ID
     * @return
     */
    public HashMap<Integer,String> getFormInfoMap(String formId){
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

    /*public void getDataByTime(GetDataJob job) throws SchedulerException {
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

    }*/

}
