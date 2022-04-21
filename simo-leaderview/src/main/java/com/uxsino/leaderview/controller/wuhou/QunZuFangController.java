package com.uxsino.leaderview.controller.wuhou;


import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.QunZuFangService;
import com.uxsino.leaderview.service.wuhou.WuHouService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Api(tags = {"智慧社区-群租房和未办证住所大屏接口"})
@RestController
@RequestMapping("/QZF")
@Slf4j
public class QunZuFangController {

    @Autowired
    QunZuFangService qunZuFangService;

    @Autowired
    WuHouService wuHouService;

    /**
     * 1、获取群租房不同类型的数量
     * @param type 群租房类型
     * @return
     */
    @GetMapping("/getQZFHomeCount")
    public JsonModel getQZFHomeCount(Integer type){

        return qunZuFangService.getQZFHomeCount(type);

    }

    /**
     * 2、获取群租房当日登记量和当日入住量以及当月累计量
     * @param type
     * @return
     */
    @GetMapping("/getTodayLiveCount")
    public JsonModel getTodayLiveCount(String type){
        return qunZuFangService.getTodayLiveCount(type);
    }

    /**
     * 3、房屋住房率、空置率排行
     * @return
     */
    @GetMapping("/getLiveSort")
    public JsonModel getLiveSort(){

        return qunZuFangService.getLiveSort();
    }

    /**
     * 4、入住人员时间分析
     * @param when
     * @return
     */
    @GetMapping("/getLiveByTime")
    public JsonModel getLiveByTime(String when){

        return qunZuFangService.getLiveByTime(when);
    }

    /**
     * 5、通过地址查询入住记录
     * @param address
     * @return
     */
    @GetMapping("/getRegisterByAddress")
    public JsonModel getRegisterByAddress(String address){
        return qunZuFangService.getRegisterByAddress(address);
    }

    /**
     * 6、通过地址查询走访记录
     * @param address
     * @return
     */
    @GetMapping("/getPatrolByAddress")
    public JsonModel getPatrolByAddress(String address){
        return qunZuFangService.getPatrolByAddress(address);
    }

    /**
     * 1、群租房-敏感人员分析
     * @return
     */
    @GetMapping("/getQZF1")
    public JsonModel getQZF1(){
        return qunZuFangService.getQZF1();
    }

    /**
     * 2、群租房-各类别场所数量
     * @return
     */
    @GetMapping("/getQZF2")
    public JsonModel getQZF2(@RequestParam(required = false) String param){
        return qunZuFangService.getQZF2(param);
    }

    /**
     * 3、群租房-入住人员来源地(归属地)分析
     * @return
     */
    @GetMapping("/getQZF3")
    public JsonModel getQZF3(){
        return qunZuFangService.getQZF3();
    }

    /**
     * 4、群租房-入住异常预警
     * @return
     */
    @GetMapping("/getQZF4")
    public JsonModel getQZF4(){
        return qunZuFangService.getQZF4();
    }

    /**
     * 5、群租房-入住人员top10:高频入住、高流动性
     * @return
     */
    @GetMapping("/getQZF5")
    public JsonModel getQZF5(){
        return qunZuFangService.getQZF5();
    }

    /**
     * 6、群租房-入住房率top5:高入住房、高空置房
     * @return
     */
    @GetMapping("/getQZF6")
    public JsonModel getQZF6(){
        return qunZuFangService.getQZF6();
    }

    /**
     * 7、群租房-敏感人员人类占比
     * @return
     */
    @GetMapping("/getQZF7")
    public JsonModel getQZF7(){
        return qunZuFangService.getQZF7();
    }

    /**
     * 8、群租房-入住时间分析
     * @return
     */
    @GetMapping("/getQZF8")
    public JsonModel getQZF8(){
        return qunZuFangService.getQZF8();
    }

    /**
     * 9、群租房-入住情况统计-当日登记/离店/当月累计量
     * @return
     */
    @GetMapping("/getQZF9")
    public JsonModel getQZF9(@RequestParam(required = false) String param){

        JsonModel jsonModel = qunZuFangService.getQZF9();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);

        Object obj1 = jsonModel2.getObj();
        //如果返回的是结果，则返回弹窗url
        if(obj1 instanceof JSONObject){
            String url = "/leaderview/QZF/getQZF12?param=name:";
            ((JSONObject) obj1).put("url",url);
        }

