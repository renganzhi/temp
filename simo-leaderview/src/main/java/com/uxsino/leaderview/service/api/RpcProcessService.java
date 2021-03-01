package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.authority.lib.util.DomainUtils;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.db.model.network.NeComponentQuery;
import com.uxsino.commons.model.AlertType;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.leaderview.model.alert.*;
import com.uxsino.leaderview.model.monitor.*;
import com.uxsino.leaderview.rpc.AlertService;
import com.uxsino.leaderview.rpc.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private DomainUtils domainUtils;


//    @SuppressWarnings("unchecked")
//    public List<Map<String,Object>> findNeComps(List<String> neIds, String indicatorName, String componentName,
//                                                String neName, List<String> neCompIdNotIn, String... keyword) throws Exception{
//        JsonModel jsonModel = monitorService.findNeComps(neIds, indicatorName, componentName, neName, neCompIdNotIn, keyword);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return (List<Map<String,Object>>) jsonModel.getObj();
//    }




//    @SuppressWarnings("unchecked")
//    public List<Map<String, Object>> vmStatics(Long domain) throws Exception{
//        JsonModel jsonModel = monitorService.vmStatics(domain);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return (List<Map<String, Object>>) jsonModel.getObj();
//    }
    //上述方法因为要使用核心接口而变更为下面的实现

    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> vmStatics(Long domain) throws Exception {
        Map<String, Object> condition = new HashMap<>();
        if(domain != null){
            condition.put("domainId", domain);
        }
        List<NeClass> neClasses = BaseNeClass.virtualization.getNeClass();
        condition.put("neClasses", neClasses);
        condition.put("pagination", false);
//        condition.put("sourceManage", false);
        condition.put("manageStatusNotIn", "Delected");
        JsonModel jsonModel = monitorService.getNeList(condition);
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        List<NeHealthHistory> list = new ArrayList<>();
        List<NetworkEntity> rawResult = toJavaBeanList(jsonModel, NetworkEntity.class);
        List<Map<String, Object>> realResult = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(rawResult)) {
            for (NeClass neClass : neClasses) {
                HashMap<String, Object> temp = Maps.newHashMap();
                long neCount;
                // 对esxi主机计数特殊处理
                if (NeClass.esxi.equals(neClass)) {
                    // 统计不是vCenter下属的esxi主机数量，即NetworkEntity中parentId == id
                    neCount = rawResult.stream().filter(e -> neClass.equals(e.getNeClass())
                            && e.getId().equals(e.getParentId())).count();
                } else {
                    neCount = rawResult.stream().filter(e -> neClass.equals(e.getNeClass())).count();
                }
                temp.put("name", neClass);
                temp.put("value", neCount);
                realResult.add(temp);
            }
        }
        return realResult;
    }

//    @SuppressWarnings("unchecked")
//    public List<Map<String, Object>> neStatistics(Long domain, BaseNeClass baseClass) throws Exception{
//        JsonModel jsonModel = monitorService.neStatistics(domain, baseClass);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return (List<Map<String, Object>>) jsonModel.getObj();
//    }

    //上述方法因为要使用核心接口而变更为下面的实现

    public List<Map<String, Object>> neStatistics(Long domain, BaseNeClass baseClass) throws Exception{
        Map<String, Object> condition = new HashMap<>();
        if(domain != null){
            condition.put("domainId", domain);
        }
        condition.put("pagination", false);
//        condition.put("sourceManage", false);
        condition.put("manageStatusNotIn", "Delected");
        JsonModel jsonModel = monitorService.getNeList(condition);
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        List<NetworkEntity> rawResult = toJavaBeanList(jsonModel, NetworkEntity.class);
        List<Map<String, Object>> realResult = new ArrayList<>();
        if(baseClass == null){
            List<BaseNeClass> baseNeClassList = Arrays.asList(BaseNeClass.values());
            Map<String, Object> temp = null;
            for(BaseNeClass baseNeClass: baseNeClassList){
                long count = 0;
                Iterator<NetworkEntity> iterator = rawResult.iterator();
                while(iterator.hasNext()){
                    NetworkEntity networkEntity = iterator.next();
                    if(networkEntity.getBaseNeClass().toString().equals(baseNeClass.toString())){
                        count++;
                        iterator.remove();    //如果已经统计过，则去掉，减少后面循环的次数
                    }
                }
                temp = new HashMap<>();
                temp.put("name", baseNeClass);
                temp.put("value", count);
                realResult.add(temp);
            }
        }else{
            List<NeClass> neClassList = Arrays.asList(NeClass.values());
            Map<String, Object> temp = null;
            for(NeClass neClass: neClassList){
                int count = 0;
                if(!neClass.getBaseNeClass().toString().equals(baseClass.toString())){
                    continue;
                }
                Iterator<NetworkEntity> iterator = rawResult.iterator();
                while(iterator.hasNext()){
                    NetworkEntity networkEntity = iterator.next();
                    if(networkEntity.getNeClass().toString().equals(neClass.toString())){
                        count++;
                        iterator.remove();    //如果已经统计过，则去掉，减少后面循环的次数
                    }
                }
                temp = new HashMap<>();
                temp.put("name", neClass);
                temp.put("value", count);
                realResult.add(temp);
            }
        }
        return realResult;
    }

