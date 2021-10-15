package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.authority.lib.util.DomainUtils;
import com.uxsino.commons.db.criteria.IndicatorValueCriteria;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.model.RunStatus;
import com.uxsino.commons.model.*;
import com.uxsino.leaderview.model.AlertType;
import com.uxsino.leaderview.model.SiteOrganizationCriteria;
import com.uxsino.leaderview.model.alert.*;
import com.uxsino.leaderview.model.asset.AssetCriteria;
import com.uxsino.leaderview.model.asset.AssetTreeVo;
import com.uxsino.leaderview.model.business.ManageStatus;
import com.uxsino.leaderview.model.business.*;
import com.uxsino.leaderview.model.datacenter.IndValueQuery;
import com.uxsino.leaderview.model.monitor.*;
import com.uxsino.leaderview.rpc.*;
import com.uxsino.leaderview.service.query.NeComponentQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
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
    private BusinessService businessService;

    @Autowired
    private AssetService assetService;

    @Autowired
    DatacenterService datacenterService;

    @Autowired
    MCService mcService;

    @Autowired
    private DomainUtils domainUtils;

    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> vmStatics(Long domain, String topoId) throws Exception {
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        if(domain != null){
            criteria.setDomainId(domain);
        }
        List<NeClass> neClasses = BaseNeClass.virtualization.getNeClass();
        criteria.setNeClasses(neClasses);
        criteria.setPagination(false);
        //当传入topoId时，只查询该拓扑下的资源
        if (!Strings.isNullOrEmpty(topoId)) {
            criteria.setTopoId(topoId);
        }
        criteria.setManageStatusNotIn(Lists.newArrayList(com.uxsino.leaderview.model.monitor.ManageStatus.Delected));
        List<NetworkEntity> rawResult = getNeList(criteria);
        List<NeHealthHistory> list = new ArrayList<>();
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

    public List<Map<String, Object>> neStatistics(Long domain, BaseNeClass baseClass, String topoId) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        if(domain != null){
            criteria.setDomainId(domain);
        }
        //当传入topoId时，只查询该拓扑下的资源
        if (!Strings.isNullOrEmpty(topoId)) {
            criteria.setTopoId(topoId);
        }
        criteria.setPagination(false);
        criteria.setManageStatusNotIn(Lists.newArrayList(com.uxsino.leaderview.model.monitor.ManageStatus.Delected));
        List<NetworkEntity> rawResult = getNeList(criteria);
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


    public Long countVr(Long domainId,String topoId) throws Exception {
        List<Map<String, Object>> statics = vmStatics(domainId,topoId);
        long value = 0L;
        if (CollectionUtils.isNotEmpty(statics)) {
            for (Map<String, Object> map : statics) {
                value = value + Long.parseLong(map.get("value").toString());
            }
        }
        return value;
    }


    public List<ArrayList> neStatusStatistics(List<Long> domainIds, BaseNeClass baseNeClass,String topoId) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        if(domainIds != null && !domainIds.isEmpty()){
            criteria.setDomainIds(domainIds);
        }
        criteria.setPagination(false);
        if(baseNeClass != null){
            List<NeClass> neClassList = baseNeClass.getNeClass();
            criteria.setNeClasses(neClassList);
        }

        //当传入topoId时，只查询该拓扑下的资源
        if (!Strings.isNullOrEmpty(topoId)) {
            criteria.setTopoId(topoId);
        }
        criteria.setManageStatusNotIn(Lists.newArrayList(com.uxsino.leaderview.model.monitor.ManageStatus.Delected));
        List<NetworkEntity> networkEntityList = getNeList(criteria);
        // 对虚拟化资源进行特殊处理，只统计parentId为空的vmWare,xen，kvm资源和parentId = id 单独发现的esxi资源
        List<NetworkEntity> rawResult = networkEntityList.stream().filter(networkEntity ->
            networkEntity.getParentId() == null
            || networkEntity.getParentId().equals(networkEntity.getId())
        ).collect(Collectors.toList());
        List<ArrayList> realResult = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(rawResult)){
            ArrayList<Object> temp = new ArrayList<>();
            for(RunStatus runStatus : RunStatus.values()){
                temp = new ArrayList<>();
                long count = 0;
                Iterator<NetworkEntity> iterator = networkEntityList.iterator();
                while(iterator.hasNext()){
                    NetworkEntity networkEntity = iterator.next();
                    if(runStatus.equals(networkEntity.getRunStatus())){
                        count++;
                        iterator.remove();
                    }
                }
                temp.add(runStatus.getName());
                temp.add(count);
                realResult.add(temp);
            }
        }
        return realResult;
    }

    /**
     * 用于旭日图的按状态统计资源，本层进行第一层包装——即子类型下含有各种状态的数量
     * @param domainIds
     * @param baseNeClass
     * @return
     * @throws Exception
     */
    public JSONArray neStatusStatisticsForSunburst(List<Long> domainIds, BaseNeClass baseNeClass) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        if(domainIds != null && !domainIds.isEmpty()){
            criteria.setDomainIds(domainIds);
        }
        criteria.setPagination(false);
        if(baseNeClass != null){
            List<NeClass> neClassList = baseNeClass.getNeClass();
            criteria.setNeClasses(neClassList);
        }
        criteria.setManageStatusNotIn(Lists.newArrayList(com.uxsino.leaderview.model.monitor.ManageStatus.Delected));
        List<NetworkEntity> networkEntityList = getNeList(criteria);
        // 对虚拟化资源进行特殊处理，只统计parentId为空的vmWare,xen，kvm资源和parentId = id 单独发现的esxi资源
        List<NetworkEntity> rawResult = networkEntityList.stream().filter(networkEntity ->
                networkEntity.getParentId() == null
                        || networkEntity.getParentId().equals(networkEntity.getId())
        ).collect(Collectors.toList());
        JSONArray nCs = new JSONArray();
        if(CollectionUtils.isNotEmpty(rawResult)){
            List<NeClass> neClasses = baseNeClass.getNeClass();
            for (NeClass neClass: neClasses) {
                JSONObject nC = new JSONObject();
                JSONArray statusResult = new JSONArray();
                JSONObject temp;
                for(RunStatus runStatus : RunStatus.values()){
                    temp = new JSONObject();
                    long count = 0;
                    Iterator<NetworkEntity> iterator = networkEntityList.iterator();
                    while(iterator.hasNext()){
                        NetworkEntity networkEntity = iterator.next();
                        if(runStatus.equals(networkEntity.getRunStatus()) && networkEntity.getNeClass().equals(neClass)){
                            count++;
                            iterator.remove();
                        }
                    }
                    temp.put("name", runStatus.getName());
                    temp.put("value", count);
                    statusResult.add(temp);
                }
                nC.put("name", neClass.getText());
                nC.put("children", statusResult);
                nCs.add(nC);
            }
        }
        return nCs;
    }

    public List<ArrayList> neHealthStatistics(List<Long> domainIds, BaseNeClass baseNeClass) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        if(domainIds != null && !domainIds.isEmpty()){
            criteria.setDomainIds(domainIds);
        }
        criteria.setPagination(false);
        if(baseNeClass != null){
            List<NeClass> neClassList = baseNeClass.getNeClass();
            criteria.setNeClasses(neClassList);
        }
        criteria.setManageStatusNotIn(Lists.newArrayList(com.uxsino.leaderview.model.monitor.ManageStatus.Delected));
        criteria.setHealthReturn(true);
        List<NetworkEntity> networkEntityList = getNeList(criteria);
        // 对虚拟化资源进行特殊处理，只统计parentId为空的vmWare,xen，kvm资源和parentId = id 单独发现的esxi资源
        List<NetworkEntity> rawResult = networkEntityList.stream().filter(networkEntity ->
                networkEntity.getParentId() == null
                        || networkEntity.getParentId().equals(networkEntity.getId())
        ).collect(Collectors.toList());
        List<ArrayList> realResult = new ArrayList<>();
        LinkedHashMap<String,Integer> map = new LinkedHashMap<>();

        Integer count100 = 0;
        Integer count90 = 0;
        Integer count80 = 0;
        Integer count60 = 0;
        Integer count0 = 0;

        for(NetworkEntity entity:rawResult){
            int health = entity.getHealth();
            switch (health/10){
                case 10:
                    count100++;break;
                case 9:
                    count90++;break;
                case 8:
                    count80++;break;
                case 7:
                    count60++;break;
                case 6:
                    count60++;break;
                default:
                    count0++;
            }
        }
        map.put("100",count100);
        map.put("90-99",count90);
        map.put("80-89",count80);
        map.put("60-79",count60);
        map.put("0-59",count0);

        ArrayList<Object> temp;
        for (Map.Entry<String,Integer> entry: map.entrySet()) {
            temp = new ArrayList<>();
            temp.add(entry.getKey());
            temp.add(entry.getValue());
            realResult.add(temp);
        }

        return realResult;
    }


    public List<AlertRecord> findByChooseForLeaderview(String[] neIds, int number) throws Exception{
        AlertQuery query = new AlertQuery();
        List<AlertHandleStatus> statuses = Lists.newArrayList(AlertHandleStatus.INVALID,
                AlertHandleStatus.FINISHED, AlertHandleStatus.RESTORED);
        query.setAlertType(AlertType.Alert);
        query.setObjectIds(StringUtils.join(neIds,","));
        query.setLimit(number);
        query.setHandleStatusNotIn(statuses.toArray(new AlertHandleStatus[0]));
        JsonModel jsonModel = alertService.getAlertRecord(query, Maps.newHashMap());
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, AlertRecord.class);
    }

    @SuppressWarnings("unchecked")
    public long getAlertCount(AlertQuery query, AlertType alert) throws Exception{
        query.setAlertType(alert);
        Map<String, Object> map = getBeanMap(query);
//        map.remove("params");
//        map.remove("objectInfo");
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
    public List<AlertLevel> findAlertLevelList(AlertLevelQuery alertLevelQuery) throws Exception{
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

    private Long getLongValue(JsonModel jsonModel) throws Exception{
        Object obj = jsonModel.getObj();
        if (obj instanceof Integer || obj instanceof Long){
            return Long.valueOf(obj.toString());
        }else {
            throw new Exception("数据转换出错");
        }
    }

    public List<StatisticsResult> getLevelStatisticsResult(StatisticsQuery query) throws Exception{
        Map<String, Object> map = getBeanMap(query);
        map.remove("params");
//        map.remove("endAlertDate");
//        map.remove("startAlertDate");
        JsonModel jsonModel = alertService.getLevelStatisticsResult(map);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, StatisticsResult.class);
    }


    public NetworkEntity findNetworkEntityById(String ids) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria.setId(ids);
        criteria.setMonitoring(true);
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
        JsonModel jsonModel = monitorService.findNeHealth(Lists.newArrayList(neId), false, "desc");
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        ArrayList<LinkedHashMap> neHealths = ((LinkedHashMap<String, ArrayList>) jsonModel.getObj()).get("neHealths");
        if (ObjectUtils.isEmpty(neHealths)){
            return null;
        }
        return JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(neHealths.get(0))), NeHealth.class);
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

    public NetworkEntity findNetworkEntityByIdIn(String id, boolean isVirtualization) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria.setId(id);
        criteria.setMonitoring(true);
        if(isVirtualization){
            criteria.setSourceManage(false);
        }
        List<NetworkEntity> neList = getNeList(criteria);
        if (ObjectUtils.isEmpty(neList)){
            return null;
        }
        return neList.get(0);
    }

    public List<NetworkEntity> findNetworkEntityByIdIn(List<String > ids) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria.setIds(ids);
        criteria.setMonitoring(true);
        return getNeList(criteria);
    }

    public List<NetworkEntity> findNetworkEntityByIdIn(String[] ids) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria.setIds(Lists.newArrayList(ids));
        criteria.setMonitoring(true);
        return getNeList(criteria);
    }

    @SuppressWarnings("unchecked")
    public List<NetworkEntity> getAllNeList(NetworkEntityCriteria criteria) throws Exception{
        JsonModel jsonModel = monitorService.getNeList(JSON.toJSONString(criteria));
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, NetworkEntity.class);
    }

    @SuppressWarnings("unchecked")
    public List<NetworkEntity> getNeList(NetworkEntityCriteria criteria) throws Exception{
        criteria.setPagination(false);
        String param = JSON.toJSONString(criteria);
        JsonModel jsonModel = monitorService.getNeList(param);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, NetworkEntity.class);
    }

    public JsonModel getNeRelationList(VirtualizationRelationCriteria criteria) throws Exception{
        String param = JSON.toJSONString(criteria);
        JsonModel jsonModel = monitorService.getNeRelationList(param);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel;
    }

    public List<String> getNeIds(NetworkEntityCriteria criteria) throws Exception{
        criteria.setMonitoring(true);
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
                return criteria;
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
        if (!ObjectUtils.isEmpty(criteria.getId()) && ObjectUtils.isEmpty(criteria.getIds())){
            criteria.setIds(Lists.newArrayList(criteria.getId()));
        }
        if (!ObjectUtils.isEmpty(criteria.getNeClass()) && ObjectUtils.isEmpty(criteria.getNeClasses())){
            criteria.setNeClasses(Lists.newArrayList(criteria.getNeClass()));
        }
        JsonModel jsonModel = monitorService.getUsableInd(indicatorName,criteria);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, IndicatorTable.class);
    }

    public IndicatorTable getIndicatorInfoByName(String indicator) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        List<IndicatorTable> usableInd = getUsableInd(indicator, criteria);
        if (ObjectUtils.isEmpty(usableInd)){
            log.error("目标指标不存在: " + indicator);
            return null;
        }
        return usableInd.get(0);
    }

    /**
     * 获取单个实时指标值
     * @param indicatorValueQO
     * @return
     * @throws Exception
     */
    public IndValue getIndValue(IndicatorValueCriteria indicatorValueQO) throws Exception{
        List<IndValue> indValues = getCurIndValues(indicatorValueQO);
        if (ObjectUtils.isEmpty(indValues)){
            return null;
        }
        return indValues.get(0);
    }

    /**
     * 根据条件获取多个实时指标值
     * @param criteria
     * @return
     * @throws Exception
     */
    public List<IndValue> getIndValues(IndicatorValueCriteria criteria) throws Exception{
        String param = JSON.toJSONString(criteria);
        JsonModel jsonModel = monitorService.getCurIndValues(param);
        if (!jsonModel.isSuccess()){
            log.error("实时指标查询失败，",jsonModel.getMsg());
        }
        return toJavaBeanListIndValue(jsonModel);
        /*Map<String, Object> beanMap = getBeanMap(indicatorValueQO);
        JsonModel jsonModel = monitorService.getIndValues(beanMap);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanListIndValue(jsonModel);*/
    }

    public List<IndValue> getCurIndValues(IndicatorValueCriteria criteria) {
        //处理指标名传参
        List<String> indicatorName = criteria.getIndicatorName();
        indicatorName = indicatorName == null || indicatorName.isEmpty() ? new ArrayList<>() : indicatorName;
        if (StringUtils.isNotBlank(criteria.getIndicatorId())) {
            String[] split = criteria.getIndicatorId().split(",");
            indicatorName.addAll(Arrays.asList(split));
        }
        String join = StringUtils.join(indicatorName, ",");
        criteria.setIndicatorId(join);
        //数据中心获取指标值
        String param = JSON.toJSONString(criteria);
        JsonModel jsonModel = monitorService.getCurIndValues(param);
        if (!jsonModel.isSuccess()){
            log.error("实时指标查询失败，",jsonModel.getMsg());
        }
        return toJavaBeanListIndValue(jsonModel);
    }

    @SuppressWarnings("unchecked")
    public JSONArray getIndAggValues(IndicatorValueQO qo) throws Exception{
        if (!ObjectUtils.isEmpty(qo.getIdentifiers()) && qo.getIdentifiers().get(0) == null){
            qo.setIdentifiers(null);
        }
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
        List<NetworkEntity> nes = getNeList(criteria);
        return nes.stream().map(NetworkEntity::getId).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<NetworkLinkModel> findNeLinks(PageModel temPage, NetworkLinkModel networkLinkModel) throws Exception{
        networkLinkModel.setPageSize(temPage.getPageSize());
        networkLinkModel.setCurrentNo(temPage.getCurrentNo());
        JsonModel jsonModel = monitorService.findNeLinks(false, networkLinkModel);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        //ArrayList<NetworkLinkModel> list = (ArrayList<NetworkLinkModel>) jsonModel.getObj();
        //PageModel pageModel = this.toJavaBean(jsonModel, PageModel.class);
        //JSONArray array = (JSONArray) pageModel.getObject();
        //List<NetworkLinkModel> nesLinkList = Lists.newArrayList();
        List<NetworkLinkModel> nesLinkList = this.toJavaBeanList(jsonModel,NetworkLinkModel.class);
//        for (int i = 0; i < array.size(); i++) {
//            NetworkLinkModel model = JSON.toJavaObject(array.getJSONObject(i), NetworkLinkModel.class);
//            nesLinkList.add(model);
//        }
        return nesLinkList;
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

    public List<BusinessSystem> findBusinessSystems(BusinessQuery businessQuery) throws Exception{
        JsonModel jsonModel = businessService.findBusinessSystems(businessQuery);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        List<BusinessSystem> businessSystems = toJavaBeanList(jsonModel, BusinessSystem.class);
        businessSystems = businessSystems.stream().filter(b -> b.getManageStatus() != ManageStatus.Delected).collect(Collectors.toList());
        return businessSystems;
    }

    public List<BusinessRecord> indicatorTop(BnsIndValQuery query) throws Exception{
        return findBusinessInfoAndCurIndValues(query);
    }

    public List<BusinessRecord> findBusinessInfoAndCurIndValues(BnsIndValQuery bnsIndValQuery) throws Exception{
        JsonModel jsonModel = businessService.findBusinessInfoAndCurIndValues(bnsIndValQuery);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, BusinessRecord.class);
    }

    public JSONObject checkPermission(BusinessRecord businessRecord, DataSourceType dataSourceType, HttpSession session) throws Exception{
        BusinessSystem businessSystem = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(businessRecord)), BusinessSystem.class);
        return checkPermission(businessSystem, dataSourceType, session);
    }

    @SuppressWarnings("unchecked")
    public JSONObject checkPermission(BusinessSystem businessSystem, DataSourceType dataSourceType, HttpSession session) throws Exception{
        String cookie = "SESSION=" + session.getId();
        JsonModel jsonModel = businessService.checkPermission(businessSystem, dataSourceType, cookie);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return JSON.parseObject(JSON.toJSONString(jsonModel.getObj()));
    }

    @SuppressWarnings("unchecked")
    public SortWayConf findSortWayByUserId(Long userId) throws Exception{
        JsonModel jsonModel = businessService.findSortWayByUserId(userId);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBean(jsonModel, SortWayConf.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, JSONObject> calcStatistics(Map<String, Date> bnsCreateDateMap,
                                           Date startDate, Date endDate) throws Exception{
        JsonModel jsonModel = businessService.calcStatistics(bnsCreateDateMap, startDate, endDate);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        Map<String, LinkedHashMap> map = (Map<String, LinkedHashMap>)jsonModel.getObj();
        Map<String, JSONObject> result = Maps.newHashMap();
        for (Map.Entry<String, LinkedHashMap> entry: map.entrySet()) {
            result.put(entry.getKey(), JSON.parseObject(JSON.toJSONString(entry.getValue())));
        }
        return result;
    }

    public ArrayList<ArrayList> getCurrIndVal(BusinessQuery query) throws Exception{
        return findBnsInfoBySortWay(query);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ArrayList> findBnsInfoBySortWay(BusinessQuery query) throws Exception{
        JsonModel jsonModel = businessService.findBnsInfoBySortWay(query);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return (ArrayList<ArrayList>) jsonModel.getObj();
    }

    public JSONArray findBnsHistoryValue(BnsIndValQuery query) throws Exception{
        JsonModel jsonModel = businessService.findHisIndValues(query, Maps.newHashMap());
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return JSON.parseArray(JSON.toJSONString(jsonModel.getObj()));
    }

    private static <T> T toJavaBean(JsonModel jsonModel, Class<T> clazz){
        LinkedHashMap map = (LinkedHashMap) jsonModel.getObj();
        return JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)),clazz);
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> toJavaBeanList(JsonModel jsonModel, Class<T> clazz){
        List<LinkedHashMap> list = (List<LinkedHashMap>) jsonModel.getObj();
        List<T> ts = Lists.newArrayList();
        if(!ObjectUtils.isEmpty(list)) {
            for (LinkedHashMap map : list) {
                T t = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)), clazz);
                ts.add(t);
            }
        }
        return ts;
    }

    @SuppressWarnings("unchecked")
    private static List<IndValue> toJavaBeanListIndValue(JsonModel jsonModel){
        List<LinkedHashMap> list = (List<LinkedHashMap>) jsonModel.getObj();
        List<IndValue> ts = Lists.newArrayList();
        if(CollectionUtils.isEmpty(list)){
           return  ts;
        }
        // 由于IndicatorValue的类型为JSON ，通过toJavaObject创建实例的过程中：
        // 对JSONObject类型的IndicatorValue字段会造成数据丢失， 所以手动赋值
        for (LinkedHashMap map: list) {
            map.remove("id");
            IndValue t = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)),IndValue.class);
            if (ObjectUtils.isEmpty(t.getIndicatorValue())){
                if (ObjectUtils.isEmpty(map.get("indicatorValue"))) continue;
                t.setIndicatorValue(JSON.parseObject(JSON.toJSONString(map.get("indicatorValue"))));
            }
            ts.add(t);
        }
        return ts;
    }

    private static <T> Map<String, Object> getBeanMap(T t){
        return getBeanMap(t, Maps.newHashMap());
    }

    @SuppressWarnings("unchecked")
    private static <T> Map<String, Object> getBeanMap(T t, Map<String, Object> params){
        Map<String, Object> map = BeanMap.create(t);
        for (Map.Entry<String,Object> entry: params.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        Map<String, Object> result = Maps.newHashMap();
        result.putAll(map);
        result.putAll(params);
        return result;
    }

    public String getTopoId() throws Exception{
        JsonModel jsonModel = monitorService.getMapNodesAndLinks(null, null);
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        //返回的结果是多个JsonModel嵌套，因此为了让service处理数据时简化操作，在这里先把有用的数据提取出来
        //唯一要用到的就是topoId
        return (String)((LinkedHashMap)jsonModel.getObj()).get("topoId");
    }

    public LinkedHashMap<String, ArrayList> getMapNodesAndLinkes(String userId, String mapLocationId) throws Exception{
        JsonModel jsonModel = monitorService.getMapNodesAndLinks(userId, mapLocationId);
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        //返回的结果是多个JsonModel嵌套，因此为了让service处理数据时简化操作，在这里先把有用的数据提取出来
        //由于只能返回一个变量，因此在这一层不先把Nodes和Links取出来，而是返回封装他们的HashMap，然后让调用
        //方法再把他们俩取出来
        return (LinkedHashMap)((LinkedHashMap)((LinkedHashMap)jsonModel.getObj()).get("data")).get("obj");
    }

    public ArrayList<LinkedHashMap> getMapLocationTree(String topoId, Long userId) throws Exception{
        JsonModel jsonModel = monitorService.getMapLocationTree(topoId);
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        //返回的结果是多个JsonModel嵌套，因此为了让service处理数据时简化操作，在这里先把有用的数据提取出来
        //这里有用的数据就是指每一张地图的信息，返回给service后由service根据前端传过来的locationCode筛选
        //出某张地图的信息，从中获取地图的id（也就是二次调用getMapNodesAndLinks）要传入的mapLocationId
        if (userId != null) {
            String userIdStr = userId.toString();
            return (ArrayList)((LinkedHashMap)jsonModel.getObj()).get(userIdStr);
        }
        return new ArrayList<>();
    }

    public JsonModel searchNe(String neClass) throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("neClass", neClass);
        JsonModel jsonModel = monitorService.searchNe(jsonObject);
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel;
    }

    public JsonModel getChannelList(String neId) throws Exception{
        JsonModel jsonModel = monitorService.getChannelList(neId);
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel;
    }

    public JsonModel statisticsNe(NetworkEntityCriteria criteria) throws Exception {
        criteria.setPagination(false);
        String param = JSON.toJSONString(criteria);
        JsonModel jsonModel = monitorService.statisticsNe(param);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel;
    }

    public JsonModel statisticsNetworkLink(NetworkLinkModel networkLinkModel) throws Exception {
        String param = JSON.toJSONString(networkLinkModel);
        JsonModel jsonModel = monitorService.statisticsNetworkLink(param);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        //Object obj = jsonModel.getObj();
        return jsonModel;
    }

    public JsonModel alert_report() throws Exception {
        JsonModel jsonModel = null;
        try {
            jsonModel = assetService.alert_report();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false,e.getMessage());
        }
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel;
    }

    public JsonModel alarmMsgByPage(Integer lastNum) throws Exception {
        JsonModel jsonModel= assetService.alarmMsgByPage(lastNum);
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel;
    }

    public JsonModel search(AssetCriteria criteria) throws Exception {
        String params = JSON.toJSONString(criteria);
        JsonModel jsonModel = assetService.search(params);
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel;
    }

    public JsonModel getnNetMoveTablePerformance(String neId, PerormanceView view) {
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        ca.setTime(new Date());
        Date endTime = ca.getTime();
        ca.add(Calendar.DATE, -1);
        Date startTime = ca.getTime();

        JsonModel jsonModel = null;
        if (PerormanceView.TopSql.equals(view)) {
            jsonModel = this.monitorService.topSQL(neId, format.format(startTime), format.format(endTime), 5);
        } else if (PerormanceView.TopEvent.equals(view)) {
            jsonModel = this.monitorService.topEvent(neId, format.format(startTime), format.format(endTime));
        } else if (PerormanceView.TopSession.equals(view)) {
            jsonModel = this.monitorService.topSession(neId, format.format(startTime), format.format(endTime));
        }

        return jsonModel;
    }

    public JsonModel searchByFieldQuery(String type, Boolean isHistory, IndValueQuery indValueQuery) {
        return datacenterService.searchByFieldQuery(type,isHistory,indValueQuery);
    }

    public JsonModel getOrganList(SiteOrganizationCriteria criteria) {
        String params = JSON.toJSONString(criteria);
        return mcService.getOrganList(params);
    }

    public JsonModel searchStandingbook(AssetCriteria criteria) throws Exception {

        JsonModel jsonModel = assetService.searchStandingbook(JSON.toJSONString(criteria));
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return jsonModel;
    }

    public JsonModel findVolume(String neIds) {
        return monitorService.findVolume(neIds);
    }

    public Map<String, String> getCategory() throws Exception {
        JsonModel jsonModel = assetService.searchAssetCategory(null);
        Map<String,String > categoryMap = (Map<String, String>) jsonModel.getObj();
        if(!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return categoryMap;
    }
}