        return jsonModel2;

    }

    /**
     * 10、群租房-入住信息查询接口
     * @return
     */
    @GetMapping("/getQZF10")
    public JsonModel getQZF10(@RequestParam(required = false) String query, @RequestParam(required = false) String param){

        return qunZuFangService.getQZF10(query, param);
    }

    /**
     * 11、群租房-场所信息查询接口
     * @return
     */
    @GetMapping("/getQZF11")
    public JsonModel getQZF11(@RequestParam(required = false) String query, @RequestParam(required = false) String param){
        JsonModel jsonModel = qunZuFangService.getQZF11(query, param.split(":")[1]);
        JSONObject result = (JSONObject) jsonModel.getObj();
        List<String> columns = Arrays.asList("名称","地址","房间数","床铺数");
        result.put("columns",columns);
        return new JsonModel(true,result);
    }

    /**
     * 12、群租房-入住人员明细信息
     * @return
     */
    @GetMapping("/getQZF12")
    public JsonModel getQZF12(@RequestParam(required = false) String param){
        return qunZuFangService.getQZF12(param);
    }

    /**
     * 、
     * @return
     */
    @GetMapping("/getQZF13")
    public JsonModel getQZF13(){
        return qunZuFangService.getQZF3();
    }

    /**
     * 、
     * @return
     */
    @GetMapping("/getQZF14")
    public JsonModel getQZF14(){
        return qunZuFangService.getQZF3();
    }

    /**
     * 、
     * @return
     */
    @GetMapping("/getQZF15")
    public JsonModel getQZF15(){
        return qunZuFangService.getQZF3();
    }

    //——————————————————————————————————————————————————未办证住所版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————未办证住所版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————未办证住所版块—————————————————————————————————————————————————————————
    /**
     * 1、未办证住所-全区网约房地图打点
     * @return
     */
    @GetMapping("/getWBZ1")
    public JsonModel getWBZ1(){
        return qunZuFangService.getWBZ1();
    }

    /**
     * 2、未办证住所-街道网约房分布
     * @return
     */
    @GetMapping("/getWBZ2")
    public JsonModel getWBZ2(){
        return qunZuFangService.getWBZ2();
    }

    /**
     * 3、未办证住所-入住街道top5接口
     * @return
     */
    @GetMapping("/getWBZ3")
    public JsonModel getWBZ3(){
        return qunZuFangService.getWBZ3();
    }

    /**
     * 4、未办证住所-入住人员户籍地TOP10
     * @return
     */
    @GetMapping("/getWBZ4")
    public JsonModel getWBZ4(){
        return qunZuFangService.getWBZ4();
    }

    /**
     * 5、未办证住所-入住情况统计
     * @return
     */
    @GetMapping("/getWBZ5")
    public JsonModel getWBZ5(@RequestParam(required = false) String param){
        JsonModel jsonModel = qunZuFangService.getWBZ5();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);
        Object obj1 = jsonModel2.getObj();
        //如果返回的是结果，则返回弹窗url
        if(obj1 instanceof JSONObject){
            //今日登记房间详情和今日登记人详情接口待提供，对接好后配置在下面url中 -类似内外资投资详情
//            ((JSONObject) obj1).put("url","/leaderview/WuHou163/getJJ9?param=name:");
        }

        return jsonModel2;
    }

    /**
     * 6、未办证住所-一月内空置排名
     * @return
     */
    @GetMapping("/getWBZ6")
    public JsonModel getWBZ6(){
        return qunZuFangService.getWBZ6();
    }

    /**
     * 7、未办证住所-一月内入住排名
     * @return
     */
    @GetMapping("/getWBZ7")
    public JsonModel getWBZ7(){
        return qunZuFangService.getWBZ7();
    }

    /**
     * 8、未办证住所-入住异常预警
     * @return
     */
    @GetMapping("/getWBZ8")
    public JsonModel getWBZ8(){
        return qunZuFangService.getWBZ8();
    }

    /**
     * 9、未办证住所-入住时间分析
     * @return
     */
    @GetMapping("/getWBZ9")
    public JsonModel getWBZ9(){
        return qunZuFangService.getWBZ9();
    }

    /**
     * 10、未办证住所-
     * @return
     */
    @GetMapping("/getWBZ10")
    public JsonModel getWBZ10(){
        return qunZuFangService.getWBZ2();
    }

    /**
     * 8、未办证住所-
     * @return
     */
    @GetMapping("/getWBZ11")
    public JsonModel getWBZ11(){
        return qunZuFangService.getWBZ2();
    }

    /**
     * 8、未办证住所-
     * @return
     */
    @GetMapping("/getWBZ12")
    public JsonModel getWBZ12(){
        return qunZuFangService.getWBZ2();
    }

    /**
     * 8、未办证住所-
     * @return
     */
    @GetMapping("/getWBZ13")
    public JsonModel getWBZ13(){
        return qunZuFangService.getWBZ2();
    }
}