//    @SuppressWarnings("unchecked")
//    public Long countVr(Long domainId) throws Exception{
//        JsonModel jsonModel = monitorService.countVr(domainId);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return getLongValue(jsonModel);
//    }
    //上述方法因为要使用核心接口而变更为下面的实现

    public Long countVr(Long domainId) throws Exception {
        List<Map<String, Object>> statics = vmStatics(domainId);
        long value = 0L;
        if (CollectionUtils.isNotEmpty(statics)) {
            for (Map<String, Object> map : statics) {
                value = value + Long.parseLong(map.get("value").toString());
            }
        }
        return value;
    }

//    @SuppressWarnings("unchecked")
//    public List<ArrayList> neStatusStatistics(List<Long> domainId, BaseNeClass baseNeClass) throws Exception{
//        JsonModel jsonModel = monitorService.neStatusStatistics(domainId, baseNeClass);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return (List<ArrayList> ) jsonModel.getObj();
//    }
    //上述方法因为要使用核心接口而变更为下面的实现

    public List<ArrayList> neStatusStatistics(List<Long> domainId, BaseNeClass baseNeClass) throws Exception{
        Map<String, Object> condition = new HashMap<>();
        if(domainId != null){
            condition.put("domainId", domainId);
        }
        if(baseNeClass != null){
            condition.put("baseNeClass", baseNeClass);
        }
        condition.put("manageStatusNotIn", "Delected");
        JsonModel jsonModel = monitorService.getNeList(condition);
        List<NetworkEntity> networkEntityList = toJavaBeanList(jsonModel, NetworkEntity.class);
        // 对虚拟化资源进行特殊处理，只统计parentId为空的vmWare,xen，kvm资源和parentId = id 单独发现的esxi资源
        List<NetworkEntity> rawResult = networkEntityList.stream().filter(networkEntity ->
            networkEntity.getParentId() == null
            || networkEntity.getParentId().equals(networkEntity.getId())
        ).collect(Collectors.toList());
        List<ArrayList> realResult = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(rawResult)){
            String[] runStatuses = new String[]{"Unknow", "Loading", "Good", "Warning", "Unconnection"};
            ArrayList<Object> temp = new ArrayList<>();
            for(String runStatus : runStatuses){
                temp = new ArrayList<>();
                long count = 0;
                Iterator<NetworkEntity> iterator = networkEntityList.iterator();
                while(iterator.hasNext()){
                    NetworkEntity networkEntity = iterator.next();
                    if(networkEntity.getRunStatus().toString().equals(runStatus)){
                        count++;
                        iterator.remove();
                    }
                }
                temp.add(runStatus);
                temp.add(count);
                realResult.add(temp);
            }
        }
        return realResult;
    }

//    public NeHealth getNeHealth(String neId) throws Exception{
//        JsonModel jsonModel = monitorService.getNeHealth(neId);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return this.toJavaBean(jsonModel, NeHealth.class);
//    }

//    public List<NeHealthHistory> findHealthByNeIdIn(List<String> neIdIn) throws Exception{
//        JsonModel jsonModel = monitorService.findHealthByNeIdIn(neIdIn);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return toJavaBeanList(jsonModel, NeHealthHistory.class);
//    }

//    public List<NeHealth> findNeHealthOrderByHealthy(String neIds, String order) throws Exception{
//        JsonModel indJsonModel = monitorService.findNeHealthOrderByHealthy(neIds, order);
//        if (!indJsonModel.isSuccess()){
//            throw new Exception(indJsonModel.getMsg());
//        }
//        return this.toJavaBeanList(indJsonModel, NeHealth.class);
//    }


