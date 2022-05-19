package com.uxsino.leaderview.controller.wuhou;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.entity.CustomDotScheme;
import com.uxsino.leaderview.service.wuhou.WuHouService;
import com.uxsino.leaderview.service.wuhou.ZHSQService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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

    /**
     *1、涉藏处突-社区民警列表
     * @return
     */
    @GetMapping("/getSZCT1")
    public JsonModel getSZCT1(@RequestParam(required = false) String param){
        return zhsqService.getSZCT1(param);
    }

    /**
     *2、涉藏处突-涉藏概况-文本下钻
     * @return
     */
    @GetMapping("/getSZCT2")
    public JsonModel getSZCT2(@RequestParam(required = false) String param){
        JsonModel jsonModel = zhsqService.getSZCT2();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        JsonModel jsonModel2 = new JsonModel();
        //当param不是打点参数时,走文本框处理方法，否则返回打点信息
        jsonModel2 = wuHouService.getTextJsonModel(param, "cnt", obj);
        Object obj2 = jsonModel2.getObj();
        //如果返回的是结果，则返回弹窗url
        if(obj2 instanceof JSONObject){
            //文本下钻列表的字段
            JSONArray ListArray = new JSONArray();
            ListArray.add("涉藏机构");
            ListArray.add("小区院落");
            ListArray.add("锅庄舞场");
            ListArray.add("娱乐场所");
            ListArray.add("涉藏商店");
            ListArray.add("藏餐茶吧");
            ListArray.add("民宿旅馆");
            if (ListArray.contains(param)) {
                ((JSONObject) obj2).put("url", "/leaderview/ZHSQ/getSZCT5?param=name:");
            }
        }
        return jsonModel2;
    }

    /**
     *3、涉藏处突-涉藏概况打点
     * @return
     */
    @GetMapping("/getSZCT3")
    public JsonModel getSZCT3(@RequestParam(required = false) String param){
        JsonModel jsonModel = zhsqService.getSZCT3();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        List<String> dotParam = Arrays.asList("1","2","3","4","5","6","7");
        JsonModel jsonModel2 = new JsonModel();

        //将文本框下钻放到 getSZCT2接口

        //当param不是打点参数时,走文本框处理方法，否则返回打点信息
        /*if(!dotParam.contains(param)) {
            jsonModel2 = wuHouService.getTextJsonModel(param, "cnt", obj);
            Object obj2 = jsonModel2.getObj();
            //如果返回的是结果，则返回弹窗url
            if(obj2 instanceof JSONObject){
                JSONArray array = new JSONArray();
                array.add("涉藏机构");
                array.add("小区院落");
                array.add("锅庄舞场");
                if (array.contains(param)) {
                    ((JSONObject) obj2).put("url", "/leaderview/ZHSQ/getSZCT5?param=name:");
                }
            }
            return jsonModel2;
        }else {*/
            HashMap<String, String> idAndNameMap = new HashMap<>();
            idAndNameMap.put("1", "涉藏商店");
            idAndNameMap.put("2", "民宿旅馆");
            idAndNameMap.put("3", "藏餐茶吧");
            idAndNameMap.put("4", "娱乐场所");
            idAndNameMap.put("5", "涉藏机构");
            idAndNameMap.put("6", "小区院落");
            idAndNameMap.put("7", "锅庄舞场");
            param = idAndNameMap.get(param);
            LinkedHashMap<String, JSONArray> typeAndDotMap = (LinkedHashMap<String, JSONArray>) obj.get("typeAndDotMap");
            JSONArray dotArray = typeAndDotMap.get(param);
            JSONObject result = new JSONObject();
            result.put("dotArray", dotArray);
            return new JsonModel(true, result);
//        }
    }

    /**
     *4、涉藏处突-网格区打点信息
     * @return
     */
    @Transactional
    @GetMapping("/getSZCT4")
    public JsonModel getSZCT4(){
        return zhsqService.getSZCT4();
    }

    /**
     *5、涉藏处突-涉藏概况下钻列表
     * @return
     */
    @GetMapping("/getSZCT5")
    public JsonModel getSZCT5(@RequestParam(required = false) String param){
        //前端传回的param是name: 加上param,所以需要取:后的param
        return zhsqService.getSZCT5(param.split(":")[1]);
    }

    /**
     *6、涉藏处突-涉藏概况重合点位打点
     * @return
     */
    @GetMapping("/getSZCT6")
    public JsonModel getSZCT6(@RequestParam(required = false) String param){
        //前端传回的param是name: 加上param,所以需要取:后的param
        return zhsqService.getSZCT6(param);
    }

    /**
     *涉藏处突-自定义打点获取
     * @return
     */
    @GetMapping("/getCustomDot")
    public JsonModel getCustomDot(){
        return zhsqService.getCustomDot();
    }

    /**
     *涉藏处突-自定义打点保存
     * @return
     */
    @Transactional
    @GetMapping("/saveCustomDot")
    public JsonModel saveCustomDot(CustomDotScheme customDotScheme){
        return zhsqService.saveCustomDot(customDotScheme);
    }

    /**
     *涉藏处突-自定义打点修改
     * @return
     */
    @Transactional
    @GetMapping("/updateCustomDot")
    public JsonModel updateCustomDot(CustomDotScheme customDotScheme){
        return zhsqService.updateCustomDot(customDotScheme);
    }

    /**
     *涉藏处突-自定义打点删除
     * @return
     */
    @Transactional
    @GetMapping("/deleteCustomDot")
    public JsonModel deleteCustomDot(CustomDotScheme customDotScheme){
        return zhsqService.deleteCustomDot(customDotScheme);
    }

    /**
     *4、涉藏处突-小区院落打点（返回数据包括涉藏小区和普通小区）
     * @return
     */
    @Transactional
    @GetMapping("/getCommunityDot")
    public JsonModel getCommunityDot(){
        return zhsqService.getCommunityDot();
    }

    /**
     *5、涉藏处突-社区网格打点
     * @return
     */
    @Transactional
    @GetMapping("/getGridDot")
    public JsonModel getGridDot(){
        return zhsqService.getGridDot();
    }

    /**
     *7、涉藏处突-涉藏人口数
     * @return
     */
    @GetMapping("/getSZCT7")
    public JsonModel getSZCT7(@RequestParam(required = false) String param){

        if(ObjectUtils.isEmpty(param)){
            JSONObject result = new JSONObject();
            result.put("name","藏区人口");
            result.put("info","1.4");
            result.put("url","/leaderview/ZHSQ/getSZCT7?param=name:");
            result.put("type","藏区人口");
            return new JsonModel(true,result);
        }else {

            //前端传回的param是name: 加上param,所以需要取:后的param
            /*JsonModel jsonModel1 = zhsqService.getSZCT7();
            JSONObject obj = (JSONObject) jsonModel1.getObj();
            JsonModel jsonModel2 = wuHouService.getPieToText(param, obj);
            Object obj1 = jsonModel2.getObj();
            //如果返回的是结果，则返回弹窗url
            if (obj1 instanceof JSONObject) {
                ((JSONObject) obj1).put("url", "");
            }*/

            return zhsqService.getSZCT7();
        }
    }

    /**
     *8、涉藏处突-实时预警
     * @return
     */
    @GetMapping("/getSZCT8")
    public JsonModel getSZCT8(@RequestParam(required = false) String param){
        //前端传回的param是name: 加上param,所以需要取:后的param
        return zhsqService.getSZCT8();
    }

    /**
     *9、涉藏处突-涉藏高校
     * @return
     */
    @GetMapping("/getSZCT9")
    public JsonModel getSZCT9(@RequestParam(required = false) String param){
        if(ObjectUtils.isEmpty(param)){
            JSONObject result = new JSONObject();
            result.put("name","涉藏高校");
            result.put("info","1");
            result.put("url","/leaderview/ZHSQ/getSZCT9?param=name:");
            result.put("type","涉藏高校");
            return new JsonModel(true,result);
        }else {

            //前端传回的param是name: 加上param,所以需要取:后的param
            /*JsonModel jsonModel1 = zhsqService.getSZCT9();
            JSONObject obj = (JSONObject) jsonModel1.getObj();
            JsonModel jsonModel2 = wuHouService.getPieToText(param, obj);
            Object obj1 = jsonModel2.getObj();
            //如果返回的是结果，则返回弹窗url
            if (obj1 instanceof JSONObject) {
                ((JSONObject) obj1).put("url", "");
            }*/

            return zhsqService.getSZCT9();
        }
    }

    /**
     *10、涉藏处突-指挥体系-表格模块
     * @return
     */
    @GetMapping("/getSZCT10")
    public JsonModel getSZCT10(@RequestParam(required = false) String param){
        //前端传回的param是name: 加上param,所以需要取:后的param
        JSONObject obj = zhsqService.getSZCT10();
        JSONObject result = new JSONObject();
        //包含name的是弹窗的
        if(!Strings.isNullOrEmpty(param) && param.contains("name")){
            param = param.split(":")[1];
        }
        result = obj.getJSONObject(param);

        return new JsonModel(true,result);
    }

    /**
     *10、涉藏处突-指挥体系-文本模块
     * @return
     */
    @GetMapping("/getSZCT11")
    public JsonModel getSZCT11(@RequestParam(required = false) String param){
        //前端传回的param是name: 加上param,所以需要取:后的param
        JSONObject obj = zhsqService.getSZCT10();
        JSONObject result = new JSONObject();
        result.put("name",param);
        result.put("info",param);
        result.put("url","/leaderview/ZHSQ/getSZCT10?param=name:");

        return new JsonModel(true,result);
    }

    /**
     *12、涉藏处突-涉藏医院
     * @return
     */
    @GetMapping("/getSZCT12")
    public JsonModel getSZCT12(@RequestParam(required = false) String param){

        if(ObjectUtils.isEmpty(param)){
            JSONObject result = new JSONObject();
            result.put("name","涉藏医院");
            result.put("info","2");
            result.put("url","/leaderview/ZHSQ/getSZCT12?param=name:");
//            result.put("type","藏区人口");
            return new JsonModel(true,result);
        }else {

            //前端传回的param是name: 加上param,所以需要取:后的param
            /*JsonModel jsonModel1 = zhsqService.getSZCT7();
            JSONObject obj = (JSONObject) jsonModel1.getObj();
            JsonModel jsonModel2 = wuHouService.getPieToText(param, obj);
            Object obj1 = jsonModel2.getObj();
            //如果返回的是结果，则返回弹窗url
            if (obj1 instanceof JSONObject) {
                ((JSONObject) obj1).put("url", "");
            }*/

            return zhsqService.getSZCT12();
        }
    }

    /**
     *9、涉藏处突-
     * @return
     */
    @GetMapping("/getSZCT13")
    public JsonModel getSZCT13(@RequestParam(required = false) String param){
        //前端传回的param是name: 加上param,所以需要取:后的param
        return zhsqService.getSZCT6(param);
    }

}
