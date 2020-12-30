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
import com.uxsino.leaderview.model.monitor.NetworkEntity;
import com.uxsino.leaderview.model.monitor.NetworkEntityQO;
import com.uxsino.leaderview.service.api.HomeDataParamsService;
import com.uxsino.reactorq.model.FieldType;
import com.uxsino.reactorq.model.INDICATOR_TYPE;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Api(tags = { "大屏数据项接口" })
@RestController
@RequestMapping("/homeData/params")
public class HomeDataParamsController {
    @Autowired
    HomeDataParamsService homeDataParamsService;

    @GetMapping("/getDomainByUser")
    @ApiOperation("根据当前用户获取所拥有权限的下拉框")
    public JsonModel getDomainByUser(HttpSession session) {
        return homeDataParamsService.getDomainByUser(session);
    }

    @ApiOperation("获取所有BaseNeClass-用于下拉列表")
    @GetMapping("/baseNeClass")
    @ResponseBody
    public JsonModel getManageObjectEnum() {
        return homeDataParamsService.getManageObjectEnum();
    }

    @ApiOperation("获取除了未知类型的其他BaseNeClass-用于下拉列表")
    @GetMapping("/baseNotKnown")
    @ResponseBody
    public JsonModel baseNotKnown() {
        return homeDataParamsService.getManageObjectEnum(BaseNeClass.unknow);
    }

    @ApiOperation("根据BaseNeClass获取所有子类型-用于下拉列表")
    @GetMapping("/neClass")
    @ResponseBody
    public JsonModel getNeClassByBase(@ApiParam("资源父类型") @RequestParam(required = false) BaseNeClass baseNeClass) {
        return homeDataParamsService.getNeClassByBase(baseNeClass);
    }

    @ApiOperation("根据BaseNeClass获取所有子类型-用于下拉列表")
    @GetMapping("/neClass-multi")
    @ResponseBody
    public JsonModel getMultiNeClassByBase(@ApiParam("资源父类型") @RequestParam(required = false) String[] baseNeClass) {
        return homeDataParamsService.getMultiNeClassByBase(baseNeClass);
    }

    @ApiOperation("获取所有资源状态-用于下拉框")
    @GetMapping("/getNeStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "abnormal", paramType = "query", dataType = "Boolean", value = "是否统计异常") })
    public JsonModel getNeStatus(@RequestParam(required = false) Boolean abnormal) {
        return homeDataParamsService.getNeStatus(abnormal);
    }

    @ApiOperation("获取指标类型的资源")
    @RequestMapping(value = "/nes", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel findNes(HttpSession session, @ApiParam("域ID") @RequestParam(required = false) Long domainId,
                             @ApiParam("资源父类型") @RequestParam(required = false) BaseNeClass baseNeClass,
                             @ApiParam("资源子类型") @RequestParam(required = false) NeClass neClass,
                             @ApiParam("过滤未知类型") @RequestParam(required = false) Boolean notUnknown) {
        NetworkEntityQO neQO = homeDataParamsService.setNeQO(session, domainId, baseNeClass, neClass);
        return homeDataParamsService.findNes(neQO,notUnknown);
    }

    @ApiOperation("查询资源可选的指标单位类型，用于下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源ID"), })
    @RequestMapping(value = "/getUnit", method = RequestMethod.GET)
    public JsonModel getUnit(@RequestParam(required = false) String[] neIds) {
        return homeDataParamsService.getUnit(neIds);
    }


    @ApiOperation("查询资源已配置的指标，或者用于TOPN的指标，用于下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源ID"),
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "String", value = "指标类型"),
            @ApiImplicitParam(name = "unit", paramType = "query", dataType = "String", value = "单位类型"),
            @ApiImplicitParam(name = "neClass", paramType = "query", dataType = "String", value = "资源子类型"),
            @ApiImplicitParam(name = "healthy", paramType = "query", dataType = "boolean", value = "是否展示健康度"), })
    @RequestMapping(value = "/getIndicator", method = RequestMethod.GET)
    public JsonModel getIndicator(@RequestParam(required = false) String[] neIds,
                                  @RequestParam(required = false) String type, @RequestParam(required = false) String unit,
                                  @RequestParam(required = false) NeClass neClass, @RequestParam(required = false) Boolean healthy) {
        return homeDataParamsService.getIndicator(neIds, type, unit, neClass, healthy);

    }

    @RequestMapping(value = "/indicatorsRight", method = RequestMethod.GET)
    public JsonModel indicatorsRight(@RequestParam(required = false) String[] neIds,
                                     @RequestParam(required = false) String type, @RequestParam(required = false) String unit,
                                     @RequestParam(required = false) NeClass neClass, @RequestParam(required = false) Boolean healthy) {
        return getIndicator(neIds, type, unit, neClass, healthy);
    }

    @RequestMapping(value = "/getIndicatorTopN", method = RequestMethod.GET)
    public JsonModel getIndicatorTopN(@RequestParam(required = false) String[] neIds,
                                  @RequestParam(required = false) NeClass neClass) {
        return homeDataParamsService.getIndicatorTopn(neIds, neClass);

    }

}
