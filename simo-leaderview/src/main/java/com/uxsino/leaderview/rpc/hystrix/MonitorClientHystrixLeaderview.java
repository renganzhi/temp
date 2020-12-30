package com.uxsino.leaderview.rpc.hystrix;

import com.alibaba.fastjson.JSON;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.monitor.NetworkEntityQO;
import com.uxsino.leaderview.rpc.MonitorService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("leaderview-MonitorClient-hystrix-leaderview")
public class MonitorClientHystrixLeaderview implements MonitorService {

    @Override
    public JsonModel findAllByDomainIdIn(List<Long> domainIds) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel findAllNetworkEntity(NetworkEntityQO entityQO) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel findNetworkEntityByIdIn(String[] ids) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel findUsableIndForNe(Map<String, List> map) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel findIndByNeClass(List<String> neClasses) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel getIndicatorInfoByName(String indicatorName) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel findNeComps(List<String> neIds, String indicatorName, String componentName, String neName, List<String> neCompIdNotIn, String... keyword) {
        return new JsonModel(false, "Monitor服务调用失败");
    }
}
