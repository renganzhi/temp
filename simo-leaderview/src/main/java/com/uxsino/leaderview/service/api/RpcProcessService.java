package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.model.AlertType;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.leaderview.model.alert.*;
import com.uxsino.leaderview.model.monitor.*;
import com.uxsino.leaderview.rpc.AlertService;
import com.uxsino.leaderview.rpc.MonitorService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @description 对服务间调用的返回数据进行成功判断以及处理
 * @date 2021-01-19
 */
@Component
@Slf4j
public class RpcProcessService {

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private AlertService alertService;

    @SuppressWarnings("unchecked")
    public List<NetworkEntity> findAllNetworkEntity(NetworkEntityQO entityQO) throws Exception{
        JsonModel neJsonModel = monitorService.findAllNetworkEntity(entityQO);
        if (!neJsonModel.isSuccess()){
            throw new Exception(neJsonModel.getMsg());
        }
        return this.toJavaBeanList(neJsonModel, NetworkEntity.class);
    }

    public NetworkEntity findNetworkEntityById(String ids) throws Exception{
        return findNetworkEntityByIdIn(ids);
    }

    public NetworkEntity findNetworkEntityByIdIn(String ids) throws Exception{
        List<NetworkEntity> list = findNetworkEntityByIdIn(Lists.newArrayList(ids));
        if (list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    public List<NetworkEntity> findNetworkEntityByIdIn(List<String > ids) throws Exception{
        return findNetworkEntityByIdIn(ids.toArray(new String[ids.size()]));
    }

    public List<NetworkEntity> findNetworkEntityByIdIn(String[] ids) throws Exception{
        JsonModel neJsonModel = monitorService.findNetworkEntityByIdIn(ids);
        if (!neJsonModel.isSuccess()){
            throw new Exception(neJsonModel.getMsg());
        }
        return this.toJavaBeanList(neJsonModel, NetworkEntity.class);
    }

    public List<IndicatorTable> findUsableIndForNe(List<NetworkEntity> neList, List<NeClass> neClasses) throws Exception{
        Map<String,List> map = Maps.newHashMap();
        map.put("NetworkEntity",neList);
        map.put("NeClass",neClasses);
        JsonModel indJsonModel = monitorService.findUsableIndForNe(map);
        if (!indJsonModel.isSuccess()){
            throw new Exception(indJsonModel.getMsg());
        }
        return this.toJavaBeanList(indJsonModel, IndicatorTable.class);
    }

    public List<IndicatorTable> findIndByNeClass(List<String> neClasses) throws Exception{
        JsonModel indJsonModel = monitorService.findIndByNeClass(neClasses);
        if (!indJsonModel.isSuccess()){
            throw new Exception(indJsonModel.getMsg());
        }
        return this.toJavaBeanList(indJsonModel, IndicatorTable.class);
    }

    public IndicatorTable getIndicatorInfoByName(String indicator) throws Exception{
        JsonModel indJsonModel = monitorService.getIndicatorInfoByName(indicator);
        if (!indJsonModel.isSuccess()){
            throw new Exception(indJsonModel.getMsg());
        }
        return this.toJavaBean(indJsonModel, IndicatorTable.class);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findNeComps(List<String> neIds, String indicatorName, String componentName,
                                                String neName, List<String> neCompIdNotIn, String... keyword) throws Exception{
        JsonModel jsonModel = monitorService.findNeComps(neIds, indicatorName, componentName, neName, neCompIdNotIn, keyword);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return (List<Map<String,Object>>) jsonModel.getObj();
    }

    @SuppressWarnings("unchecked")
    public List<String> getNeIdsByDomainIds(Long[] domains, HttpSession session) throws Exception{
        JsonModel jsonModel = monitorService.getNeIdsByDomainIds(domains, "SESSION=" + session.getId());
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return (List<String>) jsonModel.getObj();
    }

    @SuppressWarnings("unchecked")
    public PageModel findPage(PageModel temPage, NetworkLinkModel networkLinkModel) throws Exception{
        JsonModel jsonModel = monitorService.findPage(JSON.toJSONString(temPage), networkLinkModel);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return this.toJavaBean(jsonModel, PageModel.class);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> vmStatics(Long domain) throws Exception{
        JsonModel jsonModel = monitorService.vmStatics(domain);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return (List<Map<String, Object>>) jsonModel.getObj();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> neStatistics(Long domain, BaseNeClass baseClass) throws Exception{
        JsonModel jsonModel = monitorService.neStatistics(domain, baseClass);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return (List<Map<String, Object>>) jsonModel.getObj();
    }

    @SuppressWarnings("unchecked")
    public Long countVr(Long domainId) throws Exception{
        JsonModel jsonModel = monitorService.countVr(domainId);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return getLongValue(jsonModel);
    }

    @SuppressWarnings("unchecked")
    public List<ArrayList> neStatusStatistics(List<Long> domainId, BaseNeClass baseNeClass) throws Exception{
        JsonModel jsonModel = monitorService.neStatusStatistics(domainId, baseNeClass);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return (List<ArrayList> ) jsonModel.getObj();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findByIdsAndBaseNe(List<Long> domainList, String neIds, BaseNeClass baseNeClass) throws Exception{
        JsonModel jsonModel = monitorService.findByIdsAndBaseNe(domainList, neIds, baseNeClass);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return (List<Map<String,Object>>) jsonModel.getObj();
    }


    public NeHealth getNeHealth(String neId) throws Exception{
        JsonModel jsonModel = monitorService.getNeHealth(neId);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return this.toJavaBean(jsonModel, NeHealth.class);
    }

    public JSONObject getStrategy(String neId, String indicatorNames) throws Exception{
        JsonModel jsonModel = monitorService.getStrategy(neId, indicatorNames);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return JSON.parseObject(JSON.toJSONString(jsonModel.getObj()));
    }

    public List<NeHealthHistory> findHealthByNeIdIn(List<String> neIdIn) throws Exception{
        JsonModel jsonModel = monitorService.findHealthByNeIdIn(neIdIn);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, NeHealthHistory.class);
    }

    public List<NeHealth> findNeHealthOrderByHealthy(String neIds, String order) throws Exception{
        JsonModel indJsonModel = monitorService.findNeHealthOrderByHealthy(neIds, order);
        if (!indJsonModel.isSuccess()){
            throw new Exception(indJsonModel.getMsg());
        }
        return this.toJavaBeanList(indJsonModel, NeHealth.class);
    }


    public JSONObject getTopNByItObjects(String indicator, String itObjectIds, String topStr, String field,
                                         JSONArray window, String order) throws Exception{
        String windowStr = ObjectUtils.isEmpty(window)? null : window.toJSONString();
        JsonModel jsonModel = monitorService.getTopNByItObjects(indicator, itObjectIds, topStr, field, windowStr, order);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return JSON.parseObject(JSON.toJSONString(jsonModel.getObj()));
    }

    public JSONArray getFieldLables(String indicatorID) throws Exception{
        JsonModel jsonModel = monitorService.getFieldLables(indicatorID);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return JSON.parseArray(JSON.toJSONString(jsonModel.getObj()));
    }

    public IndicatorVal findLastObject(String neId, String indicatorName, JSONObject params, String fetchDate) throws Exception{
        String paramStr = ObjectUtils.isEmpty(params)? null : params.toJSONString();
        JsonModel jsonModel = monitorService.findLastObject(neId, indicatorName, paramStr, fetchDate);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return this.toJavaBean(jsonModel, IndicatorVal1.class);
    }

    public JsonModel countAlert(HttpSession session, String alertType, String alertLevel) throws Exception{
        JsonModel jsonModel = alertService.countAlert("SESSION=" + session.getId(), alertType, alertLevel);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel;
    }

    public JsonModel getOtherAlertInfo(HttpSession session, String type) throws Exception{
        JsonModel jsonModel = alertService.getOtherAlertInfo("SESSION=" + session.getId(), type);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel;
    }

    public JsonModel getOtherAlertTable(HttpSession session, String type, Long number) throws Exception{
        JsonModel jsonModel = alertService.getOtherAlertTable("SESSION=" + session.getId(), type, number);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel;
    }

    public long getAlertCount(AlertQuery query, AlertType alert) throws Exception{
        JsonModel jsonModel = alertService.getAlertCount(query, alert);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return getLongValue(jsonModel);
    }

    public List<AlertLevel> findAlertList(AlertLevelQuery alertLevelQuery) throws Exception{
        JsonModel jsonModel = alertService.findAlertList(alertLevelQuery);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, AlertLevel.class);
    }

    public List<AlertRecord> findAlert(AlertQuery query, Map<String, String> orderBy) throws Exception{
        JsonModel jsonModel = alertService.findAlert(query, JSON.toJSONString(orderBy));
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, AlertRecord.class);
    }

    public List<Alert> findByChooseForLeaderview(String[] neIds, Long number) throws Exception{
        JsonModel jsonModel = alertService.findByChooseForLeaderview(neIds, number);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, Alert.class);
    }

    public String getLevel(Integer level) throws Exception{
        JsonModel jsonModel = alertService.getLevel(level);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel.getMsg();
    }

    public JSONObject getStatByLevel(ArrayList arr, String alertLevel, AlertType alert) throws Exception{
        JsonModel jsonModel = alertService.getStatByLevel(arr, alertLevel, alert);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return JSON.parseObject(JSON.toJSONString(jsonModel.getObj()));
    }

    public JSONObject getStatByClass(JSONArray neArray, String baseClass, String levels, AlertType alertType,
                                     String neClassStr, boolean statisticsByNe) throws Exception{
        JsonModel jsonModel = alertService.getStatByClass(neArray, baseClass, levels, alertType, neClassStr, statisticsByNe);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return JSON.parseObject(JSON.toJSONString(jsonModel.getObj()));
    }

    public JSONObject getStatByNe(JSONArray neArray, String alertLevel, AlertType alert) throws Exception{
        JsonModel jsonModel = alertService.getStatByNe(neArray, alertLevel, alert);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return JSON.parseObject(JSON.toJSONString(jsonModel.getObj()));
    }

    @Data
    private static class IndicatorVal1 extends IndicatorVal{
        //IndicatorVal中的indicatorValue是Object类型，通过转换时，会获取不到值，这里使用
        private JSON indicatorValue;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> toJavaBeanList(JsonModel jsonModel, Class<T> clazz){
        List<LinkedHashMap> list = (List<LinkedHashMap>) jsonModel.getObj();
        List<T> ts = Lists.newArrayList();
        for (LinkedHashMap map: list) {
            T t = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)),clazz);
            ts.add(t);
        }
        return ts;
    }

    private <T> T toJavaBean(JsonModel jsonModel, Class<T> clazz){
        LinkedHashMap map = (LinkedHashMap) jsonModel.getObj();
        return JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)),clazz);
    }

    private Long getLongValue(JsonModel jsonModel) throws Exception{
        Object obj = jsonModel.getObj();
        if (obj instanceof Integer || obj instanceof Long){
            return Long.valueOf(obj.toString());
        }else {
            throw new Exception("数据转换出错");
        }
    }

}
