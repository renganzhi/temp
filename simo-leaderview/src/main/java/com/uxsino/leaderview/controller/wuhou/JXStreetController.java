package com.uxsino.leaderview.controller.wuhou;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.service.wuhou.JXStreetService;
import com.uxsino.leaderview.service.wuhou.WuHouService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static com.uxsino.leaderview.service.wuhou.WuHouService.getOneToPie;

@Api(tags = {"武侯智慧城市-浆洗街街道大屏展示数据接口"})
@RestController
@RequestMapping("/JX")
@Slf4j
public class JXStreetController {

    @Autowired
    WuHouService wuHouService;

    @Autowired
    JXStreetService jxStreetService;


    /**
     *1、浆洗街—区情—数字浆洗街
     * @return
     */
    @GetMapping("/getJX1")
    public JsonModel getJX1(@RequestParam(required = false) String year, @RequestParam(required = false) String param){

        JsonModel jsonModel = jxStreetService.getJX1(year);
        if(Strings.isNullOrEmpty(year)){
            return jsonModel;
        }else {
            JSONObject obj = (JSONObject) jsonModel.getObj();
            JsonModel jsonModel2 = wuHouService.getPieToText(param, obj);
            return jsonModel2;
        }

    }

    /**
     *2、浆洗街——经济——领导走访情况
     * @return
     */
    @GetMapping("/getJX2")
    public JsonModel getJX2(){
        return jxStreetService.getJX2();
    }

    /**
     *3、浆洗街——经济——3种税收历史
     * @return
     */
    @GetMapping("/getJX3")
    public JsonModel getJX3(){
        return jxStreetService.getJX3();
    }

    /**
     * 4、浆洗街——经济——3种税收全年总和
     * @param year 年份 如果年份为空，则返回年份下拉列表,不为空则走正常流程
     * @param param 参数
     * @return
     */
    @GetMapping("/getJX4")
    public JsonModel getJX4(@RequestParam(required = false) String year, @RequestParam(required = false) String param){

        JsonModel jsonModel = jxStreetService.getJX4(year);
        if(Strings.isNullOrEmpty(year)){
            return jsonModel;
        }else {
            JSONObject obj = (JSONObject) jsonModel.getObj();
            JsonModel jsonModel2 = wuHouService.getPieToText(param, obj);
            return jsonModel2;
        }

    }

    /**
     *5、浆洗街街道——驻藏机构打点
     * @return
     */
    @GetMapping("/getJX5")
    public JsonModel getJX5(@RequestParam(required = false) Integer id){

        return jxStreetService.getJX5(id);
    }

    /**
     *6、浆洗街街道——驻藏小区打点
     * @return
     */
    @GetMapping("/getJX6")
    public JsonModel getJX6(@RequestParam(required = false) Integer id){
        return jxStreetService.getJX6(id);
    }

    /**
     * 7、浆洗街——经济——纳税行业分布 ——饼图
     * @param year 年份 如果年份为空，则返回年份下拉列表,不为空则走正常流程
     * @return
     */
    @GetMapping("/getJX7")
    public JsonModel getJX7(@RequestParam(required = false) String year){

        if(Strings.isNullOrEmpty(year)){
            return jxStreetService.getJX7(year,"纳税行业分布");
        }else {

            JSONArray columns = new JSONArray();
            columns.add("行业");
            columns.add("数量");

            LinkedHashMap<String, String> keyAndNameMap = new LinkedHashMap<>();

            keyAndNameMap.put("construction_industry", "辖区建筑业企业纳税额（万元）");
            keyAndNameMap.put("leasing_business_services", "辖区租赁和商务服务业企业纳税额（万元）");
            keyAndNameMap.put("real_estate", "辖区房地产业企业纳税额（万元）");
            keyAndNameMap.put("wholesale_retail_trade", "辖区批发和零售业企业纳税额（万元）");
            keyAndNameMap.put("other_industry", "辖区其他行业企业纳税额（万元）");

            List<String> needList = Arrays.asList("construction_industry", "leasing_business_services","real_estate","wholesale_retail_trade","other_industry");


            JsonModel jsonModel = jxStreetService.getJX7(year,"纳税行业分布");
            JSONArray data = (JSONArray) jsonModel.getObj();
            JSONObject result = getOneToPie(columns, data, needList, keyAndNameMap);

            result.put("unit","万元");

            return new JsonModel(true,result);
        }
    }

    /**
     * 8、浆洗街——经济——内外资投资 ——文本框
     * @param year 年份 如果年份为空，则返回年份下拉列表,不为空则走正常流程
     * @param param 参数
     * @return
     */
    @GetMapping("/getJX8")
    public JsonModel getJX8(@RequestParam(required = false) String year, @RequestParam(required = false) String param){
//        JsonModel jsonModel = jxStreetService.getJX7(year,"内外资投资",new JSONArray, new LinkedHashMap<String,String>(),Arrays.asList(""));
        JsonModel jsonModel = new JsonModel();
        if(Strings.isNullOrEmpty(year)){
            jsonModel = jxStreetService.getJX7(year,"内外资投资");
            return jsonModel;
        }else {
            jsonModel = jxStreetService.getJX7(year,"内外资投资");
            JSONObject obj = (JSONObject) jsonModel.getObj();
            JsonModel jsonModel2 = wuHouService.getPieToText(param, obj);
            return jsonModel2;
        }
    }

