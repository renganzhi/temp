package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.leaderview.model.monitor.IndicatorTable;
import com.uxsino.leaderview.model.monitor.NetworkEntity;
import com.uxsino.leaderview.model.monitor.NetworkEntityQO;
import com.uxsino.leaderview.model.monitor.NetworkLinkModel;
import com.uxsino.leaderview.rpc.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import java.util.*;

@Component
public class RpcProcessService {

    @Autowired
    private MonitorService monitorService;

    @SuppressWarnings("unchecked")
    public List<NetworkEntity> findAllNetworkEntity(NetworkEntityQO entityQO) throws Exception{
        JsonModel neJsonModel = monitorService.findAllNetworkEntity(entityQO);
        if (!neJsonModel.isSuccess()){
            throw new Exception(neJsonModel.getMsg());
        }
        return this.toJavaBeanList(neJsonModel, NetworkEntity.class);
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
