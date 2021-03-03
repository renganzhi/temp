package com.uxsino.leaderview.controller.alert;

import com.uxsino.commons.model.JsonModel;
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
