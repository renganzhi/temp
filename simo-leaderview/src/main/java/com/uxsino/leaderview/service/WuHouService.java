package com.uxsino.leaderview.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.utils.ClassPathResourceWalker;
import com.uxsino.commons.utils.DateUtils;
import com.uxsino.commons.utils.UUIDUtils;
import com.uxsino.leaderview.dao.ITimeDataDao;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    JdbcTemplate template;

    /**
     * 初始化文件名称
     */
    private static final String FILEPATH = "classpath*:sql/init_time_data.sql";


    public String getData(String formId, String pagination, String query, Boolean ifFormInfo) throws IOException {
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
     * 将获取的数据封装为表格
     * @param formId 表单ID
     * @param column 需要展示的列，不传则展示所有的列
     * @return
     */
    public JsonModel getFormDataForTable(String formId, String column, String query, String url){
        String formInfo;
        try {
            formInfo = this.getData(formId,"",query,false);
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
            formInfo = this.getData(formId,pagination,query,false);
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
        String formInfo = this.getData(formId,"","",true);
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
        String formInfo = this.getData(formId,"","",true);
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
     * 社区人口排行
     * @param query
     * @return
     */
    public JsonModel getPopulationRanking(String query){
        String formInfo;
        try {
            formInfo = this.getData("110","?per_page=100",query,false);
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
            new ClassPathResourceWalker(FILEPATH).forEach(file -> {
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
        } catch (FileNotFoundException e) {
            log.error("未找到定时任务sql的文件[init_time_data.sql]", e);
        } catch (IOException e) {
            log.error("未找到定时任务sql的文件[init_time_data.sql]", e);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
