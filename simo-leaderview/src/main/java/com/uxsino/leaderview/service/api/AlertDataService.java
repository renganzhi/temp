package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.authority.lib.model.DomainInfo;
import com.uxsino.authority.lib.util.DomainUtils;
import com.uxsino.commons.model.AlertType;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.utils.Dates;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.leaderview.model.alert.*;
import com.uxsino.leaderview.model.monitor.NetworkEntity;
import com.uxsino.leaderview.rpc.AlertService;
import com.uxsino.leaderview.rpc.MCService;
import com.uxsino.leaderview.rpc.MonitorService;
import com.uxsino.leaderview.utils.MonitorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class AlertDataService {

    @Autowired
    DomainUtils domainUtils;

    @Autowired
    RpcProcessService rpcProcessService;

    @Autowired
    MCService mcService;

    @Autowired
    MonitorService monitorService;

    public JsonModel countAlert(HttpSession session, String alertType, String alertLevel) throws Exception{
        return rpcProcessService.countAlert(session, alertType, alertLevel);
    }

    public JsonModel getOtherAlertInfo(HttpSession session, String type) throws Exception {
        return rpcProcessService.getOtherAlertInfo(session, type);
    }

    @SuppressWarnings("unchecked")
    public JsonModel getOtherAlertTable(HttpSession session, String type, Long number, String[] column) throws Exception {
        JsonModel rpcData = rpcProcessService.getOtherAlertTable(session, type, number);
        List<String > diffColumns;
        if (!"ThirdPartyAlert".equals(type) && !"BusinessAlert".equals(type) && !"ThirdPartyAlert".equals(type)
                && !"SystemAlert".equals(type) && !"TerminalAlert".equals(type)) {
            if ("NELinkAlert".equals(type)) {
                diffColumns = Lists.newArrayList("状态","告警来源","告警内容","告警时间");
            } else {
                diffColumns = Lists.newArrayList("状态","IP地址","告警内容","告警时间");
            }
        }else {
            diffColumns = Lists.newArrayList("状态","告警内容","告警时间");
        }
        column = ObjectUtils.isEmpty(column) ? diffColumns.toArray(new String[diffColumns.size()]): column;
        JSONArray columns = newColumns(column);
        diffColumns.removeAll(getAllColumns(columns));
        Map<String , List> obj = (Map<String, List>) rpcData.getObj();
        List<Map<String, String>> rows = obj.get("rows");
        if (ObjectUtils.isEmpty(rows)){
            return rpcData;
        }
        for (Map<String, String> row: rows) {
            diffColumns.forEach(diff -> row.remove(diff));
        }
        JSONObject result = MonitorUtils.newResultObj("columns", columns, "rows", rows);
        return new JsonModel(true, result);
    }

    /**
     * 获取告警分布数量数据
     *
     * @param alertLevel
     * @param range
     * @param names
     * @param areaName
     * @param period
     * @return
     */
    @SuppressWarnings("unchecked")
    public JsonModel alertDivision(HttpSession session, String[] alertLevel, String range,
                                   String[] names, String areaName, Integer period) throws Exception{
        AlertQuery query = new AlertQuery();
        query.setRecentAlertDate(Dates.from(new Date().getTime() - 1000 * 60 * 60 * period).formatDateTime());
        AlertLevelQuery alertLevelQuery = new AlertLevelQuery();
        alertLevelQuery.setStatus(AlertLevelStatus.ACTIVATED);
        List<AlertLevel> levels = rpcProcessService.findAlertList(alertLevelQuery);
        List<String> levelStr = Lists.newArrayList();
        if (ObjectUtils.isEmpty(alertLevel)) {
            for (AlertLevel level : levels) {
                levelStr.add(level.getLevel().toString());
            }
        }else {
            List<String> levelList = levels.stream().
                    map(level -> level.getLevel().toString()).collect(Collectors.toList());
            Arrays.stream(alertLevel).forEach(level -> {
                if (levelList.contains(level)) levelStr.add(level);
            });
        }
        alertLevel = levelStr.toArray(new String[levelStr.size()]);

        JsonModel model = mcService.getDomainList(session.getId());
        ArrayList<HashMap> allDomains = (ArrayList<HashMap>) model.getObj();
        Map<String, Integer> result = Maps.newHashMap();
        for (String name : names) {
            result.put(name, 0);
        }
        //遍历所有的域，取他们的地区属性
        for (HashMap map : allDomains) {
            String region = map.get("region").toString();
            if (!region.contains(areaName)) continue;
            for (String name : names) {
                if (region.contains(name)) {
                    //这个域下的所有资源id
                    JsonModel datas = new JsonModel();
                    Long domainId= Long.parseLong(map.get("id").toString());
                    if (domainId == null){
                        List<Long> domainList = domainUtils.getUserDomainIds(session);
                        datas = monitorService.findNeIdByParams(null,null,null,null,null,null,
                                domainList.toArray(new Long[domainList.size()]), null);
                    }else {
                        datas =  monitorService.findNeIdByParams(null,null,null,null,null,
                                null,new Long[] { domainId }, null);
                    }
                    ArrayList arr = (ArrayList) datas.getObj();
                    List<String> objectIds = Lists.newArrayList();
                    for (int i = 0; i < arr.size(); i++) {
                        objectIds.add(arr.get(i).toString());
                        System.out.println("alert获取资源ID"+arr.get(i).toString());
                    }
                    if (ObjectUtils.isEmpty(objectIds)) continue;
                    query.setObjectIds(org.apache.commons.lang3.StringUtils.join(objectIds, ","));
                    Long num = 0L;
                    for (String level : alertLevel) {
                        query.setLevel(Integer.parseInt(level));
                        Long alertCount = rpcProcessService.getAlertCount(query, AlertType.Alert);
                        long linkAlertCount = rpcProcessService.getAlertCount(query, AlertType.NELinkAlert);
                        long logAlertCount = rpcProcessService.getAlertCount(query, AlertType.SysLogAlert);
                        long trapAlertCount = rpcProcessService.getAlertCount(query, AlertType.SnmpTrapAlert);
                        long businessAlertCount = rpcProcessService.getAlertCount(query, AlertType.BusinessAlert);
                        long thirdAlertCount = rpcProcessService.getAlertCount(query, AlertType.ThirdPartyAlert);
                        long systemAlertCount = rpcProcessService.getAlertCount(query, AlertType.SystemAlert);
                        num += alertCount + linkAlertCount + logAlertCount
                                + trapAlertCount + businessAlertCount + thirdAlertCount + systemAlertCount;
                    }
                    result.put(name, result.get(name) + Integer.parseInt(num.toString()));
                }
            }
        }
        return new JsonModel(true, result);
    }


    /**
     * 获取告警分布最高级别数据
     *
     * @param range
     * @param names
     * @param areaName
     * @return
     */
    @SuppressWarnings("unchecked")
    public JsonModel alertLevelDivision(HttpSession session, String range, String[] names, String areaName) throws Exception{
        AlertQuery query = new AlertQuery();
        JsonModel model = mcService.getDomainList(session.getId());
        List<DomainInfo> allDomainInfos = domainUtils.getAllDomainInfos();
        ArrayList<HashMap> allDomains = (ArrayList<HashMap>) model.getObj();
        Map<String, Integer> cityMaxLevel = Maps.newHashMap();
        for (String name : names) {
            cityMaxLevel.put(name, 0);
        }
        //遍历所有的域，取他们的地区属性
        for (HashMap map : allDomains) {
            String region = map.get("region").toString();
            if (!region.contains(areaName)) continue;
            for (String name : names) {
                if (region.contains(name)) {
                    //这个域下的所有资源id
                    // List<Object> datas=null;
                    JsonModel datas = new JsonModel();
                    Long domainId= Long.parseLong(map.get("id").toString());
                    if (domainId == null){
                        List<Long> domainList = domainUtils.getUserDomainIds(session);
                        datas = monitorService.findNeIdByParams(null,null,null,null,null,null,
                                domainList.toArray(new Long[domainList.size()]), null);
                    }else {
                        datas =  monitorService.findNeIdByParams(null,null,null,null,null,
                                null,new Long[] { domainId }, null);
                    }
                    ArrayList arr = (ArrayList) datas.getObj();
                    List<String> objectIds = Lists.newArrayList();
                    for (int i = 0; i < arr.size(); i++) {
                        objectIds.add(arr.get(i).toString());
                        System.out.println("alert获取资源ID"+arr.get(i).toString());
                    }
                    if (ObjectUtils.isEmpty(objectIds)) continue;
                    query.setObjectIds(org.apache.commons.lang3.StringUtils.join(objectIds, ","));
                    query.setAlertType(AlertType.Alert);
                    List<AlertRecord> alertRecords = rpcProcessService.findAlert(query, null);
                    Integer num = 0;
                    for (AlertRecord alertRecord : alertRecords){
                        if (alertRecord.getLevel() > num) num = alertRecord.getLevel();
                    }
                    cityMaxLevel.put(name, num);
                }
            }
        }
        JSONArray result = new JSONArray();
        for (Map.Entry entry : cityMaxLevel.entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("name", entry.getKey());
            obj.put("value", entry.getValue());
            result.add(obj);
        }
        return new JsonModel(true, result);
    }

    /**
     * 大屏展示：根据选择的域、父类型、资源id和展示条数来查询最新的规定条数的告警信息，根据文本框元件需求返回值封装String
     * @param domainId
     * @param baseNeClass
     * @param neIds
     * @param session
     * @return
     */
    public JsonModel getAlertInfoForText(Long domainId, String baseNeClass, String[] neIds, HttpSession session) throws Exception{
        JSONObject result = new JSONObject();
        if (ObjectUtils.isEmpty(neIds)){
            JsonModel datas = new JsonModel();
            if (domainId == null){
                List<Long> domainList = domainUtils.getUserDomainIds(session);
                datas = monitorService.findNeIdByParams(null,null,null,null,null,
                        baseNeClass,domainList.toArray(new Long[domainList.size()]), null);
            }else {
                datas =  monitorService.findNeIdByParams(null,null,null,null,null,
                        baseNeClass, new Long[] { domainId }, null);

            }
            // 无数据时返回空值
            if (ObjectUtils.isEmpty(datas)){
                result.put("info", "抱歉，没有数据可供展示...");
                return new JsonModel(true, "无资源数据", result);
            }
            ArrayList arr = (ArrayList) datas.getObj();
            List<String> ne = Lists.newArrayList();
            for (int i = 0; i < arr.size(); i++) {
                ne.add(arr.get(i).toString());
            }
            neIds = ne.toArray(new String[ne.size()]);
        }
        List<Alert> list = rpcProcessService.findByChooseForLeaderview(neIds,1L);
        if (ObjectUtils.isEmpty(list)){
            result.put("info", "抱歉，没有数据可供展示...");
            return new JsonModel(true, "无资源数据", result);
        }
        Alert alert = list.get(0);
        NetworkEntity ne = rpcProcessService.findNetworkEntityByIdIn(alert.getObjectId());
        if (ObjectUtils.isEmpty(ne)){
            return new JsonModel(true, "资源选择错误");
        }
        result.put("state", rpcProcessService.getLevel(alert.getLevel()));
        result.put("info", alert.getRecentAlertBrief());
        result.put("ip", ne.getIp());
        result.put("title", ne.getIp());
        result.put("time", alert.getRecentAlertDateStr());
        return new JsonModel(true, result);
    }

    /**
     * 【3.0主页】，按级别统计资源的未处理告警条数
     * @param alertLevel 多个告警级别用,分隔
     * @param domainId 域ID
     * @param baseNeClass 资源父类型
     * @param neClass 资源子类型
     * @param neIds 多个资源ID用,分隔
     * @return
     */
    public JsonModel getStatByLevel(HttpSession session, String alertLevel, Long domainId,
                                    String baseNeClass, String neClass, String neIds) throws Exception{
        JsonModel jm = new JsonModel();
        if (domainId == null){
            List<Long> domainList = domainUtils.getUserDomainIds(session);
            jm= monitorService.findNeIdByParams(null, neIds, null, null, neClass, baseNeClass,
                    domainList.toArray(new Long[domainList.size()]) , null);
        }else {
            jm = monitorService.findNeIdByParams(null, neIds, null, null, neClass, baseNeClass,
                    new Long[] { domainId }, null);
        }
        ArrayList arr = (ArrayList) jm.getObj();
        return new JsonModel(true, rpcProcessService.getStatByLevel(arr, alertLevel, AlertType.Alert));
    }

    /**
     * 【3.0主页】，按资源类型统计资源的未处理告警条数
     * @param alertLevel 多个告警级别用,分隔
     * @param domainId 域ID
     * @param baseNeClass 资源父类型
     * @return
     */
    public JsonModel getStatByClass(HttpSession session, String alertLevel, Long domainId, String baseNeClass) throws Exception{
        JsonModel jm = new JsonModel();
        if (domainId == null){
            List<Long> domainList = domainUtils.getUserDomainIds(session);
            jm = monitorService.findNeListByParams(null, null, null, null, null, baseNeClass,
                    domainList.toArray(new Long[domainList.size()]) , null);
        }else {
            jm = monitorService.findNeListByParams(null, null, null, null, null, baseNeClass,
                    new Long[] { domainId }, null);
        }
        JSONArray neArray = JSON.parseArray(JSON.toJSONString(jm.getObj()));
        return new JsonModel(true, rpcProcessService.getStatByClass(neArray, baseNeClass, alertLevel,
                AlertType.Alert, null, false));
    }

    /**
     * 【3.0主页】，按资源统计资源的未处理告警条数
     * @param alertLevel 多个告警级别用,分隔
     * @param domainId 域ID
     * @param baseNeClass 资源父类型
     * @param neClass 资源子类型
     * @param neIds 多个资源ID用,分隔
     * @return
     */
    public JsonModel getStatByNe(String alertLevel, Long domainId, String baseNeClass, String neClass, String neIds) throws Exception{
        JsonModel jm = monitorService.findNeListByParams(null, neIds, null, null, neClass, baseNeClass,
                null == domainId ? null : new Long[] { domainId }, null);
        JSONArray neArray = JSON.parseArray(JSON.toJSONString(jm.getObj()));
        return new JsonModel(true, rpcProcessService.getStatByNe(neArray, alertLevel, AlertType.Alert));
    }


    /**
     * 大屏展示：根据选择的域、父类型、资源id和展示条数来查询最新的规定条数的告警
     * @param domainId
     * @param baseNeClass
     * @param neIds
     * @param number
     * @param session
     * @return
     */
    public JsonModel getAlertInfo(Long domainId, String baseNeClass, String[] neIds,
                                  Long number, HttpSession session, String[] column) throws Exception{
        JSONObject result = new JSONObject();
        List<String > diffColumns = Lists.newArrayList("状态","告警来源","IP地址","告警内容","告警时间");
        column = ObjectUtils.isEmpty(column) ? diffColumns.toArray(new String[diffColumns.size()]): column;
        JSONArray columns = newColumns(column);
        diffColumns.removeAll(columns);
        JsonModel neList = new JsonModel();
        if (ObjectUtils.isEmpty(neIds)){
            if (domainId == null){
                List<Long> domainList = domainUtils.getUserDomainIds(session);
                neList = monitorService.findNeIdByParams(null,null,null,null,null,
                        baseNeClass,domainList.toArray(new Long[domainList.size()]), null);
            }else {
                neList =  monitorService.findNeIdByParams(null,null,null,null,null,
                        baseNeClass, new Long[] { domainId }, null);

            }
            ArrayList arr = (ArrayList) neList.getObj();
            List<String> ne = Lists.newArrayList();
            for (int i = 0; i < arr.size(); i++) {
                ne.add(arr.get(i).toString());
            }
            neIds = ne.toArray(new String[ne.size()]);
        }
        List<Alert> list = rpcProcessService.findByChooseForLeaderview(neIds,number);
        if (CollectionUtils.isEmpty(list)){
            JSONObject obj = new JSONObject();
            JSONArray emp = new JSONArray();
            obj.put("columns", columns);
            obj.put("rows", emp);
            return new JsonModel(true, obj);
        }
        JSONArray rows = new JSONArray();
        list.forEach(alert -> {
            try {
                Map<String, String> row = new LinkedHashMap<>();
                row.put("状态", rpcProcessService.getLevel(alert.getLevel()));
                row.put("告警来源", alert.getAlertOrigin().toString());
                NetworkEntity ne = rpcProcessService.findNetworkEntityByIdIn(alert.getObjectId());
                if (ObjectUtils.isEmpty(ne)){
                    return;
                }
                row.put("IP地址", ne.getIp());
                row.put("告警内容", alert.getRecentAlertBrief());
                row.put("告警时间", alert.getRecentAlertDateStr());
                diffColumns.forEach(diff -> row.remove(diff));
                rows.add(row);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        if (ObjectUtils.isEmpty(rows)){
            JSONObject obj = new JSONObject();
            JSONArray emp = new JSONArray();
            obj.put("columns", columns);
            obj.put("rows", emp);
            return new JsonModel(true, obj);
        }
        result.put("columns",columns);
        result.put("rows",rows);
        return new JsonModel(true, result);
    }


    /**
     * 按资源类型统计资源的未处理告警条数，雷达图优化
     * @param session
     * @param alertLevel
     * @param domainId
     * @param baseNeClass
     * @param neClass
     * @param neIds
     * @return
     */
    public JsonModel getStatByClassForRadar(HttpSession session, String alertLevel,
                                            Long domainId, String baseNeClass, String neClass, String neIds) throws Exception{
        JsonModel jm = new JsonModel();
        if (domainId == null){
            List<Long> domainList = domainUtils.getUserDomainIds(session);
            jm = monitorService.findNeListByParams(null, neIds, null, null, neClass, baseNeClass,
                    domainList.toArray(new Long[domainList.size()]) , null);
        }else {
            jm = monitorService.findNeListByParams(null, neIds, null, null, neClass, baseNeClass,
                    new Long[] { domainId }, null);
        }
        JSONArray neArray = JSON.parseArray(JSON.toJSONString(jm.getObj()));
        return new JsonModel(true, rpcProcessService.getStatByClass(neArray, baseNeClass, alertLevel,
                AlertType.Alert, neClass, !Strings.isNullOrEmpty(neIds)));
    }

    /**
     * 设置用户的域参数
     *
     * @param query   告警查询参数
     * @param session 会话
     */
    protected void setUserDomainIds(AlertQuery query, HttpSession session) {
        Long[] domainIds = query.getDomainIds();// 指定查询的域
        if (!isSuper(session)) {
            List<Long> domains = userDomainIds(session);// 当前登录用户所在域ID
            if (domainIds != null && domainIds.length > 0) {
                domains.retainAll(Lists.newArrayList(domainIds));// 取交集
            }
            domains.add(-1L);
            domainIds = domains.toArray(new Long[domains.size()]);
        }
        query.setDomainIds(domainIds);
    }

    protected boolean isSuper(HttpSession session) {
        return SessionUtils.isSuperAdmin(session);
    }

    protected List<Long> userDomainIds(HttpSession session) {
        return domainUtils.getUserDomainIds(session);
    }

    private JSONArray newColumns(List<String> columnsList){
        String[] columns = columnsList.toArray(new String[columnsList.size()]);
        return newColumns(columns);
    }

    private JSONArray newColumns(String... columns){
        JSONArray jsonArray = new JSONArray();
        for (String column : columns) {
            jsonArray.add(column);
        }
        return jsonArray;
    }

    private JSONArray addColumns(JSONArray jsonArray, String... columns){
        for (String column : columns) {
            jsonArray.add(column);
        }
        return jsonArray;
    }

    private List<String> getAllColumns(JSONArray columns) {
        List<String > list = Lists.newArrayList();
        StreamSupport.stream(columns.spliterator(), false).map(s -> (String) s).forEach(s -> list.add(s));
        return list;
    }

}
