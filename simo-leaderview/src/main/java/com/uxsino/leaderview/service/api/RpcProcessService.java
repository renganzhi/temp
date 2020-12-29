package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.leaderview.model.monitor.IndicatorTable;
import com.uxsino.leaderview.model.monitor.NetworkEntity;
import com.uxsino.leaderview.model.monitor.NetworkEntityQO;
import com.uxsino.leaderview.rpc.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        return this.toJavaBean(neJsonModel, NetworkEntity.class);
    }

    public List<NetworkEntity> findNetworkEntityByIdIn(String[] ids) throws Exception{
        JsonModel neJsonModel = monitorService.findNetworkEntityByIdIn(ids);
        if (!neJsonModel.isSuccess()){
            throw new Exception(neJsonModel.getMsg());
        }
        return this.toJavaBean(neJsonModel, NetworkEntity.class);
    }

    public List<IndicatorTable> findUsableIndForNe(List<NetworkEntity> neList, List<NeClass> neClasses) throws Exception{
        Map<String,List> map = Maps.newHashMap();
        map.put("NetworkEntity",neList);
        map.put("NeClass",neClasses);
        JsonModel indJsonModel = monitorService.findUsableIndForNe(map);
        if (!indJsonModel.isSuccess()){
            throw new Exception(indJsonModel.getMsg());
        }
        return this.toJavaBean(indJsonModel, IndicatorTable.class);
    }

    public List<IndicatorTable> findIndByNeClass(List<String> neClasses) throws Exception{
        JsonModel indJsonModel = monitorService.findIndByNeClass(neClasses);
        if (!indJsonModel.isSuccess()){
            throw new Exception(indJsonModel.getMsg());
        }
        return this.toJavaBean(indJsonModel, IndicatorTable.class);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> toJavaBean(JsonModel jsonModel, Class<T> clazz){
        List<LinkedHashMap> list = (List<LinkedHashMap>) jsonModel.getObj();
        List<T> ts = Lists.newArrayList();
        for (LinkedHashMap map: list) {
            T t = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)),clazz);
            ts.add(t);
        }
        return ts;
    }

}
