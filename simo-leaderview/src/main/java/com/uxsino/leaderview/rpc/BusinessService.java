package com.uxsino.leaderview.rpc;

import com.uxsino.commons.model.DataSourceType;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.business.BnsIndValQuery;
import com.uxsino.leaderview.model.business.BusinessQuery;
import com.uxsino.leaderview.model.business.BusinessSystem;
import com.uxsino.leaderview.rpc.hystrix.BusinessClientHystrixLeaderview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * @description 调用SIMO-Business的服务类
 * @date 2021年03月16日
 */
@FeignClient(value = "${service-business}", fallback = BusinessClientHystrixLeaderview.class , decode404 = true)
//@FeignClient(value = "${service-business}" , decode404 = true)
public interface BusinessService {

    @RequestMapping(method = RequestMethod.POST, value = "/businessApi/findBusinessSystems", consumes = "application/json")
    JsonModel findBusinessSystems(@RequestBody BusinessQuery businessQuery);

    @RequestMapping(method = RequestMethod.POST, value = "/businessApi/checkPermission", consumes = "application/json")
    JsonModel checkPermission(@RequestBody BusinessSystem businessSystem,
                              @RequestParam("dataSourceType") DataSourceType dataSourceType,
                              @RequestHeader("Cookie") String cookie);

    @RequestMapping(method = RequestMethod.GET, value = "/businessApi/findSortWayByUserId", consumes = "application/json")
    JsonModel findSortWayByUserId(@RequestParam("userId") Long userId);

    @RequestMapping(method = RequestMethod.POST, value = "/businessApi/findHisIndValues", consumes = "application/json")
    JsonModel findHisIndValues(@RequestBody BnsIndValQuery bnsIndValQuery, @RequestParam Map<String,Object> map);

    @RequestMapping(method = RequestMethod.POST, value = "/businessApi/findBusinessInfoAndCurIndValues", consumes = "application/json")
    JsonModel findBusinessInfoAndCurIndValues(@RequestBody BnsIndValQuery bnsIndValQuery);

    @RequestMapping(method = RequestMethod.POST, value = "/businessApi/findBnsInfoBySortWay", consumes = "application/json")
    JsonModel findBnsInfoBySortWay(@RequestBody BusinessQuery query);

    @RequestMapping(method = RequestMethod.POST, value = "/businessApi/calcStatistics", consumes = "application/json")
    JsonModel calcStatistics(@RequestBody Map<String, Date> bnsCreateDateMap,
                             @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate);


}
