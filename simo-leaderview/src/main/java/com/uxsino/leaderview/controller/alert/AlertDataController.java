package com.uxsino.leaderview.controller.alert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.utils.StringUtils;
import com.uxsino.leaderview.service.api.AlertDataService;
import com.uxsino.watcher.lib.annoation.Business;
import com.uxsino.watcher.lib.enums.BusinessConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Api(tags = { "告警-大屏展示数据接口" })
@RestController
@RequestMapping("/alert")
@Business(name = BusinessConstants.ALERT)
@Slf4j
public class AlertDataController {

    @Autowired
    AlertDataService alertDataService;

    @ApiOperation("按告警级别统计资源的未处理告警条数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertType", paramType = "query", dataType = "String", value = "告警类型"),
            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "多个告警级别用,分隔")
    })
    @RequestMapping(value = "/countAlert", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel countAlert(HttpSession session,
                                @RequestParam(required = false) String alertType,
                                @RequestParam(required = false) String alertLevel) {
        try {
            return alertDataService.countAlert(session, alertType, alertLevel);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    /**
     * 按告警类型查询最近告警
     *
     * @param type    告警类型
     * @param session 会话
     * @return
     */
    @ApiOperation("按告警类型查询最近告警")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "String", value = "告警类型")
    })
    @RequestMapping(value = "/getOtherAlertInfo", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getOtherAlertInfo(HttpSession session, @RequestParam String type) {
        try {
            return alertDataService.getOtherAlertInfo(session, type);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("按告警类型查询最近告警")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "String", value = "告警类型"),
            @ApiImplicitParam(name = "number", paramType = "query", dataType = "Long", value = "展示条数")
    })
    @RequestMapping(value = "/getOtherAlertTable", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getOtherAlertTable(HttpSession session,
                                        @RequestParam String type, @RequestParam Long number,
                                        @RequestParam(required = false)String[] column) {
        try {
            return alertDataService.getOtherAlertTable(session, type, number, column);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("获取告警分布数量数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "告警级别", required = false),
            @ApiImplicitParam(name = "range", paramType = "query", dataType = "String", value = "展示范围", required = false),
            @ApiImplicitParam(name = "areaName", paramType = "query", dataType = "String", value = "已选地区", required = false),
            @ApiImplicitParam(name = "names", paramType = "query", dataType = "String", value = "展示范围下的地区名集合", required = false),
            @ApiImplicitParam(name = "period", paramType = "query", dataType = "String", value = "颗粒度", required = false)})
    @RequestMapping(value = "/v_map/alertDivision", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel alertDivision(HttpSession session, @RequestParam(required = false) String[] alertLevel,
                                   @RequestParam(required = false) String range,
                                   @RequestParam(required = false) String[] names,
                                   @RequestParam(required = false) String areaName,
                                   @RequestParam(required = false) Integer period) {
        try {
            return alertDataService.alertDivision(session,alertLevel, range, names, areaName, period);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("获取告警分布最高级别数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "range", paramType = "query", dataType = "String", value = "展示范围", required = false),
            @ApiImplicitParam(name = "areaName", paramType = "query", dataType = "String", value = "已选地区", required = false),
            @ApiImplicitParam(name = "names", paramType = "query", dataType = "String", value = "展示范围下的地区名集合", required = false)})
    @RequestMapping(value = "/v_scatter/alertLevelDivision", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel alertLevelDivision(HttpSession session,
                                        @RequestParam(required = false) String range,
                                        @RequestParam(required = false) String[] names,
                                        @RequestParam(required = false) String areaName) {
        try {
            return alertDataService.alertLevelDivision(session, range, names, areaName);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("按告警级别统计资源的未处理告警条数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "多个告警级别用,分隔"),
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型"),
            @ApiImplicitParam(name = "neClass", paramType = "query", dataType = "String", value = "资源子类型"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "多个资源ID用,分隔")
    })
    @RequestMapping(value = "/getStatByLevel", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getStatByLevel(HttpSession session, String alertLevel, Long domainId,
                                    String baseNeClass, String neClass, String neIds) {
        try {
            return alertDataService.getStatByLevel(session, alertLevel, domainId, baseNeClass, neClass, neIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("按告警级别统计资源的未处理告警条数，用于列固定的组件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "多个告警级别用,分隔"),
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型"),
            @ApiImplicitParam(name = "neClass", paramType = "query", dataType = "String", value = "资源子类型"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "多个资源ID用,分隔")
    })
    @RequestMapping(value = "/getStatByLevelForRows", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getStatByLevelForRows(HttpSession session, String alertLevel, Long domainId,
                                    String baseNeClass, String neClass, String neIds) {
        try {
            JsonModel deprecatedWrap = alertDataService.getStatByLevel(session, alertLevel, domainId, baseNeClass, neClass, neIds);
            JSONArray oldRows = (JSONArray)((JSONObject)deprecatedWrap.getObj()).get("rows");
            JSONObject json = new JSONObject();
            JSONArray newRows = new JSONArray();
            for(Object object: oldRows){
                JSONObject oldObject = (JSONObject) object;
                JSONObject newObject = new JSONObject();
                newObject.put("name", oldObject.get("告警类型"));
                newObject.put("value", oldObject.get("数量"));
                newRows.add(newObject);
            }
            json.put("rows", newRows);
            return new JsonModel(true, json);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("按告警级别统计资源的未处理告警条数，用于值为范围的组件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "多个告警级别用,分隔"),
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型"),
            @ApiImplicitParam(name = "neClass", paramType = "query", dataType = "String", value = "资源子类型"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "多个资源ID用,分隔")
    })
    @RequestMapping(value = "/getStatByLevelForRange", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getStatByLevelForRange(HttpSession session, String alertLevel, Long domainId,
                                    String baseNeClass, String neClass, String neIds) {
        try {
            JsonModel deprecatedWrap = alertDataService.getStatByLevel(session, alertLevel, domainId, baseNeClass, neClass, neIds);
            JSONArray oldRows = (JSONArray)((JSONObject)deprecatedWrap.getObj()).get("rows");
            JSONObject json = new JSONObject();
            JSONArray newRows = new JSONArray();
            for(Object object: oldRows){
                JSONObject oldObject = (JSONObject) object;
                JSONObject newObject = new JSONObject();
                JSONArray range = new JSONArray();
                JSONArray average = new JSONArray();
                range.add(0);
                range.add(oldObject.get("数量"));
                average.add(oldObject.get("数量"));
                newObject.put("告警类型", oldObject.get("告警类型"));
                newObject.put("数量", range);
                newObject.put("均值", average);
                newRows.add(newObject);
            }
            json.put("rows", newRows);
            json.put("columns", new String[]{"告警类型","数量","均值"});
            return new JsonModel(true, json);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("按资源类型统计资源的未处理告警条数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "多个告警级别用,分隔"),
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")
    })
    @RequestMapping(value = "/getStatByClass", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getStatByClass(HttpSession session, String alertLevel, Long domainId, String baseNeClass) {
        try {
            return alertDataService.getStatByClass(session, alertLevel, domainId, baseNeClass);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("按资源类型统计资源的未处理告警条数，用于列固定的组件，仅显示总和")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "多个告警级别用,分隔"),
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")
    })
    @RequestMapping(value = "/getStatByClassForRows", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getStatByClassForRows(HttpSession session, String alertLevel, Long domainId, String baseNeClass) {
        try {
            JsonModel deprecatedWrap = alertDataService.getStatByClass(session, alertLevel, domainId, baseNeClass);
            JSONObject json = new JSONObject();
            JSONArray oldRows = (JSONArray)((JSONObject)deprecatedWrap.getObj()).get("rows");
            JSONArray oldColumns = (JSONArray)((JSONObject)deprecatedWrap.getObj()).get("columns");
            JSONArray newRows = new JSONArray();
            JSONObject temp = new JSONObject();
            ArrayList<String> keys = new ArrayList<>();
            if(baseNeClass == null || StringUtils.isEmpty(baseNeClass)) {
                BaseNeClass[] baseNeClasses = BaseNeClass.values();
                for(BaseNeClass bnc: baseNeClasses){
                    keys.add(bnc.getText());
                }
            } else {
                BaseNeClass bnc = BaseNeClass.valueOf(baseNeClass);
                List<NeClass> ncList = bnc.getNeClass();
                for(NeClass nc: ncList){
                    keys.add(nc.getText());
                }
            }
            for(String key: keys){
                temp.put(key, 0L);
            }
            /*
                由于气泡图等固定列只有name和value两个，不能做到多个设备都分别统计多个告警类型，
                因此暂时决定返回的是选中类型的所有子类型的各种告警类型自己的总和
             */
            for(Object object: oldRows){
                JSONObject oldRow = (JSONObject)object;
                String type = (String)oldRow.get("资源类型");
                long count = (long)temp.get(type);
                for(JSONObject.Entry entry: oldRow.entrySet()){
                    String k = (String)entry.getKey();
                    if(!k.equals("资源类型") && !k.equals("资源名")){
                        //在没有指定哪种告警级别时则全部都加入
                        if(alertLevel==null || StringUtils.isEmpty(alertLevel) || alertLevel.contains(k)){
                            count += (long)entry.getValue();
                        }
                    }
                }
                temp.put(type, count);
            }
            for(String key: keys){
                JSONObject t = new JSONObject();
                t.put("name", key);
                t.put("value", temp.get(key));
                newRows.add(t);
            }
            json.put("rows", newRows);
            return new JsonModel(true, json);
         } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("按资源类型统计资源的未处理告警条数，用于值为范围的组件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "多个告警级别用,分隔"),
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")
    })
    @RequestMapping(value = "/getStatByClassForRange", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getStatByClassForRange(HttpSession session, String alertLevel, Long domainId, String baseNeClass) {
        try {
            JsonModel deprecatedWrap = alertDataService.getStatByClass(session, alertLevel, domainId, baseNeClass);
            JSONArray oldRows = (JSONArray)((JSONObject)deprecatedWrap.getObj()).get("rows");
            JSONObject json = new JSONObject();
            JSONArray newRows = new JSONArray();
            for(Object object: oldRows){
                JSONObject oldObject = (JSONObject) object;
                JSONObject newObject = new JSONObject();
                JSONArray range = new JSONArray();
                for(JSONObject.Entry entry: oldObject.entrySet()){
                    if(!"资源类型".equals((String)entry.getKey()) && !"资源名".equals((String)entry.getKey())){
                        range.add(entry.getValue());
                        newObject.put((String)entry.getKey(), range);
                    }else{
                        newObject.put((String)entry.getKey(), (int)entry.getValue());
                    }
                }
                newRows.add(newObject);
            }
            json.put("rows", newRows);
            json.put("columns", (JSONArray)((JSONObject)deprecatedWrap.getObj()).get("columns"));
            return new JsonModel(true, json);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("按资源类型统计资源的未处理告警条数，用于旭日图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "多个告警级别用,分隔"),
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")
    })
    @RequestMapping(value = "/getStatByClassForSunburst", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getStatByClassForSunburst(HttpSession session, String alertLevel, Long domainId, String baseNeClass) {
        try {
            if(baseNeClass != null && !StringUtils.isEmpty(baseNeClass)){
                JsonModel deprecatedWrap = alertDataService.getStatByClass(session, alertLevel, domainId, baseNeClass);
                JSONArray oldRows = (JSONArray)((JSONObject)deprecatedWrap.getObj()).get("rows");
                JSONArray children = new JSONArray();
                for(Object object: oldRows){
                    JSONObject oldRow = (JSONObject)object;
                    JSONObject child = new JSONObject();
                    JSONArray subChildren = new JSONArray();
                    for(JSONObject.Entry<String, Object> entry: oldRow.entrySet()){
                        JSONObject subChild;
                        if(!entry.getKey().equals("资源类型")){
                            subChild = new JSONObject();
                            subChild.put("name", entry.getKey());
                            subChild.put("value", entry.getValue());
                            subChildren.add(subChild);
                        }
                    }
                    child.put("name", oldRow.get("资源类型"));
                    child.put("children", subChildren);
                    children.add(child);
                }
                JSONObject father = new JSONObject();
                father.put("name", BaseNeClass.valueOf(baseNeClass).getText());
                father.put("children", children);
                JSONArray dataArry = new JSONArray();
                dataArry.add(father);
                JSONObject result = new JSONObject();
                result.put("dataArry", dataArry);
                return new JsonModel(true, result);
            }else{
                BaseNeClass[] baseNeClasses = BaseNeClass.values();
                JSONArray dataArry = new JSONArray();
                for(BaseNeClass temp: baseNeClasses){
                    JsonModel deprecatedWrap = alertDataService.getStatByClass(session, alertLevel, domainId, temp.toString());
                    JSONArray oldRows = (JSONArray)((JSONObject)deprecatedWrap.getObj()).get("rows");
                    JSONArray children = new JSONArray();
                    for(Object object: oldRows){
                        JSONObject oldRow = (JSONObject)object;
                        JSONObject child = new JSONObject();
                        JSONArray subChildren = new JSONArray();
                        for(JSONObject.Entry<String, Object> entry: oldRow.entrySet()){
                            JSONObject subChild;
                            if(!entry.getKey().equals("资源类型")){
                                subChild = new JSONObject();
                                subChild.put("name", entry.getKey());
                                subChild.put("value", entry.getValue());
                                subChildren.add(subChild);
                            }
                        }
                        child.put("name", oldRow.get("资源类型"));
                        child.put("children", subChildren);
                        children.add(child);
                    }
                    JSONObject father = new JSONObject();
                    father.put("name", temp.getText());
                    father.put("children", children);
                    dataArry.add(father);
                }
                JSONObject result = new JSONObject();
                result.put("dataArry", dataArry);
                return new JsonModel(true, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("按资源统计资源的未处理告警条数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "多个告警级别用,分隔"),
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型"),
            @ApiImplicitParam(name = "neClass", paramType = "query", dataType = "String", value = "资源子类型"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "多个资源ID用,分隔")
    })
    @RequestMapping(value = "/getStatByNe", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getStatByNe(String alertLevel, Long domainId, String baseNeClass, String neClass, String neIds, HttpSession session) {
        try {
            return alertDataService.getStatByNe(alertLevel, domainId, baseNeClass, neClass, neIds, session);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("大屏展示：根据选择的域、父类型、资源id和展示条数来查询最新的规定条数的告警")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源ID"),
            @ApiImplicitParam(name = "number", paramType = "query", dataType = "Long", value = "展示条数", required = true)
    })
    @RequestMapping(value = "/getAlertInfo", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getAlertInfo(@RequestParam(required = false) Long domainId,
                                  @RequestParam(required = false) String baseNeClass,
                                  @RequestParam(required = false) String[] neIds,
                                  @RequestParam Long number,
                                  @RequestParam(required = false) String[] column,
                                  HttpSession session){
        try {
            return alertDataService.getAlertInfo(domainId, baseNeClass, neIds, number, session, column);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("大屏展示：根据选择的域、父类型、资源id和展示条数来查询最新的规定条数的告警，根据文本框元件需求返回值封装String")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源ID"),
    })
    @RequestMapping(value = "/getAlertInfoForText", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getAlertInfoForText(@RequestParam(required = false) Long domainId,
                                         @RequestParam(required = false) String baseNeClass,
                                         @RequestParam(required = false) String[] neIds,
                                         HttpSession session){
        try {
            return alertDataService.getAlertInfoForText(domainId, baseNeClass, neIds, session);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("按资源类型统计资源的未处理告警条数，雷达图优化")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "多个告警级别用,分隔"),
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型"),
            @ApiImplicitParam(name = "neClass", paramType = "query", dataType = "String", value = "资源子类型"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "资源Ids")
    })
    @RequestMapping(value = "/getStatByClassForRadar", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getStatByClassForRadar(HttpSession session, String alertLevel,
                                            Long domainId, String baseNeClass, String neClass, String neIds) {
        try {
            return alertDataService.getStatByClassForRadar(session, alertLevel, domainId, baseNeClass, neClass, neIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

//    @ApiOperation("按告警级别统计资源的未处理告警条数")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "alertType", paramType = "query", dataType = "String", value = "告警类型"),
//            @ApiImplicitParam(name = "alertLevel", paramType = "query", dataType = "String", value = "多个告警级别用,分隔")
//    })
//    @RequestMapping(value = "/countAlert", method = RequestMethod.GET)
//    @ResponseBody
//    public JsonModel countAlert(HttpSession session,
//                                @RequestParam(required = false) String alertType,
//                                @RequestParam(required = false) String alertLevel) {
//        return alertDataService.countAlert1(session, alertType, alertLevel);
//
//    }

}
