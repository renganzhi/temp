package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.leaderview.model.datacenter.IndicatorValueCriteria;
import com.uxsino.leaderview.model.monitor.*;
import com.uxsino.leaderview.rpc.DatacenterService;
import com.uxsino.leaderview.rpc.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import java.util.*;

@Component
@Slf4j
public class RpcProcessService {

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private IndicatorService indicatorService;

    @SuppressWarnings("unchecked")
    public List<NetworkEntity> findAllNetworkEntity(NetworkEntityQO entityQO) throws Exception{
        JsonModel neJsonModel = monitorService.findAllNetworkEntity(entityQO);
        if (!neJsonModel.isSuccess()){
            throw new Exception(neJsonModel.getMsg());
        }
        return this.toJavaBeanList(neJsonModel, NetworkEntity.class);
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
        return Long.valueOf(jsonModel.getObj().toString());
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
        return (JSONObject) jsonModel.getObj();
    }


    @SuppressWarnings("unchecked")
    public <T> List<T> toJavaBeanList(JsonModel jsonModel, Class<T> clazz){
        List<LinkedHashMap> list = (List<LinkedHashMap>) jsonModel.getObj();
        List<T> ts = Lists.newArrayList();
        for (LinkedHashMap map: list) {
            T t = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)),clazz);
            ts.add(t);
        }
        return ts;
    }

    public <T> T toJavaBean(JsonModel jsonModel, Class<T> clazz){
        LinkedHashMap map = (LinkedHashMap) jsonModel.getObj();
        return JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)),clazz);
    }



}
