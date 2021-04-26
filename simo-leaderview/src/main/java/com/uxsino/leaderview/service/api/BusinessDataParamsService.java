package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.uxsino.commons.db.redis.service.SiteUserRedis;
import com.uxsino.commons.model.DataSourceType;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.utils.ArithUtils;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.leaderview.model.business.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusinessDataParamsService {

    @Autowired
    private SiteUserRedis userRedis;

    @Autowired
    private RpcProcessService rpcProcessService;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public JsonModel getBusiness(HttpSession session) throws Exception{
        JSONArray arr = getAllCurrIndVal(session);
        JSONArray result = new JSONArray();
        for (int i = 0; i < arr.size(); i++) {
            JSONObject tmpObj = new JSONObject();
            tmpObj.put("name", arr.getJSONObject(i).getString("name"));
            tmpObj.put("value", arr.getJSONObject(i).getString("id"));
            result.add(tmpObj);
        }
        return new JsonModel(true, result);
    }

    public JsonModel getIndicator() {
        List<Indicator> indList = Indicator.getLeaderviewIndicator();
        return new JsonModel(true, loadIndicator(indList));
    }

    public JsonModel getIndicatorPercent() {
        List<Indicator> indList = Lists.newArrayList(Indicator.health, Indicator.available_rate, Indicator.busy_rate);
        return new JsonModel(true, loadIndicator(indList));
    }

    public JsonModel getHistoryIndicator(String[] business) {
        if (ObjectUtils.isEmpty(business)){
            return new JsonModel(false, "未选择业务");
        }
        List<Indicator> indicators = Lists.newArrayList(Indicator.health, Indicator.busy_rate, Indicator.resp_time);
        return new JsonModel(true, loadIndicator(indicators));
    }

    private JSONArray loadIndicator(List<Indicator> inds){
        JSONArray arr = new JSONArray();
        for (Indicator ind : inds){
            JSONObject obj = new JSONObject();
            obj.put("name", ind.getText());
            obj.put("value", ind.toString());
            arr.add(obj);
        }
        return arr;
    }

    /**
     * 获取当前用户可见的所有业务指标数据
     * @param session
     * @return
     */
    private JSONArray getAllCurrIndVal(HttpSession session) throws Exception {
        JSONArray arr = new JSONArray();
        Period period = Period._1month;
        SortWayConf sortWayConf = getUserSortWay(session, findNotDeletedIds());
        List<String> sortIdList = new ArrayList<>();
        SortWay sortWay = sortWayConf.getSortWay();
        String customSortIds = sortWayConf.getCustomSortIds();
        if (StringUtils.isNotBlank(customSortIds)) {
            sortIdList = Arrays.asList(customSortIds.split(","));
        }
        BusinessQuery query = new BusinessQuery();
        query.setPeriod(period);
        query.setSortWay(sortWay);
        List<ArrayList> list = rpcProcessService.getCurrIndVal(query);
        list = list == null || list.isEmpty() ? new ArrayList<>() : list;
        // 权限过滤
//        List<String> ids = new ArrayList<>();
//        Map<String, Date> bnsCreateDateMap = new HashMap<>();
        DataSourceType dataSourceType = DataSourceType.all;
        Map<String, JSONObject> tmpUserMap = new HashMap<>();
        for (ArrayList objArr : list) {
            String createUserId = String.valueOf(objArr.get(9));
            String handoverId = String.valueOf(objArr.get(10));
            String shareConfStr = String.valueOf(objArr.get(11));
            JSONObject shareConf = null;
            if (StringUtils.isNotBlank(shareConfStr)) {
                shareConf = JSONObject.parseObject(shareConfStr);
            }
            JSONObject obj = new JSONObject();
            obj.put("shareConf", shareConf);
            JSONObject createUser = tmpUserMap.containsKey(createUserId) ? tmpUserMap.get(createUserId)
                    : JSONObject.parseObject(userRedis.get(createUserId));
            tmpUserMap.put(createUserId, createUser);
            obj.put("createUser", createUser);
            JSONObject handoverUser = tmpUserMap.containsKey(handoverId) ? tmpUserMap.get(handoverId)
                    : JSONObject.parseObject(userRedis.get(handoverId));
            tmpUserMap.put(handoverId, handoverUser);
            obj.put("handoverUser", handoverUser);
            String liableUserId = String.valueOf(objArr.get(4));

            BusinessSystem bns = new BusinessSystem();
            bns.setCreateUserId(NumberUtils.isNumber(createUserId) ? Long.valueOf(createUserId) : null);
            bns.setHandoverId(NumberUtils.isNumber(handoverId) ? Long.valueOf(handoverId) : null);
            bns.setLiableUserId(NumberUtils.isNumber(liableUserId) ? Long.valueOf(liableUserId) : null);
            bns.setShareConf(shareConf);

            JSONObject permission = rpcProcessService.checkPermission(bns, dataSourceType, session);
            if (permission == null || permission.getBooleanValue("yes")) {
                if (permission != null && permission.getBooleanValue("isShared")) {
                    obj.put("shareUser", handoverUser != null ? handoverUser : createUser);
                }
//                String bnsId = objArr[0].toString();
//                ids.add(bnsId);
//                bnsCreateDateMap.put(bnsId, (Date) objArr[7]);
                String id = objArr.get(0).toString();
                obj.put("id", id);
                obj.put("name", objArr.get(1));
                obj.put("description", objArr.get(2));
                obj.put("runStatus", RunStatus.valueOf(objArr.get(3).toString()).getText());
                obj.put("liableUserId", liableUserId);
                JSONObject liableUser = tmpUserMap.containsKey(liableUserId) ? tmpUserMap.get(liableUserId)
                        : JSONObject.parseObject(userRedis.get(liableUserId));
                tmpUserMap.put(liableUserId, liableUser);
                obj.put("liableUser", liableUser);
                obj.put("priority", sortIdList.indexOf(id));
                obj.put("health", objArr.get(5) == null ? Indicator.health.getDefaultVal() : ArithUtils
                        .formatDouble(Double.valueOf(objArr.get(5).toString()), Indicator.health.getDefaultVal()));
                obj.put("respTime", objArr.get(6) == null ? Indicator.resp_time.getDefaultVal() : ArithUtils
                        .formatDouble(Double.valueOf(objArr.get(6).toString()), Indicator.resp_time.getDefaultVal()));
                obj.put("createDateMs", format.parse((String) objArr.get(7)).getTime());
                arr.add(obj);
            }
        }
        return arr;
    }

    /**
     * 查询所有未被删除的业务系统ID
     * @return
     */
    public List<String> findNotDeletedIds() throws Exception{
        List<BusinessSystem> businessSystems = rpcProcessService.findBusinessSystems(new BusinessQuery());
        return businessSystems.stream().map(BusinessSystem::getId).collect(Collectors.toList());
    }


    /**
     * 获取登录用户的业务排序方式
     * @param session 会话
     * @param bnsList 业务系统列表
     * @return
     */
    private SortWayConf getUserSortWay(HttpSession session, List<String> bnsList) throws Exception {
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
     * @return
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

}
