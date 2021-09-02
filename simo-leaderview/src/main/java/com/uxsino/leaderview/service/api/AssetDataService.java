package com.uxsino.leaderview.service.api;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.alert.AlertLevel;
import com.uxsino.leaderview.model.alert.AlertLevelQuery;
import com.uxsino.leaderview.model.asset.AssetCriteria;
import com.uxsino.leaderview.model.asset.AssetStatusEnum;
import com.uxsino.leaderview.model.asset.QueryCond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class AssetDataService {

    @Autowired
    RpcProcessService rpcProcessService;

    public JsonModel getAlertCount(String remindType,String remindLevel) throws Exception {
        ArrayList<LinkedHashMap<Object,Object>> arrayList = (ArrayList<LinkedHashMap<Object, Object>>) rpcProcessService.alert_report().getObj();
        List<AlertLevel> allLevel = rpcProcessService.findAlertLevelList(new AlertLevelQuery());
        JSONObject result = new JSONObject();
        JSONArray columns = new JSONArray();
        JSONArray colors= new JSONArray();
        JSONArray rows = new JSONArray();

        List<String> typeList;
        List<String> levelList;
        if(Strings.isNullOrEmpty(remindType)){
            typeList = Arrays.asList("REPERTORY", "EXPIRED","MAINTENANCEOVER");
        }else {
            typeList = Arrays.asList(remindType.split(","));
        }
        if(Strings.isNullOrEmpty(remindLevel)){
            levelList = Arrays.asList("1","2","3");
        }else {
            levelList = Arrays.asList(remindLevel.split(","));
        }

        columns.add("告警类别");
        if (levelList.contains("1")) {
            columns.add(allLevel.get(5).getName());
            colors.add(allLevel.get(5).getColor());
        }
        if (levelList.contains("2")){
            columns.add(allLevel.get(7).getName());
            colors.add(allLevel.get(7).getColor());
        }
        if (levelList.contains("3")) {
            columns.add(allLevel.get(8).getName());
            colors.add(allLevel.get(8).getColor());
        }
        LinkedHashMap<String,String> alertName = new LinkedHashMap<>();
        alertName.put("REPERTORY","库存提醒");
        alertName.put("EXPIRED","许可过期");
        alertName.put("MAINTENANCEOVER","维保过期");

        for(int i = 0;i<arrayList.size();i++){
            LinkedHashMap<Object, Object> obj = arrayList.get(i);
            JSONObject row = new JSONObject();
            if(typeList.contains(obj.get("name"))) {
                row.put("告警类别", alertName.get(obj.get("name")));
                if (levelList.contains("1"))row.put("一般", obj.get("onelevel"));
                if (levelList.contains("2"))row.put("严重", obj.get("twolevel"));
                if(levelList.contains("3"))row.put("紧急", obj.get("threelevel"));
                rows.add(row);
            }
        }

        result.put("columns",columns);
        result.put("colors",colors);
        result.put("rows",rows);

        return new JsonModel(true,result);
    }


    public JsonModel getAlertPage(Integer number) throws Exception{
        JsonModel jsonModel = rpcProcessService.alarmMsgByPage(number);
        ArrayList<LinkedHashMap<String,String>> arrayList = (ArrayList<LinkedHashMap<String,String>>) jsonModel.getObj();
        ArrayList<LinkedHashMap<Object,Object>> rows = new ArrayList<>();
        JSONArray columns = new JSONArray();
        JSONObject result = new JSONObject();
        for (int i = 0;i < arrayList.size();i++){
         LinkedHashMap<Object,Object> row = new LinkedHashMap<>();
         row.put("规格型号",arrayList.get(i).get("提醒对象"));
         row.put("资产分组",arrayList.get(i).get("资产分组"));
         row.put("提醒策略",arrayList.get(i).get("提醒策略"));
         row.put("实际剩余",arrayList.get(i).get("实际剩余"));
         row.put("告警级别",arrayList.get(i).get("告警级别"));
         row.put("提醒内容",arrayList.get(i).get("提醒内容"));
         row.put("更新时间",arrayList.get(i).get("更新时间"));
         rows.add(row);
        }

        columns.add("规格型号");
        columns.add("资产分组");
        columns.add("提醒策略");
        columns.add("实际剩余");
        columns.add("告警级别");
        columns.add("提醒内容");
        columns.add("更新时间");

        result.put("columns",columns);
        result.put("rows",rows);

        return new JsonModel(true,result);
    }

    public JsonModel countAsset(String status) throws Exception{

        List<String> statusList = Arrays.asList(status);
        JSONObject result = new JSONObject();
        AssetCriteria criteria = new AssetCriteria();
        ArrayList<QueryCond> fixquery = new ArrayList<>();
        QueryCond Status = new QueryCond();
        QueryCond assetNo = new QueryCond();
        QueryCond spercification = new QueryCond();
        QueryCond assetName = new QueryCond();
        QueryCond category = new QueryCond();
        Status.setCond("in");
        Status.setFieldKey("status");
        Status.setFieldValue(statusList);
        assetNo.setCond("contains");
        assetNo.setFieldKey("assetNo");
        assetNo.setFieldValue("");
        spercification.setCond("contains");
        spercification.setFieldKey("spercification");
        spercification.setFieldValue("");
        assetName.setCond("contains");
        assetName.setFieldKey("assetName");
        assetName.setFieldValue("");
        category.setCond("eq");
        category.setFieldKey("category");
        category.setFieldValue("");
        fixquery.add(Status);
        fixquery.add(assetName);
        fixquery.add(assetNo);
        fixquery.add(spercification);
        fixquery.add(category);
        criteria.setFixQuery(fixquery);

        JsonModel search = null;
        try {
            search = rpcProcessService.search(criteria);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
        LinkedHashMap<Object, Object> obj = (LinkedHashMap<Object, Object>) search.getObj();
        //取出该状态库存的数量
        Integer count = (Integer) obj.get("count");

        result.put("name",AssetStatusEnum.valueOf(status).getText());
        result.put("value",count);
        result.put("unit","");

        return new JsonModel(true,result);
    }

    public JsonModel assetStatusTotalCount(String status) {
        List<String> statusList = new ArrayList<>();
        if(Strings.isNullOrEmpty(status)){
            for (AssetStatusEnum statusEnum:AssetStatusEnum.values()){
                statusList.add(statusEnum.getValue());
            }
        }else {
            statusList = Arrays.asList(status.split(","));
        }
        //分别封装物理资产和软件资产的数量
        LinkedHashMap<String,Integer> hardwareMap = new LinkedHashMap<>();
        LinkedHashMap<String,Integer> softwareMap = new LinkedHashMap<>();
        for(int i = 0;i< statusList.size();i++){
            List<String> state = Arrays.asList(statusList.get(i));
            AssetCriteria hardwareCriteria = new AssetCriteria();
            AssetCriteria softwareCriteria = new AssetCriteria();
            ArrayList<QueryCond> fixquery = new ArrayList<>();
            QueryCond Status = new QueryCond();
            QueryCond assetNo = new QueryCond();
            QueryCond spercification = new QueryCond();
            QueryCond assetName = new QueryCond();
            QueryCond hard = new QueryCond();
            QueryCond soft = new QueryCond();
            Status.setCond("in");
            Status.setFieldKey("status");
            Status.setFieldValue(state);
            assetNo.setCond("contains");
            assetNo.setFieldKey("assetNo");
            assetNo.setFieldValue("");
            spercification.setCond("contains");
            spercification.setFieldKey("spercification");
            spercification.setFieldValue("");
            assetName.setCond("contains");
            assetName.setFieldKey("assetName");
            assetName.setFieldValue("");
            hard.setCond("eq");
            hard.setFieldKey("category");
            hard.setFieldValue("1000001");
            soft.setCond("eq");
            soft.setFieldKey("category");
            soft.setFieldValue("1000002");
            fixquery.add(Status);
            fixquery.add(assetName);
            fixquery.add(assetNo);
            fixquery.add(spercification);
            fixquery.add(hard);
            hardwareCriteria.setFixQuery(fixquery);

            JsonModel hardcount = null;
            JsonModel softcount = null;
            try {
                //先查询物理资产数量
                hardcount = rpcProcessService.search(hardwareCriteria);
                //查询完物理资产数量后，修改资产类别，复用fixQuery再次查询
                fixquery.remove(hard);
                fixquery.add(soft);
                softwareCriteria.setFixQuery(fixquery);
                softcount = rpcProcessService.search(softwareCriteria);
            } catch (Exception e) {
                e.printStackTrace();
                return new JsonModel(false, e.getMessage());
            }
            LinkedHashMap<Object, Object> obj1 = (LinkedHashMap<Object, Object>) hardcount.getObj();
            LinkedHashMap<Object, Object> obj2 = (LinkedHashMap<Object, Object>) softcount.getObj();
            //取出该状态库存的数量
            hardwareMap.put(statusList.get(i),(Integer) obj1.get("count"));
            softwareMap.put(statusList.get(i),(Integer) obj2.get("count"));
        }

        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        columns.add("资产状态");
        columns.add("物理资产");
        columns.add("软件资产");
        JSONArray colors = new JSONArray();
        colors.add("#fc9822");
        colors.add("#e91818");
        for(int i = 0;i < statusList.size();i++){
            JSONObject row = new JSONObject();
            row.put("资产状态",AssetStatusEnum.valueOf(statusList.get(i)).getText());
            row.put("物理资产",hardwareMap.get(statusList.get(i)));
            row.put("软件资产",softwareMap.get(statusList.get(i)));
            rows.add(row);
        }

        result.put("columns",columns);
        result.put("colors",colors);
        result.put("rows",rows);

        return new JsonModel(true,result);
    }
}
