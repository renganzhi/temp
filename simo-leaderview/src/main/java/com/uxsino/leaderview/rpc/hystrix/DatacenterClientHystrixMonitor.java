package com.uxsino.leaderview.rpc.hystrix;

import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.datacenter.IndValueQuery;
import com.uxsino.leaderview.rpc.DatacenterService;
import org.springframework.stereotype.Component;

@Component("leaderview-datacenterclient-hystrix-leaderview")
public class DatacenterClientHystrixMonitor implements DatacenterService {

    @Override
    public JsonModel searchIndicatorValue(String params) {
        JsonModel result = new JsonModel();
        result.setMsg("查询当前指标失败，数据中心发生故障，请确认是否启动！");
        return result;
    }

    @Override
    public JsonModel searchHistoryIndicatorValue(String params) {
        JsonModel result = new JsonModel();
        result.setMsg("查询历史指标失败，数据中心发生故障，请确认是否启动！");
        return result;
    }

    @Override
    public JsonModel searchCurrentIndicatorNames(String data_source) {
        JsonModel result = new JsonModel();
        result.setMsg("查询当前所有指标名称失败，数据中心发生故障，请确认是否启动！");
        return result;
    }

    @Override
    public JsonModel searchAggregationIndicatorValue(String params) {
        JsonModel result = new JsonModel();
        result.setMsg("查询实时指标聚合数据失败，数据中心发生故障，请确认是否启动！");
        return result;
    }

    @Override public JsonModel searchHisAggregationIndicatorValue(String params) {
        JsonModel result = new JsonModel();
        result.setMsg("请求历史指标聚合数据失败，数据中心发生故障，请确认是否启动！");
        return result;
    }

    @Override
    public JsonModel updateIndStatus(String params) {
        JsonModel result = new JsonModel();
        result.setMsg("请求变更指标状态接口失败，数据中心发生故障，请确认是否启动！");
        return result;
    }

    @Override public JsonModel searchHisAggIndStatisTime(String param) {
        JsonModel result = new JsonModel();
        result.setMsg("请求历史指标聚合最新统计时间接口失败，数据中心发生故障，请确认是否启动！");
        return result;
    }

    @Override
    public JsonModel delIndValueByIndicator(String dataSource, String indicator) {
        JsonModel result = new JsonModel();
        result.setMsg("请求删除指定指标历史数据接口失败，数据中心发生故障，请确认是否启动！");
        return result;
    }

    @Override
    public JsonModel delIndValueByParam(String params) {
        JsonModel result = new JsonModel();
        result.setMsg("请求删除指定条件历史指标数据接口失败，数据中心发生故障，请确认是否启动！");
        return result;
    }

    @Override
    public JsonModel searchByFieldQuery(String type, boolean isHistory, IndValueQuery indValueQuery) {
        return new JsonModel(false, "DMC服务调用失败");
    }

}
