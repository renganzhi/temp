package com.uxsino.leaderview.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.model.RunStatus;
import com.uxsino.commons.utils.DateUtils;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.commons.utils.TimeUtils;
import com.uxsino.leaderview.model.monitor.IndPeriod;
import com.uxsino.leaderview.service.api.MonitorDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;


@Api(tags = { "资源-大屏展示数据接口" })
@RestController
@RequestMapping("/homeData")
@Slf4j
public class MonitorDataController {

    @Autowired
    MonitorDataService monitorDataService;


    @ApiOperation("按类型统计资源数量")
    @ApiImplicitParams({ @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型") })
    @RequestMapping(value = "/neStatisticsByClass", method = RequestMethod.GET)
    public JsonModel statisticsResourceData(@RequestParam(required = false) Long domainId,
                                            @RequestParam(required = false) String baseNeClass, HttpSession session) {
        try {
            return monitorDataService.statisticsResourceData(domainId, baseNeClass, session);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("按状态统计资源数量")
    @ApiImplicitParams({ @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型") })
    @RequestMapping(value = "/neStatisticsByStatus", method = RequestMethod.GET)
    public JsonModel statisticsResourceStatus(HttpSession session, @RequestParam(required = false) Long domainId,
                                              @RequestParam(required = false) String baseNeClass) {
        try {
            return monitorDataService.statisticsResourceStatus(session, domainId, baseNeClass);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("资源状态列表")
    @ApiImplicitParams({ @ApiImplicitParam(name = "domainId", paramType = "query", dataType = "Long", value = "域ID"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "资源IDs")/*,*/
            /*@ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型") */ })
    @RequestMapping(value = "/neList", method = RequestMethod.POST)
    public JsonModel neList(@RequestParam(required = false) Long domainId, @RequestParam(required = false) String neIds,
                            @RequestParam(required = false) BaseNeClass baseNeClass, HttpSession session) {
        try {
            return monitorDataService.neList(domainId, neIds, baseNeClass, session);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    /**
     * 资源指标状态列表
     * @param neId 资源ID
     * @return
     */
    @ApiOperation("资源指标状态列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neId", paramType = "query", dataType = "String", value = "资源ID", required = true) })
    @RequestMapping(value = "/indicatorStatus", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel findIndicatorsStatus(@RequestParam String neId) {
        try {
            return monitorDataService.findIndicatorsStatus(neId);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("获取指标属性的单值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "资源ID", required = true),
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "指标名称", required = true),
            @ApiImplicitParam(name = "componentName", paramType = "query", dataType = "String", value = "部件名称"),
            @ApiImplicitParam(name = "field", paramType = "query", dataType = "String", value = "属性", required = true) })
    @RequestMapping(value = "/indicator/value", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getIndicatorValue(@RequestParam String neIds, @RequestParam String indicators,
                                       @RequestParam(required = false) String componentName, @RequestParam String field) {
        try {
            return monitorDataService.getIndicatorValue(neIds,indicators,componentName,field);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }

    }

    @ApiOperation("获取指标字符串属性的单值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "资源ID", required = true),
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "指标名称", required = true),
            @ApiImplicitParam(name = "componentName", paramType = "query", dataType = "String", value = "部件名称"),
            @ApiImplicitParam(name = "field", paramType = "query", dataType = "String", value = "属性", required = true) })
    @RequestMapping(value = "/indicator/valueStr", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getIndicatorValueStr(@RequestParam String neIds, @RequestParam String indicators,
                                          @RequestParam(required = false) String componentName, @RequestParam String field) {
        try {
            return monitorDataService.getIndicatorValueStr(neIds, indicators, componentName, field);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }

    }

    @ApiOperation("获取指标字符串属性的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "String", value = "资源ID", required = true),
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "指标名称", required = true),
            @ApiImplicitParam(name = "componentName", paramType = "query", dataType = "String", value = "部件名称"),
            @ApiImplicitParam(name = "field", paramType = "query", dataType = "String", value = "属性", required = true) })
    @RequestMapping(value = "/indicator/valueStrTable", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getIndicatorValueStrTable(@RequestParam String neIds, @RequestParam String indicators,
                                               @RequestParam(required = false) String[] componentName, @RequestParam String[] field) {
        try {
            return monitorDataService.getIndicatorValueStrTable(neIds, indicators, componentName, field);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }

    }

    /**
     * 指标历史统计-资源 (可以选择多个资源)(指标唯一)
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
            @ApiImplicitParam(name = "interval", paramType = "query", dataType = "Integer", value = "时间间隔", required = true) })
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
                return monitorDataService.getHistoryValue(neIds, indicators, windows, field, period, interval);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    /**
     * 指标历史统计-指标 (可以选择多个指标)(资源唯一)
     * 未处理完全 2020-07-22
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
            @ApiImplicitParam(name = "period", paramType = "query", dataType = "String", value = "统计时段", required = true) })
    @RequestMapping(value = "/multiple_indicator/record", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getMultipleIndHistoryValue(@RequestParam String[] neIds, String[] indicators,
                                                @RequestParam(required = false) String windows, @RequestParam IndPeriod period) {
        try {
            return monitorDataService.getMultipleIndHistoryValue(neIds, indicators, windows, period);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false , e.getMessage());
        }
    }


}
