package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.datacenter.IndicatorValueCriteria;
import com.uxsino.leaderview.model.monitor.IndicatorTable;
import com.uxsino.leaderview.model.monitor.IndicatorVal;
import com.uxsino.leaderview.model.monitor.NeHealth;
import com.uxsino.leaderview.model.monitor.NetworkEntity;
import com.uxsino.leaderview.rpc.DatacenterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class IndicatorService {

    @Autowired
    RpcProcessService rpcProcessService;

    @Autowired
    DatacenterService datacenterService;

    private static String sdfStr = "yyyy-MM-dd HH:mm:ss";

    public List<JSONObject> findIndicatorStatus(String neId) throws Exception{
        List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(Lists.newArrayList(neId));
        List<IndicatorTable> indicatorTable = rpcProcessService.findUsableIndForNe(nes, Lists.newArrayList(nes.get(0).getNeClass()));
        List<String> indNames = indicatorTable.stream().map(ind -> ind.getName()).collect(Collectors.toList());
        IndicatorValueCriteria criteria = new IndicatorValueCriteria();
        JSONObject must = new JSONObject();
        must.put("ne", neId);
        criteria.setIndicatorName(indNames);
        criteria.setMust(must);
        criteria.setPagination(false);
        criteria = searchIndicatorValue(criteria);
        Object InVobj = criteria.getObject();
        JSONArray valueArray = InVobj == null ? new JSONArray() : JSON.parseArray(JSON.toJSONString(InVobj));
        List<JSONObject> jsonObjects = new ArrayList<>();
        if (valueArray != null && !valueArray.isEmpty()) {
            valueArray.forEach(val -> {
                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(val));
                String indicator_name = jsonObject.getString("ind");
                String tm = jsonObject.getString("tm");
                if (!jsonObject.containsKey("run_status")) {
                    jsonObject.put("run_status", "Good");
                }
                String run_status = jsonObject.getString("run_status");
                indicatorTable.stream().filter(inT -> inT.getName().equals(indicator_name)).map(inT -> {
                    JSONObject objects = new JSONObject();
                    objects.put("label", inT.getLabel());
                    objects.put("fetch_date", tm);
                    objects.put("run_status", run_status);
                    objects.put("name", inT.getName());
                    objects.put("neids", inT.getNeIds());
                    jsonObjects.add(objects);
                    return objects;
                }).collect(Collectors.toList());
            });
        }
        return jsonObjects;

    }

    public IndicatorVal findValueByNeIdAndIndicator1(String neId, String indicatorName, String fetchDate) throws Exception{
        // 健康度指标特殊处理
        if ("healthy".equals(indicatorName)) {
            return getHealthIndicator(neId);
        }
        if (StringUtils.isEmpty(neId) || StringUtils.isEmpty(indicatorName)) {
            return null;
        }
        IndicatorVal indicatorValue = getIndicatorValue(neId, indicatorName, fetchDate);
        return indicatorValue;
    }

    private IndicatorVal getHealthIndicator(String neId) throws Exception{
        IndicatorVal indValue = new IndicatorVal();
        NeHealth neHealth = rpcProcessService.getNeHealth(neId);
        if (org.springframework.util.ObjectUtils.isEmpty(neHealth)) {
            return null;
        }
        JSONObject obj = new JSONObject();
        obj.put("result", neHealth.getHealth());
        indValue.setIndicatorValue(obj);
        return indValue;
    }


    // 从数据中心获取指标数据
    public IndicatorVal getIndicatorValue(String neId, String indicatorName, String fetchDate) {
        IndicatorValueCriteria criteria = new IndicatorValueCriteria();
        JSONObject must = new JSONObject();
        must.put("ne", neId);
        criteria.setIndicatorName(Lists.newArrayList(indicatorName));
        criteria.setMust(must);
        criteria.setPageSize(1);
        if (StringUtils.isNotBlank(fetchDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat(sdfStr);
            try {
                JSONObject scope = new JSONObject();
                // 取历史表最晚的时间 及最新的时间
                scope.put("lte", sdf.parse(fetchDate));
                must.put("tm", scope);
                JSONObject sort = new JSONObject();
                sort.put("tm", "desc");
                criteria.setSort(sort);
                criteria = searchHistoryIndicatorValue(criteria);
            } catch (ParseException e) {
                log.error("日期格式转换错误，待转换日期：{}", fetchDate, e);
            }
        } else {
            criteria = searchIndicatorValue(criteria);
        }
        if (criteria == null || criteria.getObject() == null) {
            return null;
        }
        JSONArray valueArray = JSON.parseArray(JSON.toJSONString(criteria.getObject()));
        List<IndicatorVal> list = valueArray == null ? null
                : JSONObject.parseArray(valueArray.toJSONString(), IndicatorVal.class);
        if (valueArray != null && !valueArray.isEmpty() && list.get(0) != null) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 访问数据中心获取历史指标数据
     *
     * @param criteria
     * @return
     */
    public IndicatorValueCriteria searchHistoryIndicatorValue(IndicatorValueCriteria criteria) {
        String param = JSON.toJSONString(criteria);
        JsonModel result = datacenterService.searchHistoryIndicatorValue(param);
        if (result != null && result.isSuccess()) {
            if (result.getObj() != null) {
                if (criteria.isPagination()) {
                    criteria = JSON.parseObject(JSON.toJSONString(result.getObj()), IndicatorValueCriteria.class);
                } else {
                    criteria.setObject(JSONObject.parseObject(JSON.toJSONString(result.getObj())).get("object"));
                }
            }
        } else {
            log.info("跨组件请求数据中心接口报错：参数：{}, 返回：{}", param, result);
        }
        return criteria;
    }

    /**
     * 访问数据中心获取当前指标数据
     *
     * @param criteria
     * @return
     */
    public IndicatorValueCriteria searchIndicatorValue(IndicatorValueCriteria criteria) {
        String param = JSON.toJSONString(criteria);
        JsonModel result = datacenterService.searchIndicatorValue(param);
        if (result != null && result.isSuccess()) {
            if (result.getObj() != null) {
                if (criteria.isPagination()) {
                    criteria = JSON.parseObject(JSON.toJSONString(result.getObj()), IndicatorValueCriteria.class);
                } else {
                    criteria.setObject(JSONObject.parseObject(JSON.toJSONString(result.getObj())).get("object"));
                }
            }
        } else {
            log.info("跨组件请求数据中心接口报错：参数：{}, 返回：{}", param, result);
        }
        return criteria;
    }

}
