package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.authority.lib.util.DomainUtils;
import com.uxsino.commons.db.redis.service.SiteUserRedis;
import com.uxsino.commons.model.DataSourceType;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.utils.ArithUtils;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.commons.utils.TimeUtils;
import com.uxsino.leaderview.model.AlertType;
import com.uxsino.leaderview.model.alert.*;
import com.uxsino.leaderview.model.business.*;
import com.uxsino.leaderview.utils.MonitorUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.uxsino.leaderview.model.business.Indicator.alert_count;

@Component
public class BusinessDataService {

    @Autowired
    private RpcProcessService rpcProcessService;

    @Autowired
    private BusinessDataParamsService businessDataParamsService;

    @Autowired
    private SiteUserRedis userRedis;

    @Autowired
    DomainUtils domainUtils;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 根据业务ID和指标名获取一个具体的指标数据
     * @param session 会话
     * @param business 业务Id
     * @param indicator 指标名
     */
    public JsonModel indicatorValue(HttpSession session, String business, String indicator) throws Exception{
        if (Strings.isNullOrEmpty(business)){
            JSONObject empty = new JSONObject();
            empty.put("name", Indicator.valueOf(indicator).getText());
            empty.put("value", null);
            empty.put("unit", "");
            return new JsonModel(true, empty);
        }
        JSONArray arr = getAllCurrIndVal(session, business);
        JSONObject obj = new JSONObject();
        for (int i = 0; i < arr.size(); i++) {
            if (arr.getJSONObject(i).getString("indicatorId").equals(indicator)){
                obj = arr.getJSONObject(i);
                break;
            }
        }
        // 返回的结果
        JSONObject result = new JSONObject();
        result.put("name", obj.getString("indicatorText"));
        result.put("value", obj.getString("indicatorValue"));
        if (!Strings.isNullOrEmpty(obj.getString("indicatorUnit"))){
            result.put("unit", obj.getString("indicatorUnit"));
        }else {
            result.put("unit", "");
        }
        return new JsonModel(true, result);
    }

