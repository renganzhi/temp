package com.uxsino.leaderview.controller.business;

import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.api.BusinessDataParamsService;
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

@Api(tags = { "业务-大屏展示数据项接口" })
@RestController
@RequestMapping("/business/params")
//@Business(name = BusinessConstants.BUSINESS)
@Slf4j
public class BusinessParamsController {

    @Autowired
    private BusinessDataParamsService businessDataParamsService;

    /**
     * 获取当前用户的所有有权限展示的业务，用于下拉框
     * @param session
     * @return
     */
    @ApiOperation("获取当前用户的所有业务，用于下拉框")
    @RequestMapping(value = "/getBusiness", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getBusiness(HttpSession session){
        try {
            return businessDataParamsService.getBusiness(session);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    /**
     * 获取某个业务的可选指标，用于下拉框
     * @return
     */
    @ApiOperation("获取某个业务的可选指标，用于下拉框")
    @RequestMapping(value = "/getIndicator", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getIndicator(){
        try {
            return businessDataParamsService.getIndicator();
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    /**
     * 获取某个业务的百分比类型的可选指标，用于下拉框
     * @return
     */
    @ApiOperation("获取某个业务的可选指标，用于下拉框")
    @RequestMapping(value = "/getIndicatorPercent", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getIndicatorPercent(){
        try {
            return businessDataParamsService.getIndicatorPercent();
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

    /**
     * 根据选择的业务，获取可选历史指标
     * @param business
     * @return
     */
    @ApiOperation("根据选择的业务，获取可选历史指标")
    @RequestMapping(value = "/getHistoryIndicator", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", paramType = "query", dataType = "List<String>", value = "业务")
    })
    @ResponseBody
    public JsonModel getHistoryIndicator(@RequestParam String[] business){
        try {
            return businessDataParamsService.getHistoryIndicator(business);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, e.getMessage());
        }
    }

}
