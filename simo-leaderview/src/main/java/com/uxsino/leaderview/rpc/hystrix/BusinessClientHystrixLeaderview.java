package com.uxsino.leaderview.rpc.hystrix;

import com.uxsino.commons.model.DataSourceType;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.business.BnsIndValQuery;
import com.uxsino.leaderview.model.business.BusinessQuery;
import com.uxsino.leaderview.model.business.BusinessSystem;
import com.uxsino.leaderview.model.monitor.IndicatorValueQO;
import com.uxsino.leaderview.rpc.BusinessService;
import com.uxsino.leaderview.rpc.MonitorService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("leaderview-BusinessClient-hystrix-leaderview")
public class BusinessClientHystrixLeaderview implements BusinessService {

    @Override
    public JsonModel findBusinessSystems(BusinessQuery businessQuery) {
        return new JsonModel(false, "Business服务调用失败");
    }

    @Override
    public JsonModel checkPermission(BusinessSystem businessSystem, DataSourceType dataSourceType, String cookie) {
        return new JsonModel(false, "Business服务调用失败");
    }

    @Override
    public JsonModel findSortWayByUserId(Long userId) {
        return new JsonModel(false, "Business服务调用失败");
    }

    @Override
    public JsonModel findHisIndValues(BnsIndValQuery bnsIndValQuery, Map<String, Object> map) {
        return new JsonModel(false, "Business服务调用失败");
    }

    @Override
    public JsonModel findBusinessInfoAndCurIndValues(BnsIndValQuery bnsIndValQuery) {
        return new JsonModel(false, "Business服务调用失败");
    }

    @Override
    public JsonModel findBnsInfoBySortWay(BusinessQuery query) {
        return new JsonModel(false, "Business服务调用失败");
    }

    @Override
    public JsonModel calcStatistics(Map<String, Date> bnsCreateDateMap, Date startDate, Date endDate) {
        return new JsonModel(false, "Business服务调用失败");
    }
}