    /**
     * 多业务多指标统计
     * @param session 会话
     * @param business 业务Id
     * @param indicator 指标名
     */
    public JsonModel multipleBusinessIndicator(HttpSession session, String[] business, String indicator) throws Exception{
        Period period = Period._1month;
        List<ArrayList> list = getCurrIndVal(session, period);
        JSONObject allValue = new JSONObject();
        List<String> indicatorList = Arrays.asList(indicator.split(","));
        List<String> nameList = Lists.newArrayList();
        for (String businessId : business) {
            String businessName = null;
            for (ArrayList objArr : list) {
                if (objArr.get(0).toString().equals(businessId)){
                    businessName = objArr.get(1).toString();
                }
            }
            if (Strings.isNullOrEmpty(businessName)) continue;
            JSONArray arrOneBusiness = new JSONArray();
            JSONArray arr = getAllCurrIndVal(session, businessId);
            for (int i = 0; i < arr.size(); i++) {
                if (indicatorList.contains(arr.getJSONObject(i).getString("indicatorId"))){
                    arrOneBusiness.add(arr.getJSONObject(i));
                }
            }
            nameList.add(businessName);
            allValue.put(businessName, arrOneBusiness);
        }
        if (ObjectUtils.isEmpty(allValue)){
            return new JsonModel(true, empObj());
        }
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        columns.add("业务");
        Map<String,Integer> labelMap = Maps.newHashMap();
        for (String businessName : nameList){
            JSONArray arr = allValue.getJSONArray(businessName);
            JSONObject row = new JSONObject();
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String unit = obj.getString("indicatorUnit");
                row.put(obj.getString("indicatorText") + (Strings.isNullOrEmpty(unit)? "" : "(" + unit + ")"),
                        obj.getString("indicatorValue"));
                labelMap.put(obj.getString("indicatorText") + (Strings.isNullOrEmpty(unit)? "" : "(" + unit + ")"), 0);
            }
            row.put("业务", businessName);
            rows.add(row);
        }
        for (Map.Entry<String,Integer> entry: labelMap.entrySet()) {
            columns.add(entry.getKey());
        }
        result.put("rows", rows);
        result.put("columns", columns);
        return new JsonModel(true, result);
    }

    /**
     * 业务统计-按状态接口：根据选择的业务，按状态进行统计
     * @param session 会话
     * @param business 业务id
     */
    public JsonModel businessStatisticsByStatus(HttpSession session, String[] business) throws Exception{
        JSONArray arr = getBusStatus(session,business);
        JSONObject json = new JSONObject();
        JSONArray columns = new JSONArray();
        columns.add("状态");
        columns.add("数量");
        json.put("columns", columns);
        JSONArray rows = new JSONArray();
        Map<String ,Integer> statistic = Maps.newHashMap();
        for (int i = 0; i < arr.size(); i++) {
            String key = arr.getJSONObject(i).getString("runStatus");
            Integer value = statistic.get(key);
            statistic.put(key, value == null ? 1 : value + 1);
        }
        for (Map.Entry entry: statistic.entrySet()) {
            JSONObject row = new JSONObject();
            row.put("状态", entry.getKey());
            row.put("数量", entry.getValue());
            rows.add(row);
        }
        json.put("rows",rows);
        return new JsonModel(true, json);
    }

    /**
     * 告警统计-按业务 接口：根据选择的业务，对告警进行统计
     * @param session 会话
     * @param business 业务
     */
    public JsonModel getStatByBusiness(HttpSession session, String[] business) throws Exception{
        //如果业务选择不限，统计所有业务
        List<String> businessList = Lists.newArrayList();
        List<String> nameList = Lists.newArrayList();
        JSONArray businessArr = getBusStatus(session, business);
        if(ObjectUtils.isEmpty(businessArr)){
            return new JsonModel(true, empObj());
        }
        Iterator<Object> it = businessArr.iterator();
        while(it.hasNext()){
            JSONObject obj = (JSONObject)it.next();
            businessList.add(obj.getString("id"));
            nameList.add(obj.getString("name"));
        }
        List<JSONArray> arrList = Lists.newArrayList();
        for (String id : businessList) {
            arrList.add(getAllCurrIndVal(session,id));
        }
        JSONObject json = new JSONObject();
        JSONArray columns = new JSONArray();
        columns.add("业务");
        columns.add("数量");
        json.put("columns", columns);
        JSONArray rows = new JSONArray();
        for (int i = 0; i < arrList.size(); i++) {
            JSONArray arr = arrList.get(i);
            JSONObject row = new JSONObject();
            row.put("业务", nameList.get(i));
            for (int j = 0; j < arr.size(); j++) {
                if (!"alert_count".equals(arr.getJSONObject(j).getString("indicatorId"))) continue;
                row.put("数量", arr.getJSONObject(j).get("indicatorValue"));
                rows.add(row);
            }
        }
        json.put("rows", rows);
        return new JsonModel(true, json);
    }

    /**
     * 根据状态统计业务数量
     * @param session 会话
     * @param status 状态
     */
    public JsonModel countBusiness(HttpSession session, Boolean status) throws Exception {
        BusinessQuery query = new BusinessQuery();
        Period period = query.getPeriod();
        period = period == null ? Period._1month : period;
        query.setPeriod(period);
        SortWay sortWay = query.getSortWay();
        if (sortWay == null || SortWay.custom.equals(sortWay)) {
            SortWayConf sortWayConf = getUserSortWay(session, businessDataParamsService.findNotDeletedIds());
            sortWay = sortWayConf.getSortWay();
        }
        query.setSortWay(sortWay);
        List<ArrayList> list = rpcProcessService.getCurrIndVal(query);
        if (!ObjectUtils.isEmpty(status)){
            List<ArrayList> newList = Lists.newArrayList();
            for (ArrayList obj: list) {
                if (obj == null || obj.get(3) == null) continue;
                if (RunStatus.Unavailable.toString().equals(obj.get(3).toString()) ^ status){
                    newList.add(obj);
                }
            }
            list = newList;
        }
        JSONObject result = new JSONObject();
        result.put("name","业务数量");
        result.put("value", list.size());
        result.put("unit", "");
        return new JsonModel(true, result);
    }

    /**
     * 业务告警信息接口，表格
     * @param session 会话
     * @param business 业务Id
     * @param number 展示条数
     */
    public JsonModel getBusinessAlertInfoForTable(HttpSession session, String business, Integer number, String dateFormatStr) throws Exception {
        JSONArray columns = new JSONArray();
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        columns.add("告警级别");
        columns.add("业务名称");
        columns.add("业务指标");
        columns.add("告警内容");
        columns.add("首次告警时间");
        List<AlertRecord> alertRecords = getAlertRecord(business, session);
        if (CollectionUtils.isEmpty(alertRecords)) {
            JSONObject obj = new JSONObject();
            JSONArray emp = new JSONArray();
            obj.put("columns", columns);
            obj.put("rows", emp);
            return new JsonModel(true, obj);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        Set<String> bnsIds = alertRecords.stream().map(AlertRecord::getObjectId).collect(Collectors.toSet());

        // 根据业务id获取业务id与业务名称键值对
        BusinessQuery query = new BusinessQuery();
        query.setIds(Lists.newArrayList(bnsIds));
        List<BusinessSystem> bnsList = rpcProcessService.findBusinessSystems(query);
        Map<String, String> map = bnsList.stream().collect(Collectors.toMap(BusinessSystem::getId, BusinessSystem::getName));

        // 获取告警级别键值对
        AlertLevelQuery levelQuery = new AlertLevelQuery();
        levelQuery.setStatus(AlertLevelStatus.ACTIVATED);
        List<AlertLevel> activeLevel = rpcProcessService.findAlertLevelList(levelQuery);
        Map<Integer, String> levelMap = activeLevel.stream().collect(Collectors.toMap(AlertLevel::getLevel, AlertLevel::getName));

        int j = 1;
        for (AlertRecord alertRecord : alertRecords) {
            JSONObject row = new JSONObject(true);
            row.put("告警级别", levelMap.get(alertRecord.getLevel()));
            row.put("业务名称", map.get(alertRecord.getObjectId()));
            for (Indicator ind: Indicator.values()) {
                if (ind.toString().equals(alertRecord.getIndicatorName())) {
                    row.put("业务指标", ind.getText());
                }
            }
            row.put("告警内容", alertRecord.getRecentAlertBrief());
            row.put("首次告警时间", dateFormat.format(alertRecord.getFirstAlertDate()));
            rows.add(row);
            if (++j > number)
                break;
        }
        result.put("columns", columns);
        result.put("rows", rows);
        return new JsonModel(true, result);
    }

    /**
     * 业务告警信息接口，文字格式
     * @param session 会话
     * @param business 业务Id
     */
    public JsonModel getBusinessAlertInfoForText(HttpSession session, String business) throws Exception{
        List<AlertRecord> typeList = getAlertRecord(business, session);
        if (ObjectUtils.isEmpty(typeList)) {
            JSONObject obj = new JSONObject();
            obj.put("info", "抱歉，没有数据可供展示...");
            return new JsonModel(true, null, obj);
        }
        JSONObject result = new JSONObject();
        // 根据业务id获取业务id与业务名称键值对
        BusinessQuery query = new BusinessQuery();
        query.setIds(Lists.newArrayList(business));
        List<BusinessSystem> bnsList = rpcProcessService.findBusinessSystems(query);
        Map<String, String> map = bnsList.stream().collect(Collectors.toMap(BusinessSystem::getId, BusinessSystem::getName));

        // 获取告警级别键值对
        AlertLevelQuery levelQuery = new AlertLevelQuery();
        levelQuery.setStatus(AlertLevelStatus.ACTIVATED);
        List<AlertLevel> activeLevel = rpcProcessService.findAlertLevelList(levelQuery);
        Map<Integer, String> levelMap = activeLevel.stream().collect(Collectors.toMap(AlertLevel::getLevel, AlertLevel::getName));

        for (AlertRecord alertRecord : typeList) {
            result.put("state", levelMap.get(alertRecord.getLevel()));
            result.put("name", map.get(alertRecord.getObjectId()));
            result.put("title", alertRecord.getObjectName());
            result.put("info", alertRecord.getRecentAlertBrief());
            result.put("time", TimeUtils.formatTime(alertRecord.getFirstAlertDate()));
        }
        return new JsonModel(true, null, result);
    }

    /**
     * 根据业务ID和指标名来展示历史指标数据
     * @param session 会话
     * @param business 业务Id
     * @param indicator 指标名
     * @param period 统计时段
     */
    public JsonModel getHistoryValue(HttpSession session, String[] business, Indicator indicator, String period,String dateFormatStr) throws Exception{
        if (ObjectUtils.isEmpty(business) || ObjectUtils.isEmpty(indicator)){
            return new JsonModel(true, empObj());
        }
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        columns.add("采集时间");
        Date endDate = new Date();
        Date startDate = new Date();
        int interval = 1;
        if ("_1day".equals(period)){
            startDate = new Date(endDate.getTime() - (1000 * 60 * 60 * 24L));
            interval = 5;
        }else if ("_1week".equals(period)){
            startDate = new Date(endDate.getTime() - (1000 * 60 * 60 * 24L * 7));
            interval = 60 * 8;
        }else if ("_1month".equals(period)){
            startDate = new Date(endDate.getTime() - (1000 * 60 * 60 * 24L * 30));
            interval = 60 * 12;
        }

        List<Date> allDate = Lists.newArrayList();
        Date tempDate = new Date(startDate.getTime());
        Date tempEnd = new Date(endDate.getTime() + 1000 * 60 * interval);
        while (tempDate.before(tempEnd)) {
            Date itm = new Date(tempDate.getTime());
            allDate.add(itm);
            tempDate.setTime(tempDate.getTime() + 1000 * 60 * interval);
        }

        //进行权限过滤
        JSONArray businessArr = getBusStatus(session, business);
        JSONObject allData = new JSONObject();
        for (int i = 0; i < businessArr.size(); i++) {
            String businessId = businessArr.getJSONObject(i).getString("id");
            BnsIndValQuery query = new BnsIndValQuery();
            query.setBnsId(businessId);
            query.setIndicatorName(indicator.name());
            query.setStartDate(startDate);
            query.setEndDate(endDate);
            query.setPagination(false);
            JSONArray arr = rpcProcessService.findBnsHistoryValue(query);
            JSONArray filArr = new JSONArray();
            // 遍历一遍数组，按照时间取值
            for (int j = 0; j < allDate.size() - 1; j++) {
                Long start = allDate.get(j).getTime();
                Long end = allDate.get(j + 1).getTime();
                JSONArray array = new JSONArray(arr);
                array = MonitorUtils.filter(array, data -> data.getLong("fetchDate") < end && data.getLong("fetchDate") > start);
                if (!ObjectUtils.isEmpty(array)){
                    JSONObject obj = array.getJSONObject(0);
                    obj.put("fetchDate", start);
                    filArr.add(obj);
                }
            }
            allData.put(businessArr.getJSONObject(i).getString("name"), filArr);

        }
        //组装返回数据
        JSONArray wrapArr = new JSONArray();
        for (int i = 0; i < businessArr.size(); i++) {
            String businessName = businessArr.getJSONObject(i).getString("name");
            columns.add(businessName);
            JSONArray array = allData.getJSONArray(businessName);
            for (int j = 1; j < array.size(); j++) {
                JSONObject obj = array.getJSONObject(j);
                obj.put("name", businessName);
                obj.getLong("fetchDate");
                wrapArr.add(obj);
            }
        }
        //遍历统计所有数据，将时间相同的数据归为一组数据用于展示
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
        List<Long> usedData = Lists.newArrayList();
        for (int i = 0; i < wrapArr.size(); i++) {
            JSONObject obj = wrapArr.getJSONObject(i);
            JSONObject row = new JSONObject();
            Long fetchDateLong = obj.getLong("fetchDate");
            if (usedData.contains(fetchDateLong)) continue;
            usedData.add(fetchDateLong);
            row.put("采集时间", dateFormat.format(new Date(fetchDateLong)));
            row.put(obj.getString("name"), obj.getDoubleValue("value"));
            for (int j = i + 1; j < wrapArr.size(); j++) {
                JSONObject temObj = wrapArr.getJSONObject(j);
                if (fetchDateLong.compareTo(temObj.getLong("fetchDate")) == 0){
                    row.put(temObj.getString("name"), temObj.getDoubleValue("value"));
                }
            }
            rows.add(row);
        }
        result.put("columns", columns);
        result.put("rows", rows);
        result.put("unit", indicator.getUnit());
        return new JsonModel(true, result);
    }

    /**
     * 根据业务ID和指标名和展示条数来展示指标TOPN
     * @param business 业务Id
     * @param indicator 指标名
     * @param number 展示条数
     * @param order 排序方式
     * @param bar 柱状图类型
     */
    public JsonModel getIndicatorTOPN(HttpSession session, String[] business, String indicator,
                                      Integer number, String order, boolean bar)throws Exception {
        Indicator indicatorEnum = Indicator.valueOf(indicator);
        if (ObjectUtils.isEmpty(indicatorEnum)) {
            return new JsonModel(true, "不支持该指标");
        }
        //排序获取可查看的前n个业务指标
        JSONArray topBnsInds = getTopBusinessInd(session, business, indicatorEnum, order, number);
        //根据order参数，对前n个业务指标值进行排序
        sortByorder(order, topBnsInds);
        // 封装返回的结果
        JSONObject result = new JSONObject();
        if (ObjectUtils.isEmpty(topBnsInds)){
            return new JsonModel(true, empObj());
        }
        JSONArray columns = new JSONArray();
        columns.add("业务名称");
        String IndName = indicatorEnum.getText();
        String unit = indicatorEnum.getUnit();
        if (!Strings.isNullOrEmpty(unit)) {
            IndName = IndName + "(" + unit + ")";
        } else if (!topBnsInds.isEmpty() && topBnsInds.getJSONObject(0).containsKey("unit") && StringUtils
                .isNotBlank(topBnsInds.getJSONObject(0).getString("unit"))) {
            unit = topBnsInds.getJSONObject(0).getString("unit");
            IndName = IndName + "(" + unit + ")";
        }
        columns.add(IndName);
        JSONArray rows = new JSONArray();
        for (Object topBnsIndObj : topBnsInds) {
            JSONObject topBnsInd = JSONObject.parseObject(JSON.toJSONString(topBnsIndObj));
            String businessName = topBnsInd.getString("name");
            String indVal = topBnsInd.getString("indVal");
            JSONObject row = new JSONObject();
            row.put("业务名称",businessName);
            row.put(IndName,indVal);
            rows.add(row);
        }
        result.put("columns",columns);
        result.put("rows",rows);
        if (bar){
            if (!ObjectUtils.isEmpty(rows)){
                JSONArray temRows = new JSONArray();
                for (int i = rows.size() - 1; i >= 0; i--) {
                    JSONObject obj = rows.getJSONObject(i);
                    temRows.add(obj);
                }
                result.put("rows",temRows);
            }
        }
        return new JsonModel(true, result);
    }

    private List<AlertRecord> getAlertRecord(List<String> business, HttpSession session)throws Exception{
        if (ObjectUtils.isEmpty(business)){
            return getAlertRecord(new String() , session);
        }
        return getAlertRecord(StringUtils.join(business, ","), session);
    }

    private List<AlertRecord> getAlertRecord(String business, HttpSession session) throws Exception{
        AlertQuery query = new AlertQuery();
        query.setAlertType(AlertType.BusinessAlert);
        query.setObjectIds(business);
        // 进行域过滤
        List<Long> domainId = domainUtils.getUserDomainIds(session);
        query.setDomainIds(domainId.toArray(new Long[domainId.size()]));
        query.setHandleStatusNotIn(new AlertHandleStatus[] { AlertHandleStatus.INVALID, AlertHandleStatus.FINISHED,
                AlertHandleStatus.RESTORED });
        return rpcProcessService.findAlert(query, null);
    }

    /**
      * 获取某个业务的状态
      * @param session 会话
      * @param business 业务id
      */
    private JSONArray getBusStatus(HttpSession session, String[] business) throws Exception{
        JSONArray arr = new JSONArray();
        Period period = Period._1month;
        List<ArrayList> list = getCurrIndVal(session, period);
        list = list == null || list.isEmpty() ? new ArrayList<>() : list;
        // 权限过滤
        DataSourceType dataSourceType = DataSourceType.all;
        Map<String, JSONObject> tmpUserMap = new HashMap<>();
        for (ArrayList objArr : list) {
            String createUserId = String.valueOf(objArr.get(9));
            String handoverId = String.valueOf(objArr.get(10));
            String shareConfStr = String.valueOf(objArr.get(11));
            JSONObject shareConf = null;
            if (org.apache.commons.lang.StringUtils.isNotBlank(shareConfStr)) {
                shareConf = JSONObject.parseObject(shareConfStr);
            }
            JSONObject obj = new JSONObject();
            JSONObject createUser = tmpUserMap.containsKey(createUserId) ? tmpUserMap.get(createUserId)
                    : JSONObject.parseObject(userRedis.get(createUserId));
            tmpUserMap.put(createUserId, createUser);
            JSONObject handoverUser = tmpUserMap.containsKey(handoverId) ? tmpUserMap.get(handoverId)
                    : JSONObject.parseObject(userRedis.get(handoverId));
            tmpUserMap.put(handoverId, handoverUser);
            String liableUserId = String.valueOf(objArr.get(4));

            BusinessSystem bns = new BusinessSystem();
            bns.setCreateUserId(NumberUtils.isNumber(createUserId) ? Long.valueOf(createUserId) : null);
            bns.setHandoverId(NumberUtils.isNumber(handoverId) ? Long.valueOf(handoverId) : null);
            bns.setLiableUserId(NumberUtils.isNumber(liableUserId) ? Long.valueOf(liableUserId) : null);
            bns.setShareConf(shareConf);

            JSONObject permission = rpcProcessService.checkPermission(bns, dataSourceType, session);
            if (permission == null || permission.getBooleanValue("yes")) {
                String id = objArr.get(0).toString();
                obj.put("id", id);
                obj.put("name", objArr.get(1).toString());
                obj.put("runStatus", RunStatus.valueOf(objArr.get(3).toString()).getText());
                if (!ObjectUtils.isEmpty(business)){
                    List<String> businessList = Arrays.asList(business);
                    if (!businessList.contains(id)) continue;
                    arr.add(obj);
                }else {
                    arr.add(obj);
                }
            }
        }
        return arr;
    }

    /**
     * 获取某业务的所有指标当前数据
     * @param session 会话
     */
    private JSONArray getAllCurrIndVal(HttpSession session, String business) throws Exception{
        JSONArray arr = new JSONArray();
        Period period = Period._1month;
        List<ArrayList> list = getCurrIndVal(session, period);
        list = list == null || list.isEmpty() ? new ArrayList<>() : list;
        // 权限过滤
        Map<String, Date> bnsCreateDateMap = new HashMap<>();
        DataSourceType dataSourceType = DataSourceType.all;
        Map<String, JSONObject> tmpUserMap = new HashMap<>();
        Date endDate = new Date();
        Date startDate = Period.getStartDate(period, TimeUtils.getCurDayTime(0, 0, 0));
        ArrayList objArr = null;
        for (ArrayList tem : list) {
            if (tem.get(0).equals(business)){
                objArr = tem;
                break;
            }
        }
        if (ObjectUtils.isEmpty(objArr)){
            return new JSONArray();
        }
        String createUserId = String.valueOf(objArr.get(9));
        String handoverId = String.valueOf(objArr.get(10));
        String shareConfStr = String.valueOf(objArr.get(11));
        String bnsId = objArr.get(0).toString();
        bnsCreateDateMap.put(bnsId, format.parse((String )objArr.get(7)));
        JSONObject shareConf = null;
        if (org.apache.commons.lang.StringUtils.isNotBlank(shareConfStr)) {
            shareConf = JSONObject.parseObject(shareConfStr);
        }
        JSONObject createUser = tmpUserMap.containsKey(createUserId) ? tmpUserMap.get(createUserId)
                : JSONObject.parseObject(userRedis.get(createUserId));
        tmpUserMap.put(createUserId, createUser);
        JSONObject handoverUser = tmpUserMap.containsKey(handoverId) ? tmpUserMap.get(handoverId)
                : JSONObject.parseObject(userRedis.get(handoverId));
        tmpUserMap.put(handoverId, handoverUser);
        String liableUserId = String.valueOf(objArr.get(4));

        BusinessSystem bns = new BusinessSystem();
        bns.setCreateUserId(NumberUtils.isNumber(createUserId) ? Long.valueOf(createUserId) : null);
        bns.setHandoverId(NumberUtils.isNumber(handoverId) ? Long.valueOf(handoverId) : null);
        bns.setLiableUserId(NumberUtils.isNumber(liableUserId) ? Long.valueOf(liableUserId) : null);
        bns.setShareConf(shareConf);

        JSONObject permission = rpcProcessService.checkPermission(bns, dataSourceType, session);
        if (permission != null && !permission.getBooleanValue("yes")) {
            return new JSONArray();
        }
        BnsIndValQuery bnsIndValQuery = new BnsIndValQuery();
        bnsIndValQuery.setBnsIds(Lists.newArrayList(bnsId));
        List<BusinessRecord> Bnslist = rpcProcessService.findBusinessInfoAndCurIndValues(bnsIndValQuery);
        Map<Indicator, BusinessRecord> indMap = new HashMap<>();
        if (Bnslist != null && !Bnslist.isEmpty()) {
            Bnslist.forEach(indVal -> {
                if (!ObjectUtils.isEmpty(indVal.getIndicatorName())){
                    indMap.put(Indicator.valueOf(indVal.getIndicatorName()), indVal);
                }
            });
        }

        JSONObject alertInfos = getBnsAlertCountAndAlertLevel(new String[]{business}, session);
        JSONObject alertInfo = alertInfos.getJSONObject(business);
        double alertTotal = 0d;
        JSONObject indLevel = new JSONObject();
        if (!ObjectUtils.isEmpty(alertInfo)){
            alertTotal = alertInfo.getDoubleValue("alertTotal");
            indLevel = alertInfo.getJSONObject("indLevel");
        }


        BusinessRecord defaultInd = new BusinessRecord();
        defaultInd.setCreateDate(format.parse((String )objArr.get(7)));
        for (Indicator ind : Indicator.getMonitorIndicator()) {
            defaultInd.setIndicator(ind);
            BusinessRecord record = indMap.getOrDefault(ind, defaultInd);
            record.setIndicator(ind);
            JSONObject indObj = getIndObj(record);
            indObj.put("alertLevel", indLevel.get(ind.toString()));
            arr.add(indObj);
        }


//
        // 可用率
        defaultInd.setIndicator(Indicator.available_rate);
        defaultInd.setFetchDate(endDate);
//        // 统计指标对象
        Map<String, JSONObject> statisticsMap = rpcProcessService.calcStatistics(bnsCreateDateMap, startDate, endDate);
        if (ObjectUtils.isEmpty(statisticsMap)){
            return new JSONArray();
        }
        JSONObject statisticObj = statisticsMap.getOrDefault(business, new JSONObject());
        defaultInd.setIndicatorValue(statisticObj.getDouble("availableRate"));
        arr.add(getIndObj(defaultInd));
        defaultInd.setIndicator(Indicator.alert_count);
        defaultInd.setIndicatorValue(alertTotal);
        arr.add(getIndObj(defaultInd));
        //MTTR
        defaultInd.setIndicator(Indicator.MTTR);
        String mttr = statisticObj.getString("MTTR");
        JSONObject time = TimeValStrSplitUnit(mttr);
        String unit = time.getString("unit");
        String value = time.getString("value");
        arr.add(getSpecialIndObj(defaultInd, value, unit));
        //MTBF
        defaultInd.setIndicator(Indicator.MTBF);
        String mtbf = statisticObj.getString("MTBF");
        time = TimeValStrSplitUnit(mtbf);
        unit = time.getString("unit");
        value = time.getString("value");
        arr.add(getSpecialIndObj(defaultInd, value, unit));
        //不可用次数
        defaultInd.setIndicator(Indicator.down_times);
        defaultInd.setIndicatorValue(statisticObj.getDouble("downTimes"));
        arr.add(getIndObj(defaultInd));
        return arr;
    }

    private List<ArrayList> getCurrIndVal(HttpSession session, Period period) throws Exception{
        SortWayConf sortWayConf = getUserSortWay(session, businessDataParamsService.findNotDeletedIds());
        SortWay sortWay = sortWayConf.getSortWay();
        BusinessQuery query = new BusinessQuery();
        query.setPeriod(period);
        query.setSortWay(sortWay);
        return rpcProcessService.getCurrIndVal(query);
    }

    /**
     * 根据选择的业务id查询业务列表，并且进行权限过滤，如果选择不限，展示全部
     * @param session 会话
     * @param business 业务id
     */
    private List<String> getBusiness(HttpSession session, String[] business) throws Exception{
        List<String> businessList = Lists.newArrayList();
        JSONArray businessArr = getBusStatus(session, business);
        for (int i = 0; i < businessArr.size(); i++) {
            businessList.add(businessArr.getJSONObject(i).getString("id"));
        }
        return businessList;
    }

    /**
     * 获取登录用户的业务排序方式
     * @param session 会话
     * @param bnsList 业务系统列表
     */
    private SortWayConf getUserSortWay(HttpSession session, List<String> bnsList) throws Exception{
        Long userId = SessionUtils.getCurrentUserIdFromSession(session);
        SortWayConf sortWayConf = rpcProcessService.findSortWayByUserId(userId);
        sortWayConf = sortWayConf == null ? new SortWayConf(SortWay.createDate_asc) : sortWayConf;
        String customSortIds = sortWayConf.getCustomSortIds();
        customSortIds = getSortIds(org.apache.commons.lang.StringUtils.isNotBlank(customSortIds) ? customSortIds.split(",") : null, bnsList);
        sortWayConf.setCustomSortIds(customSortIds);
        return sortWayConf;
    }

    /**
     * 获取排序的业务ID
     * @param sortIds 排序的业务ID数组
     * @param allIdList 所有业务系统ID列表
     */
    private String getSortIds(String[] sortIds, List<String> allIdList) {
        List<String> sortIdList = sortIds == null || sortIds.length == 0 ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(sortIds));
        if (allIdList != null && !allIdList.isEmpty()) {
            sortIdList.retainAll(allIdList);
            allIdList.removeAll(sortIdList);
            sortIdList.addAll(allIdList);
        }
        return org.apache.commons.lang.StringUtils.join(sortIdList, ",");
    }

    private JSONObject TimeValStrSplitUnit(String statistic) {
        JSONObject time = new JSONObject();
        int length = statistic.length();
        String value = statistic.substring(0, length - 2);
        String unit = statistic.substring(length - 2, length);
        if ("分钟".equals(unit)){
            unit = "小时";
            value = String.format("%.1f", Double.parseDouble(value) / 60);
        }
        time.put("unit",unit);
        time.put("value",value);
        return time;
    }

    /**
     * 组装指标对象
     * @param indVal 当前指标值
     */
    private JSONObject getIndObj(BusinessRecord indVal) {
        JSONObject indObj = new JSONObject();
        Indicator ind = indVal.getIndicator();
        indObj.put("indicatorId", ind);
        indObj.put("indicatorText", ind.getText());
        indObj.put("indicatorValue", ArithUtils.formatDouble(indVal.getIndicatorValue(), ind.getDefaultVal()));
        indObj.put("indicatorUnit", ind.getUnit());
        if (ObjectUtils.isEmpty(indVal.getFetchDate())){
            indObj.put("fetchDate", simpleDateFormat.format(indVal.getCreateDate()));
        }else {
            indObj.put("fetchDate", simpleDateFormat.format(indVal.getFetchDate()));
        }
        return indObj;
    }

    private JSONObject getSpecialIndObj(BusinessRecord indVal, String value, String unit){
        JSONObject indObj = new JSONObject();
        Indicator ind = indVal.getIndicator();
        indObj.put("indicatorId", ind);
        indObj.put("indicatorText", ind.getText());
        indObj.put("indicatorValue", value);
        indObj.put("indicatorUnit", unit);
        if (ObjectUtils.isEmpty(indVal.getFetchDate())){
            indObj.put("fetchDate", simpleDateFormat.format(indVal.getCreateDate()));
        }else {
            indObj.put("fetchDate", simpleDateFormat.format(indVal.getFetchDate()));
        }
        return indObj;
    }

    /**
     * 构造vCharts的空对象
     */
    private JSONObject empObj() {
        JSONObject obj = new JSONObject();
        JSONArray emp = new JSONArray();
        obj.put("columns", emp);
        obj.put("rows", emp);
        return obj;
    }

    private JSONArray getTopBusinessInd(HttpSession session, String[] business,
                                        Indicator indicatorEnum, String order ,Integer number) throws Exception{
        JSONArray topBnsInds = new JSONArray();
        //获取默认的业务指标值，应对空值进行处理
        double nullDef = getIndicatorDefaultVal(indicatorEnum);
        //获取用户可查看的业务id
        List<String> businessList = getBusiness(session, business);
        BusinessQuery query = new BusinessQuery();
        query.setIds(businessList);
        List<BusinessSystem> businessSystems= rpcProcessService.findBusinessSystems(query);
        if(indicatorEnum.isInstant()){//指标是否持久化，即是否保存至业务指标实时表
            //获取可查看的前n个业务指标
            BnsIndValQuery valQuery = new BnsIndValQuery();
            valQuery.setBnsIds(businessList);
            valQuery.setLimit(number);
            valQuery.setValSort(order);
            valQuery.setIndicatorName(indicatorEnum.toString());
            List<BusinessRecord> list = rpcProcessService.indicatorTop(valQuery);
            if(list!=null && !list.isEmpty()){
                for (BusinessRecord record : list) {
                    JSONObject obj = new JSONObject();
                    String id = record.getBusinessId();
                    obj.put("id", id);
                    obj.put("name", record.getBnsName());
                    Double indVal = record.getRealIndValue() == null ?  nullDef : Double.valueOf(record.getRealIndValue().toString());
                    obj.put("indVal", indVal);
                    topBnsInds.add(obj);
                }
            }
        }else {
            Period period = Period._7days;
            String unit = "";
            JSONObject alertInfos = new JSONObject();
            if(alert_count.equals(indicatorEnum)){//跨组件查询业务告警数及指标告警级别
                alertInfos = getBnsAlertCountAndAlertLevel(businessSystems, session);
            }

            for (BusinessSystem businessSystem : businessSystems) {
                JSONObject obj = new JSONObject();
                String bnsId = businessSystem.getId();
                String bnsName = businessSystem.getName();
                Date createDate = businessSystem.getCreateDate();
                Date endDate = new Date();
                Date startDate = Period.getStartDate(period, TimeUtils.getCurDayTime(0, 0, 0));
                Object indVal = null;
                if(alert_count.equals(indicatorEnum)){//跨组件查询业务告警数及指标告警级别
                    JSONObject alertInfo = alertInfos.getJSONObject(businessSystem.getId());
                    indVal = alertInfo.getDoubleValue("alertTotal");
                }else {
                    Map<String, Date> bnsCreateDateMap = new HashMap<>();
                    bnsCreateDateMap.put(bnsId,createDate);
                    // 查询统计指标，并返回又业务系统ID为键的统计指标对象
                    Map<String, JSONObject> statisticsMap = rpcProcessService.calcStatistics(bnsCreateDateMap, startDate, endDate);
                    // 统计指标对象
                    JSONObject statisticObj = statisticsMap.getOrDefault(bnsId, new JSONObject());
                    switch (indicatorEnum) {
                        case available_rate:
                            indVal = ArithUtils.formatDouble(statisticObj.getDouble("availableRate"),
                                    Indicator.available_rate.getDefaultVal());
                            break;
                        case down_times:
                            indVal = ArithUtils.formatDouble(statisticObj.getDouble("downTimes"),
                                    Indicator.down_times.getDefaultVal());
                            break;
                        case MTBF:
                            String mttr = statisticObj.getString("MTTR");
                            JSONObject mttrJSON = TimeValStrSplitUnit(mttr);
                            unit = mttrJSON.getString("unit");
                            indVal= mttrJSON.getString("value");
                            break;
                        case MTTR:
                            String mtbf = statisticObj.getString("MTBF");
                            JSONObject mtbfJSON = TimeValStrSplitUnit(mtbf);
                            unit = mtbfJSON.getString("unit");
                            indVal= mtbfJSON.getString("value");
                            break;
                        default:
                            break;
                    }
                }
                obj.put("id", bnsId);
                obj.put("name",bnsName);
                obj.put("indVal", indVal);
                obj.put("unit", unit);
                topBnsInds.add(obj);
            }
            if (!topBnsInds.isEmpty()) {
                //获取业务指标排行榜
                sortByorder("desc", topBnsInds);
                //获取前n个业务指标
                number = topBnsInds.size() < number ? topBnsInds.size() : number;
                topBnsInds = JSONArray.parseArray(JSON.toJSONString(topBnsInds.subList(0, number)));
            }
        }
        return topBnsInds;
    }

    private void sortByorder(String order, JSONArray topBnsInds) {
        if (Strings.isNullOrEmpty(order)){
            order = "desc";
        }
        if("desc".equals(order)){
            topBnsInds.sort(Comparator.comparing(topBnsInd -> ((JSONObject) topBnsInd).getDoubleValue("indVal")).reversed());
        }else {
            topBnsInds.sort(Comparator.comparing(topBnsInd -> ((JSONObject) topBnsInd).getDoubleValue("indVal")));
        }
    }

    private double getIndicatorDefaultVal(Indicator indicatorEnum) {
        String defaultVal = indicatorEnum.getDefaultVal();
        return "--".equals(defaultVal) ? 0 : Double.valueOf(defaultVal);
    }

    private JSONObject getBnsAlertCountAndAlertLevel(String[] business, HttpSession session) throws Exception{

        JSONObject result = new JSONObject();
        //查询业务告警
        List<AlertRecord> alertRecord = getAlertRecord(Lists.newArrayList(business), session);

        //告警级别排序，获取告警等级最高的告警
        AlertLevelQuery query = new AlertLevelQuery();
        query.setStatus(AlertLevelStatus.ACTIVATED);
        List<AlertLevel> levels = rpcProcessService.findAlertLevelList(query);
        levels.sort(Comparator.comparing(AlertLevel::getLevel).reversed());
        for (String bnsId: business) {
            List<AlertRecord> records = alertRecord.stream()
                    .filter(record -> record.getObjectId().equals(bnsId))
                    .sorted(Comparator.comparing(AlertRecord::getLevel).reversed())
                    .collect(Collectors.toList());

            JSONObject alertInfo = new JSONObject();
            double alertTotal = records.size();
            JSONObject indLevel = new JSONObject();
            if (!ObjectUtils.isEmpty(records)){
                AlertLevel level = levels.stream().filter(l -> records.get(0).getLevel().equals(l.getLevel())).findFirst().orElse(null);
                indLevel = JSON.parseObject(JSON.toJSONString(level));
            }

            alertInfo.put("alertTotal",alertTotal);
            alertInfo.put("indLevel",indLevel);
            result.put(bnsId, alertInfo);
        }
        return result;
    }

    private JSONObject getBnsAlertCountAndAlertLevel(List<BusinessSystem> business, HttpSession session) throws Exception {
        String[] bnsIds = business.stream().map(BusinessSystem::getId).toArray(String[]::new);
        return getBnsAlertCountAndAlertLevel(bnsIds, session);
    }

}
