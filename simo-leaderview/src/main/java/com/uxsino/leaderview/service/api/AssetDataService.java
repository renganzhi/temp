package com.uxsino.leaderview.service.api;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.utils.DateUtils;
import com.uxsino.leaderview.model.SiteOrganizationCriteria;
import com.uxsino.leaderview.model.alert.AlertLevel;
import com.uxsino.leaderview.model.alert.AlertLevelQuery;
import com.uxsino.leaderview.model.asset.AssetCriteria;
import com.uxsino.leaderview.model.asset.AssetStatusEnum;
import com.uxsino.leaderview.model.asset.QueryCond;
import org.apache.poi.poifs.filesystem.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class AssetDataService {

    @Autowired
    RpcProcessService rpcProcessService;

    public JsonModel getAlertCount(String remindType,String remindLevel) throws Exception {
        ArrayList<LinkedHashMap<Object,Object>> arrayList = (ArrayList<LinkedHashMap<Object, Object>>) rpcProcessService.alert_report().getObj();
        if(ObjectUtils.isEmpty(arrayList)){
            return new JsonModel(false,"资产服务调用失败");
        }
        //List<AlertLevel> allLevel = rpcProcessService.findAlertLevelList(new AlertLevelQuery());
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
            columns.add("一般");
            colors.add("");
        }
        if (levelList.contains("2")){
            columns.add("严重");
            colors.add("");
        }
        if (levelList.contains("3")) {
            columns.add("紧急");
            colors.add("");
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


    public JsonModel getAlertPage(Integer number,String dateFormatStr) throws Exception{
        JsonModel jsonModel = rpcProcessService.alarmMsgByPage(number);
        ArrayList<LinkedHashMap<String,String>> arrayList = (ArrayList<LinkedHashMap<String,String>>) jsonModel.getObj();
        ArrayList<LinkedHashMap<Object,Object>> rows = new ArrayList<>();
        JSONArray columns = new JSONArray();
        JSONObject result = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
        SimpleDateFormat sdf2 = new SimpleDateFormat(MonitorDataService.sdfStr);
        for (int i = 0;i < arrayList.size();i++){
         LinkedHashMap<Object,Object> row = new LinkedHashMap<>();
         row.put("规格型号",arrayList.get(i).get("提醒对象"));
         row.put("资产分组",arrayList.get(i).get("资产分组"));
         row.put("提醒策略",arrayList.get(i).get("提醒策略"));
         row.put("实际剩余",arrayList.get(i).get("实际剩余"));
         row.put("告警级别",arrayList.get(i).get("告警级别"));
         row.put("提醒内容",arrayList.get(i).get("提醒内容"));
         row.put("更新时间",sdf.format(sdf2.parse(arrayList.get(i).get("更新时间"))));
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

    public JsonModel assetStatusTotalCount(String status, String category) throws Exception {
        List<String> statusList = new ArrayList<>();
        if(Strings.isNullOrEmpty(status)){
            for (AssetStatusEnum statusEnum:AssetStatusEnum.values()){
                statusList.add(statusEnum.getValue());
            }
        }else {
            statusList = Arrays.asList(status.split(","));
        }
        if(Strings.isNullOrEmpty(category)){
            category = "1000001,1000002";
        }
        List<String> categoryList = Arrays.asList(category.split(","));
        //用来存放不同状态的不同资产类别的数量
        List<LinkedHashMap<String,Integer>> categoryMapList = new ArrayList<>();
        for(int i = 0;i< statusList.size();i++) {
            LinkedHashMap<String,Integer> countMap = new LinkedHashMap<>();
            for (String categoryone : categoryList) {
                List<String> state = Arrays.asList(statusList.get(i));
                AssetCriteria Criteria = new AssetCriteria();
                ArrayList<QueryCond> fixquery = new ArrayList<>();
                QueryCond Status = new QueryCond();
                QueryCond assetNo = new QueryCond();
                QueryCond spercification = new QueryCond();
                QueryCond assetName = new QueryCond();
                QueryCond categoryCond = new QueryCond();
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
                categoryCond.setCond("eq");
                categoryCond.setFieldKey("category");
                categoryCond.setFieldValue(categoryone);
                fixquery.add(Status);
                fixquery.add(assetName);
                fixquery.add(assetNo);
                fixquery.add(spercification);
                fixquery.add(categoryCond);
                Criteria.setFixQuery(fixquery);

                JsonModel count = null;
                try {
                    count = rpcProcessService.search(Criteria);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new JsonModel(false, e.getMessage());
                }
                LinkedHashMap<Object, Object> obj = (LinkedHashMap<Object, Object>)count.getObj();
                //取出该状态库存的数量
                countMap.put(categoryone, (Integer) obj.get("count"));
            }
            categoryMapList.add(countMap);
        }

        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        columns.add("资产状态");
        JSONArray colors = new JSONArray();
        Map<String,String > categoryNameMap = rpcProcessService.getCategory();
        for(int i = 0;i < categoryMapList.size();i++){
            JSONObject row = new JSONObject();
            row.put("资产状态",AssetStatusEnum.valueOf(statusList.get(i)).getText());
            for(String categoryone : categoryList) {
                if(i == 0)columns.add(categoryNameMap.get(categoryone));
                row.put(categoryNameMap.get(categoryone), categoryMapList.get(i).get(categoryone));
            }
            rows.add(row);
        }

        result.put("columns",columns);
        result.put("colors",colors);
        result.put("rows",rows);

        return new JsonModel(true,result);
    }

    public JsonModel assetCountByOrga(String orgaIds, String category) throws Exception {
        //用来存放选择了的部门ID
        List<String> orgaIdsList ;
        //用来存放所有的部门ID
        List<String> orgaIdsList2 = new ArrayList<>();

        //查询部门，做一个部门id和name的Map
        SiteOrganizationCriteria criteria = new SiteOrganizationCriteria();
        JsonModel jsonModel = rpcProcessService.getOrganList(criteria);
        LinkedHashMap<Object,Object> obj = (LinkedHashMap<Object, Object>) jsonModel.getObj();
        List<LinkedHashMap<Object,Object>> orgaList = (List<LinkedHashMap<Object, Object>>) obj.get("object");
        Map<String,String> organNameMap = new HashMap<>();
        for (LinkedHashMap<Object,Object> map : orgaList){
            organNameMap.put((String) map.get("id"), (String) map.get("name"));
            orgaIdsList2.add((String) map.get("id"));
        }
        if(Strings.isNullOrEmpty(orgaIds)){
            //如果没选部门，则展示所有部门
            orgaIdsList = orgaIdsList2;
        }else {
            orgaIdsList = Arrays.asList(orgaIds.split(","));
        }

        if(Strings.isNullOrEmpty(category)){
            category = "1000001,1000002";
        }
        List<String> categoryList = Arrays.asList(category.split(","));
        //用来存放不同状态的不同资产类别的数量
        List<LinkedHashMap<String,Integer>> categoryMapList = new ArrayList<>();
        for(int i = 0;i< orgaIdsList.size();i++) {
            LinkedHashMap<String, Integer> countMap = new LinkedHashMap<>();
            //各部门的资产总数量，用于排序
            Integer totalCount = 0;
            for (String categoryone : categoryList) {
                AssetCriteria Criteria = new AssetCriteria();
                ArrayList<QueryCond> fixquery = new ArrayList<>();
                QueryCond Status = new QueryCond();
                QueryCond assetNo = new QueryCond();
                QueryCond spercification = new QueryCond();
                QueryCond assetName = new QueryCond();
                QueryCond categoryCond = new QueryCond();

                Status.setCond("in");
                Status.setFieldKey("department");
                JSONObject department = new JSONObject();
                department.put("0", orgaIdsList.get(i));
                department.put("1", orgaIdsList.get(i));
                department.put("2", orgaIdsList.get(i));
                Status.setFieldValue(department);
                assetNo.setCond("contains");
                assetNo.setFieldKey("assetNo");
                assetNo.setFieldValue("");
                spercification.setCond("contains");
                spercification.setFieldKey("spercification");
                spercification.setFieldValue("");
                assetName.setCond("contains");
                assetName.setFieldKey("assetName");
                assetName.setFieldValue("");
                categoryCond.setCond("eq");
                categoryCond.setFieldKey("category");
                categoryCond.setFieldValue("1000001");
                fixquery.add(Status);
                fixquery.add(assetName);
                fixquery.add(assetNo);
                fixquery.add(spercification);
                fixquery.add(categoryCond);
                Criteria.setFixQuery(fixquery);

                JsonModel count = null;
                try {
                    count = rpcProcessService.search(Criteria);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new JsonModel(false, e.getMessage());
                }
                LinkedHashMap<Object, Object> countobj = (LinkedHashMap<Object, Object>) count.getObj();
                //取出该状态库存的数量
                countMap.put(categoryone, (Integer) countobj.get("count"));
                totalCount+= (Integer) countobj.get("count");
            }
            countMap.put("totalCount",totalCount);
            categoryMapList.add(countMap);
        }
        //对各部门资产总数量进行排序
        Collections.sort(categoryMapList,new Comparator<LinkedHashMap<String, Integer>>(){
            @Override
            public int compare(LinkedHashMap<String, Integer> o1, LinkedHashMap<String, Integer> o2) {
                int count1 = o1.get("totalCount");
                int count2 = o2.get("totalCount");
                return count2 - count1;
            }
        });

        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        columns.add("部门");
        JSONArray colors = new JSONArray();
        Map<String,String > categoryNameMap = rpcProcessService.getCategory();
        for(int i = 0;i < categoryMapList.size();i++){
            JSONObject row = new JSONObject();
            row.put("部门",organNameMap.get(orgaIdsList.get(i)));
            for(String categoryone : categoryList) {
                if(i == 0)columns.add(categoryNameMap.get(categoryone));
                row.put(categoryNameMap.get(categoryone), categoryMapList.get(i).get(categoryone));
            }
            rows.add(row);
        }

        result.put("columns",columns);
        result.put("colors",colors);
        result.put("rows",rows);

        return new JsonModel(true,result);
    }

    public JsonModel searchStandingBook(String operType, Integer interval) {
        if(ObjectUtils.isEmpty(operType)){
            operType = "in,transfer,taking,onSecondment,backToWarehouse";
        }
        List<String> typelist = Arrays.asList(operType.split(","));
        //做一个台账类型的nameMap
        LinkedHashMap<String,String> operTypeNameMap = new LinkedHashMap<>();
        operTypeNameMap.put("in","入库");
        operTypeNameMap.put("transfer","转移");
        operTypeNameMap.put("taking","领用");
        operTypeNameMap.put("onSecondment","借调");
        operTypeNameMap.put("backToWarehouse","回库");

        //去asset查询数据循环的次数,按年查则查询12次，否则都查询7次
        int times;
        if (interval == 30){
            times = 12;
        }else {
            times = 7;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");

        JSONArray resultArray = new JSONArray();
        for(Integer i = 0;i < typelist.size();i++){
            List<LinkedHashMap<Object,Object>> list = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            //endtime要先加一天
            calendar2.add(Calendar.DATE,1);
            for(int j = 0;j < times;j++){
                LinkedHashMap<Object,Object> map = new LinkedHashMap<>();
                //先把查询的时间设置好
                if(j > 0) {
                    calendar.add(Calendar.DATE, -1 * interval);
                    calendar2.add(Calendar.DATE, -1 * interval);
                }
                String startTime = sf.format(calendar.getTime());
                String fetchDate = sf2.format(calendar.getTime());
                String endTime = sf.format(calendar2.getTime());

                AssetCriteria criteria = new AssetCriteria();
                ArrayList<QueryCond> fixquery = new ArrayList<>();
                QueryCond docmakerDate1 = new QueryCond();
                QueryCond docmakerDate2 = new QueryCond();
                QueryCond operType1 = new QueryCond();
                QueryCond Status = new QueryCond();
                QueryCond assetNo = new QueryCond();
                QueryCond spercification = new QueryCond();
                QueryCond assetName = new QueryCond();
                QueryCond category = new QueryCond();
                Status.setCond("contains");
                Status.setFieldKey("status");
                Status.setFieldValue("");
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
                operType1.setCond("eq");
                operType1.setFieldKey("operType");
                operType1.setFieldValue(typelist.get(i));
                docmakerDate1.setCond("gt");
                docmakerDate1.setFieldKey("docmakerDate");
                docmakerDate1.setFieldValue(startTime);
                docmakerDate2.setCond("lt");
                docmakerDate2.setFieldKey("docmakerDate");
                docmakerDate2.setFieldValue(endTime);

                fixquery.add(operType1);
                fixquery.add(docmakerDate1);
                fixquery.add(docmakerDate2);
                criteria.setFixQuery(fixquery);
                JsonModel jsonModel;
                try {
                     jsonModel = rpcProcessService.searchStandingbook(criteria);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new JsonModel(false,e.getMessage());
                }
                LinkedHashMap<Object, Object> obj = (LinkedHashMap<Object, Object>) jsonModel.getObj();
                map.put(typelist.get(i),obj.get("count"));
                map.put("time",fetchDate);
                list.add(map);
            }
            resultArray.add(list);
        }

        //封装数据
        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        columns.add("制单日期");
        for (int i = 0;i < typelist.size(); i++){
            columns.add(operTypeNameMap.get(typelist.get(i)));
        }

        for(int i = 0;i <times;i++){
            JSONObject row = new JSONObject();
            for (int j = 0 ;j <typelist.size();j++ ){
                List<LinkedHashMap<Object,Object>> list = (List<LinkedHashMap<Object, Object>>) resultArray.get(j);
                if (j == 0)row.put("制单日期",list.get(i).get("time"));
                row.put(operTypeNameMap.get(typelist.get(j)),list.get(i).get(typelist.get(j)));
            }
            rows.add(row);
        }

        JSONObject result = new JSONObject();
        result.put("columns",columns);
        result.put("rows",rows);
        result.put("unit","个");


        return new JsonModel(true,result);
    }
}
