package com.uxsino.leaderview.controller.wuhou;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.entity.CustomDotScheme;
import com.uxsino.leaderview.service.wuhou.WuHouService;
import com.uxsino.leaderview.service.wuhou.ZHSQService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
     *2、涉藏处突-涉藏商店企业概况
     * @return
     */
    @GetMapping("/getSZCT2")
    public JsonModel getSZCT2(@RequestParam(required = false) String param){
        JsonModel jsonModel = zhsqService.getSZCT2();
        JSONObject obj = (JSONObject) jsonModel.getObj();
        List<String> dotParam = Arrays.asList("1","2","3","4","5","6","7");
        JsonModel jsonModel2 = new JsonModel();
        //当param不是打点参数时,走文本框处理方法，否则返回打点信息
        if(!dotParam.contains(param)) {
            jsonModel2 = wuHouService.getTextJsonModel(param, "cnt", obj);
        }else {

            HashMap<String, String> idAndNameMap = new HashMap<>();
            idAndNameMap.put("1", "涉藏商店");
            idAndNameMap.put("2", "民宿旅馆");
            idAndNameMap.put("3", "藏餐茶吧");
            idAndNameMap.put("4", "娱乐场所");
            idAndNameMap.put("5", "涉藏机构");
            idAndNameMap.put("6", "小区院落");
            idAndNameMap.put("7", "演出场所");
            param = idAndNameMap.get(param);
            LinkedHashMap<String, JSONArray> typeAndDotMap = (LinkedHashMap<String, JSONArray>) obj.get("typeAndDotMap");
            JSONArray dotArray = typeAndDotMap.get(param);
//            JSONObject result = (JSONObject) jsonModel2.getObj();
            JSONObject result = new JSONObject();
            result.put("dotArray", dotArray);
            return new JsonModel(true,result);

        }

        return jsonModel2;
    }

    /**
     *3、涉藏处突-涉藏商店企业概况
     * @return
     */
    @GetMapping("/getSZCT3")
    public JsonModel getSZCT3(@RequestParam(required = false) String param){
        return zhsqService.getSZCT1(param);
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

}
