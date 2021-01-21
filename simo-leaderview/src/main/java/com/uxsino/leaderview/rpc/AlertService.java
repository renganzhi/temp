package com.uxsino.leaderview.rpc;

import com.alibaba.fastjson.JSONArray;
import com.uxsino.commons.model.AlertType;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.alert.AlertLevelQuery;
import com.uxsino.leaderview.model.alert.AlertQuery;
import com.uxsino.leaderview.rpc.hystrix.AlertClinetHystrixLeaderview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


/**
 * @description 调用SIMO-Alert的服务类
 * @date 2021年01月19日
 */
@FeignClient(value = "${service-alert}", fallback = AlertClinetHystrixLeaderview.class)
public interface AlertService {

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/countAlert", consumes = "application/json")
    JsonModel countAlert(@RequestHeader("Cookie") String cookie, @RequestParam("alertType") String alertType,
                         @RequestParam("alertLevel") String alertLevel);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/getOtherAlertInfo", consumes = "application/json")
    JsonModel getOtherAlertInfo(@RequestHeader("Cookie") String cookie, @RequestParam("type") String type);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/getOtherAlertTable", consumes = "application/json")
    JsonModel getOtherAlertTable(@RequestHeader("Cookie") String cookie, @RequestParam("type") String type, @RequestParam("number") Long number);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/getAlertCount", consumes = "application/json")
    JsonModel getAlertCount(@RequestBody AlertQuery query, @RequestParam("alertType") AlertType alertType);


    @RequestMapping(method = RequestMethod.POST, value = "/homeData/findAlertList", consumes = "application/json")
    JsonModel findAlertList(@RequestBody AlertLevelQuery query);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/findAlert", consumes = "application/json")
    JsonModel findAlert(@RequestBody AlertQuery query, @RequestParam("orderBy") String orderBy);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/getLevel", consumes = "application/json")
    JsonModel getLevel(@RequestParam("level") Integer level);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/findByChooseForLeaderview", consumes = "application/json")
    JsonModel findByChooseForLeaderview(@RequestParam("neIds") String[] neIds, @RequestParam("number") Long number);

    @RequestMapping(method = RequestMethod.GET, value = "/homeData/getStatByLevel", consumes = "application/json")
    JsonModel getStatByLevel(@RequestParam("arr") ArrayList arr,
                       @RequestParam("levels") String levels,
                       @RequestParam("alertType") AlertType alertType);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/getStatByClass", consumes = "application/json")
    JsonModel getStatByClass(@RequestBody JSONArray neArray, @RequestParam("baseClass") String baseClass,
                             @RequestParam("levels") String levels, @RequestParam("alertType") AlertType alertType,
                             @RequestParam("neClassStr") String neClassStr, @RequestParam("statisticsByNe") boolean statisticsByNe);

    @RequestMapping(method = RequestMethod.POST, value = "/homeData/getStatByNe", consumes = "application/json")
    JsonModel getStatByNe(@RequestBody JSONArray neArray, @RequestParam("levels") String levels,
                          @RequestParam("alertType") AlertType alertType);

}
