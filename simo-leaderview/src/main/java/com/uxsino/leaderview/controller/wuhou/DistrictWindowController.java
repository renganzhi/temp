package com.uxsino.leaderview.controller.wuhou;

import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.wuhou.DistrictWindowService;
import com.uxsino.leaderview.service.wuhou.WuHouService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"区级标准平台-弹窗视图展示数据接口"})
@RestController
@RequestMapping("/Window")
@Slf4j
public class DistrictWindowController {

    @Autowired
    DistrictWindowService windowService;

    @Autowired
    WuHouService wuHouService;

    /**
     * 1、城区人口-街道实有、流入人口分组柱图
     * @param param
     * @return
     */
    @GetMapping("/GetPopulation1")
    public JsonModel GetPopulation1(@RequestParam(required = false) String param){

        return windowService.GetPopulation1(param);
    }

    @GetMapping("/GetPopulation2")
    public JsonModel GetPopulation2(@RequestParam(required = false) String param){
        return new JsonModel(true);
    }

    /**
     * 1、教育资源-街道教师分布柱状图
     * @param param
     * @return
     */
    @GetMapping("/GetEducation1")
    public JsonModel GetEducation1(@RequestParam(required = false) String param){
        return windowService.GetEducation1(param);
    }

    /**
     * 2、教育资源-街道学生分布柱状图
     * @param param
     * @return
     */
    @GetMapping("/GetEducation2")
    public JsonModel GetEducation2(@RequestParam(required = false) String param){
        return windowService.GetEducation2(param);
    }

    /**
     * 3、教育资源-街道学校分布柱状图
     * @param param
     * @return
     */
    @GetMapping("/GetEducation3")
    public JsonModel GetEducation3(@RequestParam(required = false) String param){
        return windowService.GetEducation3(param);
    }

    @GetMapping("/GetPopulation4")
    public JsonModel GetPopulation4(@RequestParam(required = false) String param){
        return new JsonModel(true);
    }

    @GetMapping("/GetPopulation5")
    public JsonModel GetPopulation5(@RequestParam(required = false) String param){
        return new JsonModel(true);
    }

}
