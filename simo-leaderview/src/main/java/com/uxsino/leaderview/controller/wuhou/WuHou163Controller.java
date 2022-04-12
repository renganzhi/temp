package com.uxsino.leaderview.controller.wuhou;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.dao.IVideoSchemeDao;
import com.uxsino.leaderview.entity.VideoScheme;
import com.uxsino.leaderview.service.wuhou.DailyManagementService;
import com.uxsino.leaderview.service.wuhou.RHZHService;
import com.uxsino.leaderview.service.wuhou.WuHouService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"武侯智慧城市-16:3大屏展示数据接口"})
@RestController
@RequestMapping("/WuHou163")
@Slf4j
public class WuHou163Controller {

    @Autowired
    WuHouService wuHouService;

    @Autowired
    DailyManagementService dailyManagementService;

    @Autowired
    RHZHService rhzhService;

    @Autowired
    IVideoSchemeDao videoSchemeDao;

    //————————————————————————————————————————经济版块————————————————————————————————————————————————————————————————————
    //————————————————————————————————————————经济版块————————————————————————————————————————————————————————————————————
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
        JsonModel jsonModel2 = wuHouService.getTextJsonModel(param, "num", obj);
        Object obj1 = jsonModel2.getObj();
        //如果返回的是结果，则返回弹窗url
        if(obj1 instanceof JSONObject){
            ((JSONObject) obj1).put("url","/leaderview/WuHou163/getJJ7?param=name:");
        }
        return jsonModel2;
    }

    /**
     * 7、重大项目情况-项目分类明细 ——弹窗详情
     * @param param 项目名称
     * @return
     */
    @GetMapping("/getJJ7")
    public JsonModel getJJ7(String param){
        return wuHouService.getJJ7(param.split(":")[1]);
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
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);
        Object obj1 = jsonModel2.getObj();
        //如果返回的是结果，则返回弹窗url
        if(obj1 instanceof JSONObject){
            ((JSONObject) obj1).put("url","/leaderview/WuHou163/getJJ9?param=name:");
        }

        return jsonModel2;
    }

    /**
     * 9、内外资投资情况 月度列表 —— 弹窗表格
     * @return
     */
    @GetMapping("/getJJ9")
    public JsonModel getJJ9(String param){
        return wuHouService.getJJ9(param.split(":")[1]);
    }

    /**
     *  10、领导率队赴外开展投资促进活动情况 —— 地图迁徙图 DataFlow
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

    //——————————————————————————————————————————————————区情版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————区情版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————区情版块—————————————————————————————————————————————————————————

    /**
     * 1、社区实有人口、流动人口切Top10  —— 可切换柱状图
     * @param param
     * @return
     */
    @GetMapping("/getQQ1")
    public JsonModel getQQ1(@RequestParam(required = false) String param){
        JSONObject obj1 = null;
        try {
            obj1 = wuHouService.getQQ1();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }
        JSONObject obj2 = null;
        try {
            obj2 = wuHouService.getQQ4();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }
        obj1.put("unit","人");
        obj2.put("unit","人");

        JSONObject result = new JSONObject();
        //存放总数据的对象
        JSONObject dataResult = new JSONObject();
        //存放两组数据名称的集合
        JSONArray nameAarry = new JSONArray();
        nameAarry.add("社区实有人口TOP10");
        nameAarry.add("社区流动人口TOP10");
        dataResult.put("nameArray",nameAarry);
        //存放两组数据的集合
        JSONArray dataArray = new JSONArray();
        dataArray.add(obj1);
        dataArray.add(obj2);
        dataResult.put("dataArray",dataArray);

        result.put("dataArray",dataResult);

        return new JsonModel(true,result);
    }


    /**
     * 2、数字武侯 —— 可选文本框
     * @param param 条目明细
     * @return
     */
    @GetMapping("/getQQ2")
    public JsonModel getQQ2(@RequestParam(required = false) String param){
        JsonModel jsonModel = wuHouService.getQQ2();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        return wuHouService.getTextJsonModel(param,"val",obj);
    }

    /**
     * 2.2、数字武侯 —— 轮播组件
     * @return
     */
    @GetMapping("/getQQ22")
    public JsonModel getQQ22(){
        return wuHouService.getQQ22();
    }

    /**
     * 3、行政区划-地图数据
     * @param param
     * @return
     */
    @GetMapping("/getQQ3")
    public JsonModel getQQ3(@RequestParam(required = false) String param){
        return wuHouService.getQQ3();
    }

    /**
     * 4、区情板块-社区常住人口排行
     * @return
     */
    @GetMapping("/getQQ4")
    public JsonModel getQQ4(){
        return new JsonModel(true,wuHouService.getQQ1());
    }

    /**
     * 5、区情板块-流动人口数量排行
     * @return
     */
    @GetMapping("/getQQ5")
    public JsonModel getQQ5(){
        return new JsonModel(true,wuHouService.getQQ4());
    }

    /**
     * 6、区情板块-经济人口介绍文案
     * @param param 内资/外资
     * @return
     */
    @GetMapping("/getQQ6")
    public JsonModel getQQ6(@RequestParam(required = false) String param){
        JsonModel jsonModel = wuHouService.getQQ5();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);
        return jsonModel2;
    }

    //——————————————————————————————————————————————————事件版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————事件版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————事件版块—————————————————————————————————————————————————————————

    /**
     *1、今日重大活动信息
     * @return
     */
    @GetMapping("/getSJ1")
    public JsonModel getSJ1(){
        return wuHouService.getEvent1();
    }

    /**
     *2、110非警推送-警综管辖 分类统计
     * @return
     */
    @GetMapping("/getSJ2")
    public JsonModel getSJ2(){
        return wuHouService.getEvent2();
    }

    /**
     *3、110非警推送-明细
     * @return
     */
    @GetMapping("/getSJ3")
    public JsonModel getSJ3(String param){
        return wuHouService.getEvent3(param);
    }

    /**
     *4、网络舆情分类分析
     * @return
     */
    @GetMapping("/getSJ4")
    public JsonModel getSJ4(){
        return wuHouService.getEvent4();
    }

    /**
     *5、网络舆情高频词
     * @return
     */
    @GetMapping("/getSJ5")
    public JsonModel getSJ5(){
        return wuHouService.getEvent5();
    }

    /**
     *6、数据分析（左）无 —— 中台尚未开发完毕
     * @return
     */
    @GetMapping("/getSJ6")
    public JsonModel getSJ6(){
        return wuHouService.getEvent6();
    }

    /**
     *7、事件类型分类-来源  —— 可选文本框
     * @return
     */
    @GetMapping("/getSJ7")
    public JsonModel getSJ7(@RequestParam(required = false) String param){
        /*JsonModel jsonModel = wuHouService.getEvent7();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        return wuHouService.getTextJsonModel(param,"num",obj);*/
        return wuHouService.getEvent7();
    }

    /**
     *8、事件类型分类-事件类型 占比
     * @return
     */
    @GetMapping("/getSJ8")
    public JsonModel getSJ8(String param){
        JsonModel jsonModel = wuHouService.getEvent8();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        return wuHouService.getTextJsonModel(param,"percentage",obj);
    }

    /**
     *9、事件类型分类-事件列表
     * @return
     */
    @GetMapping("/getSJ9")
    public JsonModel getSJ9(String param, Integer pageSize){
        return wuHouService.getEvent9(param,pageSize);
    }

    /**
     *10、事件类型分类-事件详情
     * @return
     */
    @GetMapping("/getSJ10")
    public JsonModel getSJ10(String param){
        return wuHouService.getEvent10(param);
    }

    /**
     *11、 事件类型分类-数据分析
     * @return
     */
    @GetMapping("/getSJ11")
    public JsonModel getSJ11(){
        return wuHouService.getEvent11();
    }

    /**
     *12、 地图-热力图坐标
     * @return
     */
    @GetMapping("/getSJ12")
    public JsonModel getSJ12(){
        return wuHouService.getEvent12();
    }

    /**
     *13、地图-派出所点位
     * @return
     */
    @GetMapping("/getSJ13")
    public JsonModel getSJ13(){
        return wuHouService.getEvent13();
    }

    /**
     *14、110非警推送-数据分析（左）
     * @return
     */
    @GetMapping("/getSJ14")
    public JsonModel getSJ14(){
        return wuHouService.getEvent14();
    }

    //——————————————————————————————————————————————————安全版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————安全版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————安全版块—————————————————————————————————————————————————————————
    /**
     *1、危险源情况
     * @return
     */
    @GetMapping("/getAQ1")
    public JsonModel getAQ1(){
        return wuHouService.getAQ1();
    }

    /**
     *2、危险源详情
     * @return
     */
    @GetMapping("/getAQ2")
    public JsonModel getAQ2(String param){
        return wuHouService.getAQ2(param.split(":")[1]);
    }

    /**
     *3、报警类型数量 ——可选文本框
     * @return
     */
    @GetMapping("/getAQ3")
    public JsonModel getAQ3(String param){
        JsonModel jsonModel = wuHouService.getAQ3();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getTextJsonModel(param, "num", obj);
        return jsonModel2;
    }

    /**
     *4、企业危险源情况top10 —— 表格
     * @return
     */
    @GetMapping("/getAQ4")
    public JsonModel getAQ4(){
        return wuHouService.getAQ4();
    }

    /**
     *5、报警变化趋势-时间 ——曲线图
     * @return
     */
    @GetMapping("/getAQ5")
    public JsonModel getAQ5(){
        return wuHouService.getAQ5();
    }

    /**
     *6、报警变化趋势-分类
     * 有问题 接口返回数据和 3、报警类型数量 的一样
     * @return
     */
    @GetMapping("/getAQ6")
    public JsonModel getAQ6(){
        return wuHouService.getAQ6();
    }

    /**
     *7、隐患排查情况-年（当年）
     * @return
     */
    @GetMapping("/getAQ7")
    public JsonModel getAQ7(@RequestParam(required = false) String param){
        JsonModel jsonModel = wuHouService.getAQ7(false);
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);
        return jsonModel2;
    }

    /**
     *7、隐患排查情况-年（当年） —— 武侯柱状图
     * @return
     */
    @GetMapping("/getAQ72")
    public JsonModel getAQ72(@RequestParam(required = false) String param){
        return wuHouService.getAQ7(true);
    }

    /**
     *8、隐患排查情况-月(当月)
     * @return
     */
    @GetMapping("/getAQ8")
    public JsonModel getAQ8(@RequestParam(required = false) String param){
        JsonModel jsonModel = wuHouService.getAQ8(false);
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);
        return jsonModel2;
    }

    /**
     *8、隐患排查情况-月(当月)
     * @return
     */
    @GetMapping("/getAQ82")
    public JsonModel getAQ82(@RequestParam(required = false) String param){
        return wuHouService.getAQ8(true);
    }

    /**
     *9、隐患报警列表
     * @return
     */
    @GetMapping("/getAQ9")
    public JsonModel getAQ9(){
        return wuHouService.getAQ9();
    }

    /**
     *10、隐患报警列表-明细
     * @return
     */
    @GetMapping("/getAQ10")
    public JsonModel getAQ10(String param){
        return wuHouService.getAQ10(param);
    }

    /**
     *隐患趋势变化-年/月/日
     * @return
     */
    @GetMapping("/getAQ11")
    public JsonModel getAQ11(){

        JSONObject obj1 = null;
        try {
            obj1 = wuHouService.getAQ11();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject obj2 = null;
        try {
            obj2 = wuHouService.getAQ12();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        JSONObject obj3 = null;
        try {
            obj3 = wuHouService.getAQ13();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,"优易中台调用失败",e.getMessage());
        }

        obj1.put("unit","时间");
        obj2.put("unit","时间");
        obj3.put("unit","时间");

        JSONObject result = new JSONObject();
        //存放总数据的对象
        JSONObject dataResult = new JSONObject();
        //存放两组数据名称的集合
        JSONArray nameAarry = new JSONArray();
        nameAarry.add("隐患趋势变化-年");
        nameAarry.add("隐患趋势变化-月");
        nameAarry.add("隐患趋势变化-日");
        dataResult.put("nameArray",nameAarry);
        //存放两组数据的集合
        JSONArray dataArray = new JSONArray();
        dataArray.add(obj1);
        dataArray.add(obj2);
        dataArray.add(obj3);
        dataResult.put("dataArray",dataArray);

        result.put("dataArray",dataResult);

        return new JsonModel(true,result);

    }

    /**
     *14、隐患企业统计排行TOP榜单
     * @return
     */
    @GetMapping("/getAQ14")
    public JsonModel getAQ14(){
        return wuHouService.getAQ14();
    }

    //——————————————————————————————————————————————————生态环保版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————生态环保版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————生态环保版块—————————————————————————————————————————————————————————

    /**
     *1.1、天气
     * @return
     */
    @GetMapping("/getHB1")
    public JsonModel getHB11(String param){
        JsonModel jsonModel = wuHouService.getHB11();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);
        return jsonModel2;
    }

    /**
     *1.2、天气预报
     * @return
     */
    @GetMapping("/getHB2")
    public JsonModel getHB12(){
        return wuHouService.getHB12();
    }

    /**
     *2、域内河流
     * @return
     */
    @GetMapping("/getHB3")
    public JsonModel getHB3(@RequestParam(required = false) String param){
        JsonModel jsonModel = wuHouService.getHB2();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getTextJsonModel(param, "num", obj);
        return jsonModel2;
    }

    /**
     *4、浆洗街街道五项绿地面积
     * @return
     */
    @GetMapping("/getHB4")
    public JsonModel getHB4(){
        return wuHouService.getHB4();
    }

    /**
     *环境/绿地（五项绿地）
     * @return
     */
    @GetMapping("/getHB5")
    public JsonModel getHB5(){
        return wuHouService.getHB5();
    }

    /**
     *环境-垃圾
     * @return
     */
    @GetMapping("/getHB6")
    public JsonModel getHB6(){
        return wuHouService.getHB6();
    }

    /**
     *大气-成都市aqi
     * @return
     */
    @GetMapping("/getHB7")
    public JsonModel getHB7(@RequestParam(required = false) String param){

        JsonModel jsonModel = wuHouService.getHB7();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);

        return jsonModel2;
    }

    /**
     *大气-成都市 近30天AQI
     * @return
     */
    @GetMapping("/getHB8")
    public JsonModel getHB8(){
        return wuHouService.getHB8();
    }

    /**
     *道路绿化面积
     * @return
     */
    @GetMapping("/getHB9")
    public JsonModel getHB9(){
        return wuHouService.getHB9();
    }

    //——————————————————————————————————————————————————日常管理—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————日常管理—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————日常管理—————————————————————————————————————————————————————————

    /**
     * 1、日程管理-一网通办
     * @param param
     * @return
     */
    @GetMapping("/getRC1")
    public JsonModel getRC1(@RequestParam(required = false) String param){

        JsonModel jsonModel = dailyManagementService.getRC1();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);

        return jsonModel2;
    }

    /**
     * 2、日程管理-区领导值班情况
     * @return
     */
    @GetMapping("/getRC2")
    public JsonModel getRC2(){
        return dailyManagementService.getRC2();
    }

    /**
     * 22、日程管理-区值班领导姓名
     * @return
     */
    @GetMapping("/getRC22")
    public JsonModel getRC22(@RequestParam(required = false) String param){
        JsonModel jsonModel = dailyManagementService.getRC22("y15-01");
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);

        return jsonModel2;
    }

    /**
     * 3、日程管理-疫情监控
     * @param param
     * @return
     */
    @GetMapping("/getRC3")
    public JsonModel getRC3(@RequestParam(required = false) String param){

        JsonModel jsonModel = dailyManagementService.getRC3();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);

        return jsonModel2;
    }

    /**
     * 4、日程管理——两地四区指数
     * @param param
     * @return
     */
    @GetMapping("/getRC4")
    public JsonModel getRC4(@RequestParam(required = false) String param){

        JsonModel jsonModel = dailyManagementService.getRC4();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = wuHouService.getPieToText(param,obj);

        return jsonModel2;
    }

    /**
     * 5、日程管理——城区事件 ——智慧武侯标签轮播
     * @return
     */
    @GetMapping("/getRC5")
    public JsonModel getRC5(){
        return dailyManagementService.getRC5();
    }

    /**
     * 6、日程管理——城区人口 ——智慧武侯标签轮播
     * @return
     */
    @GetMapping("/getRC6")
    public JsonModel getRC6(){
        return dailyManagementService.getRC6();
    }

    //——————————————————————————————————————————————————融合指挥版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————融合指挥版块—————————————————————————————————————————————————————————
    //——————————————————————————————————————————————————融合指挥版块—————————————————————————————————————————————————————————

    /**
     * 1、视频融合方案保存
     */
    @PostMapping("/saveScheme")
    public JsonModel saveScheme(VideoScheme videoScheme){
        String name = videoScheme.getName();
        VideoScheme scheme = videoSchemeDao.getByName(name);
        if(ObjectUtils.isEmpty(scheme)) {
            videoSchemeDao.save(videoScheme);
        }else {
            scheme.setVideoArry(videoScheme.getVideoArry());
            videoSchemeDao.save(scheme);
        }
        return new JsonModel(true);
    }

    /**
     * 2、视频融合方案获取
     * @return
     */
    @GetMapping("/getAllScheme")
    public JsonModel getAllScheme(){
        List<VideoScheme> all = videoSchemeDao.findAll();
        return new JsonModel(true,all);
    }

    /**
     * 3、领导班子通讯录文本框-弹窗表格
     * @return
     */
    @GetMapping("/getRH3")
    public JsonModel getRH3(@RequestParam(required = false) String param){

        if(Strings.isNullOrEmpty(param)) {
            JSONObject resultObj = new JSONObject();
            resultObj.put("name", "通讯录");
            resultObj.put("info", "通讯录");
            resultObj.put("unit", "通讯录");
            resultObj.put("url", "/leaderview/WuHou163/getRH3?param=name:");

            return new JsonModel(true, resultObj);
        }else {
            return rhzhService.getRH1();
        }
    }

}
