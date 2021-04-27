package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.leaderview.model.datacenter.IndicatorValueCriteria;
import com.uxsino.leaderview.model.monitor.*;
import com.uxsino.leaderview.rpc.DatacenterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    private IndValue getHealthIndicator(String neId) throws Exception{
        IndValue indValue = new IndValue();
        NeHealth neHealth = rpcProcessService.findNeHealth(neId);
        if (ObjectUtils.isEmpty(neHealth)) {
            return null;
        }
        JSONObject obj = new JSONObject();
        obj.put("healthy", neHealth.getHealth());
        indValue.setIndicatorValue(obj);
        return indValue;
    }




    public IndValue findIndValue(String neId, String indicatorName, String fetchDate) throws Exception{
        // 健康度指标特殊处理
        if ("healthy".equals(indicatorName)) {
            return getHealthIndicator(neId);
        }
        if (StringUtils.isEmpty(neId) || StringUtils.isEmpty(indicatorName)) {
            return null;
        }
        return getIndValueByIdInd(neId, indicatorName);
    }

    public IndValue getIndValueByIdInd(String neId, String indicatorName) throws Exception{
        com.uxsino.commons.db.criteria.IndicatorValueCriteria qo = new com.uxsino.commons.db.criteria.IndicatorValueCriteria();
        qo.setNeId(neId);
        qo.setIndicatorName(Lists.newArrayList(indicatorName));
        List<IndValue> indValues = rpcProcessService.getCurIndValues(qo);
        if (ObjectUtils.isEmpty(indValues)){
            return null;
        }
        JSON indicatorValue = indValues.get(0).getIndicatorValue();
        if (ObjectUtils.isEmpty(indicatorValue)){
            return null;
        }
        return indValues.get(0);
    }

    /**
     * 以下与数据中心对接的接口全部调整成从monitor中获取数据，当前还剩余历史指标值部分接口未完成调整工作
     */

    //    public List<JSONObject> findIndicatorStatus(String neId) throws Exception{
//        List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(Lists.newArrayList(neId));
//        NetworkEntityCriteria networkEntityCriteria = new NetworkEntityCriteria();
//        networkEntityCriteria.setId(neId);
//        networkEntityCriteria.setNeClass(nes.get(0).getNeClass());
//        List<IndicatorTable> indicatorTable = rpcProcessService.getUsableInd(null, networkEntityCriteria);
//        List<String> indNames = indicatorTable.stream().map(ind -> ind.getName()).collect(Collectors.toList());
//        IndicatorValueCriteria criteria = new IndicatorValueCriteria();
//        JSONObject must = new JSONObject();
//        must.put("ne", neId);
//        criteria.setIndicatorName(indNames);
//        criteria.setMust(must);
//        criteria.setPagination(false);
//        criteria = searchIndicatorValue(criteria);
//        Object InVobj = criteria.getObject();
//        JSONArray valueArray = InVobj == null ? new JSONArray() : JSON.parseArray(JSON.toJSONString(InVobj));
//        List<JSONObject> jsonObjects = new ArrayList<>();
//        if (valueArray != null && !valueArray.isEmpty()) {
//            valueArray.forEach(val -> {
//                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(val));
//                String indicator_name = jsonObject.getString("ind");
//                String tm = jsonObject.getString("tm");
//                if (!jsonObject.containsKey("run_status")) {
//                    jsonObject.put("run_status", "Good");
//                }
//                String run_status = jsonObject.getString("run_status");
//                indicatorTable.stream().filter(inT -> inT.getName().equals(indicator_name)).map(inT -> {
//                    JSONObject objects = new JSONObject();
//                    objects.put("label", inT.getLabel());
//                    objects.put("fetch_date", tm);
//                    objects.put("run_status", run_status);
//                    objects.put("name", inT.getName());
//                    objects.put("neids", inT.getNeIds());
//                    jsonObjects.add(objects);
//                    return objects;
//                }).collect(Collectors.toList());
//            });
//        }
//        return jsonObjects;
//
//    }

//    public IndicatorVal findValueByNeIdAndIndicator1(String neId, String indicatorName, String fetchDate) throws Exception{
//        // 健康度指标特殊处理
//        if ("healthy".equals(indicatorName)) {
//            return getHealthIndicator(neId);
//        }
//        if (StringUtils.isEmpty(neId) || StringUtils.isEmpty(indicatorName)) {
//            return null;
//        }
//        IndicatorVal indicatorValue = getIndicatorValue(neId, indicatorName, fetchDate);
//        return indicatorValue;
//    }

    // 从数据中心获取指标数据
