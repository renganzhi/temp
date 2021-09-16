package com.uxsino.leaderview.controller.monitor;


import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.leaderview.model.monitor.IndPeriod;
import com.uxsino.leaderview.model.monitor.NetworkEntityCriteria;
import com.uxsino.leaderview.model.monitor.PerormanceView;
import com.uxsino.leaderview.service.api.MonitorDataParamsService;
import com.uxsino.leaderview.service.api.RpcProcessService;
import com.uxsino.leaderview.utils.MonitorUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Api(tags = { "资源-大屏展示数据项接口" })
@RestController
@RequestMapping("/monitor/params")
public class MonitorDataParamsController {
    @Autowired
    MonitorDataParamsService monitorDataParamsService;

    @Autowired
    RpcProcessService rpcProcessService;

    @GetMapping("/getDomainByUser")
    @ApiOperation("根据当前用户获取所拥有权限的下拉框")
    public JsonModel getDomainByUser(HttpSession session) {
        return monitorDataParamsService.getDomainByUser(session);
    }

    @ApiOperation("获取所有BaseNeClass-用于下拉列表")
    @GetMapping("/baseNeClass")
    @ResponseBody
    public JsonModel getManageObjectEnum() {
        return monitorDataParamsService.getManageObjectEnum();
    }

    @ApiOperation("获取所有BaseNeClass-用于下拉列表")
    @GetMapping("/getRunStatus")
    @ResponseBody
    public JsonModel getRunStatus() {
        return monitorDataParamsService.getRunStatus();
    }


    @ApiOperation("获取硬件BaseNeClass -- 用于下拉列表")
    @GetMapping("/baseNeClassByIsHardware")
    public JsonModel baseNeClassByIsHardware(@ApiParam("资源父类型") @RequestParam(required = false) Boolean isHardware) {
        return monitorDataParamsService.baseNeClassByIsHardware(isHardware);
    }

    @ApiOperation("获取除了未知类型的其他BaseNeClass-用于下拉列表")
    @GetMapping("/baseNotKnown")
    @ResponseBody
    public JsonModel baseNotKnown() {
        return monitorDataParamsService.getManageObjectEnum(null, Arrays.asList(BaseNeClass.unknow));
    }

    @ApiOperation("根据BaseNeClass获取所有子类型-用于下拉列表")
    @GetMapping("/neClass")
    @ResponseBody
    public JsonModel getNeClassByBase(@ApiParam("资源父类型") @RequestParam(required = false) BaseNeClass baseNeClass,
                                      @RequestParam(required = false)Boolean isHardware) {
        return monitorDataParamsService.getNeClassByBase(baseNeClass,isHardware);
    }

    @ApiOperation("根据BaseNeClass获取所有子类型-用于下拉列表")
    @GetMapping("/neClass-multi")
    @ResponseBody
    public JsonModel getMultiNeClassByBase(@ApiParam("资源父类型") @RequestParam(required = false) String[] baseNeClass) {
        return monitorDataParamsService.getMultiNeClassByBase(baseNeClass);
    }

    @ApiOperation("获取所有资源状态-用于下拉框")
    @GetMapping("/getNeStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "abnormal", paramType = "query", dataType = "Boolean", value = "是否统计异常") })
    public JsonModel getNeStatus(@RequestParam(required = false) Boolean abnormal) {
        return monitorDataParamsService.getNeStatus(abnormal);
    }

    @ApiOperation("获取指标类型的资源")
    @RequestMapping(value = "/nes", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel findNes(HttpSession session, @ApiParam("域ID") @RequestParam(required = false) Long domainId,
                             @ApiParam("资源父类型") @RequestParam(required = false) BaseNeClass baseNeClass,
                             @ApiParam("资源子类型") @RequestParam(required = false) NeClass neClass,
                             @ApiParam("过滤未知类型") @RequestParam(required = false) Boolean notUnknown,@RequestParam(required = false)Boolean isHardware) {
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria = rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
        criteria = rpcProcessService.setCriteriaNeClass(criteria, baseNeClass, neClass);
        return monitorDataParamsService.findNes(criteria,notUnknown,isHardware);
    }


    @ApiOperation("查询资源可选的指标单位类型，用于下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源ID"), })
    @RequestMapping(value = "/getUnit", method = RequestMethod.GET)
    public JsonModel getUnit(@RequestParam(required = false) String[] neIds) {
        return monitorDataParamsService.getUnit(neIds);
    }


