package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Longs;
import com.uxsino.authority.lib.model.DomainInfo;
import com.uxsino.authority.lib.util.DomainUtils;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.utils.Dates;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.leaderview.model.AlertType;
import com.uxsino.leaderview.model.alert.*;
import com.uxsino.leaderview.model.monitor.NetworkEntity;
import com.uxsino.leaderview.model.monitor.NetworkEntityCriteria;
import com.uxsino.leaderview.rpc.MCService;
import com.uxsino.leaderview.rpc.MonitorService;
import com.uxsino.leaderview.utils.MonitorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.uxsino.leaderview.utils.MonitorUtils.empObj;

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
        JSONObject result = new JSONObject();
        Boolean special = "virtualization".equals(alertType) || "cloud".equals(alertType) || "video".equals(alertType);
        AlertType alertType1 = special ? AlertType.Alert : AlertType.valueOf(alertType);
        String name;
        if (Objects.equals("virtualization", alertType)) {
            name = "虚拟化告警";
        } else if (Objects.equals("cloud", alertType)) {
            name = "云监控告警";
        } else if (Objects.equals("video", alertType)) {
            name = "视频监控告警";
        } else {
            name = alertType1.getText();
        }
        result.put("name", name + "数");
        result.put("unit", "");
        result.put("value", 0);
        AlertQuery queryObject = new AlertQuery();
        queryObject.setAlertType(alertType1);
        if (special) queryObject.setObjectType(alertType);
        setUserDomainIds(queryObject, session);
