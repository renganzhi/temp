package com.uxsino.leaderview.controller;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.WuHouService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.reflections.Reflections.log;

@Api(tags = {"武侯智慧城市-大屏展示数据接口"})
@RestController
@RequestMapping("/WuHou")
@Slf4j
public class WuHouController {

    @Autowired
    WuHouService wuHouService;

    @RequestMapping(value = "/getFormInfo", method = RequestMethod.GET)
    public JsonModel getFormInfo(String formId){
        try {
            return new JsonModel(true, wuHouService.getFormInfo(formId));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return new JsonModel(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/getFormData", method = RequestMethod.GET)
    public JsonModel getFormData(String formId, String column){
        try {
            return wuHouService.getFormDataForTable(formId, column);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return new JsonModel(false, e.getMessage());
        }
    }


    /**
     * 获取社会治理大屏-重点人员、事件各模块-基础人员总数数据-文本框
     * @return
     */
    @GetMapping("/getPeopleTotalCount")
    public JsonModel getPeopleTotalCount(String formId){
//        String query90 = "/search.json?query[634]=浆洗街";
        return new JsonModel(true,wuHouService.getFormDataForText(formId,null));
    }


}