    @ApiOperation("查询资源可选的指标单位类型，用于下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源ID"), })
    @RequestMapping(value = "/getUnitHost", method = RequestMethod.GET)
    public JsonModel getUnitHost(@RequestParam(required = false) String[] neIds) {
        return monitorDataParamsService.getUnitHost(neIds);
    }


    @ApiOperation("查询资源已配置的指标，或者用于TOPN的指标，用于下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源ID"),
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源ID"),
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "String", value = "指标类型"),
            @ApiImplicitParam(name = "unit", paramType = "query", dataType = "String", value = "单位类型"),
            @ApiImplicitParam(name = "neClass", paramType = "query", dataType = "String", value = "资源子类型"),
            @ApiImplicitParam(name = "healthy", paramType = "query", dataType = "boolean", value = "是否展示健康度"), })
    @RequestMapping(value = "/getIndicator", method = RequestMethod.GET)
    public JsonModel getIndicator(@RequestParam(required = false) String[] neIds,@RequestParam(required = false) String type,
                                  @RequestParam(required = false) String unit, @RequestParam(required = false) NeClass neClass,
                                  @RequestParam(required = false) Boolean healthy,@RequestParam(required = false) BaseNeClass baseNeClass) {
        return monitorDataParamsService.getIndicator(neIds,type, unit, neClass, healthy,baseNeClass);
    }

    @ApiOperation("查询资源已配置的指标，或者用于TOPN的指标，用于下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源ID"),
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "String", value = "指标类型"),
            @ApiImplicitParam(name = "unit", paramType = "query", dataType = "String", value = "单位类型"),
            @ApiImplicitParam(name = "neClass", paramType = "query", dataType = "String", value = "资源子类型"),
            @ApiImplicitParam(name = "healthy", paramType = "query", dataType = "boolean", value = "是否展示健康度"), })
    @RequestMapping(value = "/getHostIndicator", method = RequestMethod.GET)
    public JsonModel getHostIndicator(@RequestParam(required = false) String[] neIds,
                                  @RequestParam(required = false) String type, @RequestParam(required = false) String unit,
                                  @RequestParam(required = false) NeClass neClass, @RequestParam(required = false) Boolean healthy) {
        return monitorDataParamsService.getHostIndicator(neIds, type, unit, neClass, healthy);

    }

    @RequestMapping(value = "/indicatorsRight", method = RequestMethod.GET)
    public JsonModel indicatorsRight(@RequestParam(required = false) String[] neIds, @RequestParam(required = false) String type,
                                     @RequestParam(required = false) String unit, @RequestParam(required = false) NeClass neClass,
                                     @RequestParam(required = false) Boolean healthy,@RequestParam(required = false) BaseNeClass baseNeClass) {
        return getIndicator(neIds,type, unit, neClass, healthy, baseNeClass);
    }

    @RequestMapping(value = "/getIndicatorTopN", method = RequestMethod.GET)
    public JsonModel getIndicatorTopN(@RequestParam(required = false) String[] neIds,
                                  @RequestParam(required = false) NeClass neClass,
                                      @RequestParam(required = false) BaseNeClass baseNeClass) {
        return monitorDataParamsService.getIndicatorTopn(neIds, neClass,baseNeClass);

    }


    @RequestMapping(value = "/getIndicatorHostTopN", method = RequestMethod.GET)
    public JsonModel getIndicatorHostTopN(@RequestParam(required = false) String[] neIds,
                                      @RequestParam(required = false) NeClass neClass,
                                      @RequestParam(required = false) BaseNeClass baseNeClass) {
        return monitorDataParamsService.getIndicatorHostTopN(neIds, neClass,baseNeClass);

    }


    @ApiOperation("查询资源已配置的指标，或者用于TOPN的指标，用于下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "资源ID"),
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "String", value = "指标类型"),
            @ApiImplicitParam(name = "unit", paramType = "query", dataType = "String", value = "单位类型"),
            @ApiImplicitParam(name = "neClass", paramType = "query", dataType = "String", value = "资源子类型"),
            @ApiImplicitParam(name = "healthy", paramType = "query", dataType = "boolean", value = "是否展示健康度"), })
    @RequestMapping(value = "/getIndicatorStr", method = RequestMethod.GET)
    public JsonModel getIndicatorStr(@RequestParam(required = false) String[] neIds,
                                     @RequestParam(required = false) NeClass neClass,
                                     @RequestParam(required = false) BaseNeClass baseNeClass,
                                     @RequestParam(required = false) Boolean healthy,@RequestParam(required = false)Boolean runStatus) {
        return monitorDataParamsService.getIndicatorStr(neIds,neClass,baseNeClass, healthy,runStatus);
    }


    @ApiOperation("根据资源ID和指标名获取部件ID与名称的键值对:用于下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", allowMultiple = true, value = "资源IDs"),
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "指标名称") })
    @RequestMapping(value = "/getComponentName", method = RequestMethod.GET)
    public JsonModel getComponentName(@RequestParam String[] neIds, @RequestParam String indicators) {
        try {
            return monitorDataParamsService.getComponentName(neIds, indicators);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
    }


    @ApiOperation("获取某指标的数值属性-用于下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "指标名称") })
    @RequestMapping(value = "/getIndField", method = RequestMethod.GET)
    public JsonModel getIndField(@RequestParam String indicators) {
        try {
            return monitorDataParamsService.getIndField(indicators);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
    }


    @ApiOperation("获取某指标的数值属性-用于下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "String", value = "指标名称") })
    @RequestMapping(value = "/getIndFieldByType/{type}", method = RequestMethod.GET)
    public JsonModel getIndFieldByType(@RequestParam String indicators, @PathVariable String type) {
        try {
            return monitorDataParamsService.getIndField(indicators, type);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }


    @ApiOperation("根据已选资源和指标查询可选部件与可选属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "List<String>", value = "多指标名称"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "多资源id"),
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "String", value = "指标类型"),
            @ApiImplicitParam(name = "unit", paramType = "query", dataType = "String", value = "指标单位"), })
    @RequestMapping(value = "/getComponentNameAndIndFieldByType", method = RequestMethod.GET)
    public JsonModel getComponentNameAndIndFieldByType(@RequestParam String[] indicators, @RequestParam String[] neIds,
                                                       @RequestParam(required = false) String type, @RequestParam(required = false) String unit) {
        try {
            return monitorDataParamsService.getComponentNameAndIndFieldByType(indicators, neIds, type, unit);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("根据已选资源和指标查询可选部件与可选属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "List<String>", value = "多指标名称"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "多资源id"),
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "String", value = "指标类型"),
            @ApiImplicitParam(name = "unit", paramType = "query", dataType = "String", value = "指标单位"), })
    @RequestMapping(value = "/getHostComponentNameAndIndFieldByType", method = RequestMethod.GET)
    public JsonModel getHostComponentNameAndIndFieldByType(@RequestParam String[] indicators, @RequestParam String[] neIds,
                                                       @RequestParam(required = false) String type, @RequestParam(required = false) String unit) {
        try {
            return monitorDataParamsService.getHostComponentNameAndIndFieldByType(indicators, neIds, type, unit);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("根据已选资源和指标查询可选部件与可选属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "List<String>", value = "多指标名称"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "多资源id"),
            @ApiImplicitParam(name = "multipleComp", paramType = "query", dataType = "boolean", value = "部件是否多选"),
            @ApiImplicitParam(name = "multipleField", paramType = "query", dataType = "boolean", value = "属性是否多选") })
    @RequestMapping(value = "/getComponentNameAndIndField", method = RequestMethod.GET)
    public JsonModel getComponentNameAndIndField(@RequestParam String[] indicators, @RequestParam String[] neIds,
                                                 @RequestParam(required = false) Boolean multipleComp, @RequestParam(required = false) Boolean multipleField) {
        multipleComp = monitorDataParamsService.existJudgment(multipleComp);
        multipleField = monitorDataParamsService.existJudgment(multipleField, true);
        try {
            return monitorDataParamsService.getWindows(indicators, neIds, multipleComp, multipleField);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("根据已选资源和指标查询可选部件，展示弹窗可选信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indicators", paramType = "query", dataType = "List<String>", value = "多指标名称"),
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", value = "多资源id") })
    @RequestMapping(value = "/getComponentNameForWindows", method = RequestMethod.GET)
    public JsonModel getComponentNameForWindows(@RequestParam String[] indicators, @RequestParam String[] neIds) {
        try {
            JsonModel jsonModel = monitorDataParamsService.getWindows(indicators, neIds, false, true);
            // TODO 这里的核心利用率部件选择有误
            // 如果参数有误，直接返回false
            if (!jsonModel.isSuccess()) {
                return jsonModel;
            }
            if (ObjectUtils.isEmpty(jsonModel.getObj())) {
                return new JsonModel(false, "windows");
            }
            // 取消属性可选项
            JSONArray result = (JSONArray) jsonModel.getObj();
            // 由于是单部件选取，所以只有第一位是有数据的
            result.getJSONObject(0).put("fields", new JSONArray());
            return new JsonModel(true, "windows", result);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("根据已选的统计时段提供可选展示时间间隔下拉框")
    @ApiImplicitParams({ @ApiImplicitParam(name = "period", paramType = "query", dataType = "String", value = "统计时段") })
    @RequestMapping(value = "/getInterval", method = RequestMethod.GET)
    public JsonModel getInterval(@RequestParam IndPeriod period) {
        JSONArray result = new JSONArray();
        String[] dayArray = { "5分钟","15分钟","30分钟", "1小时", "2小时"};
        Integer[] dayValue = { 5, 15, 30, 60, 120 };
        String[] weekArray = { "2小时","4小时","8小时", "16小时", "1天"};
        Integer[] weekValue = { 2, 4, 8 ,16, 24};
        String[] monthArray = { "8小时","12小时","1天", "2天", "3天"};
        Integer[] monthValue = { 8, 12, 24, 48, 72};

        if ("_1month".equals(period.name())) {
            for (int i = 0; i < monthArray.length; i++) {
                result.add(MonitorUtils.newResultObj( monthArray[i], monthValue[i]));
            }
        } else if ("_1week".equals(period.name())) {
            for (int i = 0; i < weekArray.length; i++) {
                result.add(MonitorUtils.newResultObj( weekArray[i], weekValue[i]));
            }
        } else {
            for (int i = 0; i < weekArray.length; i++) {
                result.add(MonitorUtils.newResultObj( dayArray[i], dayValue[i]));
            }
        }
        return new JsonModel(true, result);
    }


    @ApiOperation("获取链路的源ip下拉框")
    @ApiImplicitParams({})
    @RequestMapping(value = "/getNetworkSourceId", method = RequestMethod.GET)
    public JsonModel getNetworkSourceId(HttpSession session) {
        try {
            return monitorDataParamsService.getNetworkSourceId(session);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("获取链路的源接口下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", paramType = "query", dataType = "String", value = "源id") })
    @RequestMapping(value = "/getNetworkSourceIfName", method = RequestMethod.GET)
    public JsonModel getNetworkSourceIfName(HttpSession session, @RequestParam String sourceId) {
        try {
            return monitorDataParamsService.getNetworkSourceIfName(session, sourceId);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("获取链路的目标ip下拉框")
    @ApiImplicitParams({ @ApiImplicitParam(name = "sourceId", paramType = "query", dataType = "String", value = "源id"),
            @ApiImplicitParam(name = "sourceIfName", paramType = "query", dataType = "String", value = "源接口") })
    @RequestMapping(value = "/getNetworkTargetId", method = RequestMethod.GET)
    public JsonModel getNetworkTargetId(HttpSession session, @RequestParam String sourceId,
                                        @RequestParam String sourceIfName) {
        try {
            return monitorDataParamsService.getNetworkTargetId(session, sourceId, sourceIfName);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("获取链路的目的接口下拉框")
    @ApiImplicitParams({ @ApiImplicitParam(name = "sourceId", paramType = "query", dataType = "String", value = "源id"),
            @ApiImplicitParam(name = "sourceIfName", paramType = "query", dataType = "String", value = "源接口"),
            @ApiImplicitParam(name = "targetId", paramType = "query", dataType = "String", value = "目的id") })
    @RequestMapping(value = "/getNetworkTargetIfName", method = RequestMethod.GET)
    public JsonModel getNetworkTargetIfName(HttpSession session, @RequestParam String sourceId,
                                            @RequestParam String sourceIfName, @RequestParam String targetId) {
        try {
            return monitorDataParamsService.getNetworkTargetIfName(session, sourceId, sourceIfName, targetId);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }

    }

    @ApiOperation("双轴曲线图的部件下拉框获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neIds", paramType = "query", dataType = "List<String>", allowMultiple = true, value = "资源IDs"),
            @ApiImplicitParam(name = "indicatorsLeft", paramType = "query", dataType = "String", value = "左指标"),
            @ApiImplicitParam(name = "indicatorsRight", paramType = "query", dataType = "String", value = "右指标") })
    @RequestMapping(value = "/getComponentName/doubleAxis", method = RequestMethod.GET)
    public JsonModel getComponentNameDoubleAxis(@RequestParam(required = false) String[] neIds,
                                                @RequestParam(required = false) String indicatorsLeft, @RequestParam(required = false) String indicatorsRight) {
        String indicators = StringUtils.isEmpty(indicatorsLeft) ? indicatorsRight : indicatorsLeft;
        return getComponentName(neIds, indicators);
    }

    @ApiOperation("双轴曲线图的指标下拉框获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indicatorsRight", paramType = "query", dataType = "String", value = "右指标"),
            @ApiImplicitParam(name = "indicatorsLeft", paramType = "query", dataType = "String", value = "左指标") })
    @RequestMapping(value = "/getIndFieldByType/doubleAxis", method = RequestMethod.GET)
    public JsonModel getIndFieldByTypeDoubleAxis(@RequestParam(required = false) String indicatorsRight,
                                                 @RequestParam(required = false) String indicatorsLeft) {
        String indicators = StringUtils.isEmpty(indicatorsLeft) ? indicatorsRight : indicatorsLeft;
        return getIndFieldByType(indicators, "ALL");
    }

    @ApiOperation("实时视频监控获取摄像头设备信息下拉框")
    @RequestMapping(value = "/getMonitorList", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel searchMonitor(){
        try {
            return monitorDataParamsService.searchNe("hcnet");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("实时视频监控获取指定类型摄像头的通道信息下拉框")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "neId", paramType = "query", dataType = "String", value = "摄像头设备id")
    })
    @RequestMapping(value = "/getChannelList", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getChannelList(@RequestParam("neId") String neId){
        try {
            return monitorDataParamsService.getChannelList(neId);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    @ApiOperation("查询资源可获取的性能视图")
    @ApiImplicitParams({@ApiImplicitParam(name = "neId", paramType = "query", dataType = "String", value = "资源ID")})
    @GetMapping({"/getPerformance"})
    public JsonModel getPerformance(@RequestParam(required = false) String neId) {
        return this.monitorDataParamsService.getPerormance(neId);
    }

    @ApiOperation("查询性能视图的可展示列")
    @ApiImplicitParams({@ApiImplicitParam(name = "neId", paramType = "query", dataType = "String", value = "资源ID"),
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "String", value = "性能视图类型")})
    @GetMapping({"/getPerformanceColumn"})
    public JsonModel getPerformanceColumn(@RequestParam(required = false) String neId, @RequestParam PerormanceView view) {
        return this.monitorDataParamsService.getPerformanceColumn(neId, view);
    }

    @ApiOperation("查询性能视图的可展示列")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "baseNeClass", paramType = "query", dataType = "String", value = "资源父类型")
    })
    @GetMapping({"/getNeStatusColumn"})
    public JsonModel getNeStatusColumn(@RequestParam(required = false)BaseNeClass baseNeClass) {
        return this.monitorDataParamsService.getNeStatusColumn(baseNeClass);
    }

    @ApiOperation("获取各部门的id")
    @ApiImplicitParams({
    })
    @GetMapping({"/getorgas"})
    public JsonModel getNeStatusColumn() {
        return this.monitorDataParamsService.getorgas();
    }

}