//    public JsonModel countAlert(HttpSession session, String alertType, String alertLevel) throws Exception{
//        JsonModel jsonModel = alertService.countAlert("SESSION=" + session.getId(), alertType, alertLevel);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return jsonModel;
//    }

//    public JsonModel getOtherAlertInfo(HttpSession session, String type) throws Exception{
//        JsonModel jsonModel = alertService.getOtherAlertInfo("SESSION=" + session.getId(), type);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return jsonModel;
//    }

//    public JsonModel getOtherAlertTable(HttpSession session, String type, Long number) throws Exception{
//        JsonModel jsonModel = alertService.getOtherAlertTable("SESSION=" + session.getId(), type, number);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return jsonModel;
//    }

    public List<Alert> findByChooseForLeaderview(String[] neIds, Long number) throws Exception{
        AlertQuery query = new AlertQuery();
        query.setAlertType(AlertType.Alert);
        query.setObjectIds(StringUtils.join(neIds,","));
        JsonModel jsonModel = alertService.getAlertRecord(query, Maps.newHashMap());
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, Alert.class);
    }

    @SuppressWarnings("unchecked")
    public long getAlertCount(AlertQuery query, AlertType alert) throws Exception{
        query.setAlertType(alert);
        Map<String, Object> map = getBeanMap(query);
        JsonModel jsonModel = alertService.getAlertCount(map);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return getLongValue(jsonModel);
    }

    public List<AlertRecord> findAlert(AlertQuery query, Map<String, String> orderBy) throws Exception{
//        Map<String, Object> params = Maps.newHashMap();
//        params.put("orderBy", orderBy);
//        Map<String, Object> map = getBeanMap(query, params);
//        map.put("objectInfo", null);
//        map.put("params", null);
        orderBy = ObjectUtils.isEmpty(orderBy)? Maps.newHashMap() : orderBy;
        JsonModel jsonModel = alertService.getAlertRecord(query, orderBy);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, AlertRecord.class);
    }

    @SuppressWarnings("unchecked")
    public List<AlertLevel> findAlertList(AlertLevelQuery alertLevelQuery) throws Exception{
        Map<String, Object> map = getBeanMap(alertLevelQuery);
        JsonModel jsonModel = alertService.getAlertLevels(map);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, AlertLevel.class);
    }

    @SuppressWarnings("unchecked")
    public String getLevel(Integer level) throws Exception{
        Map<String,Object> map = getBeanMap(new AlertLevelQuery());
        JsonModel jsonModel = alertService.getAlertLevels(map);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        List<AlertLevel> list = toJavaBeanList(jsonModel, AlertLevel.class);
        for (AlertLevel alertLevel: list) {
            if (alertLevel.getLevel().equals(level)){
                return alertLevel.getName();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public String getObjectIds(HttpSession session, AlertQuery query) throws Exception{
        String cookie = "SESSION=" + session.getId();
        Map<String,Object> map = getBeanMap(query);
        map.put("objectInfo", null);
        JsonModel jsonModel = alertService.getObjectIdsByAlertType(cookie, query);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel.getMsg();
    }

//    public JSONObject getStatByLevel(ArrayList arr, String alertLevel, AlertType alert) throws Exception{
//        JsonModel jsonModel = alertService.getStatByLevel(arr, alertLevel, alert);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return JSON.parseObject(JSON.toJSONString(jsonModel.getObj()));
//    }

//    public JSONObject getStatByClass(JSONArray neArray, String baseClass, String levels, AlertType alertType,
//                                     String neClassStr, boolean statisticsByNe) throws Exception{
//        JsonModel jsonModel = alertService.getStatByClass(neArray, baseClass, levels, alertType, neClassStr, statisticsByNe);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return JSON.parseObject(JSON.toJSONString(jsonModel.getObj()));
//    }
//
//    public JSONObject getStatByNe(JSONArray neArray, String alertLevel, AlertType alert) throws Exception{
//        JsonModel jsonModel = alertService.getStatByNe(neArray, alertLevel, alert);
//        if (!jsonModel.isSuccess()){
//            throw new Exception(jsonModel.getMsg());
//        }
//        return JSON.parseObject(JSON.toJSONString(jsonModel.getObj()));
//    }


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

    private <T> Map<String, Object> getBeanMap(T t){
        return getBeanMap(t, Maps.newHashMap());
    }

    @SuppressWarnings("unchecked")
    private <T> Map<String, Object> getBeanMap(T t, Map<String, Object> params){
        Map<String, Object> map = BeanMap.create(t);
        for (Map.Entry<String,Object> entry: params.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        Map<String, Object> result = Maps.newHashMap();
        result.putAll(map);
        result.putAll(params);
        return result;
    }

    private Long getLongValue(JsonModel jsonModel) throws Exception{
        Object obj = jsonModel.getObj();
        if (obj instanceof Integer || obj instanceof Long){
            return Long.valueOf(obj.toString());
        }else {
            throw new Exception("数据转换出错");
        }
    }

//    @SuppressWarnings("unchecked")
//    public List<NetworkEntity> findAllNetworkEntity(NetworkEntityQO entityQO) throws Exception{
//        JsonModel neJsonModel = monitorService.findAllNetworkEntity(entityQO);
//        if (!neJsonModel.isSuccess()){
//            throw new Exception(neJsonModel.getMsg());
//        }
//        return this.toJavaBeanList(neJsonModel, NetworkEntity.class);
//    }


//    public NetworkEntity findNetworkEntityByIdIn(String ids) throws Exception{
//        List<NetworkEntity> list = findNetworkEntityByIdIn(Lists.newArrayList(ids));
//        if (list.isEmpty()){
//            return null;
//        }
//        return list.get(0);
//    }

    public List<StatisticsResult> getLevelStatisticsResult(StatisticsQuery query) throws Exception{
        Map<String, Object> map = getBeanMap(query);
        map.put("params", null);
        JsonModel jsonModel = alertService.getLevelStatisticsResult(map);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, StatisticsResult.class);
    }


    public NetworkEntity findNetworkEntityById(String ids) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria.setId(ids);
        List<NetworkEntity> list = getNeList(criteria);
        if (ObjectUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    @SuppressWarnings("unchecked")
    public NeHealth findNeHealth(String neId) throws Exception{
        Map<String, Object> map = Maps.newHashMap();
        map.put("neIds", Lists.newArrayList(neId));
        map.put("isHistory", false);
        map.put("order", "desc");
//        JsonModel jsonModel = monitorService.findNeHealth(map);
        JsonModel jsonModel = monitorService.findNeHealth(Lists.newArrayList(neId), false, "desc");
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        List<NeHealth> neHealths = (List<NeHealth>)((JSONObject) jsonModel.getObj()).get("neHealths");
        if (ObjectUtils.isEmpty(neHealths)){
            return null;
        }
        return neHealths.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<NeHealth> findNeHealthOrderByHealthy(List<String> neIds, String order) throws Exception{
        JsonModel jsonModel = monitorService.findNeHealth(neIds, false, order);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        ArrayList<LinkedHashMap> neHealths = (ArrayList)((LinkedHashMap) jsonModel.getObj()).get("neHealths");
        List<NeHealth> ts = Lists.newArrayList();
        for (LinkedHashMap map: neHealths) {
            NeHealth t = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)), NeHealth.class);
            ts.add(t);
        }
        return ts;
    }

    @SuppressWarnings("unchecked")
    public List<NeHealthHistory> findHealthByNeIdIn(List<String> neIdIn) throws Exception{
        JsonModel jsonModel = monitorService.findNeHealth(neIdIn, true, "");
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        ArrayList<LinkedHashMap> neHealths = (ArrayList)((LinkedHashMap) jsonModel.getObj()).get("historyNeHealth");
        List<NeHealthHistory> ts = Lists.newArrayList();
        for (LinkedHashMap map: neHealths) {
            NeHealthHistory t = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)), NeHealthHistory.class);
            ts.add(t);
        }
        return ts;
    }

    public NetworkEntity findNetworkEntityByIdIn(String id) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria.setId(id);
        List<NetworkEntity> neList = getNeList(criteria);
        if (ObjectUtils.isEmpty(neList)){
            return null;
        }
        return neList.get(0);
    }

    public List<NetworkEntity> findNetworkEntityByIdIn(List<String > ids) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria.setIds(ids);
        return getNeList(criteria);
    }

    public List<NetworkEntity> findNetworkEntityByIdIn(String[] ids) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria.setIds(Lists.newArrayList(ids));
        return getNeList(criteria);
    }

    @SuppressWarnings("unchecked")
    public List<NetworkEntity> getNeList(NetworkEntityCriteria criteria) throws Exception{
        Map<String , Object> map = BeanMap.create(criteria);
        map.put("cls", null);
        JsonModel jsonModel = monitorService.getNeList(map);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return this.toJavaBeanList(jsonModel, NetworkEntity.class);
    }

    public List<String> getNeIds(NetworkEntityCriteria criteria) throws Exception{
        List<NetworkEntity> neList = getNeList(criteria);
        List<String> result = Lists.newArrayList();
        for (NetworkEntity ne:neList) {
            result.add(ne.getId());
        }
        return result;
    }

    public NetworkEntityCriteria setCriteriaDomainIds(NetworkEntityCriteria criteria, HttpSession session, Long domainId){
        if (domainId != null) {
            criteria.setDomainIds(Lists.newArrayList(domainId));
        } else {
            List<Long> domainIds = domainUtils.getUserDomainIds(session);
            if (domainIds.isEmpty()) {
                //TODO 该用户权限下无资源
                return null;
            }
            criteria.setDomainIds(domainIds);
        }
        return criteria;
    }

    public NetworkEntityCriteria setCriteriaNeClass(NetworkEntityCriteria criteria, BaseNeClass baseNeClass, NeClass neClass){
        // 过滤掉不被监控的资源
        criteria.setMonitoring(true);
        if (!ObjectUtils.isEmpty(neClass)) {
            criteria.setNeClasses(Lists.newArrayList(neClass));
            // 虚拟化资源的sourceManage为false
            if (neClass.getBaseNeClass().equals(BaseNeClass.virtualization)) {
                criteria.setSourceManage(false);
            }
        } else if (!ObjectUtils.isEmpty(baseNeClass)){
            criteria.setNeClasses(baseNeClass.getNeClass());
            // 虚拟化资源的sourceManage为false
            if (baseNeClass.equals(BaseNeClass.virtualization)) {
                criteria.setSourceManage(false);
            }
        }
        return criteria;
    }

    public void setCriteriaNeClass(NetworkEntityCriteria criteria, String baseNeClass, String neClass){
        // 过滤掉不被监控的资源
        criteria.setMonitoring(true);
        if (!ObjectUtils.isEmpty(neClass)) {
            NeClass neClass1 = NeClass.valueOf(neClass);
            criteria.setNeClasses(Lists.newArrayList(neClass1));
            // 虚拟化资源的sourceManage为false
            if (neClass1.getBaseNeClass().equals(BaseNeClass.virtualization)) {
                criteria.setSourceManage(false);
            }
        } else if (!ObjectUtils.isEmpty(baseNeClass)){
            BaseNeClass baseNeClass1 = BaseNeClass.valueOf(baseNeClass);
            criteria.setNeClasses(baseNeClass1.getNeClass());
            // 虚拟化资源的sourceManage为false
            if (baseNeClass1.equals(BaseNeClass.virtualization)) {
                criteria.setSourceManage(false);
            }
        }
    }

    public void setCriteriaNeClass(NetworkEntityCriteria criteria, String baseNeClass){
        // 过滤掉不被监控的资源
        criteria.setMonitoring(true);
        if (!ObjectUtils.isEmpty(baseNeClass)){
            BaseNeClass baseNeClass1 = BaseNeClass.valueOf(baseNeClass);
            criteria.setNeClasses(baseNeClass1.getNeClass());
            // 虚拟化资源的sourceManage为false
            if (baseNeClass1.equals(BaseNeClass.virtualization)) {
                criteria.setSourceManage(false);
            }
        }
    }

    public List<IndicatorTable> getUsableInd(String indicatorName, NetworkEntityCriteria criteria) throws Exception{
        if (ObjectUtils.isEmpty(criteria.getIds())){
            criteria.setIds(Lists.newArrayList(criteria.getId()));
        }
        if (ObjectUtils.isEmpty(criteria.getNeClasses())){
            criteria.setNeClasses(Lists.newArrayList(criteria.getNeClass()));
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("indicatorName", indicatorName);
        Map<String, Object> beanMap = getBeanMap(criteria, map);
        beanMap.put("cls", null);
        JsonModel jsonModel = monitorService.getUsableInd(beanMap);
        criteria.setIds(Lists.newArrayList(criteria.getId()));
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return this.toJavaBeanList(jsonModel, IndicatorTable.class);
    }

    public IndicatorTable getIndicatorInfoByName(String indicator) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        List<IndicatorTable> usableInd = getUsableInd(indicator, criteria);
        if (ObjectUtils.isEmpty(usableInd)){
            throw new Exception("目标指标不存在");
        }
        return usableInd.get(0);
    }

    public IndValue getIndValue(IndicatorValueQO indicatorValueQO) throws Exception{
        List<IndValue> indValues = getIndValues(indicatorValueQO);
        if (ObjectUtils.isEmpty(indValues)){
            return null;
        }
        return getIndValues(indicatorValueQO).get(0);
    }

    public List<IndValue> getIndValues(IndicatorValueQO indicatorValueQO) throws Exception{
        Map<String, Object> beanMap = getBeanMap(indicatorValueQO);
        JsonModel jsonModel = monitorService.getIndValues(beanMap);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, IndValue.class);
    }

    @SuppressWarnings("unchecked")
    public JSONArray getIndAggValues(IndicatorValueQO qo) throws Exception{
        JsonModel jsonModel = monitorService.getIndAggValues(qo);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return JSON.parseArray(JSON.toJSONString(jsonModel.getObj()));
    }

    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findNeComps(NeComponentQuery neComponentQuery) throws Exception{
        Map<String, Object> beanMap = getBeanMap(neComponentQuery);
        JsonModel jsonModel = monitorService.findNeComps(beanMap);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return (List<Map<String,Object>>) jsonModel.getObj();
    }

    @SuppressWarnings("unchecked")
    public List<String> getNeIdsByDomainIds(Long[] domains, HttpSession session) throws Exception{
        List<Long> queryDomainIds = domains == null ? Lists.newArrayList() : Lists.newArrayList(domains);
        List<Long> domainIds = domainUtils.getUserDomainIds(session);
        domainIds.add(-1L);
        List<Long> legal = queryDomainIds.stream().filter(id -> domainIds.contains(id)).collect(Collectors.toList());
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        if (queryDomainIds.isEmpty()) {// 条件中没有查询条件
            criteria.setDomainIds(domainIds);
        } else {// 存在则做校验
            if (legal.isEmpty()) {// 表示用户查询的所有的域都不属于自己的
                return null;// 屏蔽查询结果，返回空
            } else {// 查询权限范围的Domain的数据
                criteria.setDomainIds(legal);
            }
        }
        criteria.setPagination(false);
        criteria.setSourceManage(null);
        Map<String, Object> beanMap = getBeanMap(criteria);
        beanMap.put("cls", null);
        JsonModel jsonModel = monitorService.getNeList(beanMap);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        List<NetworkEntity> nes = toJavaBeanList(jsonModel, NetworkEntity.class);
        return nes.stream().map(NetworkEntity::getId).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public PageModel findNeLinks(PageModel temPage, NetworkLinkModel networkLinkModel) throws Exception{
        Map<String, Object> map = Maps.newHashMap();
        map.put("pagination", false);
        Map<String, Object> pageMap = getBeanMap(temPage);
        Map<String, Object> linkModelMap = getBeanMap(networkLinkModel);
        map.putAll(pageMap);
        map.putAll(linkModelMap);
        JsonModel jsonModel = monitorService.findNeLinks(map);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return this.toJavaBean(jsonModel, PageModel.class);
    }

    public JSONArray getFieldLables(String indicatorID) throws Exception{
        IndicatorTable indicator = getIndicatorInfoByName(indicatorID);
        JSONArray fieldLabel = new JSONArray();
        if (null == indicator || null == indicator.getFields() || indicator.getFields().size() <= 0) {
            return fieldLabel;
        }
        JSONArray fields = indicator.getFields();
        JSONObject json = null;
        for (int i = 0; i < fields.size(); i++) {
            json = fields.getJSONObject(i);
            if (null != json && json.containsKey("name")) {
                JSONObject name_Lable = new JSONObject();
                name_Lable.put("name", json.getString("name"));
                name_Lable.put("label", json.getString("label"));
                name_Lable.put("type", json.getString("fieldType"));
                name_Lable.put("tag", json.getString("tag"));
                if (json.containsKey("unit")) {
                    name_Lable.put("unit", json.getString("unit"));
                }
                if (json.containsKey("withoutrule")) {
                    name_Lable.put("withoutrule", json.getString("withoutrule"));
                }
                if (json.containsKey("desc")) {
                    name_Lable.put("desc", json.getJSONObject("desc"));
                }
                fieldLabel.add(name_Lable);
            }
        }
        return fieldLabel;
    }

    public JSONObject getStrategy(String neId, String indicatorNames) throws Exception{
        JsonModel jsonModel = monitorService.FindFieldIsMonitoringInStrategy(neId, indicatorNames);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return JSON.parseObject(JSON.toJSONString(jsonModel.getObj()));
    }


}
