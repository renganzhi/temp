package com.uxsino.leaderview.controller.monitor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.db.model.IntervalType;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.RunStatus;
import com.uxsino.leaderview.model.monitor.IndPeriod;
import com.uxsino.leaderview.model.monitor.NetworkEntityCriteria;
import com.uxsino.leaderview.model.monitor.PerormanceView;
import com.uxsino.leaderview.rpc.AlertService;
import com.uxsino.leaderview.rpc.MonitorService;
import com.uxsino.leaderview.service.VideoMonitoringService;
import com.uxsino.leaderview.service.api.MonitorDataService;
import com.uxsino.leaderview.service.api.RpcProcessService;
import com.uxsino.leaderview.utils.MonitorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.*;


@Api(tags = {"资源-大屏展示数据接口"})
@RestController
@RequestMapping("/monitor")
@Slf4j
public class MonitorDataController {

    @Autowired
    MonitorDataService monitorDataService;


    @ApiOperation("按类型统计资源数量")
    @ApiImplicitParams({@ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")})
    @RequestMapping(value = "/neStatisticsByClass", method = RequestMethod.GET)
    public JsonModel statisticsResourceData(@RequestParam(required = false) Long domainId,
                                            @RequestParam(required = false) String baseNeClass, HttpSession session) {
        try {
            return monitorDataService.statisticsResourceData(domainId, baseNeClass, session);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("按类型统计资源数量，用于列固定的元件")
    @ApiImplicitParams({@ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")})
    @RequestMapping(value = "/neStatisticsByClassForRows", method = RequestMethod.GET)
    public JsonModel statisticsResourceDataForRows(@RequestParam(required = false) Long domainId,
                                                   @RequestParam(required = false) String baseNeClass, HttpSession session) {
        try {
            JsonModel deprecatedWrap = monitorDataService.statisticsResourceData(domainId, baseNeClass, session);
            JSONArray oldRows = (JSONArray) ((JSONObject) deprecatedWrap.getObj()).get("rows");
            JSONObject json = new JSONObject();
            JSONArray newRows = new JSONArray();
            for (Object object : oldRows) {
                JSONObject oldObject = (JSONObject) object;
                JSONObject newObject = new JSONObject();
                newObject.put("name", oldObject.get("资源类型"));
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

    @ApiOperation("按类型统计资源数量，用于值为范围的元件")
    @ApiImplicitParams({@ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")})
    @RequestMapping(value = "/neStatisticsByClassForRange", method = RequestMethod.GET)
    public JsonModel statisticsResourceDataForRange(@RequestParam(required = false) Long domainId,
                                                    @RequestParam(required = false) String baseNeClass, HttpSession session) {
        try {
            JsonModel deprecatedWrap = monitorDataService.statisticsResourceData(domainId, baseNeClass, session);
            JSONArray oldRows = (JSONArray) ((JSONObject) deprecatedWrap.getObj()).get("rows");
            JSONObject json = new JSONObject();
            JSONArray newRows = new JSONArray();
            for (Object object : oldRows) {
                JSONObject oldObject = (JSONObject) object;
                JSONObject newObject = new JSONObject();
                JSONArray range = new JSONArray();
                JSONArray average = new JSONArray();
                range.add(0);
                range.add(oldObject.get("数量"));
                average.add(oldObject.get("数量"));
                newObject.put("资源类型", oldObject.get("资源类型"));
                newObject.put("数量", range);
                newObject.put("均值", average);
                newRows.add(newObject);
            }
            json.put("rows", newRows);
            json.put("columns", new String[]{"资源类型", "数量", "均值"});
            return new JsonModel(true, json);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("按类型统计资源数量，用于旭日图组件")
    @ApiImplicitParams({@ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")})
    @RequestMapping(value = "/neStatisticsByClassForSunburst", method = RequestMethod.GET)
    public JsonModel statisticsResourceDataForSunburst(@RequestParam(required = false) Long domainId,
                                                       @RequestParam(required = false) String baseNeClass, HttpSession session) {
        try {
            JsonModel deprecatedWrap;
            if (baseNeClass != null && !StringUtils.isEmpty(baseNeClass)) {
                deprecatedWrap = monitorDataService.statisticsResourceData(domainId, baseNeClass, session);
                JSONArray oldRows = (JSONArray) ((JSONObject) deprecatedWrap.getObj()).get("rows");
                JSONObject json = new JSONObject();
                JSONArray newRows = new JSONArray();
                for (Object object : oldRows) {
                    JSONObject oldObject = (JSONObject) object;
                    JSONObject newObject = new JSONObject();
                    newObject.put("name", oldObject.get("资源类型"));
                    newObject.put("value", oldObject.get("数量"));
                    newRows.add(newObject);
                }
                json.put("name", BaseNeClass.valueOf(baseNeClass).getText());
                json.put("children", newRows);
                JSONArray realRes = new JSONArray();
                realRes.add(json);
                JSONObject res = new JSONObject();
                res.put("dataArry", realRes);
                return new JsonModel(true, res);
            } else {
                BaseNeClass[] baseNeClasses = BaseNeClass.values();
                JSONObject res = new JSONObject();
                JSONArray realRes = new JSONArray();
                for (BaseNeClass temp : baseNeClasses) {
                    deprecatedWrap = monitorDataService.statisticsResourceData(domainId, temp.toString(), session);
                    JSONArray oldRows = (JSONArray) ((JSONObject) deprecatedWrap.getObj()).get("rows");
                    JSONObject json = new JSONObject();
                    JSONArray newRows = new JSONArray();
                    for (Object object : oldRows) {
                        JSONObject oldObject = (JSONObject) object;
                        JSONObject newObject = new JSONObject();
                        newObject.put("name", oldObject.get("资源类型"));
                        newObject.put("value", oldObject.get("数量"));
                        newRows.add(newObject);
                    }
                    json.put("name", temp.getText());
                    json.put("children", newRows);
                    realRes.add(json);
                }
                res.put("dataArry", realRes);
                return new JsonModel(true, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("按状态统计资源数量")
    @ApiImplicitParams({@ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")})
    @RequestMapping(value = "/neStatisticsByStatus", method = RequestMethod.GET)
    public JsonModel statisticsResourceStatus(HttpSession session, @RequestParam(required = false) Long domainId,
                                              @RequestParam(required = false) String baseNeClass) {
        try {
            return monitorDataService.statisticsResourceStatus(session, domainId, baseNeClass);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("按类型统计资源数量")
    @ApiImplicitParams({@ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")})
    @RequestMapping(value = "/statisticsResourceStatusByRunstatus", method = RequestMethod.GET)
    public JsonModel statisticsResourceStatusByRunstatus(@RequestParam(required = false) Long domainId,@RequestParam(required = false) RunStatus runStatus,
                                                     @RequestParam(required = false) String baseNeClass, HttpSession session){
        try {
            return monitorDataService.statisticsResourceStatusByRunstatus(domainId,runStatus,baseNeClass, session);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("资源性能视图")
    @ApiImplicitParams({@ApiImplicitParam(name = "neId", paramType = "query", dataType = "String", value = "资源Id", required = false),
            @ApiImplicitParam(name = "view", paramType = "query", dataType = "PerormanceView", value = "资源视图", required = false),
            @ApiImplicitParam(name = "columns", paramType = "query", dataType = "Arrays", value = "需要的列", required = false)})
    @RequestMapping(
            value = {"/getNetMoveTablePerformance"},
            method = {RequestMethod.GET}
    )
    public JsonModel getnNetMoveTablePerformance(@RequestParam(required = false) String neId, @RequestParam(required = false) PerormanceView view, @RequestParam(required = false) String[] columns) {
        try {
            return this.monitorDataService.getnNetMoveTablePerformance(neId, view, columns);
        } catch (Exception var5) {
            var5.printStackTrace();
            return new JsonModel(false, var5.getMessage());
        }
    }



    @ApiOperation("按状态统计资源数量，用于列固定的组件")
    @ApiImplicitParams({@ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")})
    @RequestMapping(value = "/neStatisticsByStatusForRows", method = RequestMethod.GET)
    public JsonModel statisticsResourceStatusForRows(HttpSession session, @RequestParam(required = false) Long domainId,
                                                     @RequestParam(required = false) String baseNeClass) {
        try {
            JsonModel deprecatedWrap = monitorDataService.statisticsResourceStatus(session, domainId, baseNeClass);
            JSONArray oldRows = (JSONArray) ((JSONObject) deprecatedWrap.getObj()).get("rows");
            JSONObject json = new JSONObject();
            JSONArray newRows = new JSONArray();
            for (Object object : oldRows) {
                JSONObject oldObject = (JSONObject) object;
                JSONObject newObject = new JSONObject();
                newObject.put("name", oldObject.get("状态"));
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

    @ApiOperation("按状态统计资源数量，用于值为范围的组件")
    @ApiImplicitParams({@ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")})
    @RequestMapping(value = "/neStatisticsByStatusForRange", method = RequestMethod.GET)
    public JsonModel statisticsResourceStatusForRange(HttpSession session, @RequestParam(required = false) Long domainId,
                                                      @RequestParam(required = false) String baseNeClass) {
        try {
            JsonModel deprecatedWrap = monitorDataService.statisticsResourceStatus(session, domainId, baseNeClass);
            JSONArray oldRows = (JSONArray) ((JSONObject) deprecatedWrap.getObj()).get("rows");
            JSONObject json = new JSONObject();
            JSONArray newRows = new JSONArray();
            for (Object object : oldRows) {
                JSONObject oldObject = (JSONObject) object;
                JSONObject newObject = new JSONObject();
                JSONArray range = new JSONArray();
                JSONArray average = new JSONArray();
                range.add(0);
                range.add(oldObject.get("数量"));
                newObject.put("状态", oldObject.get("状态"));
                newObject.put("数量", range);
                newObject.put("均值", average);
                newRows.add(newObject);
            }
            json.put("rows", newRows);
            json.put("columns", new String[]{"状态", "数量", "均值"});
            return new JsonModel(true, json);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("按状态统计资源数量，用于旭日图")
    @ApiImplicitParams({@ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")})
    @RequestMapping(value = "/neStatisticsByStatusForSunburst", method = RequestMethod.GET)
    public JsonModel statisticsResourceStatusForSunburst(HttpSession session, @RequestParam(required = false) Long domainId,
                                                         @RequestParam(required = false) String baseNeClass) {
        try {
            JsonModel deprecatedWrap;
            JSONArray dataArry = new JSONArray();
            if (baseNeClass != null && !StringUtils.isEmpty(baseNeClass)) {
                deprecatedWrap = monitorDataService.statisticsResourceStatusForSunburst(session, domainId, baseNeClass);
                JSONArray children = (JSONArray) deprecatedWrap.getObj();
                JSONObject father = new JSONObject();
                father.put("name", BaseNeClass.valueOf(baseNeClass).getText());
                father.put("children", children);
                dataArry.add(father);
            } else {
                BaseNeClass[] baseNeClasses = BaseNeClass.values();
                for (BaseNeClass temp : baseNeClasses) {
                    deprecatedWrap = monitorDataService.statisticsResourceStatusForSunburst(session, domainId, temp.toString());
                    JSONArray children = (JSONArray) deprecatedWrap.getObj();
                    JSONObject father = new JSONObject();
                    father.put("name", temp.getText());
                    father.put("children", children);
                    dataArry.add(father);
                }
            }
            JSONObject result = new JSONObject();
            result.put("dataArry", dataArry);
            return new JsonModel(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("资源状态列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "资源IDs"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型"),
            @ApiImplicitParam(name = "column", paramType = "query", dataType = "String", value = "筛选列"),
    })
    @RequestMapping(value = "/neList", method = RequestMethod.POST)
    public JsonModel neList(@RequestParam(required = false) Long domainId, @RequestParam(required = false) String neIds,
                            @RequestParam(required = false) BaseNeClass baseNeClass, HttpSession session,
                            @RequestParam(required = false) String[] column) {
        try {
            return monitorDataService.neList(domainId, neIds, baseNeClass, session, column);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    //已被废除的大屏接口 不需要进行维护了
//    /**
//     * 资源指标状态列表
//     * @param neId 资源ID
//     * @return
//     */
//    @ApiOperation("资源指标状态列表")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "neId", paramType = "query", dataType = "String", value = "资源ID", required = true) })
//    @RequestMapping(value = "/indicatorStatus", method = RequestMethod.GET)
//    @ResponseBody
//    public JsonModel findIndicatorsStatus(@RequestParam String neId) {
//        try {
//            return monitorDataService.findIndicatorsStatus(neId);
//        }catch (Exception e){
//            e.printStackTrace();
//            return new JsonModel(false, e.getMessage());
//        }
//    }


    @ApiOperation("获取指标属性的单值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "资源ID", required = true),
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "指标名称", required = true),
            @ApiImplicitParam(name = "componentName", paramType = "query", dataType = "String", value = "部件名称"),
            @ApiImplicitParam(name = "field", paramType = "query", dataType = "String", value = "属性", required = true)})
    @RequestMapping(value = "/indicator/value", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getIndicatorValue(@RequestParam String neIds, @RequestParam String indicators,
                                       @RequestParam(required = false) String componentName, @RequestParam String field) {
        try {
            return monitorDataService.getIndicatorValueData(neIds, indicators, componentName, field);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }

    }

    @ApiOperation("获取指标字符串属性的单值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "资源ID", required = true),
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "指标名称", required = true),
            @ApiImplicitParam(name = "componentName", paramType = "query", dataType = "String", value = "部件名称"),
            @ApiImplicitParam(name = "field", paramType = "query", dataType = "String", value = "属性", required = true)})
    @RequestMapping(value = "/indicator/valueStr", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getIndicatorValueStr(@RequestParam String neIds, @RequestParam String indicators,
                                          @RequestParam(required = false) String componentName, @RequestParam String field) {
        try {
            return monitorDataService.getIndicatorValueStr(neIds, indicators, componentName, field);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("获取指标字符串属性的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "资源ID", required = true),
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "指标名称", required = true),
            @ApiImplicitParam(name = "componentName", paramType = "query", dataType = "String", value = "部件名称"),
            @ApiImplicitParam(name = "field", paramType = "query", dataType = "String", value = "属性", required = true)})
    @RequestMapping(value = "/indicator/valueStrTable", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getIndicatorValueStrTable(@RequestParam String neIds, @RequestParam String indicators,
                                               @RequestParam(required = false) String[] componentName, @RequestParam String[] field) {
        try {
            return monitorDataService.getIndicatorValueStrTable(neIds, indicators, componentName, field);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }

    }

    /**
     * 指标历史统计-资源 (可以选择多个资源)(指标唯一)
     *
     * @param neIds
     * @param indicators
     * @param windows
     * @param field
     * @param period
     * @param interval
     * @return
     */
    @ApiOperation("指标历史统计-资源 (可以选择多个资源)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源IDs", required = true),
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "指标名称", required = true),
            @ApiImplicitParam(name = "windows", paramType = "query", dataType = "String", value = "弹窗数据"),
            @ApiImplicitParam(name = "field", paramType = "query", dataType = "String", value = "属性", required = true),
            @ApiImplicitParam(name = "period", paramType = "query", dataType = "String", value = "统计时段", required = true),
            @ApiImplicitParam(name = "interval", paramType = "query", dataType = "Integer", value = "时间间隔", required = true)})
    @RequestMapping(value = "/indicator/history/record", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getIndHistoryValue(@RequestParam String[] neIds, String indicators,
                                        @RequestParam(required = false) String windows, @RequestParam(required = false) String field,
                                        @RequestParam IndPeriod period, @RequestParam Integer interval) {
        try {
            if (Objects.equals("healthy", indicators)) {
                if (IndPeriod._1day == period) {
                    interval = 5;
                }
                return monitorDataService.getHistoryHealth(neIds, period, interval);
            } else {
                //TODO 这里的时间值在之后的版本中可调整至灵活输入的。
                IntervalType intervalType = IntervalType.minute;
                //interval = 5;
                if(ObjectUtils.isEmpty(interval)) interval=5;
                if (IndPeriod._1day == period) {
                    intervalType = IntervalType.minute;
                    //interval = 5;
                } else if (IndPeriod._1week == period) {
                    intervalType = IntervalType.hour;
                    //interval = 8;
                } else if (IndPeriod._1month == period) {
                    intervalType = IntervalType.hour;
                    //interval = 24;
                }
                return monitorDataService.getHistoryValue(neIds, indicators, windows, field, intervalType, interval);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    /**
     * 指标历史统计-指标 (可以选择多个指标)(资源唯一)
     *
     * @param neIds
     * @param indicators
     * @param windows
     * @param period
     * @return
     */
    @ApiOperation("指标历史统计-指标 (可以选择多个指标)(资源唯一)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源IDs", required = true),
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "List<String>", value = "指标名称", required = true),
            @ApiImplicitParam(name = "windows", paramType = "query", dataType = "String", value = "弹窗数据"),
            @ApiImplicitParam(name = "period", paramType = "query", dataType = "String", value = "统计时段", required = true),
            @ApiImplicitParam(name = "interval", paramType = "query", dataType = "String", value = "时间间隔", required = true)})
    @RequestMapping(value = "/multiple_indicator/record", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getMultipleIndHistoryValue(@RequestParam String[] neIds, String[] indicators,
                                                @RequestParam(required = false) String windows,
                                                @RequestParam IndPeriod period,
                                                @RequestParam Integer interval) {
        try {
            IntervalType intervalType = IntervalType.minute;
            if (IndPeriod._1day == period) {
                intervalType = IntervalType.minute;
            } else if (IndPeriod._1week == period) {
                intervalType = IntervalType.hour;
            } else if (IndPeriod._1month == period) {
                intervalType = IntervalType.hour;
            }

            return monitorDataService.getMultipleIndHistoryValue(neIds, indicators, windows, intervalType, interval, period);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("获取topN的展示数据(定位到资源)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "topN展示的指标类型", required = true),
            @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "资源IDs"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型"),
            @ApiImplicitParam(name = "neClass", paramType = "query", dataType = "String", value = "资源子类型"),
            @ApiImplicitParam(name = "field", paramType = "query", dataType = "String", value = "属性"),
            @ApiImplicitParam(name = "number", paramType = "query", dataType = "String", value = "topN展示的记录条数"),
            @ApiImplicitParam(name = "windows", paramType = "query", dataType = "String", value = "弹窗返回值"),
            @ApiImplicitParam(name = "order", paramType = "query", dataType = "String", value = "排序方式")})
    @RequestMapping(value = "/indicator/topN", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getTopNByItObjects(@RequestParam String indicators, @RequestParam(required = false) Long domainId,
                                        @RequestParam(required = false) String neIds, @RequestParam(required = false) String baseNeClass,
                                        @RequestParam(required = false) String neClass, @RequestParam(required = false) String field,
                                        @RequestParam String number, @RequestParam String windows, @RequestParam String order, HttpSession session,
                                        Boolean bar) {
        try {
            return monitorDataService.getTopNByItObjects(indicators, domainId, neIds, baseNeClass, neClass, field, number, windows, order, session, bar);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("获取多资源多指标统计的展示数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源IDs"),
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "List<String>", value = "展示的指标类型"),
            @ApiImplicitParam(name = "windows", paramType = "query", dataType = "String", value = "弹窗返回值")})
    @RequestMapping(value = "/indicator/multipleIndicator", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getMultipleIndicatorObject(@RequestParam String[] neIds, @RequestParam String[] indicators,
                                                @RequestParam String windows, HttpSession session) {
        try {
            return monitorDataService.getMultipleIndicatorObject(neIds, indicators, windows, session);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }

    }


    @ApiOperation("根据所选域、拓扑图id统计链路数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "abnormal", paramType = "query", dataType = "Boolean", value = "统计异常数据")})
    @RequestMapping(value = "/countNeLink", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel countNeLink(@RequestParam(required = false) Boolean abnormal, HttpSession session) {
        try {
            return monitorDataService.countNeLink(abnormal, session);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }

    }


    @ApiOperation("根据所选域、父类型、子类型和状态进行统计资源数量")
    @ApiImplicitParams({@ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型"),
            @ApiImplicitParam(name = "neClass", paramType = "query", dataType = "String", value = "资源子类型"),
            @ApiImplicitParam(name = "status", paramType = "query", dataType = "String", value = "状态")})
    @RequestMapping(value = "/countNe", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel countNe(@RequestParam(required = false) String domainId,
                             @RequestParam(required = false) String[] baseNeClass, @RequestParam(required = false) String[] neClass,
                             @RequestParam(required = false) String status, HttpSession session) {
        try {
            return monitorDataService.countNe(domainId, baseNeClass, neClass, status, session);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    @ApiOperation("指标统计-链路")
    @ApiImplicitParams({@ApiImplicitParam(name = "sourceId", paramType = "query", dataType = "String", value = "源id"),
            @ApiImplicitParam(name = "sourceIfName", paramType = "query", dataType = "String", value = "源接口"),
            @ApiImplicitParam(name = "targetId", paramType = "query", dataType = "String", value = "目的id"),
            @ApiImplicitParam(name = "targetIfName", paramType = "query", dataType = "String", value = "目的接口"),
            @ApiImplicitParam(name = "field", paramType = "query", dataType = "String", value = "指标")})
    @RequestMapping(value = "/indicator/valueNetwork", method = RequestMethod.GET)
    public JsonModel valueNetwork(HttpSession session, @RequestParam String sourceId, @RequestParam String sourceIfName,
                                  @RequestParam String targetId, @RequestParam String targetIfName, @RequestParam String field) {
        try {
            return monitorDataService.valueNetwork(session, sourceId, sourceIfName, targetId, targetIfName, field);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }

    }

    @SuppressWarnings("unchecked")
    @ApiOperation("链路展示")
    @ApiImplicitParams({@ApiImplicitParam(name = "network", paramType = "query", dataType = "String", value = "链路"),
            @ApiImplicitParam(name = "number", paramType = "query", dataType = "Long", value = "展示条数"),
            @ApiImplicitParam(name = "field", paramType = "query", dataType = "String", value = "属性")})
    @RequestMapping(value = "/networkTable", method = RequestMethod.GET)
    public JsonModel networkTable(HttpSession session, @RequestParam String network, @RequestParam Long number,
                                  @RequestParam String[] field) {
        try {
            return monitorDataService.networkTable(session, network, number, field);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @ApiOperation("获取资源分布数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", paramType = "query", dataType = "String", value = "资源状态", required = false),
            @ApiImplicitParam(name = "range", paramType = "query", dataType = "String", value = "展示范围", required = false),
            @ApiImplicitParam(name = "areaName", paramType = "query", dataType = "String", value = "已选地区", required = false),
            @ApiImplicitParam(name = "names", paramType = "query", dataType = "String", value = "展示范围下的地区名集合", required = false),
            @ApiImplicitParam(name = "period", paramType = "query", dataType = "String", value = "颗粒度", required = false)})
    @RequestMapping(value = "/v_map/neDivision", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel neDivision(HttpSession session, @RequestParam(required = false) String[] status,
                                @RequestParam(required = false) String range, @RequestParam(required = false) String[] names,
                                @RequestParam(required = false) String areaName, @RequestParam(required = false) String period) {
        try {
            return monitorDataService.neDivision(session, status, range, names, areaName, period);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("双轴-历史值统计图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源IDs", required = true),
            @ApiImplicitParam(name = "indicatorsLeft", paramType = "query", dataType = "String", value = "左侧指标分类", required = true),
            @ApiImplicitParam(name = "componentNameLeft", paramType = "query", dataType = "String", value = "左侧部件", required = false),
            @ApiImplicitParam(name = "fieldLeft", paramType = "query", dataType = "String", value = "左侧指标", required = true),
            @ApiImplicitParam(name = "indicatorsRight", paramType = "query", dataType = "String", value = "右侧指标分类", required = true),
            @ApiImplicitParam(name = "componentNameRight", paramType = "query", dataType = "String", value = "右侧部件", required = false),
            @ApiImplicitParam(name = "fieldRight", paramType = "query", dataType = "String", value = "右侧指标", required = true),
            @ApiImplicitParam(name = "period", paramType = "query", dataType = "String", value = "统计时段", required = true)})
    @RequestMapping(value = "/multiple_indicator/recordDoubleAxis", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel recordDoubleAxis(@RequestParam String[] neIds, @RequestParam String indicatorsLeft,
                                      @RequestParam String componentNameLeft, @RequestParam String fieldLeft, @RequestParam String indicatorsRight,
                                      @RequestParam String componentNameRight, @RequestParam String fieldRight, @RequestParam IndPeriod period) {
        try {
            if (ObjectUtils.isEmpty(neIds)) {
                return new JsonModel(true, MonitorUtils.empObj());
            }
            return monitorDataService.multipleIndicatorHistory(neIds[0], indicatorsLeft, componentNameLeft, fieldLeft, indicatorsRight,
                    componentNameRight, fieldRight, period);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("同资源多部件统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "资源ID", required = true),
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "指标名称", required = true),
            @ApiImplicitParam(name = "componentName", paramType = "query", dataType = "String", value = "部件名称"),
            @ApiImplicitParam(name = "field", paramType = "query", dataType = "String", value = "属性", required = true)})
    @RequestMapping(value = "/indicator/multipleComponent", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel multipleComponent(@RequestParam String neIds, @RequestParam String indicators,
                                       @RequestParam(required = false) String[] componentName, @RequestParam String field) {
        try {
            return monitorDataService.multipleComponent(neIds, indicators, componentName, field);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("链路统计-按城市")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "locationCode", paramType = "query", dataType = "String", value = "当前展示地图区域代码", required = false),
            @ApiImplicitParam(name = "displayMode", paramType = "query", dataType = "String", value = "按名字绑定还是按经纬度绑定", required = false)
    })
    @RequestMapping(value = "/getNELinkByCity", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getNELinkByCity(
            @RequestParam(value = "locationCode", required = false) String locationCode,
            @RequestParam(value = "displayMode", required = false) String displayMode, HttpSession session) {
        try {
            return monitorDataService.getNELinkByCity(locationCode, displayMode, session);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("按拓扑统计资源数")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "topoId", paramType = "query", dataType = "String", value = "拓扑ID", required = true),
        @ApiImplicitParam(name = "baseNeclass", paramType = "query", dataType = "String", value = "父类型", required = false)
    })
    @RequestMapping(value = "/getTopoResourcesCount", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel getTopostatisticsResources(
            @RequestParam(required = true) String topoId,
            @RequestParam(required = false) String baseNeClass) {
        try {
            return monitorDataService.getTopostatisticsResources(topoId, baseNeClass);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(true, e.getMessage());
        }
    }

    @ApiOperation("按拓扑统计链路条数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topoId", paramType = "query", dataType = "String", value = "拓扑ID", required = true),
            @ApiImplicitParam(name = "abnormal", paramType = "query", dataType = "Boolean", value = "统计异常数据", required = false)
    })
    @RequestMapping(value = "/countTopoLink", method = RequestMethod.POST)
    @ResponseBody
    public  JsonModel CountTopoLink(
            @RequestParam(required = true) String topoId,
            @RequestParam(required = false) Boolean abnormal
    ){
        try {
            return monitorDataService.CountTopoLink(topoId, abnormal);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(true, e.getMessage());
        }
    }

    @Autowired
    RpcProcessService rpcProcessService;

    @Autowired
    private VideoMonitoringService videoMonitoringService;

    /*@Autowired
    private MonitorService monitorService;

    @GetMapping("/test")
    public JsonModel test(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("neClass", "hcnet");
        JsonModel monitorResult = monitorService.searchNe(jsonObject);
        LinkedHashMap monitor = (LinkedHashMap)((ArrayList)((LinkedHashMap)monitorResult.getObj()).get("object")).get(0);
        String neId = (String)monitor.get("id");
        JsonModel channelResult = monitorService.getChannelList(neId);
        String channelNo = (String)((LinkedHashMap)((ArrayList)((LinkedHashMap)((LinkedHashMap)channelResult.getObj()).get("indicatorValue")).get("object")).get(0)).get("channel");
        videoMonitoringService.register(new VideoMonitoringService.VideoConsumer<byte[]>() {
            @Override
            public void accept(byte[] bytes) {
                System.out.println("接收到帧数据长度：" + bytes.length);
            }
        }.set("1", neId, "sub", channelNo));
        return new JsonModel(true);
    }*/

}
