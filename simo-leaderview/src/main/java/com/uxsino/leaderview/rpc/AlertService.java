package com.uxsino.leaderview.rpc;

import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.alert.AlertQuery;
import com.uxsino.leaderview.rpc.hystrix.AlertClinetHystrixLeaderview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @description 调用SIMO-Alert的服务类
 * @date 2021年01月19日
 */
@FeignClient(value = "${service-alert}", fallback = AlertClinetHystrixLeaderview.class)
//@FeignClient(value = "${service-alert}")
public interface AlertService {

    @RequestMapping(method = RequestMethod.GET, value = "/alertApi/getAlertLevels", consumes = "application/json")
    JsonModel getAlertLevels(@RequestParam Map<String, Object> map);

    @RequestMapping(method = RequestMethod.POST, value = "/alertApi/getAlertRecord", consumes = "application/json")
    JsonModel getAlertRecord(@RequestBody AlertQuery alertQuery, @RequestParam("orderBy") Map<String, String> orderBy);

    @RequestMapping(method = RequestMethod.POST, value = "/alertApi/getAlertCount", consumes = "application/json")
    JsonModel getAlertCount(@RequestBody Map<String, Object> map);

    @RequestMapping(method = RequestMethod.POST, value = "/alertApi/getObjectIdsByAlertType", consumes = "application/json")
    JsonModel getObjectIdsByAlertType(@RequestHeader("Cookie") String cookie, @RequestBody AlertQuery query);

    @RequestMapping(method = RequestMethod.POST, value = "/alertApi/getLevelStatisticsResult", consumes = "application/json")
    JsonModel getLevelStatisticsResult(@RequestBody Map<String, Object> map);

    //TODO 还没开发出来的接口
    @RequestMapping(method = RequestMethod.GET, value = "/homeData/findByChooseForLeaderview", consumes = "application/json")
    JsonModel findByChooseForLeaderview(@RequestParam("neIds") String[] neIds, @RequestParam("number") Long number);



}
