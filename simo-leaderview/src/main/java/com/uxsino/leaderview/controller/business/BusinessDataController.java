package com.uxsino.leaderview.controller.business;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.api.BusinessDataService;
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
import java.util.Map;

@Api(tags = { "业务-大屏展示数据接口" })
@RestController
@RequestMapping("/business")
@Business(name = BusinessConstants.BUSINESS)
@Slf4j
public class BusinessDataController {

    @Autowired
    private BusinessDataService businessDataService;


    @ApiOperation("根据业务ID和指标名获取具体指标数据")
    @RequestMapping(value = "/indicatorValue", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType =  "query", dataType = "String", value = "业务Id"),
            @ApiImplicitParam(name = "indicator", paramType = "query", dataType = "String", value = "指标名")
    })
    @ResponseBody
    public JsonModel indicatorValue(HttpSession session, @RequestParam String business, @RequestParam String indicator){
        try {
            return businessDataService.indicatorValue(session, business, indicator);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("多业务多指标统计")
    @RequestMapping(value = "/multipleBusinessIndicator", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType =  "query", dataType = "List<String>", value = "业务Id"),
            @ApiImplicitParam(name = "indicator", paramType = "query", dataType = "String", value = "指标名")
    })
    @ResponseBody
    public JsonModel multipleBusinessIndicator(HttpSession session, @RequestParam String[] business, @RequestParam String indicator){
        try {
            return businessDataService.multipleBusinessIndicator(session, business, indicator);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("根据选择的业务，按状态进行统计")
    @RequestMapping(value = "/businessStatisticsByStatus", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType = "query", dataType = "List<String>", value = "业务")
    })
    @ResponseBody
    public JsonModel businessStatisticsByStatus(HttpSession session, @RequestParam String[] business){
        try {
            return businessDataService.businessStatisticsByStatus(session, business);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("根据选择的业务，按状态进行统计,用于列固定的元件")
    @RequestMapping(value = "/businessStatisticsByStatusForRows", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType = "query", dataType = "List<String>", value = "业务")
    })
    @ResponseBody
    public JsonModel businessStatisticsByStatusForRows(HttpSession session, @RequestParam String[] business){
        try {
            JsonModel deprecatedWrap = businessDataService.businessStatisticsByStatus(session, business);
            JSONArray oldRows = (JSONArray)((JSONObject)deprecatedWrap.getObj()).get("rows");
            JSONObject json = new JSONObject();
            JSONArray newRows = new JSONArray();
            for(Object object: oldRows){
                JSONObject oldObject = (JSONObject) object;
                JSONObject newObject = new JSONObject();
                newObject.put("name", oldObject.get("状态"));
                newObject.put("value", oldObject.get("数量"));
                newRows.add(newObject);
            }
            json.put("rows", newRows);
            return new JsonModel(true, json);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("根据选择的业务，按状态进行统计,用于值为范围的组件")
    @RequestMapping(value = "/businessStatisticsByStatusForRange", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType = "query", dataType = "List<String>", value = "业务")
    })
    @ResponseBody
    public JsonModel businessStatisticsByStatusForRange(HttpSession session, @RequestParam String[] business){
        try {
            JsonModel deprecatedWrap = businessDataService.businessStatisticsByStatus(session, business);
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
                newObject.put("状态", oldObject.get("状态"));
                newObject.put("数量", range);
                newObject.put("均值", average);
                newRows.add(newObject);
            }
            json.put("rows", newRows);
            json.put("columns", new String[]{"状态","数量","均值"});
            return new JsonModel(true, json);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("根据选择的业务，对告警进行统计")
    @RequestMapping(value = "/getStatByBusiness", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType = "query", dataType = "List<String>", value = "业务")
    })
    @ResponseBody
    public JsonModel getStatByBusiness(HttpSession session, @RequestParam String[] business){
        try {
            return businessDataService.getStatByBusiness(session, business);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("根据选择的业务，对告警进行统计，用于列固定的组件")
    @RequestMapping(value = "/getStatByBusinessForRows", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType = "query", dataType = "List<String>", value = "业务")
    })
    @ResponseBody
    public JsonModel getStatByBusinessForRows(HttpSession session, @RequestParam String[] business){
        try {
            JsonModel deprecatedWrap =  businessDataService.getStatByBusiness(session, business);
            JSONArray oldRows = (JSONArray)((JSONObject)deprecatedWrap.getObj()).get("rows");
            JSONObject json = new JSONObject();
            JSONArray newRows = new JSONArray();
            for(Object object: oldRows){
                JSONObject oldObject = (JSONObject) object;
                JSONObject newObject = new JSONObject();
                newObject.put("name", oldObject.get("业务"));
                newObject.put("value", oldObject.get("数量"));
                newRows.add(newObject);
            }
            json.put("rows", newRows);
            return new JsonModel(true, json);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("根据选择的业务，对告警进行统计，用于值为范围的组件")
    @RequestMapping(value = "/getStatByBusinessForRange", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType = "query", dataType = "List<String>", value = "业务")
    })
    @ResponseBody
    public JsonModel getStatByBusinessForRange(HttpSession session, @RequestParam String[] business){
        try {
            JsonModel deprecatedWrap =  businessDataService.getStatByBusiness(session, business);
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
                newObject.put("业务", oldObject.get("业务"));
                newObject.put("数量", range);
                newObject.put("均值", average);
                newRows.add(newObject);
            }
            json.put("rows", newRows);
            json.put("columns", new String[]{"业务","数量","均值"});
            return new JsonModel(true, json);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("根据业务ID和指标名和展示条数来展示指标TOPN")
    @RequestMapping(value = "/getIndicatorTOPN", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType =  "query", dataType = "String",allowMultiple=true, value = "业务Id"),
            @ApiImplicitParam(name = "indicator", paramType = "query", dataType = "String", value = "指标名"),
            @ApiImplicitParam(name = "number", paramType = "query", dataType = "Integer", value = "展示条数"),
            @ApiImplicitParam(name = "order", paramType = "query", dataType = "String", value = "排序方式"),
            @ApiImplicitParam(name = "bar", paramType = "query", dataType = "boolean", value = "柱状图类型")
    })
    @ResponseBody
    public JsonModel getIndicatorTOPN(HttpSession session, @RequestParam String[] business,
                                      @RequestParam String indicator, @RequestParam Integer number,
                                      @RequestParam(required = false) String order, boolean bar){
        try {
            return businessDataService.getIndicatorTOPN(session, business, indicator, number, order, bar);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("根据状态统计业务数量")
    @RequestMapping(value = "/countBusiness", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", paramType = "query", dataType = "Boolean", value = "状态")
    })
    @ResponseBody
    public JsonModel countBusiness(HttpSession session, @RequestParam Boolean status){
        try {
            return businessDataService.countBusiness(session, status);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

//    /**
//     * 根据业务ID和指标名来展示历史指标数据
//     * @param session
//     * @param business
//     * @param indicator
//     * @param period
//     * @return
//     */
//    @ApiOperation("根据业务ID和指标名来展示历史指标数据")
//    @RequestMapping(value = "/history/record", method = RequestMethod.GET)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "business", paramType =  "query", dataType = "List<String>", value = "业务Id"),
//            @ApiImplicitParam(name = "indicator", paramType = "query", dataType = "String", value = "指标名"),
//            @ApiImplicitParam(name = "number", paramType = "query", dataType = "String", value = "统计时段")
//    })
//    @ResponseBody
//    public JsonModel getHistoryValue(HttpSession session, @RequestParam String[] business,
//                                     @RequestParam Indicator indicator, @RequestParam String period){
//        if (ObjectUtils.isEmpty(business) || ObjectUtils.isEmpty(indicator)){
//            return new JsonModel(true, empObj());
//        }
//        JSONObject result = new JSONObject();
//        JSONArray rows = new JSONArray();
//        JSONArray columns = new JSONArray();
//        columns.add("采集时间");
//        Date endDate = new Date();
//        Date startDate = new Date();
//        if ("_1day".equals(period)){
//            startDate = new Date(endDate.getTime() - (1000 * 60 * 60 * 24L));
//        }else if ("_1week".equals(period)){
//            startDate = new Date(endDate.getTime() - (1000 * 60 * 60 * 24L * 7));
//        }else if ("_1month".equals(period)){
//            startDate = new Date(endDate.getTime() - (1000 * 60 * 60 * 24L * 30));
//        }
//        //进行权限过滤
//        JSONArray businessArr = getBusStatus(session, business);
//        JSONObject allData = new JSONObject();
//        for (int i = 0; i < businessArr.size(); i++) {
//            String businessId = businessArr.getJSONObject(i).getString("id");
//            JSONArray arr = indicatorService.findHistoryValue(businessId, indicator, startDate, endDate, false, null);
//            allData.put(businessArr.getJSONObject(i).getString("name"), arr);
//        }
//        //组装返回数据
//        JSONArray wrapArr = new JSONArray();
//        for (int i = 0; i < businessArr.size(); i++) {
//            String businessName = businessArr.getJSONObject(i).getString("name");
//            columns.add(businessName);
//            JSONArray array = allData.getJSONArray(businessName);
//            for (int j = 1; j < array.size(); j++) {
//                JSONObject obj = array.getJSONObject(j);
//                obj.put("name", businessName);
//                obj.getLong("fetchDate");
//                wrapArr.add(obj);
//            }
//        }
//        //遍历统计所有数据，将时间相同的数据归为一组数据用于展示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        List<Long> usedData = Lists.newArrayList();
//        for (int i = 0; i < wrapArr.size(); i++) {
//            JSONObject obj = wrapArr.getJSONObject(i);
//            JSONObject row = new JSONObject();
//            Long fetchDateLong = obj.getLong("fetchDate");
//            if (usedData.contains(fetchDateLong)) continue;
//            usedData.add(fetchDateLong);
//            row.put("采集时间", format.format(new Date(fetchDateLong)));
//            row.put(obj.getString("name"), obj.getDoubleValue("value"));
//            for (int j = i + 1; j < wrapArr.size(); j++) {
//                JSONObject temObj = wrapArr.getJSONObject(j);
//                if (fetchDateLong.compareTo(temObj.getLong("fetchDate")) == 0){
//                    row.put(temObj.getString("name"), temObj.getDoubleValue("value"));
//                }
//            }
//            rows.add(row);
//        }
//        result.put("columns", columns);
//        result.put("rows", rows);
//        result.put("unit", indicator.getUnit());
//        return new JsonModel(true, result);
//    }
//

    /**
     * 获取业务拓扑图
     * @param session 会话
     * @param topoId 拓扑id
     */
    @ApiOperation("获取业务拓扑图")
    @RequestMapping(value = "/getTopo", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topoId", paramType = "query", dataType = "String", value = "拓扑名称")
    })
    @ResponseBody
    public JsonModel getTopo(HttpSession session, @RequestParam String topoId){
        Map<String, String> map = Maps.newHashMap();
        map.put("topoId",topoId);
        return new JsonModel(true, map);
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ApiOperation("业务告警信息接口，表格格式")
    @RequestMapping(value = "/getBusinessAlertInfoForTable", method = RequestMethod.GET)
    @ApiImplicitParams({ @ApiImplicitParam(name = "business", paramType = "query", dataType = "String", value = "业务Id"),
            @ApiImplicitParam(name = "number", paramType = "query", dataType = "int", value = "展示条数") })
    @ResponseBody
    public JsonModel getBusinessAlertInfoForTable(HttpSession session, @RequestParam String business,
                                                  @RequestParam Integer number) {
        try {
            return businessDataService.getBusinessAlertInfoForTable(session, business, number);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("业务告警信息接口，文字格式")
    @RequestMapping(value = "/getBusinessAlertInfoForText", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType = "query", dataType = "String", value = "业务Id") })
    @ResponseBody
    public JsonModel getBusinessAlertInfoForText(HttpSession session, @RequestParam String business) {
        try {
            return businessDataService.getBusinessAlertInfoForText(session, business);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

}
