package com.uxsino.leaderview.rpc.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.db.model.network.NeComponentQuery;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.monitor.IndicatorValueQO;
import com.uxsino.leaderview.model.monitor.NetworkEntityCriteria;
import com.uxsino.leaderview.model.monitor.NetworkEntityQO;
import com.uxsino.leaderview.model.monitor.NetworkLinkModel;
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
    public JsonModel getNeList(String param) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel getUsableInd(Map<String, Object> map) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override public JsonModel getCurIndValues(String param) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override public JsonModel getHisIndValues(String param) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel findNeComps(Map<String, Object> map) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel findNeLinks(Map<String, Object> map) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel findNeHealth(List<String> neIds, boolean isHistory, String order) {
        return null;
    }

    @Override
    public JsonModel FindFieldIsMonitoringInStrategy(String neId, String indicatorNames) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel getIndAggValues(IndicatorValueQO indicatorValueQO) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel getTopoData(String topoId, String mapLocationId, Long userId) {
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel getMapNodesAndLinks(String userId, String mapLocationId){
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel getMapLocationTree(String topoId){
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel searchNe(JSONObject jsonObject){
        return new JsonModel(false, "Monitor服务调用失败");
    }

    @Override
    public JsonModel getChannelList(String neId){
        return new JsonModel(false, "Monitor服务调用失败");
    }
}