    /**
     * 9、浆洗街——经济——辖区“四上”企业数量 ——柱状图
     * @param year 年份 如果年份为空，则返回年份下拉列表,不为空则走正常流程
     * @param param 参数
     * @return
     */
    @GetMapping("/getJX9")
    public JsonModel getJX9(@RequestParam(required = false) String year, @RequestParam(required = false) String param){
        if(Strings.isNullOrEmpty(year)){
            return jxStreetService.getJX7(year,"辖区“四上”企业数量");
        }else {

            JSONArray columns = new JSONArray();
            columns.add("企业");
            columns.add("数量");

            LinkedHashMap<String, String> keyAndNameMap = new LinkedHashMap<>();
            //XQSSQYMap.put("辖区规上工业企业数量","regulated_industry");
            //            XQSSQYMap.put("辖区有资质建筑业企业数量","qualified_construction_industry");
            //            XQSSQYMap.put("辖区规上服务业企业数量","regulated_service_industry");
            //            XQSSQYMap.put("辖区限上商贸业企业数量","limited_to_commerce");

            keyAndNameMap.put("regulated_industry", "规上工业");
            keyAndNameMap.put("qualified_construction_industry", "有资质建筑业");
            keyAndNameMap.put("regulated_service_industry", "规上服务业");
            keyAndNameMap.put("limited_to_commerce", "限上商贸业");

            List<String> needList = Arrays.asList("regulated_industry", "qualified_construction_industry","regulated_service_industry","limited_to_commerce");

            JsonModel jsonModel = jxStreetService.getJX7(year,"辖区“四上”企业数量");
            JSONArray data = (JSONArray) jsonModel.getObj();
            JSONObject result = getOneToPie(columns, data, needList, keyAndNameMap);

            result.put("unit","个");

            return new JsonModel(true,result);
        }
    }

    /**
     * 10、浆洗街——经济——企业纳税规模分布 ——饼图
     * @param year 年份 如果年份为空，则返回年份下拉列表,不为空则走正常流程
     * @param param 参数
     * @return
     */
    @GetMapping("/getJX10")
    public JsonModel getJX10(@RequestParam(required = false) String year, @RequestParam(required = false) String param){
        if(Strings.isNullOrEmpty(year)){
            return jxStreetService.getJX7(year,"企业纳税规模分布");
        }else {

            JSONArray columns = new JSONArray();
            columns.add("行业");
            columns.add("数量");

            LinkedHashMap<String, String> keyAndNameMap = new LinkedHashMap<>();
            ////"more_than_100_million": "8", //辖区企业纳税规模1亿以上数量
            //            //"10_million_to_100_million": "77", //辖区企业纳税规模1千万~1亿数量
            //            //"3_million_to_10_million": "103", //辖区企业纳税规模300万~1千万数量
            //            //"1_million_to_3_million": "282", //辖区企业纳税规模100万~300万数量
            //            //"below_1_million": "9753", //辖区企业纳税规模100万以下数量

            keyAndNameMap.put("more_than_100_million", "1亿以上");
            keyAndNameMap.put("10_million_to_100_million", "1千万~1亿");
            keyAndNameMap.put("3_million_to_10_million", "300万~1千万");
            keyAndNameMap.put("1_million_to_3_million", "100万~300万");
            keyAndNameMap.put("below_1_million", "100万以下");

            List<String> needList = Arrays.asList("more_than_100_million", "10_million_to_100_million","3_million_to_10_million","1_million_to_3_million","below_1_million");

            JsonModel jsonModel = jxStreetService.getJX7(year,"企业纳税规模分布");
            JSONArray data = (JSONArray) jsonModel.getObj();
            JSONObject result = getOneToPie(columns, data, needList, keyAndNameMap);

//            result.put("unit","个");

            return new JsonModel(true,result);
        }
    }

    /**
     * 8、浆洗街——经济——重大项目情况 ——文本框
     * @param year 年份 如果年份为空，则返回年份下拉列表,不为空则走正常流程
     * @param param 参数
     * @return
     */
    @GetMapping("/getJX11")
    public JsonModel getJX11(@RequestParam(required = false) String year, @RequestParam(required = false) String param){
        JsonModel jsonModel = new JsonModel();
        if(Strings.isNullOrEmpty(year)){
            jsonModel = jxStreetService.getJX7(year,"重大项目情况");
            return jsonModel;
        }else {
            jsonModel = jxStreetService.getJX7(year,"重大项目情况");
            JSONObject obj = (JSONObject) jsonModel.getObj();
            JsonModel jsonModel2 = wuHouService.getPieToText(param, obj);
            return jsonModel2;
        }
    }

    /**
     * 9、浆洗街——经济——辖区企业总纳税额（万元）
     * @return
     */
    @GetMapping("/getJX12")
    public JsonModel getJX12(){
        return jxStreetService.getJX8();
    }

}
