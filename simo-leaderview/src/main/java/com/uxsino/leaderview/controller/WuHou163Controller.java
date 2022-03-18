package com.uxsino.leaderview.controller;


import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.WuHouService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"武侯智慧城市-16:3大屏展示数据接口"})
@RestController
@RequestMapping("/WuHou163")
@Slf4j
public class WuHou163Controller {

    @Autowired
    WuHouService wuHouService;


    //————————————————————————————————————————经济版块————————————————————————————————————————————————————————————————————
    /**
     * 1、土地资源（待上市地块）总数 —— 文本框
     * @return
     */
    @GetMapping("/getJJ1")
    public JsonModel getJJ1(){
        return wuHouService.getJJ1();
    }

    /**
     * 2、土地资源（待上市地块）分类汇总 —— 条形图
     * @return
     */
    @GetMapping("/getJJ2")
    public JsonModel getJJ2(){
        return wuHouService.getJJ2();
    }

    /**
     * 3、土地资源（待上市地块）分类列表明细 —— 弹窗详情
     * 点击上面条形图时，前端将分类名称作为参数传入
     * @return
     */
    @GetMapping("/getJJ3")
    public JsonModel getJJ3(String param){
        return wuHouService.getJJ3(param);
    }

    /**
     * 4、地图坐标 土地资源 —— 前端地图打点
     * @return
     */
    @GetMapping("/getJJ4")
    public JsonModel getJJ4(){
        return wuHouService.getJJ4();
    }

    /**
     * 5、地图 土地资源-详情 —— 地图点击弹窗详情
     * @return
     */
    @GetMapping("/getJJ5")
    public JsonModel getJJ5(String param){
        return wuHouService.getJJ5(param);
    }

    /**
     * 6、重大项目情况-项目分类统计 —— 可选文本框
     * @return
     */
    @GetMapping("/getJJ6")
    public JsonModel getJJ6(@RequestParam(required = false) String param){
        JsonModel jsonModel = wuHouService.getJJ6();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        return wuHouService.getTextJsonModel(param,"num",obj);
    }

    /**
     * 7、重大项目情况-项目分类明细 ——弹窗详情
     * @param param 项目名称
     * @return
     */
    @GetMapping("/getJJ7")
    public JsonModel getJJ7(String param){
        return wuHouService.getJJ7(param);
    }

    /**
     * 8、内外资投资情况 —— 可选文本框
     * @param param 内资/外资
     * @return
     */
    @GetMapping("/getJJ8")
    public JsonModel getJJ8(@RequestParam(required = false) String param){
        JsonModel jsonModel = wuHouService.getJJ8();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        return wuHouService.getPieToText(param,obj);
    }

    /**
     * 9、内外资投资情况 月度列表 —— 弹窗表格
     * @return
     */
    @GetMapping("/getJJ9")
    public JsonModel getJJ9(String param){
        return wuHouService.getJJ9(param);
    }

    /**
     *  10、领导率队赴外开展投资促进活动情况
     * @return
     */
    @GetMapping("/getJJ10")
    public JsonModel getJJ10(){
        return wuHouService.getJJ10();
    }

    /**
     *  11、企业发展情况(数据源未定)
     * @return
     */
    @GetMapping("/getJJ11")
    public JsonModel getJJ11(String ifToday, String fieldType){
        return wuHouService.getJJ11(ifToday, fieldType);
    }

    /**
     *  12、经济板块--所有企业行业分类情况
     *  中台修改中
     * @return
     */
    @GetMapping("/getJJ12")
    public JsonModel getJJ12(){
        return wuHouService.getJJ12();
    }

    /**
     *  13、所有企业行业分类情况（未定）
     *  中台修改中
     * @return
     */
    @GetMapping("/getJJ13")
    public JsonModel getJJ13(){
        return wuHouService.getJJ13();
    }

    /**
     * 获取企业发展情况数据
     * @param ifToday
     * @param fieldType
     * @return
     */
    public JsonModel getEconomicQYFZ(String ifToday, String fieldType){
        ifToday = "thisyear";
        fieldType = "slnum";
        return new JsonModel(true);
    }

    @GetMapping("/getQQ2")
    public JsonModel getQQ2(@RequestParam(required = false) String param){
        JsonModel jsonModel = wuHouService.getQQ2();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        return wuHouService.getTextJsonModel(param,"val",obj);
    }

    //——————————————————————————————————————————————————区情版块—————————————————————————————————————————————————————————




}
