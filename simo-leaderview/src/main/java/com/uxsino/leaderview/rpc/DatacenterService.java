package com.uxsino.leaderview.rpc;

import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.datacenter.IndValueQuery;
import com.uxsino.leaderview.rpc.hystrix.DatacenterClientHystrixMonitor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "${service-dmc}", fallback = DatacenterClientHystrixMonitor.class)
public interface DatacenterService {

    @RequestMapping(method = RequestMethod.POST, value = "/indicator/current/values", consumes = "application/json")
    JsonModel searchIndicatorValue(@RequestBody String params);

    @RequestMapping(method = RequestMethod.POST, value = "/indicator/history/values", consumes = "application/json")
    JsonModel searchHistoryIndicatorValue(@RequestBody String params);

    @RequestMapping(method = RequestMethod.GET, value = "/indicator/current/indicators/{data_source}", consumes = "application/json")
    JsonModel searchCurrentIndicatorNames(@PathVariable(value = "data_source") String dataSource);

    @RequestMapping(method = RequestMethod.POST, value = "/indicator/aggregation/current/values", consumes = "application/json")
    JsonModel searchAggregationIndicatorValue(@RequestBody String params);

    @RequestMapping(method = RequestMethod.POST, value = "/indicator/aggregation/history/values", consumes = "application/json")
    JsonModel searchHisAggregationIndicatorValue(@RequestBody String params);

    @RequestMapping(method = RequestMethod.POST, value = "/indicator/change/run-status", consumes = "application/json")
    JsonModel updateIndStatus(@RequestBody String params);

    @RequestMapping(method = RequestMethod.POST, value = "/indicator/aggregation/staticTime/values", consumes = "application/json")
    JsonModel searchHisAggIndStatisTime(String param);

    @RequestMapping(method = RequestMethod.DELETE, value = "/indicator/index/{data_source}/{indicator}", consumes = "application/json")
    JsonModel delIndValueByIndicator(@PathVariable(value = "data_source") String dataSource,
                                     @PathVariable(value = "indicator") String indicator);

    @RequestMapping(method = RequestMethod.DELETE, value = "/indicator/index/by-search", consumes = "application/json")
    JsonModel delIndValueByParam(@RequestBody String params);

    @RequestMapping(method = RequestMethod.POST,value = "/api/indicator/{type}/{isHistory}", consumes = "application/json")
    JsonModel searchByFieldQuery(@PathVariable(value = "type") String type,
                                 @PathVariable(value = "isHistory") boolean isHistory,
                                 @RequestBody IndValueQuery indValueQuery);
}