//        queryObject.setNotifyUserIds(alertInit.getFilterList().contains(alertType1.toString())
//                ? SessionUtils.getCurrentUserIdBySession(session) : null);
        queryObject.setNotifyUserIds(SessionUtils.getCurrentUserIdBySession(session));
        if (Objects.equals("video", alertType) ||Objects.equals("cloud", alertType)||Objects.equals("virtualization", alertType)) {
            queryObject.setSourceManage(false);
        } else {
            queryObject.setSourceManage(true);
        }
        if (AlertType.NELinkAlert.equals(alertType1)) {
            queryObject.setSourceManage(null);
        }
        String ids = rpcProcessService.getObjectIds(session, queryObject);
        if (StringUtils.isEmpty(ids) && !AlertType.TerminalAlert.equals(alertType1)
                && !AlertType.ThirdPartyAlert.equals(alertType1)) {
            return new JsonModel(true, result);
        }
        // 第三方告警中资源Id可能没有
        if (!AlertType.ThirdPartyAlert.equals(alertType1)) {
            queryObject.setObjectIds(ids);
        }
        if (AlertType.Alert.equals(alertType1) && queryObject.getIsAvailability() != null) {
            if (queryObject.getIsAvailability()) {
//                Set<String> neIds = loadRootTreeHandler.getNeIds();
//                if (!neIds.isEmpty()) {
//                    neIds.retainAll(Arrays.asList(ids.split(",")));
//                    queryObject.setObjectIds(org.apache.commons.lang3.StringUtils.join(neIds, ","));
//                } else {
//                    return new JsonModel(true, result);
//                }
                queryObject.setIndicatorIds("useable_state");
            } else {
                queryObject.setIndicatorIdNotIn(Lists.newArrayList("useable_state"));
            }
        }
        queryObject.setHandleStatusIn(new AlertHandleStatus[]{AlertHandleStatus.INVALID, AlertHandleStatus.FINISHED, AlertHandleStatus.RESTORED});
        if(!alertLevel.isEmpty()){
            String[] split = StringUtils.split(alertLevel, ",");
            List<Integer> levels = Lists.newArrayList();
            if (ObjectUtils.isEmpty(split)) {
                levels.add(Integer.valueOf(alertLevel));
            } else {
                for(String temp: split){
                    levels.add(Integer.valueOf(temp));
                }
            }
            queryObject.setLevels(levels);
        }
    //当alert上10w时，直接将整个告警list全部查过来耗费内存过多，且会造cpu占用飙升，改为使用count()方式获取
    /*
        List<AlertRecord> list = rpcProcessService.findAlert(queryObject, null);
        List<AlertHandleStatus> statuses =
                Lists.newArrayList(AlertHandleStatus.INVALID, AlertHandleStatus.FINISHED, AlertHandleStatus.RESTORED);
        if (!alertLevel.isEmpty()) {
            List<AlertRecord> levelList = Lists.newArrayList();
            String[] split = StringUtils.split(alertLevel, ",");
            List<String> level = Lists.newArrayList();
            if (ObjectUtils.isEmpty(split)) {
                level.add(alertLevel);
            } else {
                level = Arrays.asList(split);
            }
            for (AlertRecord alertRecord : list) {
                if (!statuses.contains(alertRecord.getHandleStatus())
                        && level.contains(alertRecord.getLevel().toString())){
                    levelList.add(alertRecord);
                }
            }
            list = levelList;
        }else {
            List<AlertRecord> temList = Lists.newArrayList();
            list.forEach(alertRecord -> {
                if (!statuses.contains(alertRecord.getHandleStatus())){
                    temList.add(alertRecord);
                }
            });
            list = temList;
        }
    */
        result.put("value", rpcProcessService.getAlertCount(queryObject, alertType1));
        return new JsonModel(true, result);
    }

    public JsonModel getOtherAlertInfo(HttpSession session, String type) throws Exception {
        JSONObject result = new JSONObject();
        try {
            AlertQuery queryObject = new AlertQuery();
            queryObject.setAlertType(AlertType.valueOf(type));
            queryObject.setSourceManage(true);
            setUserDomainIds(queryObject, session);
            AlertType alertType = queryObject.getAlertType();
            queryObject.setNotifyUserIds(SessionUtils.getCurrentUserIdBySession(session));
            if (AlertType.NELinkAlert.equals(alertType)) {
                queryObject.setSourceManage(null);
            }
            String ids = rpcProcessService.getObjectIds(session, queryObject);
            if (StringUtils.isEmpty(ids) && !AlertType.TerminalAlert.equals(alertType)
                    && !AlertType.ThirdPartyAlert.equals(alertType)) {
                result.put("info", "抱歉，没有数据可供展示...");
                return new JsonModel(true, result);
            }
            // 第三方告警中资源Id可能没有
            if (!AlertType.ThirdPartyAlert.equals(alertType)) {
                queryObject.setObjectIds(ids);
            }
            if (AlertType.Alert.equals(alertType) && queryObject.getIsAvailability() != null) {
                if (queryObject.getIsAvailability()) {
                    queryObject.setIndicatorIds("useable_state");
                } else {
                    queryObject.setIndicatorIdNotIn(Lists.newArrayList("useable_state"));
                }
            }
            // 查全部，过滤关键字后再排序、分页
            List<AlertRecord> list = rpcProcessService.findAlert(queryObject, null);
            List<AlertHandleStatus> statuses = Lists.newArrayList(AlertHandleStatus.INVALID,
                    AlertHandleStatus.FINISHED, AlertHandleStatus.RESTORED);
            list = list.stream().filter(alert -> !statuses.contains(alert.getHandleStatus())).collect(Collectors.toList());
            list = list.stream().sorted(Comparator.comparing(AlertRecord::getRecentAlertDate).reversed()).collect(Collectors.toList());
            if (ObjectUtils.isEmpty(list)) {
                result.put("info", "抱歉，没有数据可供展示...");
                return new JsonModel(true, result);
            }
            AlertRecord alert = list.get(0);
            if (!"ThirdPartyAlert".equals(type) && !"BusinessAlert".equals(type)
                    && !"ThirdPartyAlert".equals(type) && !"SystemAlert".equals(type) && !"TerminalAlert".equals(type)) {
                if ("NELinkAlert".equals(type)) {
                    result.put("title", alert.getObjectName());
                    result.put("ip", alert.getObjectName());
                } else {
                    NetworkEntity ne = rpcProcessService.findNetworkEntityById(alert.getObjectId());
                    result.put("title", ne.getIp());
                    result.put("ip", ne.getIp());
                }
            }
            result.put("state", rpcProcessService.getLevel(alert.getLevel()));
            result.put("time", alert.getRecentAlertDateStr());
            result.put("info", alert.getRecentAlertBrief());
            return new JsonModel(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(true, e.getMessage(), result);
        }
    }

    @SuppressWarnings("unchecked")
    public JsonModel getOtherAlertTable(HttpSession session, String type, Long number, String[] column, String dateFormatStr) throws Exception {
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        List<String > diffColumns;
        SimpleDateFormat oldDateFormat = new SimpleDateFormat(MonitorDataService.sdfStr);
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        if ("SysLogAlert".equals(type) || "SnmpTrapAlert".equals(type) || "TerminalAlert".equals(type) || "IpAlert".equals(type)) {
            diffColumns = Lists.newArrayList("状态","IP地址","告警内容","告警时间");
        }else {
            diffColumns = Lists.newArrayList("状态","告警内容","告警时间");
        }
        column = ObjectUtils.isEmpty(column) ? diffColumns.toArray(new String[diffColumns.size()]): column;
        JSONArray columns = newColumns(column);
        diffColumns.removeAll(getAllColumns(columns));

        AlertQuery queryObject = new AlertQuery();
        queryObject.setAlertType(AlertType.valueOf(type));
        queryObject.setSourceManage(true);
        setUserDomainIds(queryObject, session);
        AlertType alertType = queryObject.getAlertType();
        queryObject.setNotifyUserIds(SessionUtils.getCurrentUserIdBySession(session));
        if (AlertType.NELinkAlert.equals(alertType)) {
            queryObject.setSourceManage(null);
        }
        String ids = rpcProcessService.getObjectIds(session, queryObject);
        if (StringUtils.isEmpty(ids) && !AlertType.TerminalAlert.equals(alertType)
                && !AlertType.ThirdPartyAlert.equals(alertType)) {
            return new JsonModel(true, result);
        }
        // 第三方告警中资源Id可能没有
        if (!AlertType.ThirdPartyAlert.equals(alertType)) {
            queryObject.setObjectIds(ids);
        }
        if (AlertType.Alert.equals(alertType) && queryObject.getIsAvailability() != null) {
            if (queryObject.getIsAvailability()) {
//                Set<String> neIds = loadRootTreeHandler.getNeIds();
//                if (!neIds.isEmpty()) {
//                    neIds.retainAll(Arrays.asList(ids.split(",")));
//                    queryObject.setObjectIds(org.apache.commons.lang3.StringUtils.join(neIds, ","));
//                } else {
//                    return new JsonModel(true, result);
//                }
                queryObject.setIndicatorIds("useable_state");
            } else {
                queryObject.setIndicatorIdNotIn(Lists.newArrayList("useable_state"));
            }
        }
        // 查全部，过滤关键字后再排序、分页
        List<AlertRecord> list = rpcProcessService.findAlert(queryObject, null);
        List<AlertHandleStatus> statuses = Lists.newArrayList(AlertHandleStatus.INVALID,
                AlertHandleStatus.FINISHED, AlertHandleStatus.RESTORED);
        list = list.stream().filter(alert -> !statuses.contains(alert.getHandleStatus())).collect(Collectors.toList());
        list = list.stream().sorted(Comparator.comparing(AlertRecord::getRecentAlertDate).reversed()).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(list)) {
            result.put("info", "抱歉，没有数据可供展示...");
            return new JsonModel(true, result);
        }
        int num = 0;
        for (AlertRecord alert : list) {
            JSONObject row = new JSONObject(true);
            row.put("状态", rpcProcessService.getLevel(alert.getLevel()));
            if ("SysLogAlert".equals(type) || "SnmpTrapAlert".equals(type) || "TerminalAlert".equals(type) || "IpAlert".equals(type)) {
                row.put("IP地址", alert.getIp());
            }
            row.put("告警内容", alert.getRecentAlertBrief());
            row.put("告警时间", dateFormat.format(oldDateFormat.parse(alert.getRecentAlertDateStr())));
            if (++num > number) break;
            rows.add(row);
        }
        result.put("rows", rows);

        if (ObjectUtils.isEmpty(rows)){
            return new JsonModel(true, result);
        }
        for (int i = 0; i < rows.size(); i++) {
            JSONObject row = rows.getJSONObject(i);
            diffColumns.forEach(diff -> row.remove(diff));
        }

        result = MonitorUtils.newResultObj("columns", columns, "rows", rows);
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
        List<AlertLevel> levels = rpcProcessService.findAlertLevelList(alertLevelQuery);
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
                    Long domainId= Long.parseLong(map.get("id").toString());
                    NetworkEntityCriteria criteria = new NetworkEntityCriteria();
                    rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
                    List<String> neIds = rpcProcessService.getNeIds(criteria);
                    List<String> objectIds = Lists.newArrayList();
                    neIds.forEach(neId ->{
                        objectIds.add(neId);
                        System.out.println("alert获取资源ID" + neId);
                    });
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
        // 对0值做处理，将0值修改为null值，以便前端进行背景颜色展示
        for (Map.Entry<String,Integer> entry: result.entrySet()) {
            if (entry.getValue().equals(0)){
                entry.setValue(null);
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
                    Long domainId= Long.parseLong(map.get("id").toString());
                    NetworkEntityCriteria criteria = new NetworkEntityCriteria();
                    rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
                    List<String> neIds = rpcProcessService.getNeIds(criteria);
                    List<String> objectIds = Lists.newArrayList();
                    neIds.forEach(neId ->{
                        objectIds.add(neId);
                        System.out.println("alert获取资源ID" + neId);
                    });
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
            NetworkEntityCriteria criteria = new NetworkEntityCriteria();
            rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
            rpcProcessService.setCriteriaNeClass(criteria, baseNeClass);
            List<String > neIdList = rpcProcessService.getNeIds(criteria);
            neIds = neIdList.toArray(new String[neIdList.size()]);
            //如果父类型已经被指定但是依然没有符合条件的设备id，证明该账号下没有该类型的设备，直接返回无数据
            if(baseNeClass!=null && neIds.length==0){
                result.put("info", "抱歉，没有数据可供展示...");
                return new JsonModel(true, "无资源数据", result);
            }
        }
        List<AlertRecord> list = rpcProcessService.findByChooseForLeaderview(neIds,1);
        list = list.stream().sorted(Comparator.comparing(AlertRecord::getRecentAlertDate).reversed()).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(list)){
            result.put("info", "抱歉，没有数据可供展示...");
            return new JsonModel(true, "无资源数据", result);
        }
        AlertRecord alert = list.get(0);
        NetworkEntity ne = rpcProcessService.findNetworkEntityByIdIn(alert.getObjectId(),
                BaseNeClass.virtualization.toString().equals(baseNeClass));
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
                                    String baseNeClass, String neClass, String neIds, String topoId) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
        if(neIds!=null && !neIds.isEmpty())
            criteria.setIds(Lists.newArrayList(neIds.split(",")));
        rpcProcessService.setCriteriaNeClass(criteria, baseNeClass, neClass);
        //当传入topoId时，只查询该拓扑下的资源
        if (!Strings.isNullOrEmpty(topoId)) {
            criteria.setTopoId(topoId);
        }
        ArrayList arr = (ArrayList<String>) rpcProcessService.getNeIds(criteria);
        return new JsonModel(true, getStatByLevel(arr, alertLevel, AlertType.Alert));
    }

    private JSONObject getStatByLevel(ArrayList arr, String alertLevel, AlertType alertType) throws Exception{
        if (ObjectUtils.isEmpty(arr)) {
            return empObj();
        }
        Set<String> neIds = new LinkedHashSet<>();
        for (int i = 0; i < arr.size(); i++) {
            neIds.add(arr.get(i).toString());
        }

        List<Integer> levelList = new ArrayList<>();
        AlertLevelQuery query = new AlertLevelQuery();
        query.setStatus(AlertLevelStatus.ACTIVATED);
        List<AlertLevel> activeLevel = rpcProcessService.findAlertLevelList(query);
        if (Strings.isNullOrEmpty(alertLevel)) {
            for (AlertLevel level : activeLevel) {
                levelList.add(level.getLevel());
            }
        } else {
            levelList = Arrays.stream(alertLevel.split(",")).map(e -> {
                Long level = Longs.tryParse(e);
                return level != null ? level.intValue() : null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        /*
        AlertQuery alertQuery = new AlertQuery();
        alertQuery.setAlertType(alertType);
        alertQuery.setHandleStatus(AlertHandleStatus.UNTREATED);
        if (!ObjectUtils.isEmpty(neIds)) {
            alertQuery.setObjectIds(org.apache.commons.lang3.StringUtils.join(arr,","));
        }

        List<AlertRecord> alertList = rpcProcessService.findAlert(alertQuery, null);
        if (ObjectUtils.isEmpty(activeLevel)) {
            return empObj();
        }
         */
        //上述查询是将所有警告结果全部查询回来，然后在内存中用list.size()方法获取数量，这样数据库方面查询花费时间过多，且内存有可能溢出，
        //因此将count操作由alert方面在数据库中完成。
        StatisticsQuery statisticsQuery = new StatisticsQuery(org.apache.commons.lang3.StringUtils.join(arr, ","), levelList, alertType, AlertHandleStatus.UNTREATED);
        List<StatisticsResult> statisticsResults = rpcProcessService.getLevelStatisticsResult(statisticsQuery);
        Map<Object, String> levelMap = new LinkedHashMap<>();
        Map<Object, String> colorMap = new LinkedHashMap<>();
        activeLevel.forEach(e -> {
            levelMap.put(e.getLevel(), e.getName());
            colorMap.put(e.getLevel(), e.getColor());
        });
        JSONObject result = new JSONObject();
        JSONArray columns = new JSONArray();
        columns.add("告警类型");
        columns.add("数量");
        result.put("columns", columns);
        JSONArray rows = new JSONArray();
        // 告警颜色数组，顺序跟rows一致
        JSONArray colors = new JSONArray();
        for (Integer level: levelList) {
            JSONObject row = new JSONObject();
            row.put("告警类型", levelMap.get(level));
            //如果某一分类下没有该类型的通知，则alert方面不返回对应StatisticsResult，所以先全部初始化，然后如果有则在forEach中进行替换。
            row.put("数量", 0L);
            statisticsResults.forEach(e -> {
                if(e.getScopeValue().equals(String.valueOf(level))) {
                    row.put("数量", e.getAlertCount());
                }
            });
            rows.add(row);
            colors.add(colorMap.get(level));
        }
        result.put("rows", rows);
        result.put("colors", colors);
        return result;
    }

    public JsonModel getStatByStatusText(HttpSession session, String status, Long domainId,
                                     String baseNeClass, String neClass, String neIds) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
        if(StringUtils.isEmpty(neIds)){
            return new JsonModel(false,"未传入关键数据");
        }
        criteria.setIds(Lists.newArrayList(neIds));
        rpcProcessService.setCriteriaNeClass(criteria, baseNeClass, neClass);

        ArrayList arr = (ArrayList<String>) rpcProcessService.getNeIds(criteria);
        if (ObjectUtils.isEmpty(arr)) {
            return new JsonModel(false,"未查询到相关数据");
        }
        List<String> statusList = new ArrayList<>();
        if(StringUtils.isEmpty(status)){
            for (AlertHandleStatus value : AlertHandleStatus.values()) {
                statusList.add(value.toString());
            }
        }else {
            statusList = Arrays.asList(status.split(","));
        }

        long count = 0;
        StatisticsQuery statisticsQuery = new StatisticsQuery();
        statisticsQuery.setGroupField("handleStatus");
        statisticsQuery.setObjIds(neIds);
        statisticsQuery.setAlertType(AlertType.Alert);
        //不传就是查所有状态
        statisticsQuery.setHandleStatus(null);
        List<StatisticsResult> statisticsResults = rpcProcessService.getLevelStatisticsResult(statisticsQuery);
        for (String s: statusList) {
            for (int i = 0; i < statisticsResults.size(); i++) {
                StatisticsResult statisticsResult = statisticsResults.get(i);
                if(statisticsResult.getScopeValue().equals(s)) {
                    count += statisticsResult.getAlertCount();
                }
            }
        }
        if(!ObjectUtils.isEmpty(statisticsResults)){
            count = + statisticsResults.get(0).getAlertCount();
        }
        JSONObject result = new JSONObject();
        result.put("unit","个");
        result.put("value",count);
        result.put("info",count);
        result.put("name","资源告警数");
        return new JsonModel(true,result);
    }


    /**
     * 按状态统计资源的告警条数
     * @param domainId 域ID
     * @param baseNeClass 资源父类型
     * @param neClass 资源子类型
     * @param neIds 多个资源ID用,分隔
     * @return
     */
    public JsonModel getStatByStatus(HttpSession session, String status, Long domainId,
                                    String baseNeClass, String neClass, String neIds, String topoId) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
        if(neIds!=null && !neIds.isEmpty())
            criteria.setIds(Lists.newArrayList(neIds.split(",")));
        rpcProcessService.setCriteriaNeClass(criteria, baseNeClass, neClass);
        //当传入topoId时，只查询该拓扑下的资源
        if (!Strings.isNullOrEmpty(topoId)) {
            criteria.setTopoId(topoId);
        }
        ArrayList arr = (ArrayList<String>) rpcProcessService.getNeIds(criteria);
        return new JsonModel(true, getStatByStatus(arr, status, AlertType.Alert));
    }

    private Object getStatByStatus(ArrayList arr, String status, AlertType alertType) throws Exception{
        if (ObjectUtils.isEmpty(arr)) {
            return empObj();
        }
        Set<String> neIds = new LinkedHashSet<>();
        for (int i = 0; i < arr.size(); i++) {
            neIds.add(arr.get(i).toString());
        }

        StatisticsQuery statisticsQuery = new StatisticsQuery();
        statisticsQuery.setGroupField("handleStatus");
        statisticsQuery.setObjIds(org.apache.commons.lang3.StringUtils.join(arr, ","));
        statisticsQuery.setAlertType(alertType);

        List<String> statusList = new ArrayList<>();
        if(!ObjectUtils.isEmpty(status)) {
            statusList = Arrays.asList(status.split(","));
        }
        //用来判断状态参数的输入情况
        Integer type = 3;
        if(statusList.size() == 0){
            type = 0;
        }else if (statusList.size() == 1 && arr.size() == 1 ){
            //只选了一种资源、一种状态，用来作为文本框的返回
            type = 1;
        }
        if(type == 1) {
            statisticsQuery.setHandleStatus(AlertHandleStatus.valueOf(status));
        }
        List<StatisticsResult> statisticsResults = rpcProcessService.getLevelStatisticsResult(statisticsQuery);

        JSONObject result = new JSONObject();

        //List<String> statusList = new ArrayList<>();
        if (type == 1) {
            Object count ;
            if(ObjectUtils.isEmpty(statisticsResults)){
                count = 0;
            }else {
                count = statisticsResults.get(0).getAlertCount();
            }
            result.put("unit",null);
            result.put("总数",count);
            result.put("info",count);
            return result;
        }else {
            JSONArray columns = new JSONArray();
            columns.add("告警状态");
            columns.add("数量");
            result.put("columns", columns);
            JSONArray rows = new JSONArray();
            Map<Object,String> statusMap = new HashMap<>();
            for (AlertHandleStatus handleStatus:AlertHandleStatus.values()){
                statusMap.put(String.valueOf(handleStatus),handleStatus.getText());
                if(type == 0) {
                    String s = String.valueOf(handleStatus);
                    statusList.add(s);
                }
            }
            // 告警颜色数组，顺序跟rows一致
            JSONArray colors = new JSONArray();
            for (String s: statusList) {
                JSONObject row = new JSONObject();
                row.put("告警状态", statusMap.get(s));
                //如果某一分类下没有该类型的通知，则alert方面不返回对应StatisticsResult，所以先全部初始化，然后如果有则在forEach中进行替换。
                row.put("数量", 0L);
                statisticsResults.forEach(e -> {
                    if(e.getScopeValue().equals(s)) {
                        row.put("数量", e.getAlertCount());
                    }
                });
                rows.add(row);
                colors.add(null);
                result.put("rows", rows);
                result.put("colors", colors);
            }
            return result;
        }
    }

    /**
     * 【3.0主页】，按资源类型统计资源的未处理告警条数
     * @param alertLevel 多个告警级别用,分隔
     * @param domainId 域ID
     * @param baseNeClass 资源父类型
     * @return
     */
    public JsonModel getStatByClass(HttpSession session, String alertLevel, Long domainId, String baseNeClass) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
        rpcProcessService.setCriteriaNeClass(criteria, baseNeClass);
        criteria.setMonitoring(true);
        List<NetworkEntity> nes = rpcProcessService.getNeList(criteria);
        JSONArray neArray = JSON.parseArray(JSON.toJSONString(nes));
        return new JsonModel(true, getStatByClass(neArray, baseNeClass, alertLevel,
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
    public JsonModel getStatByNe(String alertLevel, Long domainId, String baseNeClass, String neClass, String neIds, HttpSession session) throws Exception{
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
        criteria.setIds(Lists.newArrayList(neIds.split(",")));
        rpcProcessService.setCriteriaNeClass(criteria, baseNeClass, neClass);
        criteria.setMonitoring(true);
        List<NetworkEntity> nes = rpcProcessService.getNeList(criteria);
        JSONArray neArray = JSON.parseArray(JSON.toJSONString(nes));
        return new JsonModel(true, getStatByNe(neArray, alertLevel, AlertType.Alert));
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
                                  Integer number, HttpSession session, String[] column, String topoId, String dateFormatStr) throws Exception{
        JSONObject result = new JSONObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        SimpleDateFormat oldDateFormat = new SimpleDateFormat(MonitorDataService.sdfStr);
        List<String > diffColumns = Lists.newArrayList("资源名称","告警级别","告警来源","IP地址","告警内容","告警时间","状态");
        column = ObjectUtils.isEmpty(column) ? diffColumns.toArray(new String[diffColumns.size()]): column;
        JSONArray columns = newColumns(column);
        diffColumns.removeAll(columns);
        boolean isVirtualization = BaseNeClass.virtualization.toString().equals(baseNeClass);
        if (ObjectUtils.isEmpty(neIds)){
            NetworkEntityCriteria criteria = new NetworkEntityCriteria();
            rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
            rpcProcessService.setCriteriaNeClass(criteria, baseNeClass);
            //当传入topoId时，只查询该拓扑下的资源
            if (!Strings.isNullOrEmpty(topoId)) {
                criteria.setTopoId(topoId);
            }
            criteria.setSourceManage(!isVirtualization);
            List<String > neIdList = rpcProcessService.getNeIds(criteria);
            neIds = neIdList.toArray(new String[neIdList.size()]);
        }
        List<AlertRecord> list = rpcProcessService.findByChooseForLeaderview(neIds,number);
        if (CollectionUtils.isEmpty(list)){
            JSONObject obj = new JSONObject();
            JSONArray emp = new JSONArray();
            obj.put("columns", columns);
            obj.put("rows", emp);
            return new JsonModel(true, obj);
        }
        JSONArray rows = new JSONArray();
        list = list.stream().sorted(Comparator.comparing(AlertRecord::getRecentAlertDate).reversed()).limit(number).collect(Collectors.toList());
        list.forEach(alert -> {
            try {
                NetworkEntity ne = rpcProcessService.findNetworkEntityByIdIn(alert.getObjectId(), isVirtualization);
                Map<String, String> row = new LinkedHashMap<>();
                row.put("资源名称", ne.getName());
                row.put("告警级别", rpcProcessService.getLevel(alert.getLevel()));
                row.put("告警来源", alert.getAlertOrigin());
                if (ObjectUtils.isEmpty(ne)){
                    return;
                }
                row.put("IP地址", ne.getIp());
                row.put("告警内容", alert.getRecentAlertBrief());
                row.put("告警时间", dateFormat.format(oldDateFormat.parse(alert.getRecentAlertDateStr())));
                row.put("状态", alert.getHandleStatusName());
                diffColumns.forEach(row::remove);
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
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
        rpcProcessService.setCriteriaNeClass(criteria, baseNeClass, neClass);
        criteria.setIds(Lists.newArrayList(neIds.split(",")));
        criteria.setMonitoring(true);
        List<NetworkEntity> nes = rpcProcessService.getNeList(criteria);
        JSONArray neArray = JSON.parseArray(JSON.toJSONString(nes));
        return new JsonModel(true, getStatByClass(neArray, baseNeClass, alertLevel,
                AlertType.Alert, neClass, !Strings.isNullOrEmpty(neIds)));
    }

    /**
     * 按资源类型统计资源的未处理告警条数
     */
    public JSONObject getStatByClass(JSONArray neArray, String baseClass, String levels, AlertType alertType,
                                     String neClassStr, boolean statisticsByNe) throws Exception{
        if (ObjectUtils.isEmpty(neArray)) {
            return empObj();
        }
        // 如果资源父类型为空，则按照BaseNeClass.values进行分类，如果不为空，则按照neClass分类
        boolean isBaseClass = Strings.isNullOrEmpty(baseClass);
        // 如果资源子类型不为空，或者已选具体资源，则按照资源进行分类
        boolean isByNe = !Strings.isNullOrEmpty(neClassStr) || statisticsByNe;
        // 如果选择了子类型，但是未选具体资源，则不予展示。
        if (!Strings.isNullOrEmpty(neClassStr) && !statisticsByNe) {
            return empObj();
        }
        // 基本资源类型对应的资源ID（多个资源ID用,分隔）
        Map<String, StringBuilder> typeIdMap = Maps.newHashMap();
        if (!isByNe) {
            for (int i = 0; i < neArray.size(); i++) {
                JSONObject ne = neArray.getJSONObject(i);
                NeClass neClass = NeClass.valueOf(ne.getString("neClass"));
                String type = isBaseClass ? neClass.getBaseNeClass().getText() : neClass.getText();
                StringBuilder ids = typeIdMap.get(type);
                ids = null != ids ? ids : new StringBuilder();
                typeIdMap.put(type, ids.append(ne.getString("id")).append(","));
            }
        } else {
            for (int i = 0; i < neArray.size(); i++) {
                JSONObject ne = neArray.getJSONObject(i);
                String type = ne.getString("name") + ne.getString("ip");
                StringBuilder ids = typeIdMap.get(type);
                ids = null != ids ? ids : new StringBuilder();
                typeIdMap.put(type, ids.append(ne.getString("id")).append(","));
            }
        }

        List<AlertLevel> allLevel = rpcProcessService.findAlertLevelList(new AlertLevelQuery());
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(allLevel)) {
            return empObj();
        }

        // 用户选定的告警等级列表
        List<Integer> chosenLevels = new ArrayList<>();
        if (Strings.isNullOrEmpty(levels)) {
            AlertLevelQuery query = new AlertLevelQuery();
            query.setStatus(AlertLevelStatus.ACTIVATED);
            List<AlertLevel> activeLevel = rpcProcessService.findAlertLevelList(query);
            for (AlertLevel level : activeLevel) {
                chosenLevels.add(level.getLevel());
            }
        } else {
            chosenLevels = Arrays.stream(levels.split(",")).map(e -> {
                Long level = Longs.tryParse(e);
                return level != null ? level.intValue() : null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
        // 告警等级和告警名称的映射集合
        Map<Integer, String> levelNameMap = new LinkedHashMap<>();
        Map<Integer, String> colorMap = new LinkedHashMap<>();
        allLevel.forEach(e -> {
            levelNameMap.put(e.getLevel(), e.getName());
            colorMap.put(e.getLevel(), e.getColor());
        });
        JSONObject result = new JSONObject();
        JSONArray columns = new JSONArray();
        if (isByNe) {
            columns.add("资源名");
        } else {
            columns.add("资源类型");
        }
        // 告警颜色数组，顺序和columns一致
        JSONArray colors = new JSONArray();
        chosenLevels.forEach(e -> {
            columns.add(levelNameMap.get(e));
            colors.add(colorMap.get(e));
        });
        result.put("colors", colors);
        result.put("columns", columns);
        JSONArray rows = new JSONArray();
        for (Map.Entry<String, StringBuilder> entry : typeIdMap.entrySet()) {
            JSONObject row = new JSONObject();
            if (isByNe) {
                row.put("资源名", entry.getKey());
            } else {
                row.put("资源类型", entry.getKey());
            }
            String[] idList = entry.getValue().toString().split(",");
            String[] idParam = new String[idList.length%50==0?(idList.length/50):(idList.length/50 + 1)];
            int j = 0;
            List<StatisticsResult> levelResult = null;
            for(int i=0; i<idParam.length; i++){
                StringBuffer sb = new StringBuffer();
                while(true){
                    sb.append(idList[j]);
                    sb.append(",");
                    j++;
                    //如果是前面几次查询，只能刚满50个
                    if(i!=idParam.length-1 && j%50==0)
                        break;
                    //最后一次查询，有多少则加多少
                    if(i==idParam.length-1 && j==idList.length)
                        break;
                }
                //去掉最后一个逗号
                sb.setLength(sb.length()-1);
                idParam[i] = sb.toString();
                StatisticsQuery query = new StatisticsQuery(idParam[i], chosenLevels, alertType, AlertHandleStatus.UNTREATED);
                //如果是第一次查询，则将就这个List作为levelResult，之后的查询结果则全部加入之前的这个List
                if(i == 0)
                    levelResult = rpcProcessService.getLevelStatisticsResult(query);
                else
                    levelResult.addAll(rpcProcessService.getLevelStatisticsResult(query));
            }
            List<Integer> existLevel = new ArrayList<>(chosenLevels);
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(levelResult)) {
                chosenLevels.forEach(e -> row.put(levelNameMap.get(e), 0L));
                existLevel.removeAll(chosenLevels);
            } else {
                levelResult.forEach(e -> {
                    row.put(levelNameMap.get(Integer.valueOf(e.getScopeValue())), e.getAlertCount());
                    existLevel.remove(Integer.valueOf(e.getScopeValue()));
                });
            }
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(existLevel)) {
                existLevel.forEach(e -> row.put(levelNameMap.get(e), 0L));
            }
            rows.add(row);
        }
        result.put("rows", rows);
        return result;
    }

    /**
     * 【主页3.0】按资源统计告警个数
     */
    @SuppressWarnings({ "unchecked" })
    public JSONObject getStatByNe(JSONArray neArray, String levels, AlertType alertType) throws Exception{
        if (ObjectUtils.isEmpty(neArray) || Strings.isNullOrEmpty(levels)) {
            return empObj();
        }
        // 资源ID和name的映射表
        Map<String, String> idNameMap = new HashMap<>(neArray.size());
        for (int i = 0; i < neArray.size(); i++) {
            JSONObject ne = neArray.getJSONObject(i);
            idNameMap.put(ne.getString("id"), ne.getString("name"));
        }
        // 用户选定的告警等级列表
        List<Integer> chosenLevels = Arrays.stream(levels.split(",")).map(Integer::valueOf)
                .collect(Collectors.toList());
        // 统计各个资源的告警情况
        StatisticsQuery query = new StatisticsQuery(String.join(",", idNameMap.keySet()), chosenLevels, alertType,
                AlertHandleStatus.UNTREATED);
        // data可能为空
        List<StatisticsResult> data = rpcProcessService.getLevelStatisticsResult(query);
        List<AlertLevel> allLevel = rpcProcessService.findAlertLevelList(new AlertLevelQuery());
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(allLevel)) {
            return empObj();
        }
        // 告警等级和告警名称的映射集合
        Map<Integer, String> levelNameMap = new LinkedHashMap<>();
        allLevel.forEach(e -> levelNameMap.put(e.getLevel(), e.getName()));

        JSONObject result = new JSONObject();
        JSONArray columns = new JSONArray();

        columns.add("资源名称");
        chosenLevels.forEach(e -> columns.add(levelNameMap.get(e)));
        result.put("columns", columns);

        JSONArray rows = new JSONArray();
        for (String objId : idNameMap.keySet()) {
            JSONObject row = new JSONObject();
            // 被选中的levelIds
            List<Integer> chosenIds = new ArrayList<>(chosenLevels);
            row.put("资源名称", idNameMap.get(objId));
            // 如果data为null，代表根据条件未能查询到这些资源的告警信息
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(data)) {
                chosenIds.forEach(e -> row.put(levelNameMap.get(e), 0));
            } else {
                // 在data中已经查询出来的level集合，用于后续判断哪些为空
                Set<Integer> existLevels = new HashSet<>();
                data.stream().filter(e -> ObjectUtils.nullSafeEquals(e.getScopeValue(), objId)).forEach(e -> {
                    existLevels.add(e.getAlertLevel());
                    row.put(levelNameMap.get(e.getAlertLevel()), e.getAlertCount());
                });
                // 移除掉已经添加的等级，只剩下应该为0的告警个数
                chosenIds.removeAll(existLevels);
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(chosenIds)) {
                    chosenIds.forEach(e -> row.put(levelNameMap.get(e), 0));
                }
            }
            rows.add(row);
        }
        result.put("rows", rows);
        return result;
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

    public Object CountTopoAlert(String topoId, String alertLevel) throws Exception {
        List<NetworkEntity> nes = Lists.newArrayList();
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria.setTopoId(topoId);
        criteria.setMonitoring(true);
        nes = rpcProcessService.getNeList(criteria);
        String ids = nes.stream().map(NetworkEntity::getId).collect(Collectors.joining(","));

        AlertQuery queryObject = new AlertQuery();
        queryObject.setObjectIds(ids);
        queryObject.setAlertType(AlertType.Alert);
        queryObject.setSourceManage(true);
        if(!alertLevel.isEmpty()){
            String[] split = StringUtils.split(alertLevel, ",");
            List<Integer> levels = Lists.newArrayList();
            if (ObjectUtils.isEmpty(split)) {
                levels.add(Integer.valueOf(alertLevel));
            } else {
                for(String temp: split){
                    levels.add(Integer.valueOf(temp));
                }
            }
            queryObject.setLevels(levels);
        }

        JSONObject result = new JSONObject();
        result.put("count", rpcProcessService.getAlertCount(queryObject, AlertType.Alert));

        return result;
    }
}
