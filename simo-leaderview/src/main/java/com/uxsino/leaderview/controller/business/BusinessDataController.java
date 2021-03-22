package com.uxsino.leaderview.controller.business;

import com.google.common.collect.Maps;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.business.Indicator;
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
//@Business(name = BusinessConstants.BUSINESS)
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


    @ApiOperation("根据业务ID和指标名来展示历史指标数据")
    @RequestMapping(value = "/history/record", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType =  "query", dataType = "List<String>", value = "业务Id"),
            @ApiImplicitParam(name = "indicator", paramType = "query", dataType = "String", value = "指标名"),
            @ApiImplicitParam(name = "number", paramType = "query", dataType = "String", value = "统计时段")
    })
    @ResponseBody
    public JsonModel getHistoryValue(HttpSession session, @RequestParam String[] business,
                                     @RequestParam Indicator indicator, @RequestParam String period){
        try {
            return businessDataService.getHistoryValue(session, business, indicator, period);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

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
