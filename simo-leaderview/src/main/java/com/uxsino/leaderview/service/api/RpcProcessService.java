package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
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
import org.springframework.beans.factory.annotation.Autowired;
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

//    @SuppressWarnings("unchecked")
//    public List<NetworkEntity> findAllNetworkEntity(NetworkEntityQO entityQO) throws Exception{
//        JsonModel neJsonModel = monitorService.findAllNetworkEntity(entityQO);
//        if (!neJsonModel.isSuccess()){
//            throw new Exception(neJsonModel.getMsg());
//        }
//        return this.toJavaBeanList(neJsonModel, NetworkEntity.class);
//    }

    public NetworkEntity findNetworkEntityById(String ids) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria.setId(ids);
        List<NetworkEntity> list = getNeList(criteria);
        if (ObjectUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

//    public NetworkEntity findNetworkEntityByIdIn(String ids) throws Exception{
//        List<NetworkEntity> list = findNetworkEntityByIdIn(Lists.newArrayList(ids));
//        if (list.isEmpty()){
//            return null;
//        }
//        return list.get(0);
//    }

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

    public List<NetworkEntity> getNeList(NetworkEntityCriteria criteria) throws Exception{
        JsonModel jsonModel = monitorService.getNeList(criteria);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return this.toJavaBeanList(jsonModel, NetworkEntity.class);
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
        } else {
            criteria.setNeClasses(BaseNeClass.getNeClass(false));
            // 虚拟化资源的sourceManage为false
            if (!ObjectUtils.isEmpty(baseNeClass) && baseNeClass.equals(BaseNeClass.virtualization)) {
                criteria.setSourceManage(false);
            }
        }
        return criteria;
    }

    public List<IndicatorTable> getUsableInd(String indicatorName, NetworkEntityCriteria criteria) throws Exception{
        JsonModel jsonModel = monitorService.getUsableInd(indicatorName, criteria);
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
        JsonModel jsonModel = monitorService.getIndValues(indicatorValueQO);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        return toJavaBeanList(jsonModel, IndValue.class);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> findNeComps(NeComponentQuery neComponentQuery) throws Exception{
        JsonModel jsonModel = monitorService.findNeComps(neComponentQuery);
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
        JsonModel jsonModel = monitorService.getNeList(criteria);
        if (!jsonModel.isSuccess()){
            throw new Exception(jsonModel.getMsg());
        }
        List<NetworkEntity> nes = toJavaBeanList(jsonModel, NetworkEntity.class);
        return nes.stream().map(itm -> itm.getId()).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public PageModel findNeLinks(PageModel temPage, NetworkLinkModel networkLinkModel) throws Exception{
        JsonModel jsonModel = monitorService.findNeLinks(false, temPage, networkLinkModel);
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
}