//    public IndicatorVal getIndicatorValue(String neId, String indicatorName, String fetchDate) {
//        IndicatorValueCriteria criteria = new IndicatorValueCriteria();
//        JSONObject must = new JSONObject();
//        must.put("ne", neId);
//        criteria.setIndicatorName(Lists.newArrayList(indicatorName));
//        criteria.setMust(must);
//        criteria.setPageSize(1);
//        if (StringUtils.isNotBlank(fetchDate)) {
//            SimpleDateFormat sdf = new SimpleDateFormat(sdfStr);
//            try {
//                JSONObject scope = new JSONObject();
//                // 取历史表最晚的时间 及最新的时间
//                scope.put("lte", sdf.parse(fetchDate));
//                must.put("tm", scope);
//                JSONObject sort = new JSONObject();
//                sort.put("tm", "desc");
//                criteria.setSort(sort);
//                criteria = searchHistoryIndicatorValue(criteria);
//            } catch (ParseException e) {
//                log.error("日期格式转换错误，待转换日期：{}", fetchDate, e);
//            }
//        } else {
//            criteria = searchIndicatorValue(criteria);
//        }
//        if (criteria == null || criteria.getObject() == null) {
//            return null;
//        }
//        JSONArray valueArray = JSON.parseArray(JSON.toJSONString(criteria.getObject()));
//        List<IndicatorVal> list = valueArray == null ? null
//                : JSONObject.parseArray(valueArray.toJSONString(), IndicatorVal.class);
//        if (valueArray != null && !valueArray.isEmpty() && list.get(0) != null) {
//            return list.get(0);
//        } else {
//            return null;
//        }
//    }

//    /**
//     * 访问数据中心获取当前指标数据
//     *
//     * @param criteria
//     * @return
//     */
//    public IndicatorValueCriteria searchIndicatorValue(IndicatorValueCriteria criteria) {
//        String param = JSON.toJSONString(criteria);
//        JsonModel result = datacenterService.searchIndicatorValue(param);
//        if (result != null && result.isSuccess()) {
//            if (result.getObj() != null) {
//                if (criteria.isPagination()) {
//                    criteria = JSON.parseObject(JSON.toJSONString(result.getObj()), IndicatorValueCriteria.class);
//                } else {
//                    criteria.setObject(JSONObject.parseObject(JSON.toJSONString(result.getObj())).get("object"));
//                }
//            }
//        } else {
//            log.info("跨组件请求数据中心接口报错：参数：{}, 返回：{}", param, result);
//        }
//        return criteria;
//    }

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
            log.error("跨组件请求数据中心接口报错：参数：{}, 返回：{}", param, result);
        }
        return criteria;
    }

    public IndicatorValueCriteria searchHistoryRecords(IndicatorValueCriteria criteria) {
        // 健康度历史值获取
        if ("healthy".equals(criteria.getIndicatorId())) {
            List<NeHealthHistory> healthList = Lists.newArrayList();
            try {
                 healthList = rpcProcessService.findHealthByNeIdIn(Lists.newArrayList(criteria.getNeId()));
            }catch (Exception e){
                e.printStackTrace();
            }
            List<IndicatorVal> object = Lists.newArrayList();
            healthList.forEach(ne -> {
                JSONObject obj = new JSONObject();
                obj.put("result", ne.getHealth());
                IndicatorVal value = new IndicatorVal();
                value.setId(ne.getId());
                value.setInd("healthy");
                value.setNeId(ne.getNeId());
                value.setTm(ne.getUpdateDate());
                value.setIndicatorValue(obj);
                object.add(value);
            });
            criteria.setObject(object);
            return criteria;
        }
        JSONObject must = criteria.getMust();
        if (StringUtils.isNotBlank(criteria.getIdentifier())) {
            must.put(getSearchFieldKey("identifier"), criteria.getIdentifier());
            criteria.setSourceType("include");
            criteria.setSourceField(Arrays.asList("tm,v".split(",")));
        }
        return searchHistoryIndicatorValue(criteria);
    }

    public String getSearchFieldKey(String field) {
        return StringUtils.isNotBlank(field) ? "v." + field : "v";
    }

}
