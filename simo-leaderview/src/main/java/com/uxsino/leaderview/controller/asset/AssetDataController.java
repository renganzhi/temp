package com.uxsino.leaderview.controller.asset;


import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.api.AssetDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"资产-大屏展示数据接口"})
@RestController
@RequestMapping("/asset")
@Slf4j
public class AssetDataController {

    @Autowired
    AssetDataService assetDataService;


    @ApiOperation("资产告警类别数量统计")
    @RequestMapping(value = "/getAlertCount",method = RequestMethod.GET)
    public JsonModel getAlertCount(@RequestParam String remindType,@RequestParam String remindLevel){

        try {
            return assetDataService.getAlertCount(remindType,remindLevel);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
    }

    @ApiOperation("资产告警信息")
    @RequestMapping(value = "/getAlertByPage",method = RequestMethod.GET)
    public JsonModel getAlertByPage(@RequestParam Integer number){
        try {
            return assetDataService.getAlertPage(number);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
    }

    @ApiOperation("资产数量统计接口")
    @ApiImplicitParam(name = "status", paramType = "query", dataType = "String", value = "要统计的资产状态",required = true)
    @RequestMapping(value = "/countAsset",method = RequestMethod.POST)
    public JsonModel  countAsset(@RequestParam String status){
        try {
            return  assetDataService.countAsset(status);
        } catch (Exception e) {
            e.printStackTrace();
            return  new JsonModel(false,e.getMessage());
        }
    }

    @ApiOperation("资产状态总量统计")
    @ApiImplicitParam(name = "status",paramType = "query",dataType = "String",value = "需要统计的资产状态的字符串，用,分割",required = false)
    @RequestMapping("/assetStatusTotalCount")
    public JsonModel assetStatusTotalCount(@RequestParam(required = false) String status){
        try {
            return assetDataService.assetStatusTotalCount(status);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
    }

    @ApiOperation("TOP N部门资产类别统计")
    @ApiImplicitParam(name = "status",paramType = "query",dataType = "String",value = "需要统计的资产状态的字符串，用,分割",required = false)
    @RequestMapping("/assetCountByOrga")
    public JsonModel assetCountByOrga(@RequestParam String orgaIds){
        try {
            return assetDataService.assetCountByOrga(orgaIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
    }

    @ApiOperation("仓储物资变动趋势")
    @ApiImplicitParam(name = "status",paramType = "query",dataType = "String",value = "需要统计的资产状态的字符串，用,分割",required = false)
    @RequestMapping("/searchStandingBook")
    public JsonModel searchStandingBook(@RequestParam String operType,@RequestParam Integer interval){
        try {
            return assetDataService.searchStandingBook(operType,interval);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
    }
}
