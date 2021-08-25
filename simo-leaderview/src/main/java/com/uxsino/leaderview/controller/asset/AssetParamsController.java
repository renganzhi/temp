package com.uxsino.leaderview.controller.asset;


import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.api.AssetDataParamsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = { "资产-大屏展示数据项接口" })
@RestController
@RequestMapping("/asset/params")
public class AssetParamsController {

    @Autowired
    AssetDataParamsService assetDataParamsService;

    @ApiOperation("返回所有的资产状态，用于下拉列表")
    @GetMapping("/assetStatus")
    @ResponseBody
    public JsonModel getAssetStatusEnum(){
        try {
            return  assetDataParamsService.getAssetStatusEnum();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
    }

    @ApiOperation("返回所有的资产告警类型")
    @GetMapping("remindType")
    @ResponseBody
    public JsonModel getRemindTypeEnum(){
        try {
            return assetDataParamsService.getRemindTypeEnum();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
    }

    @ApiOperation("返回所有的资产告警级别")
    @GetMapping("remindLevel")
    @ResponseBody
    public JsonModel getAlertLevelEnum(){
        try {
            return assetDataParamsService.getRemindLevelEnum();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
    }

}
