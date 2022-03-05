package com.uxsino.leaderview.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.dao.ITimeDataDao;
import com.uxsino.leaderview.entity.TimeData;
import com.uxsino.leaderview.model.DataJob;
import com.uxsino.leaderview.service.WuHouService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Api(tags = {"武侯智慧城市-大屏展示数据接口"})
@RestController
@RequestMapping("/WuHou")
@Slf4j
public class WuHouController {

    @Autowired
    WuHouService wuHouService;

    @Autowired
    ITimeDataDao timeDataDao;

    @RequestMapping(value = "/getFormInfo", method = RequestMethod.GET)
    public JsonModel getFormInfo(String formId){
        try {
            return new JsonModel(true, wuHouService.getFormInfo(formId));
        } catch (Exception e) {
            log.error("中台接口报错");
            log.error(e.getMessage(),e);
            return new JsonModel(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/getFormData", method = RequestMethod.GET)
    public JsonModel getFormData(String formId, String column, @RequestParam(required = false) String query){
        try {
            return wuHouService.getFormDataForTable(formId, column, query, null);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return new JsonModel(false,e.getMessage());
        }
    }

    @RequestMapping(value = "/getFormDataAndUrl", method = RequestMethod.GET)
    public JsonModel getFormDataAndUrl(String formId, String column,
                                       @RequestParam(required = false) String query){
        try {
            String[] idAndType = formId.split(",");
            String url = "/leaderview/WuHou/getZFXQ?type=人员类别:" + idAndType[1] +"&name=人员姓名:";
            return wuHouService.getFormDataForTable(idAndType[0], column, query, url);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return new JsonModel(false,e.getMessage());
        }
    }

    /**
     * 获取重点人员走访表数据
     * @param formId
     * @param column
     * @param query
     * @return
     */
    @RequestMapping(value = "/getFormDataForZF", method = RequestMethod.GET)
    public JsonModel getFormDataForZF(String formId, String column, @RequestParam(required = false) String query){
        try {
            return wuHouService.getFormDataForTable(formId, column, query,null);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return new JsonModel(false, e.getMessage());
        }
    }


    /**
     * 获取社会治理大屏-重点人员、事件各模块-基础人员总数数据-文本框
     * @return
     */
    @GetMapping("/getPeopleTotalCount")
    public JsonModel getPeopleTotalCount(String formId){
//        String query90 = "/search.json?query[634]=浆洗街";
        return new JsonModel(true,wuHouService.getFormDataForText(formId,null));
    }

    /**
     * 浆洗街街道群租房治理-各类别总数
     * @param category 群租房类别:包含小旅馆、民宿旅店、群租房
     * @return
     */
    @GetMapping("/getHouseCount")
    public JsonModel getHouseCount(String category){
        String query87 = "query[773]=" + category;
        return new JsonModel(true,wuHouService.getFormDataForText("87",query87));
    }

    /**
     * 社区人口排行-条形图
     * @param street 街道
     * @return
     */
    @GetMapping("/getPopulationRanking")
    public JsonModel getPopulationRanking(String street){
        String query110 = "query[777]=" + street;
        return wuHouService.getPopulationRanking(query110);
    }

    /**
     * 吸毒走访人员详情弹窗接口
     * 通过参数人员类别、人员姓名、身份证来定位单条走访详情，但是目前中台身份证作为参数查询有问题
     * @return
     */
    @GetMapping("/getZFXQ")
    public JsonModel getZFXQ(String type,String name){
        //目前身份证号的查询有问题
        String params = type + "," + name;
        //params = "人员类别:吸毒人员,人员姓名:毛智勇";
        // return wuHouService.getFormDataForWindow("106","人员姓名:毛智勇、人员身份证号:510107********2973、人员类别:吸毒人员");
        return wuHouService.getFormDataForWindow("106",params);
    }

    @RequestMapping(value = "/getFormDataAndUrlForHistogram", method = RequestMethod.GET)
    public JsonModel getFormforHistogram(String formId, String column,
                                       String street, @RequestParam(required = false) String query){
        try {
            //String[] idAndType = formId.split(",");
            column = "637/姓名,639/吸毒原因,640/管控情况,走访详情";
            formId = "90";
            query = "query[634]=" + street;
            String url = "/leaderview/WuHou/getZFXQ?type=人员类别:吸毒人员&name=人员姓名:";
            return wuHouService.getFormDataForTable(formId, column, query, url);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return new JsonModel(false,e.getMessage());
        }
    }

    /**
     * 获取浆洗街道数据下拉项
     * @return
     */
    @GetMapping("getEconomicDataInfo")
    public JsonModel getJXDataInfo(String year, String jieDao, String jiDu){
        String query = "query[756]=" + year + "&query[758]=" + jieDao + "&query[757]=" + jiDu;
        JSONObject obj = new JSONObject();
        try {
            obj = wuHouService.getJXData("108",query);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"中台接口报错",e.getMessage());
        }
        if(ObjectUtils.isEmpty(obj)){
            return new JsonModel(true,"暂无数据");
        }
        JSONArray res = (JSONArray) obj.get("info");
        return new JsonModel(true,res);
    }

    /**
     * 获取浆洗街道指定数据
     * @param fieldId
     * @return
     */
    @GetMapping("/getEconomicDataValue")
    public JsonModel getJXDataValue(Integer fieldId, String year, String jieDao, String jiDu){
        String query = "query[756]=" + year + "&query[758]=" + jieDao + "&query[757]=" + jiDu;
        JSONObject obj = new JSONObject();
        try {
            obj = wuHouService.getJXData("108",query);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"中台接口报错",e.getMessage());
        }
        if(ObjectUtils.isEmpty(obj)){
            return new JsonModel(true,"暂无数据");
        }
        HashMap<Integer,String> valueMap = (HashMap<Integer, String>) obj.get("value");
        JSONObject res = new JSONObject();
        res.put("name","值");
        res.put("info",valueMap.get(fieldId));
        res.put("value",valueMap.get(fieldId));
        return new JsonModel(true,res);
    }

    /**
     * 区情板块-数字武侯
     * @param name 展示的具体条目名称，如：科学技术投入
     * @return
     */
    @GetMapping("/getSZWH")
    public JsonModel getSZWH(String name){
        String query = "query[595]=" + name;
        JSONObject obj = new JSONObject();
        try {
            obj = wuHouService.getJXData("86",query);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonModel(false,"中台接口报错",e.getMessage());
        }
        if(ObjectUtils.isEmpty(obj)){
            return new JsonModel(true,"暂无数据");
        }
        HashMap<Integer,String> valueMap = (HashMap<Integer, String>) obj.get("value");
        String value = valueMap.get(596) + valueMap.get(597);
        JSONObject res = new JSONObject();
        res.put("name","值");
        res.put("info",value);
        res.put("value",value);
        return new JsonModel(true,res);
    }

    @GetMapping("/startTask")
    public void startTask(){

        /*DataJob job = new DataJob();
        job.setName("时间测试");
        job.setGroup("获取全量数据");
        job.setCron("0/5 * * * * ? *");
        job.setConf("获取重点事件全量数据");*/

        wuHouService.savePieData();
        List<TimeData> dataList = timeDataDao.findAll();
        List<DataJob> jobs = wuHouService.createJobFromTimeData(dataList);
        try {
            for(DataJob job : jobs) {
                wuHouService.getDataByTime(job);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }

    /**
     * 获取涉访类别详细分析-饼图
     * @return
     */
    @GetMapping("/getPieData")
    public JsonModel getPieData(String name){
        TimeData data = timeDataDao.findByName("涉访类别详细分析");
        String value = data.getValue();
        if(ObjectUtils.isEmpty(value)){
            return new JsonModel(true,"暂无数据");
        }
        JSONObject result = JSONObject.parseObject(value);
        return new JsonModel(true,result);
    }

    @GetMapping("/getPieDataTest")
    public JsonModel getPieDataTest(String name){
        TimeData data = timeDataDao.findByName("涉访类别详细分析");
        List<TimeData> dataList = new ArrayList<>();
        dataList.add(data);
        List<DataJob> jobList = wuHouService.createJobFromTimeData(dataList);
        JSONObject result = wuHouService.getPieData(jobList.get(0));
        return new JsonModel(true,result);
    }

    @GetMapping("/getHousingPersonnelTop")
    public JsonModel getHousingPersonnelTop(){
        return wuHouService.getHousingPersonnelTop();
    }

}
