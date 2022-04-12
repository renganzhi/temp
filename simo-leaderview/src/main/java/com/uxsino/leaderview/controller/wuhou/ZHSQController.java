package com.uxsino.leaderview.controller.wuhou;

import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.wuhou.WuHouService;
import com.uxsino.leaderview.service.wuhou.ZHSQService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"武侯智慧城市-智慧社区大屏展示数据接口"})
@RestController
@RequestMapping("/ZHSQ")
@Slf4j
public class ZHSQController {

    @Autowired
    WuHouService wuHouService;

    @Autowired
    ZHSQService zhsqService;


    /**
     *1、智慧社区-通用-基础人员总数(
     * @param param 所选人员类型的接口id
     * @return
     */
    @GetMapping("/getZHSQ1")
    public JsonModel getZHSQ1(@RequestParam(required = false) String  param){
        return zhsqService.getZHSQ1(param);
    }

    /**
     *1、智慧社区-社区矫正-街道管控数据 弹窗表格
     * @return
     */
    @GetMapping("/getSQJZ1")
    public JsonModel getSQJZ1(){
        return zhsqService.getSQJZ1();
    }

    /**
     *2、智慧社区-社区矫正-街道人员表格  弹窗
     * @return
     */
    @GetMapping("/getSQJZ2")
    public JsonModel getSQJZ2(@RequestParam(required = false) String param){
        return zhsqService.getSQJZ2(param.split(":")[1]);
    }

    /**
     *1、智慧社区-社区康复-吸毒人员分布 弹窗柱状图
     * @return
     */
    @GetMapping("/getSQKF1")
    public JsonModel getSQKF1(){
        return zhsqService.getSQKF1();
    }

    /**
     *2、智慧社区-社区康复-街道吸毒人员
     * @return
     */
    @GetMapping("/getSQKF2")
    public JsonModel getSQKF2(@RequestParam(required = false) String param){
        return zhsqService.getSQKF2(param.split(":")[1]);
    }





}
