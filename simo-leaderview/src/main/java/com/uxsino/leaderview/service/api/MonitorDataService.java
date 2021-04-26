package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.uxsino.authority.lib.util.DomainUtils;
import com.uxsino.commons.db.model.IntervalType;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.db.model.network.NeComponentQuery;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.model.RunStatus;
import com.uxsino.commons.utils.DateUtils;
import com.uxsino.commons.utils.JSONUtils;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.leaderview.model.MigrationLink;
import com.uxsino.leaderview.model.MigrationStation;
import com.uxsino.leaderview.model.datacenter.IndicatorValueCriteria;
import com.uxsino.leaderview.model.monitor.*;
import com.uxsino.leaderview.rpc.MCService;
import com.uxsino.leaderview.utils.IndicatorValueUtils;
import com.uxsino.leaderview.utils.MonitorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.uxsino.leaderview.utils.MonitorUtils.getValueJSON;


@Component
@Slf4j
public class MonitorDataService {

    private static String sdfStr = "yyyy-MM-dd HH:mm:ss";

    private static Pattern NUMBER_PATTERN = Pattern.compile("(-?[1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])");

    private static Pattern CHINESE_PATTERN = Pattern
            .compile("(\\d*?\\s[\\u4e00-\\u9fa5]+)(\\d*?\\s[\\u4e00-\\u9fa5]+)");

    private static SimpleDateFormat sdf = new SimpleDateFormat(sdfStr);

    @Autowired
    private DomainUtils domainUtils;

    @Autowired
    private RpcProcessService rpcProcessService;

    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    private MCService mcService;

    @Autowired
    private MonitorDataParamsService monitorDataParamsService;

    /**
     * 按类型统计资源数量
     * @param domainId 域ID
     * @param baseNeClass 资源父类型
     * @return
     */
    public JsonModel statisticsResourceData(Long domainId, String baseNeClass, HttpSession session) throws Exception{
        BaseNeClass baseClass = null;
        if (StringUtils.isNoneBlank(baseNeClass)) {
            try {
                baseClass = BaseNeClass.valueOf(baseNeClass);
            } catch (Exception e) {
                return new JsonModel(false, "资源父类型无法识别！");
            }
        }
        List<Map<String, Object>> list = Lists.newArrayList();
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Long> domainList = getDomainList(domainId, session);
        if (BaseNeClass.virtualization.equals(baseClass)) {
            for (Long domain : domainList) {
                list.addAll(rpcProcessService.vmStatics(domain));
            }
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                for (int j = i + 1; j < list.size(); j++) {
                    Map<String, Object> tmpMap = list.get(j);
                    if (map.get("name").toString().equals(tmpMap.get("name").toString())) {
                        map.put("value", Integer.parseInt(map.get("value").toString())
                                + Integer.parseInt(tmpMap.get("value").toString()));
                        list.remove(j);
                        j--;
                    }
                }
                mapList.add(map);
            }
        } else if (null == baseClass) {
            for (Long domain : domainList) {
                list.addAll(rpcProcessService.neStatistics(domain, null));
            }
            List<String> valueList = Lists.newArrayList();
            list.forEach(map -> valueList.add(map.get("name").toString()));
            for (BaseNeClass bnc : BaseNeClass.values()) {
                if (!valueList.contains(bnc.toString())) {
                    list.add(newResultObj(bnc, 0));
                }
            }
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                for (int j = i + 1; j < list.size(); j++) {
                    Map<String, Object> tmpMap = list.get(j);
                    if (map.get("name").toString().equals(tmpMap.get("name").toString())) {
                        map.put("value", Integer.parseInt(map.get("value").toString())
                                + Integer.parseInt(tmpMap.get("value").toString()));
                        list.remove(j);
                        j--;
                    }
                }
                mapList.add(map);
            }
            for (Map<String, Object> map : mapList) {
                String baseName = map.get("name").toString();
                if (BaseNeClass.virtualization.toString().equals(baseName)) {
                    map.put("value", rpcProcessService.countVr(domainId));
                }
            }
        } else {
            for (Long domain : domainList) {
                list.addAll(rpcProcessService.neStatistics(domain, baseClass));
            }
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                for (int j = i + 1; j < list.size(); j++) {
                    Map<String, Object> tmpMap = list.get(j);
                    if (map.get("name").toString().equals(tmpMap.get("name").toString())) {
                        map.put("value", Integer.parseInt(map.get("value").toString())
                                + Integer.parseInt(tmpMap.get("value").toString()));
                        list.remove(j);
                        j--;
                    }
                }
                mapList.add(map);
            }
        }
        JSONObject json = new JSONObject();
        json.put("columns", newColumns("资源类型","数量"));
        JSONArray rows = new JSONArray();
        Map<String, Object> map = Maps.newHashMap();
        if (null == baseClass) {
            for (BaseNeClass baseNeClass1 : BaseNeClass.getBaseClass(true)) {
                map.put(baseNeClass1.getText(), "0");
            }
            list.forEach(v -> map.put(BaseNeClass.valueOf(v.get("name").toString()).getText(), v.get("value")));
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                rows.add(newResultObj("资源类型", entry.getKey(), "数量", entry.getValue()));
            }
        } else {
            for (NeClass neClass : baseClass.getNeClass()) {
                map.put(neClass.getText(), 0);
            }
            list.forEach(v -> map.put(NeClass.valueOf(v.get("name").toString()).getText(), v.get("value")));
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                rows.add(newResultObj("资源类型", entry.getKey(), "数量", entry.getValue()));
            }
        }
        json.put("rows", rows);
        return new JsonModel(true, json);
    }

    /**
     * 按状态统计资源数量
     * @param domainId 域ID
     * @param baseNeClass 资源父类型
     * @return
     */
    public JsonModel statisticsResourceStatus(HttpSession session, Long domainId, String baseNeClass) throws Exception {
        BaseNeClass baseClass = null;
        if (StringUtils.isNoneBlank(baseNeClass)) {
            try {
                baseClass = BaseNeClass.valueOf(baseNeClass);
            } catch (Exception e) {
                return new JsonModel(false, "资源父类型无法识别！");
            }
        }
        List<Long> domainList = getDomainList(domainId, session);

        List<ArrayList> list = rpcProcessService.neStatusStatistics(domainList, baseClass);
        JSONObject json = new JSONObject();
        json.put("columns", newColumns("状态","数量"));
        JSONArray rows = new JSONArray();
        Map<String, Object> map = Maps.newHashMap();

        List<RunStatus> statusList = Arrays.asList(RunStatus.values());
        statusList.forEach(v -> map.put(v.getName(), 0));

        list = list.stream().filter(v -> v.size() == 2).collect(Collectors.toList());
        list.forEach(v -> map.put((String)v.get(0), v.get(1)));

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            rows.add(newResultObj("状态", entry.getKey(), "数量", entry.getValue()));
        }
        json.put("rows", rows);
        return new JsonModel(true, null, json);
    }

    public JsonModel statisticsResourceStatusForSunburst(HttpSession session, Long domainId, String baseNeClass) throws Exception{
        List<Long> domainList = getDomainList(domainId, session);
        return new JsonModel(true, rpcProcessService.neStatusStatisticsForSunburst(domainList, BaseNeClass.valueOf(baseNeClass)));
    }

    /**
     * 资源状态列表
     * @param domainId 域ID
     * @param neIds 资源IDs
     * @param baseNeClass 资源父类型
     * @param session
     * @return
     */
    public JsonModel neList(Long domainId, String neIds, BaseNeClass baseNeClass, HttpSession session, String[] column) throws Exception{
        List<String > diffColumns = Lists.newArrayList("资源名称","IP地址","资源类型","运行状态","更新时间");
        column = ObjectUtils.isEmpty(column) ? diffColumns.toArray(new String[diffColumns.size()]): column;
        JSONArray columns = new JSONArray();
        columns = addColumns(columns, column);
        diffColumns.removeAll(getAllColumns(columns));
        // 检查指定的资源是否是符合用户权限的资源
        if (StringUtils.isNoneBlank(neIds) && !SessionUtils.isSuperAdmin(session)) {
            List<String> targetNeIds = Arrays.asList(neIds.split(","));
            NetworkEntityCriteria criteria = new NetworkEntityCriteria();
            if (!ObjectUtils.isEmpty(criteria.getIds())) {
                neIds = targetNeIds.stream().filter(id -> criteria.getIds().contains(id))
                        .collect(Collectors.joining(","));
            }
        }
        JSONObject json = new JSONObject(true);
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        rpcProcessService.setCriteriaDomainIds(criteria ,session ,domainId);
        if(baseNeClass!=null)
            rpcProcessService.setCriteriaNeClass(criteria, baseNeClass.toString());
        if (!ObjectUtils.isEmpty(neIds)){
            criteria.setIds(Lists.newArrayList(neIds.split(",")));
        }
        criteria.setMonitoring(true);
        List<NetworkEntity> list = rpcProcessService.getNeList(criteria);
        if (CollectionUtils.isEmpty(list)) {
            return new JsonModel(true, newResultObj("columns",columns,"rows",new JSONArray()));
        }
        json.put("columns", columns);
        JSONArray rows = new JSONArray();
        for (NetworkEntity ne : list) {
            if (!ne.isMonitoring()) {
                continue;
            }
            JSONObject row = new JSONObject(true);
            row.put("资源名称", ne.getName());
            row.put("IP地址", ne.getIp());
            row.put("资源类型", ne.getNeClass());
            row.put("运行状态", Optional.ofNullable(ne.getRunStatus()).map(RunStatus::getName).orElse(""));
            row.put("更新时间",ne.getPatrolTime());
            diffColumns.forEach(diff -> row.remove(diff));
            rows.add(row);
        }
        json.put("rows", rows);
        return new JsonModel(true, json);
    }

//    public JsonModel findIndicatorsStatus(String neId) throws Exception {
//        List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(new String[]{neId});
//        NetworkEntity ne = new NetworkEntity();
//        if (!nes.isEmpty()){
//            ne = nes.get(0);
//        }
//        List<String> indicators = null;
//        if (ne.getIndicators() != null) {
//            indicators = JSON.parseArray(ne.getIndicators().toString()).toJavaList(String.class);
//        }
//        List<JSONObject> list = indicatorService.findIndicatorStatus(neId);
//        JSONObject json = new JSONObject();
//        json.put("columns", newColumns("指标名称","运行状态","更新时间"));
//        JSONArray rows = new JSONArray();
//        for (JSONObject indicator : list) {
//            if (indicator.getString("neids") != null && !Arrays.asList(indicator.getString("neids").split(",")).contains(neId)) {
//                continue;
//            }
//            if (indicators == null || indicators.contains(indicator.getString("name"))) {
//                JSONObject row = newResultObj("指标名称", indicator.getString("label"),
//                        "运行状态", indicator.getString("run_status"));
//                try {
//                    row.put("fetch_date", TimeUtils.formatTime(sdf.parse(indicator.getString("fetch_date"))));
//                } catch (ParseException e) {
//                    row.put("fetch_date", indicator.getString("fetch_date"));
//                }
//                rows.add(row);
//            }
//        }
//        json.put("rows", rows);
//        return new JsonModel(true, json);
//    }

    /**
     * 获取指标属性的单值
     * @param neIds 资源ID
     * @param indicators 指标名称
     * @param componentName 部件名称
     * @param field 属性
     * @return
     */
    public JsonModel getIndicatorValueData(String neIds, String indicators, String componentName, String field) throws Exception {
        IndicatorTable ind = null;
        try {
            ind = rpcProcessService.getIndicatorInfoByName(indicators);
        }catch (Exception e){
            return new JsonModel(true,  e.getMessage(), newResultObj("name","","unit",""));
        }
        // 单值元件中，错误数据也需要展示正确图例
        JSONObject empObj = newResultObj("name", Objects.isNull(ind) ? "" : ind.getLabel(), "unit","");
        // 资源ID和指标名为必选项
        if (StringUtils.isEmpty(neIds) || StringUtils.isEmpty(indicators)) {
            return new JsonModel(true, empObj);
        }
        NetworkEntity ne = rpcProcessService.findNetworkEntityById(neIds);
        // 判断资源是否存在或者是否已被销毁或者未监控
        if (ObjectUtils.isEmpty(ne) || ne.getManageStatus().equals(ManageStatus.Delected) || !ne.isMonitoring()) {
            return new JsonModel(true, empObj);
        }
        // IPMI数据进行指标单位特殊处理
        List<String> ipmiIndicator = Lists.newArrayList("ipmi_temp", "ipmi_current", "ipmi_fan", "ipmi_voltage");
        // 返回的结果
        JSONObject result = new JSONObject();
        // 属性字段的JSON
        JSONObject fieldLabel = null;

        // 类型为NUMBER、PERCENT的指标均无属性，所以需要判断一下指标类型
        if (validHasFields(ind)) {
            JSONArray fields = ind.getFields();
            if (CollectionUtils.isEmpty(fields)) {
                return new JsonModel(true, empObj);
            }
            // 查找属性的label
            fields = filter(fields, o -> field.equalsIgnoreCase(o.getString("name")));
            if (!ObjectUtils.isEmpty(fields)){
                fieldLabel = fields.getJSONObject(0);
            }else {
                return new JsonModel(true, empObj);
            }
        }

        // 获取指标监控策略
        Boolean strategyField = getStrategy(neIds, indicators, field);
        IndValue indValue = null;
        // 若指标被监控
        //TODO 监控策略 strategyField
        if (strategyField) {
            // 获取指标的值
            indValue = indicatorService.findIndValue(neIds, indicators, null);
        }
        if (Objects.isNull(indValue) || ObjectUtils.isEmpty((JSON) indValue.getIndicatorValue())) {
            return new JsonModel(true, empObj);
        }

        // 属性值的JSON
        JSONObject valueJSON = getValueJSON((JSON) indValue.getIndicatorValue(), componentName);

        // 如果是类型为NUMBER、PERCENT的指标，直接取值返回。就不用做之后的部件判断了
        if (!validHasFields(ind)) {
            String value = valueJSON.getString(ind.getName());
            value = getMatcherString(NUMBER_PATTERN, value);
            if (Objects.equals(value, "false")) {
                value = "0";
            }
            String unit = "PERCENT".equals(ind.getIndicatorType()) ? "%" : "";
            result = newResultObj(ind.getLabel(), value, unit);
            // 直接返回值显示
            return new JsonModel(true, result);
        }

        IndicatorValueUtils valueUtils = new IndicatorValueUtils();
        String unit = fieldLabel.getString("unit");
        if (StringUtils.isEmpty(componentName)) {
            valueUtils.transferItem(fieldLabel, valueJSON);
            String value = valueJSON.getString(field);
            if (Strings.isNullOrEmpty(value)) {
                return new JsonModel(true, empObj);
            }
            value = getMatcherString(CHINESE_PATTERN, value);
            String name = fieldLabel.getString("label");
            if (Strings.isNullOrEmpty(unit)) {
                unit = "";
                value = String.format("%.2f", Double.parseDouble(value));
            } else {
                int index = value.lastIndexOf(" ");
                unit = index > 0 ? value.substring(index + 1) : unit;
                value = index > 0 ? String.format("%.2f", Double.parseDouble(value.substring(0, index)))
                        : String.format("%.2f", Double.parseDouble(value));
            }
            if (ipmiIndicator.contains(indicators)) {
                name = ind.getLabel();
                unit = valueJSON.getString("unit");
            }
            result = newResultObj(name, value, unit);
        } else {
            // 根据指标ID取对应参数
            JSON indicatorValues = (JSON) indValue.getIndicatorValue();
            JSONArray fieldArray = JSON.parseArray(indicatorValues.toJSONString());

            Map<String, String> componentNameMap = Maps.newHashMap();
            NeComponentQuery compQuery = new NeComponentQuery();
            compQuery.setNeIds(Lists.newArrayList(neIds));
            compQuery.setIndicatorName(indicators);
            List<Map<String, Object>> idAndComponent = rpcProcessService.findNeComps(compQuery);
            for (Map<String, Object> map : idAndComponent) {
                if (map.get("identifier") == null || map.get("componentName") == null) {
                    continue;
                }
                componentNameMap.put(map.get("identifier").toString(), map.get("componentName").toString());
            }

            fieldArray = filter(fieldArray, o -> o.get("identifier").toString().equals(componentName));
            if (!CollectionUtils.isEmpty(fieldArray)){

                JSONObject fieldObj = fieldArray.getJSONObject(0);
                String name = componentNameMap.get(componentName) + ":" + fieldLabel.getString("label");
                valueUtils.transferItem(fieldLabel, fieldObj);
                String value = fieldObj.getString(field);
                if (Strings.isNullOrEmpty(value)) {
                    return new JsonModel(true, empObj);
                }
                Matcher m = CHINESE_PATTERN.matcher(value);
                while (m.find()) {
                    String group1 = m.group(1);
                    String group2 = m.group(2);
                    List<String> arr = Arrays.asList("天", "时", "分", "秒", "厘秒", "毫秒", "微秒", "纳秒");
                    double[] gap = { 24, 60, 60, 100, 10, 1000, 1000 };
                    String[] split = group2.split(" ");
                    int index = arr.indexOf(split[1]);
                    double decimal = Double.parseDouble(split[0]) / gap[index - 1];
                    double val = Double.parseDouble(group1.substring(0, group1.indexOf(" "))) + decimal;
                    value = val + " " + group1.substring(group1.indexOf(" "));
                    break;
                }
                if (Strings.isNullOrEmpty(unit)) {
                    value = String.format("%.2f", Double.parseDouble(value));
                    unit = "";
                } else {
                    int index = value.lastIndexOf(" ");
                    unit = index > 0 ? value.substring(index + 1) : unit;
                    value = index > 0 ? String.format("%.2f", Double.parseDouble(value.substring(0, index)))
                            : String.format("%.2f", Double.parseDouble(value));
                }
                if (ipmiIndicator.contains(indicators)) {
                    name = componentNameMap.get(componentName) + ":" + ind.getLabel();
                    unit = fieldObj.getString("unit");
                }
                result = newResultObj(name, value, unit);

            }

        }
        return new JsonModel(true, result);
    }

    /**
     * 获取指标字符串属性的单值
     * @param neIds 资源ID
     * @param indicators 指标名称
     * @param componentName 部件名称
     * @param field 属性
     * @return
     */
    public JsonModel getIndicatorValueStr(String neIds, String indicators, String componentName, String field) throws Exception{
        IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicators);
        // 用于转换枚举数据
        JSONObject desc = new JSONObject();
        JSONObject empObj = new JSONObject();
        empObj.put("info", "--");
        // 资源ID和指标名为必选项
        if (StringUtils.isEmpty(neIds) || StringUtils.isEmpty(indicators)) {
            return new JsonModel(true, empObj);
        }
        NetworkEntity ne = rpcProcessService.findNetworkEntityById(neIds);
        // 判断资源是否存在或者是否已被销毁或者未监控
        if (ObjectUtils.isEmpty(ne) || ne.getManageStatus().equals(ManageStatus.Delected) || !ne.isMonitoring()) {
            return new JsonModel(true, empObj);
        }
        // 返回的结果
        JSONObject result = new JSONObject();
        // 属性字段的JSON
        JSONObject fieldLabel = null;
        if (validHasFields(ind)) {
            JSONArray fields = ind.getFields();
            if (CollectionUtils.isEmpty(fields)) {
                return new JsonModel(true, empObj);
            }
            // 查找属性的label
            fields = filter(fields, o -> field.equalsIgnoreCase(o.getString("name")));
            if (!ObjectUtils.isEmpty(fields)){
                fieldLabel = fields.getJSONObject(0);
            }else {
                return new JsonModel(true, empObj);
            }
            desc = fieldLabel.getJSONObject("desc");
        }

        // 获取指标监控策略列表
        Boolean strategyField = getStrategy(neIds, indicators, field);

        IndValue indValue = null;
        // 若指标被监控
        if (strategyField) {
            // 获取指标的值
            indValue = indicatorService.findIndValue(neIds, indicators, null);
        }
        if (Objects.isNull(indValue) || ObjectUtils.isEmpty((JSON) indValue.getIndicatorValue())) {
            result.put("info", "该资源策略已被关闭或者指标未被监控");
            return new JsonModel(true, result);
        }

        // indicatorValues的类型可能是JSONArray,也可能是JSONObject
        JSON indicatorValues = (JSON) indValue.getIndicatorValue();
        IndicatorValueUtils valueUtils = new IndicatorValueUtils();
        // 属性值的JSON
        JSONObject valueJSON = new JSONObject();
        if (!validHasFields(ind)) {
            valueJSON = getValueJSON(indicatorValues,componentName);
            String value = valueJSON.getString(indValue.getIndicatorName());
            result = newResultObj(ind.getLabel(), value);
            result.put("info", value);
            // 直接返回值显示
            return new JsonModel(true, result);
        }
        String value = new String();
        if (StringUtils.isEmpty(componentName)) {
            valueJSON = getValueJSON(indicatorValues);
            valueUtils.transferItem(fieldLabel, valueJSON);
            value = valueJSON.getString(field);
            if (Strings.isNullOrEmpty(value)) {
                return new JsonModel(true, empObj);
            }
            result.put("value", value);
        } else {
            // 根据指标ID取对应参数
            JSONArray fieldArray = JSON.parseArray(indicatorValues.toJSONString());
            for (int i = 0; i < fieldArray.size(); i++) {
                JSONObject fieldObj = fieldArray.getJSONObject(i);
                if (fieldObj.get("identifier").toString().equals(componentName)) {
                    // valueJSON.put(field, fieldObj.getString(field));
                    valueUtils.transferItem(fieldLabel, fieldObj);
                    value = fieldObj.getString(field);
                    if (Strings.isNullOrEmpty(value)) {
                        return new JsonModel(true, empObj);
                    }
                    result.put("value", value);
                    break;
                }
            }
        }
        result.put("name", ObjectUtils.isEmpty(fieldLabel) ? null : fieldLabel.getString("label"));
        result.put("info", ObjectUtils.isEmpty(desc) ? value : desc.getString(value));
        return new JsonModel(true, result);
    }


    /**
     * 获取指标字符串属性的列表
     * @param neIds 资源ID
     * @param indicators 指标名称
     * @param componentName 部件名称
     * @param field 属性
     * @return
     */
    public JsonModel getIndicatorValueStrTable(String neIds, String indicators, String[] componentName, String[] field) throws Exception{
        JSONObject empObj = new JSONObject();
        empObj.put("info", "数据获取发生错误");
        IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicators);
        if (Objects.equals("LIST", ind.getIndicatorType()) && ObjectUtils.isEmpty(componentName)) {
            return getListEmptyComponentTable(neIds, field, ind);
        }
        JSONArray columns = new JSONArray();
        if (ObjectUtils.isEmpty(componentName)) {
            columns.add("指标名");
        } else {
            columns.add("部件名");
        }
        // 资源ID和指标名为必选项
        if (StringUtils.isEmpty(neIds) || StringUtils.isEmpty(indicators)) {
            return new JsonModel(true, empObj);
        }
        NetworkEntity ne = rpcProcessService.findNetworkEntityById(neIds);
        // 判断资源是否存在或者是否已被销毁或者未监控
        if (ObjectUtils.isEmpty(ne) || ne.getManageStatus().equals(ManageStatus.Delected) || !ne.isMonitoring()) {
            return new JsonModel(true, empObj);
        }
        // 返回的结果
        JSONObject result = new JSONObject();
        // 属性字段的JSON
        JSONObject fieldLabel = null;
        // 属性值的JSON
        JSONObject valueJSON = new JSONObject();

        // 获取指标监控策略
        Boolean strategyField = getStrategy(neIds, indicators, field);
        IndValue indValue = null;
        // 若指标被监控
        if (strategyField) {
            // 获取指标的值
            indValue = indicatorService.findIndValue(neIds, indicators, null);
        }
        if (Objects.isNull(indValue) || ObjectUtils.isEmpty((JSON) indValue.getIndicatorValue())) {
            return new JsonModel(true, empObj);
        }

        // indicatorValues的类型可能是JSONArray,也可能是JSONObject
        JSON indicatorValues = (JSON) indValue.getIndicatorValue();
        IndicatorValueUtils valueUtils = new IndicatorValueUtils();
        if (!validHasFields(ind)) {
            columns.add("参数");
            JSONArray rows = new JSONArray();
            valueJSON = getValueJSON(indicatorValues);
            String value = valueJSON.getString(indValue.getIndicatorName());
            JSONObject row = new JSONObject(true);
            row.put("指标名", ind.getLabel());
            row.put("参数", value);
            rows.add(row);
            result.put("columns", columns);
            result.put("rows", rows);
            return new JsonModel(true, result);
        }
        String value = new String();
        JSONArray rows = new JSONArray();
        if (ObjectUtils.isEmpty(componentName)) {
            columns.add("参数");
            for (int i = 0; i < field.length; i++) {
                JSONObject row = new JSONObject(true);
                JSONArray fields = ind.getFields();
                if (CollectionUtils.isEmpty(fields)) {
                    return new JsonModel(true, empObj);
                }
                // 查找属性的label
                for (int j = 0; j < fields.size(); j++) {
                    JSONObject fieldJson = fields.getJSONObject(j);
                    if (field[i].equalsIgnoreCase(fieldJson.getString("name"))) {
                        fieldLabel = fieldJson;
                        break;
                    }
                }
                row.put("指标名", fieldLabel.getString("label"));
                String fieldStr = field[i];
                valueJSON = getValueJSON(indicatorValues);
                valueUtils.transferItem(fieldLabel, valueJSON);
                value = valueJSON.getString(fieldStr);
                if (Strings.isNullOrEmpty(value)) {
                    row.put("参数", "--");
                } else {
                    row.put("参数", ObjectUtils.isEmpty(fieldLabel.getJSONObject("desc")) ? value
                            : fieldLabel.getJSONObject("desc").get(value));
                }
                rows.add(row);
            }
        } else {
            Map<String, String> componentNameMap = Maps.newHashMap();
            NeComponentQuery compQuery = new NeComponentQuery();
            compQuery.setNeIds(Lists.newArrayList(neIds));
            compQuery.setIndicatorName(indicators);
            List<Map<String, Object>> idAndComponent = rpcProcessService.findNeComps(compQuery);
            for (Map<String, Object> map : idAndComponent) {
                if (map.get("identifier") == null || map.get("componentName") == null) {
                    continue;
                }
                componentNameMap.put(map.get("identifier").toString(), map.get("componentName").toString());
            }
            Map<String, JSONObject> fieldLabelMap = Maps.newHashMap();
            for (int i = 0; i < field.length; i++) {
                JSONArray fields = ind.getFields();
                if (CollectionUtils.isEmpty(fields)) {
                    return new JsonModel(true, empObj);
                }
                // 查找属性的label
                for (int j = 0; j < fields.size(); j++) {
                    JSONObject fieldJson = fields.getJSONObject(j);
                    if (field[i].equalsIgnoreCase(fieldJson.getString("name"))) {
                        fieldLabelMap.put(field[i], fieldJson);
                        columns.add(fieldJson.getString("label"));
                        break;
                    }
                }
            }
            for (String componentId : componentName) {
                JSONObject row = new JSONObject(true);
                // 根据指标ID取对应参数
                JSONArray fieldArray = JSON.parseArray(indicatorValues.toJSONString());
                for (int j = 0; j < fieldArray.size(); j++) {
                    JSONObject fieldObj = fieldArray.getJSONObject(j);
                    if (fieldObj.get("identifier").toString().equals(componentId)) {
                        row.put("部件名", componentNameMap.get(componentId));
                        for (String fieldStr : field) {
                            fieldLabel = fieldLabelMap.get(fieldStr);
                            valueUtils.transferItem(fieldLabel, fieldObj);
                            value = fieldObj.getString(fieldStr);
                            if (Strings.isNullOrEmpty(value)) {
                                row.put(fieldLabel.getString("label"), "--");
                            } else {
                                row.put(fieldLabel.getString("label"),
                                        ObjectUtils.isEmpty(fieldLabel.getJSONObject("desc")) ? value
                                                : fieldLabel.getJSONObject("desc").get(value));
                            }
                        }
                        break;
                    }
                }
                rows.add(row);
            }
        }
        result.put("columns", columns);
        result.put("rows", rows);
        return new JsonModel(true, result);
    }

    private JsonModel getListEmptyComponentTable(String neIds, String[] field, IndicatorTable ind) throws Exception{
        JSONArray columns = new JSONArray();
        Map<String, String> fieldLabelMap = Maps.newHashMap();
        Arrays.stream(field).forEach(f -> {
            for (int i = 0; i < ind.getFields().size(); i++) {
                JSONObject itm = (JSONObject) ind.getFields().get(i);
                if (Objects.equals(f, itm.getString("name"))) {
                    columns.add(itm.getString("label"));
                    fieldLabelMap.put(f, itm.getString("label"));
                }
            }
        });
        NetworkEntity ne = rpcProcessService.findNetworkEntityById(neIds);
        // 判断资源是否存在或者是否已被销毁或者未监控
        if (ObjectUtils.isEmpty(ne) || ne.getManageStatus().equals(ManageStatus.Delected) || !ne.isMonitoring()) {
            return new JsonModel(true, new JSONObject());
        }
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();

        // 获取指标监控策略
        Boolean strategyField = getStrategy(neIds, ind.getName(), field);
        IndValue indValue = null;
        // 若指标被监控
        if (strategyField) {
            // 获取指标的值
            indValue = indicatorService.findIndValue(neIds, ind.getName(), null);
        }
        if (Objects.isNull(indValue) || ObjectUtils.isEmpty((JSON) indValue.getIndicatorValue())) {
            return new JsonModel(true, new JSONObject());
        }

        if (indValue.getIndicatorValue() instanceof JSONArray) {
            JSONArray indicatorValue = (JSONArray) indValue.getIndicatorValue();
            for (int i = 0; i < indicatorValue.size(); i++) {
                JSONObject value = indicatorValue.getJSONObject(i);
                JSONObject row = new JSONObject(true);
                Arrays.stream(field).forEach(f -> {
                    if (ObjectUtils.isEmpty(value.getString(f))) {
                        row.put(fieldLabelMap.get(f), "--");
                    } else
                        row.put(fieldLabelMap.get(f), value.getString(f));
                });
                rows.add(row);
            }
        } else {
            return new JsonModel(false, "指标值类型转换错误，JSONObject不可以被转换成JSONArray", empObj());
        }
        result.put("rows", rows);
        result.put("columns", columns);
        return new JsonModel(true, result);
    }

//    private JSONObject getValueJSON(JSON indicatorValues) {
//        return MonitorUtils.getVa
//
// lueJSON(indicatorValues);
//    }

//    private JSONObject getValueJSON(JSON indicatorValues, String componentName) {
//        return MonitorUtils.getValueJSON(indicatorValues, componentName);
//    }

    private IndicatorVal validStrategy(IndicatorVal indValue, Boolean strategyField){
        // 若指标未监控，取消其数值展示
        if (!strategyField) {
            indValue = new IndicatorVal();
        }
        return indValue;
    }

    private Boolean getStrategy(String neIds, String indicators, String[] fields){
        return getStrategy(neIds, indicators, ObjectUtils.isEmpty(fields) ? "": fields[0]);
    }

    private Boolean getStrategy(String neIds, String indicators, String field){
        Boolean strategyField = true;
        try {
            // 获取指标监控策略列表
            JSONObject strategyObj = rpcProcessService.getStrategy(neIds, indicators);
            Object fieldStraObj = strategyObj.get(indicators);
            if (fieldStraObj instanceof JSONObject) {
                strategyField = ((JSONObject) fieldStraObj).getBoolean(field);
            } else if (fieldStraObj instanceof Boolean) {
                strategyField = (Boolean) fieldStraObj;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return ObjectUtils.isEmpty(strategyField) ? false : strategyField;
    }

    private String getMatcherString(Pattern p, String value) {
        Matcher m = p.matcher(value);
        while (m.find()) {
            value =  m.group(1);
            break;
        }
        return value;
    }


    private List<Long> getDomainList(Long domainId ,HttpSession session){
        if (ObjectUtils.isEmpty(domainId)) {
            return domainUtils.getUserDomainIds(session);
        } else {
            return Lists.newArrayList(domainId);
        }
    }


    /** 对JSONArray进行遍历，根据predicate过滤 */
    private JSONArray filter(JSONArray array, Predicate<? super JSONObject> predicate){
        return MonitorUtils.filter(array, predicate);
    }

    /** 对JSONArray进行遍历，执行操作action */
    private void action(JSONArray array,Consumer<? super JSONObject> action){
        MonitorUtils.action(array, action);
    }

    private <T> List<T> filterToList(List<T> list, Predicate<? super T> predicate){
        return MonitorUtils.filterToList(list, predicate);
    }

    public JSONObject newResultObj(Object name, Object value, String unit){
        JSONObject result = MonitorUtils.newResultObj(name, value);
        result.put("unit", unit);
        return result;
    }

    public JSONObject newResultObj(Object name, Object value){
        return MonitorUtils.newResultObj(name, value);
    }

    public JSONObject newResultObj(String nameStr, Object name, String valueStr ,Object value){
        return MonitorUtils.newResultObj(nameStr, name, valueStr, value);
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

    /**
     *  判断某一指标是否应该存在部件
     */
    private boolean validHasFields(IndicatorTable ind) {
        return MonitorUtils.validHasFields(ind);
    }

    /**
     * 构造vCharts的空对象
     */
    private JSONObject empObj() {
        return MonitorUtils.empObj();
    }

    /** Boolean类型参数存在性判断，默认为false */
    public Boolean existJudgment(Boolean value){
        return MonitorUtils.existJudgment(value);
    }

    public Boolean existJudgment(Boolean value ,Boolean defaultValue){
        return MonitorUtils.existJudgment(value, defaultValue);
    }

    public JsonModel getHistoryHealth(String[] neIds, IndPeriod period, Integer interval) throws Exception{
        JSONArray rows = getHistoryHealthValues(neIds, period, interval);
        JSONArray columns = new JSONArray();
        columns.add("采集时间");
        List<NetworkEntity> neList = rpcProcessService.findNetworkEntityByIdIn(neIds);
        neList.forEach(itm -> columns.add(itm.getIp() + itm.getName()));

        JSONObject result = new JSONObject();
        result.put("columns", columns);
        result.put("rows", rows);
        result.put("unit", null);
        return new JsonModel().success(result);
    }

    public JSONArray getHistoryHealthValues(String[] neIds, IndPeriod period, Integer interval) throws Exception{
        Date now = new Date();
        Date startDate = IndPeriod.getStartDate(period, now);
        startDate.setTime(startDate.getTime() - startDate.getTime() % 60000L);
        Date beginTag = new Date(startDate.getTime());
        List<Date> allDate = Lists.newArrayList();
        while (startDate.before(now)) {
            Date itm = new Date(startDate.getTime());
            allDate.add(itm);
            startDate.setTime(startDate.getTime() + 1000 * 60 * interval);
        }

        Map<String, List<NeHealthHistory>> healthMap = Maps.newConcurrentMap();
        List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(neIds);
        for (NetworkEntity ne: nes) {
            List<NeHealthHistory> healthList = rpcProcessService.findHealthByNeIdIn(Lists.newArrayList(ne.getId()));
            healthMap.put(ne.getId(), healthList);
        }


        JSONArray rows = new JSONArray();
        for (Date date: allDate) {
            JSONObject row = new JSONObject();
            for (NetworkEntity ne: nes) {
                List<NeHealthHistory> healthList = healthMap.get(ne.getId());
                // 判断起始时间之前有无数据
                if (date.compareTo(beginTag) == 0) {
                    Optional<NeHealthHistory> firstHealth = healthList.stream()
                            .filter(itm -> itm.getUpdateDate().before(date))
                            .collect(Collectors.maxBy(Comparator.comparing(NeHealthHistory::getUpdateDate)));
                    if (firstHealth.isPresent()) {
                        row.put(ne.getIp() + ne.getName(), firstHealth.get().getHealth());
                    } else {
                        row.put(ne.getIp() + ne.getName(), null);
                    }
                    continue;
                }
                Optional<NeHealthHistory> optional = healthList.stream()
                        .filter(itm -> itm.getUpdateDate().before(date)
                                && itm.getUpdateDate().after(new Date(date.getTime() - 1000 * 60 * interval)))
                        .collect(Collectors.maxBy(Comparator.comparing(NeHealthHistory::getUpdateDate)));
                if (optional.isPresent()) {
                    row.put(ne.getIp() + ne.getName(), optional.get().getHealth());
                } else {
                    Object lastValue = rows.getJSONObject(rows.size() - 1).get(ne.getIp() + ne.getName());
                    row.put(ne.getIp() + ne.getName(), lastValue);
                }
            }
            row.put("采集时间", DateUtils.formatCommonDate(date));
            rows.add(row);
        }
        return rows;
    }

    public JsonModel getHistoryValue(String[] neIds, String indicators, String windows, String field, IntervalType intervalType,
                                      Integer interval) throws Exception{
        // 从弹窗数据中取得各资源选择的指标名和资源名
        JSONArray windowsJsonArray = JSONArray.parseArray(windows);
        if (ObjectUtils.isEmpty(windowsJsonArray)) {
            return new JsonModel(true, "弹窗数据未选择", empObj());
        }
        windowsJsonArray = windowBreakUp(windowsJsonArray);
        JSONArray componentArray = new JSONArray();
        for (int i = 0; i < windowsJsonArray.size(); i++) {
            componentArray = windowsJsonArray.getJSONObject(i).getJSONArray("ne");
        }
        String[] componentNames = new String[componentArray.size()];
        // 封装资源id和部件标识
        HashMap<String, String> componentMap = new HashMap<>();
        List<String> identifiers = Lists.newArrayList();
        for (int i = 0; i < componentArray.size(); i++) {
            componentNames[i] = componentArray.getJSONObject(i).getString("component");
            neIds[i] = componentArray.getJSONObject(i).getString("id");
            componentMap.put(neIds[i], componentNames[i]);
            identifiers.add(componentNames[i]);
        }


        // 如果资源选择为空，无数据展示
        if (ObjectUtils.isEmpty(neIds)) {
            return new JsonModel(true, "未选择资源", empObj());
        }
        // 排除无效、被删除、未监控资源
        List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(neIds);
        nes = nes.stream().filter(ne -> !ne.getManageStatus().equals(ManageStatus.Delected) && ne.isMonitoring()).collect(Collectors.toList());
        IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicators);
        field = !MonitorUtils.validHasFields(ind)? ind.getName() : field ;
        String finalField = field;
        if (ObjectUtils.isEmpty(ind)) {
            return new JsonModel(true, empObj());
        }
        // 返回的结果
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        columns.add("采集时间");
        // 判断该指标是否有属性
        // 封装资源id和资源
        HashMap<String, NetworkEntity> neMap = new HashMap<>();
        // 开始对多资源遍历循环查询
        Map<String, String> neIpMap = Maps.newHashMap();
        nes = nes.stream().filter(ne -> {
            columns.add(ne.getIp() + ne.getName());
            neIpMap.put(ne.getId(), ne.getIp() + ne.getName());
            Boolean strategyField = getStrategy(ne.getId(), indicators, finalField);
            if (strategyField) {
                neMap.put(ne.getId(), ne);
            }
            // 只要被监控的指标
            return strategyField;
        }).collect(Collectors.toList());


        FieldModel model = new FieldModel();

        IndicatorValueQO qo = new IndicatorValueQO();
        qo.setNeIds(Lists.newArrayList(neIds));
        qo.setIndicatorNames(Lists.newArrayList(ind.getName()));

        if (!MonitorUtils.validHasFields(ind)){
            field = ind.getName();
        }
        Map<String,List<String>> fieldMap = Maps.newHashMap();
        fieldMap.put(ind.getName(), Lists.newArrayList(field));
        qo.setFields(fieldMap);

        Map<String,String> indicatorTypeMap = Maps.newHashMap();
        indicatorTypeMap.put( ind.getName() ,ind.getIndicatorType());
        qo.setIndicatorTypes(indicatorTypeMap);

        if (!ObjectUtils.isEmpty(componentMap)){
            qo.setIdentifiers(identifiers);
            // 对CPU核心利用率指标进行特殊处理
            if ("cpu_usage_core".equals(ind.getName())){
                qo.setIdentifiers(null);
            }
        }

        qo.setIntervalType(intervalType);
        qo.setInterval(Long.valueOf(interval));
        JSONArray values = rpcProcessService.getIndAggValues(qo);
        if (ObjectUtils.isEmpty(values)){
            return new JsonModel(true, empObj());
        }
        model.setIndicator(ind);
        model.setField(field);
        JSONObject unitTransfer = MonitorUtils.unitTransfer(values, getUnit(model), field);
        values = unitTransfer.getJSONArray("result");
        List<String> cacheTime = Lists.newArrayList();
        for (int i = 0; i < values.size(); i++) {
            JSONObject obj = values.getJSONObject(i);
            JSONObject row = new JSONObject();
            String fetchDate = obj.getString("fetchDate");
            if (!cacheTime.contains(fetchDate)){
                JSONArray arr = values;
                JSONArray filter = MonitorUtils.filter(arr, o -> o.getString("fetchDate").equals(fetchDate));
                row.put("采集时间", fetchDate);
                MonitorUtils.action(filter, o -> row.put( neIpMap.get(o.getString("neId")), o.getString(finalField)));
                cacheTime.add(fetchDate);
                rows.add(row);
            }
        }
        result.put("rows", rows);
        result.put("columns", columns);
        result.put("unit", unitTransfer.getString("unit"));
        return new JsonModel(true, result);
    }

    private String getLabel(IndicatorTable ind, String field){
        String label = new String();
        if (!validHasFields(ind)) {
            label = ind.getLabel();
        } else {
            JSONArray fieldArr = ind.getFields();
            fieldArr = filter(fieldArr, o -> field.equals(o.getString("name")));
            if (!ObjectUtils.isEmpty(fieldArr)){
                label = ind.getLabel() + ":" + fieldArr.getJSONObject(0).getString("label");
            }
        }
        return label;
    }

    /**
     * 统一历史数据，并且去重
     * @param resultArray
     */
    private JSONArray unifyHistory(JSONArray resultArray) {
        JSONArray rows = new JSONArray();
        // 统计已遍历的采集时间
        String useTime = new String();
        boolean valueAllNull = false;
        boolean continueNull = false;
        for (int i = 0; i < resultArray.size(); i++) {
            continueNull = valueAllNull;
            JSONObject row = new JSONObject();
            String time = resultArray.getJSONObject(i).getString("采集时间");
            // 如果该采集时间已经被统计过了，跳过
            if (useTime.indexOf(time) > 0) {
                continue;
            }
            String name = resultArray.getJSONObject(i).getString("name");
            row.put("采集时间", time);
            String value = resultArray.getJSONObject(i).getString("value");
            row.put(name, MonitorUtils.getValueDob(value));
            valueAllNull = StringUtils.isEmpty(value);
            // 从下一个采集数据开始遍历，查询是否有相同时间、不同资源名的数据
            for (int j = i + 1; j < resultArray.size(); j++) {
                String jTime = resultArray.getJSONObject(j).getString("采集时间");
                String jName = resultArray.getJSONObject(j).getString("name");
                if (jTime.equals(time) && !jName.equals(name)) {
                    String otherValue = resultArray.getJSONObject(j).getString("value");
                    row.put(jName, MonitorUtils.getValueDob(otherValue));
                    valueAllNull = StringUtils.isEmpty(otherValue);
                }
            }
            useTime += " " + time;
            // 连续两次为空，跳过此数据
            if (continueNull && valueAllNull) {
                continue;
            }
            rows.add(row);
        }
        return rows;
    }

    private JSONArray getHistoryArray(List<IndicatorVal> records, FieldModel model,
                                      HashMap<String, String> componentMap,
                                      HashMap<String, NetworkEntity> neMap, IndPeriod period, Integer interval) {

        JSONArray resultArray = new JSONArray();

        IndicatorTable ind = model.getIndicator();
        String field = model.getField();


        // 属性值的JSON
        JSONObject valueJSON = null;

        // 采集时间缓存判断：避免产生同一资源相同采集时间的数据
        String cacheTime = new String();
        // 创建一个采集时间判断数，用于判断采集时间与显示时间的关系
        Integer timeValid = 0;
        // 创建一个缓存数据保存值
        Double cacheValue = null;

        for (IndicatorVal historyValue : records) {

            NetworkEntity ne = neMap.get(historyValue.getNeId());
            String name = ne.getIp() + ne.getName();
            String component = componentMap.get(historyValue.getNeId());

            JSON indicatorValues = (JSON) historyValue.getIndicatorValue();
            if (ObjectUtils.isEmpty(indicatorValues)) {
                continue;
            }
            // 若存在属性
            String unit = getUnit(model);

            if (ObjectUtils.isEmpty(indicatorValues)) {
                continue;
            }
            BigDecimal indValue;
            // 判断该指标是否有部件,COMPOUND、NUMBER、PERCENT类型的指标无部件，并且LIST类型中也有部件为空的(CPU核心利用率)
            if ((!"COMPOUND".equals(ind.getIndicatorType()) && validHasFields(ind))
                    && !StringUtils.isEmpty(component)) {
                // 如果是部件，则indicatorValues肯定为JSONArray类型
                JSONArray fieldArray = (JSONArray) indicatorValues;
                for (int j = 0; j < fieldArray.size(); j++) {
                    JSONObject obj = fieldArray.getJSONObject(j);
                    // 跳过错误的采集数据
                    if (Strings.isNullOrEmpty(obj.getString("identifier"))) {
                        continue;
                    }
                    if (obj.getString("identifier").equals(component)) {
                        valueJSON = obj;
                        break;
                    }
                }
                if (null == valueJSON) {
                    continue;
                }
                String tmpStr = valueJSON.getString(field);
                if (!Strings.isNullOrEmpty(tmpStr) && NUMBER_PATTERN.matcher(tmpStr).matches()) {
                    indValue = valueJSON.getBigDecimal(field);
                } else {
                    continue;
                }
            } else {
                // 非部件指标直接取值
                valueJSON = getValueJSON(indicatorValues);
                if (null == valueJSON) {
                    return new JSONArray();
                }
                try {
                    if (!validHasFields(ind)) {
                        if (!ObjectUtils.isEmpty(valueJSON.getString("result"))) {
                            String str = valueJSON.getString("result");
                            Matcher m = NUMBER_PATTERN.matcher(str);
                            while (m.find()) {
                                str = m.group();
                            }
                            indValue = new BigDecimal(str);
                        } else {
                            indValue = null;
                        }
                    } else {
                        if (!ObjectUtils.isEmpty(valueJSON.getString(field))) {
                            String str = valueJSON.getString(field);
                            Matcher m = NUMBER_PATTERN.matcher(str);
                            while (m.find()) {
                                str = m.group();
                            }
                            indValue = new BigDecimal(str);
                        } else {
                            indValue = null;
                        }
                    }
                } catch (Exception e) {
                    indValue = null;
                }
            }
            if (indValue != null && Objects.equals(unit, "%")) {
                indValue = indValue.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            // 采集时间判断：判断是否与上一采集时间点相同
            if (cacheTime.equals(historyValue.getFetchDateStr())) {
                // 如果相同则跳过该采集点
                continue;
            }
            // 缓存时间更新
            cacheTime = historyValue.getFetchDateStr();

            // 根据统计时段的选择，将采集时间轴给固定好
            if (ObjectUtils.isEmpty(period)) {
                return new JSONArray();
            }
            // 用一个JSONArray保存所有的展示时间
            JSONArray dateArray = new JSONArray();
            Date startDate = IndPeriod.getStartDate(period, new Date());
            // 先将startDate取整
            startDate.setTime(startDate.getTime() - startDate.getTime() % 60000L);
            if (!ObjectUtils.isEmpty(interval)) {
                Long currTime = System.currentTimeMillis();
                while (startDate.getTime() < currTime) {
                    JSONObject dateObj = new JSONObject();
                    dateObj.put("time", DateUtils.formatCommonDate(startDate));
                    dateObj.put("value", startDate.getTime());
                    dateArray.add(dateObj);
                    startDate.setTime(startDate.getTime() + 1000 * 60 * interval);
                }
            }

            /**
             * 采集时间存放
             */
            JSONObject resultObj = new JSONObject();

            // 如果采集间隔时间不存在，则展示所有存在的数据
            if (ObjectUtils.isEmpty(interval)) {
                resultObj.put("采集时间", DateUtils.formatCommonDate(
                        new Date(historyValue.getTm().getTime() - historyValue.getTm().getTime() % 60000L)));
                resultObj.put("name", name);
                resultObj.put("value", indValue);
                resultArray.add(resultObj);
            } else {
                // 找到距离采集时间最近的展示时间
                for (int j = timeValid; j < dateArray.size(); j++) {
                    Long showTime = dateArray.getJSONObject(j).getLong("value");
                    Long findTime = historyValue.getTm().getTime();
                    if (showTime > findTime && (showTime - 60 * 1000 * interval) < findTime) {
                        timeValid = j;
                        // 将数据先保存到缓存值中
                        if (cacheValue == null) {
                            cacheValue = indValue == null ? null : indValue.doubleValue();
                        } else {
                            cacheValue = indValue == null ? null : (cacheValue + indValue.doubleValue()) / 2;
                        }
                        break;
                    } else {
                        if (showTime - 60 * 1000 * interval > findTime) {
                            continue;
                        }
                        resultObj = new JSONObject();
                        resultObj.put("采集时间", dateArray.getJSONObject(j).get("time"));
                        resultObj.put("name", name);
                        resultObj.put("value", cacheValue);
                        resultArray.add(resultObj);
                        // 清除缓存
                        cacheValue = null;
                    }
                }
            }
        }
        return resultArray;
    }

    /**
     * 根据IndicatorTable和fieldLabel获取属性的基本单位
     * @param model
     * @return
     */
    private String getUnit(FieldModel model) {
        IndicatorTable ind = model.getIndicator();
        JSONObject fieldLabel = model.getFieldLabel();
        String unit = null;
        if (ObjectUtils.isEmpty(ind) && ObjectUtils.isEmpty(fieldLabel)){
            return unit;
        }
        if (validHasFields(ind)) {
            unit = fieldLabel.getString("unit");
        } else if ("PERCENT".equals(ind.getIndicatorType())) {
            unit = "%";
        } else {
            unit = null;
        }
        return unit;
    }

    private JSONArray getHistoryValByNe(FieldModel model, IndPeriod period, String sortStr) throws Exception{
        // 从数据中心获取监控指标 将其展示
        IndicatorValueCriteria criteria = new IndicatorValueCriteria();
        JSONObject sort = new JSONObject();
        sort.put("tm", sortStr);
        criteria.setSort(sort);
        criteria.setPagination(false);
        criteria.setIndicatorName(Lists.newArrayList(model.getIndicator().getName()));
        JSONObject must = new JSONObject();
        criteria.setSourceType("include");
        criteria.setSourceField(Lists.newArrayList("tm,v,ne,ind".split(",")));

        JSONObject obj = new JSONObject();
        obj.put("v." + model.getField(), "exists");
        criteria.setShould(obj);
        if (ObjectUtils.isEmpty(model.getComponent())){
            criteria.setFieldsNotNull(model.getField());
        }
        must.put("tm", newResultObj("gte", IndPeriod.getStartDate(period, new Date()),"lte", new Date()));
        must.put("ne", model.getNe().getId());
        criteria.setMust(must);
        indicatorService.searchHistoryIndicatorValue(criteria);
        if (criteria.getObject() == null) {
            return new JSONArray();
        }
        JSONArray result = JSON.parseArray(JSON.toJSONString(criteria.getObject()));
        //TODO 可以在这里做统一处理
        if (!ObjectUtils.isEmpty(model.getComponent())){
            action(result, o -> {
                JSONArray v = o.getJSONArray("v");
                filter(v, o1 -> o1.getString("identifier").equals(model.getComponent()));
                if (ObjectUtils.isEmpty(v)){
                    return;
                }
                o.put("value", v.getJSONObject(0).get(model.getField()));
            });
        }else {
            String field = Strings.isNullOrEmpty(model.getField()) ? model.getIndicator().getName() : model.getField();
            action(result, o -> {
                JSONObject v ;
                JSON json = (JSON) o.get("v");
                if (json instanceof JSONObject){
                    v = (JSONObject) json;
                }else if (json instanceof JSONArray){
                    v = ((JSONArray) json).getJSONObject(0);
                }else {
                    return;
                }
                o.put("value", v.get(field));
            });
        }
        return result;
    }

    /**
     * 将弹窗多选属性数据和多选部件数据进行拆分
     * @param window
     * @return
     */
    private JSONArray windowBreakUp(JSONArray window) {
        JSONArray tmpResult = new JSONArray();
        for (int i = 0; i < window.size(); i++) {
            JSONObject obj = window.getJSONObject(i);
            Object json = obj.get("fields");
            if (json instanceof JSONArray) {
                JSONArray fieldArr = JSONArray.parseArray(json.toString());
                if (ObjectUtils.isEmpty(fieldArr)) {
                    tmpResult.add(obj);
                    continue;
                }
                for (int j = 0; j < fieldArr.size(); j++) {
                    String field = fieldArr.getString(j);
                    JSONObject object = new JSONObject();
                    object.put("indicator", obj.get("indicator"));
                    object.put("ne", obj.get("ne"));
                    object.put("fields", field);
                    tmpResult.add(object);
                }
            } else {
                tmpResult.add(obj);
            }
        }
        JSONArray result = new JSONArray();
        for (int i = 0; i < tmpResult.size(); i++) {
            JSONObject obj = tmpResult.getJSONObject(i);
            JSONArray neArr = obj.getJSONArray("ne");
            for (int j = 0; j < neArr.size(); j++) {
                JSONObject neObj = neArr.getJSONObject(j);
                if ("false".equals(neObj.getString("multipleComponent"))) {
                    break;
                } else {
                    Object json = neObj.get("component");
                    if (json instanceof JSONArray) {
                        JSONArray compArr = neObj.getJSONArray("component");
                        if (ObjectUtils.isEmpty(compArr)) {
                            continue;
                        }
                        for (int k = 0; k < compArr.size(); k++) {
                            JSONObject object = new JSONObject();
                            JSONObject newNeObj = new JSONObject();
                            newNeObj.put("component", compArr.getString(k));
                            newNeObj.put("id", neObj.get("id"));
                            newNeObj.put("multipleComponent", neObj.get("multipleComponent"));
                            object.put("indicator", obj.get("indicator"));
                            object.put("ne", newNeObj);
                            object.put("fields", obj.get("fields"));
                            result.add(object);
                        }
                    } else {
                        JSONObject object = new JSONObject();
                        JSONObject newNeObj = new JSONObject();
                        newNeObj.put("component", json);
                        newNeObj.put("id", neObj.get("id"));
                        newNeObj.put("multipleComponent", neObj.get("multipleComponent"));
                        object.put("indicator", obj.get("indicator"));
                        object.put("ne", newNeObj);
                        object.put("fields", obj.get("fields"));
                        result.add(object);
                    }
                }
            }
        }
        result = ObjectUtils.isEmpty(result) ? tmpResult : result;
        return result;
    }

    public JsonModel getMultipleIndHistoryValue(String[] neIds, String[] indicators, String windows,
                                                IntervalType intervalType, Integer interval, IndPeriod period) throws Exception{
        // 从弹窗数据中取得各资源选择的指标名和资源名
        JSONArray windowsJsonArray = JSONArray.parseArray(windows);
        if (ObjectUtils.isEmpty(windowsJsonArray)) {
            return new JsonModel(true, "弹窗数据未选择", empObj());
        }
        windowsJsonArray = windowBreakUp(windowsJsonArray);
        JSONArray componentArray = new JSONArray();
        JSONArray indicatorArr = new JSONArray();
        for (int i = 0; i < windowsJsonArray.size(); i++) {
            componentArray.add(windowsJsonArray.getJSONObject(i).getJSONArray("ne"));
            indicatorArr.add(windowsJsonArray.getJSONObject(i).getString("indicator"));
        }
        String[] componentNames = new String[componentArray.size()];
        for (int i = 0; i < componentArray.size(); i++) {
            componentNames[i] = componentArray.getJSONArray(i).getJSONObject(0).getString("component");
        }

        JSONArray values = new JSONArray();
        List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(neIds);
        // 排除无效、被删除、未监控资源
        nes = nes.stream().filter(ne -> !ne.getManageStatus().equals(ManageStatus.Delected) && ne.isMonitoring()).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(nes)){
            return new JsonModel(true, "资源无效", empObj());
        }
        NetworkEntity ne = nes.get(0);

        // 返回的结果
        JSONObject result = new JSONObject();
        JSONArray columns = new JSONArray();
        columns.add("采集时间");
        String unit = new String() ;

        List<String> filedList = Lists.newArrayList();
        Map<String, String> filedLabelMap = Maps.newHashMap();

        // 获取数据中心的指标数据
        for (int i = 0; i < indicatorArr.size(); i++) {
            String indicatorId = indicatorArr.getString(i);
            JSONArray aggValues = new JSONArray();
            if ("healthy".equals(indicatorId)){
                // 对健康度指标特殊处理获取数据
                aggValues = getHistoryHealthValues(neIds, period, interval);
                filedLabelMap.put("healthy", "健康度");
                filedList.add("healthy");
                columns.add("健康度");
                action(aggValues, o -> {
                    o.put("field", "healthy");
                    o.put("neId", ne.getId());
                    o.put("fetchDate", o.get("采集时间"));
                    o.put("healthy", o.get(ne.getIp() + ne.getName()));
                });
            }else {
                String identifier = componentNames[i];
                IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicatorId);
                String field = windowsJsonArray.getJSONObject(i).getString("fields");
                filedList.add(field);
                if (Objects.isNull(ind)) {
                    return new JsonModel(true, empObj());
                }
                FieldModel model = new FieldModel();
                model.setNe(ne);
                model.setIndicator(ind);
                model.setField(field);
                unit = getUnit(model);
                String label = getLabel(ind, field);
                columns.add(label);
                filedLabelMap.put(field, label);
                IndicatorValueQO qo = new IndicatorValueQO();
                qo.setNeIds(Lists.newArrayList(ne.getId()));
                qo.setIndicatorNames(Lists.newArrayList(ind.getName()));

                Map<String,List<String>> fieldMap = Maps.newHashMap();
                fieldMap.put(ind.getName(), Lists.newArrayList(field));
                qo.setFields(fieldMap);

                Map<String,String> indicatorTypeMap = Maps.newHashMap();
                indicatorTypeMap.put( ind.getName() ,ind.getIndicatorType());
                qo.setIndicatorTypes(indicatorTypeMap);

                if (!ObjectUtils.isEmpty(identifier)){
                    qo.setIdentifiers(Lists.newArrayList(identifier));
                }

                qo.setIntervalType(intervalType);
                qo.setInterval(Long.valueOf(interval));
                aggValues = rpcProcessService.getIndAggValues(qo);
                // 该接口以指标为最小单位，保证同指标分类中可选多个指标进行数据展示
                action(aggValues, o -> o.put("field", field));
            }
            values.addAll(aggValues);
        }
        if (ObjectUtils.isEmpty(values)){
            return new JsonModel(true, empObj());
        }
        JSONObject unitTransfer = MonitorUtils.unitTransfer(values, unit, filedList);
        values = unitTransfer.getJSONArray("result");

        // 遍历统计
        List<String> cacheTime = Lists.newArrayList();
        JSONArray rows = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            JSONObject obj = values.getJSONObject(i);
            JSONObject row = new JSONObject();
            String fetchDate = obj.getString("fetchDate");
            if (!cacheTime.contains(fetchDate)){
                JSONArray arr = values;
                JSONArray filter = MonitorUtils.filter(arr, o -> o.getString("fetchDate").equals(fetchDate));
                row.put("采集时间", fetchDate);
                MonitorUtils.action(filter, o -> row.put( filedLabelMap.get(o.getString("field")), o.getString(MonitorUtils.getValueKey(o, filedList))));
                cacheTime.add(fetchDate);
                rows.add(row);
            }
        }

        result.put("unit", unitTransfer.getString("unit"));
        result.put("rows", rows);
        result.put("columns", columns);

        return new JsonModel(true, result);
    }

    /**
     * 获取topN的展示数据(定位到资源)
     * @param indicators topN展示的指标类型
     * @param domainId 域ID
     * @param neIds 资源IDs
     * @param baseNeClass 资源父类型
     * @param neClass 资源子类型
     * @param field 属性
     * @param number topN展示的记录条数
     * @param windows 弹窗返回值
     * @param order 排序方式
     * @param session
     * @return
     */
    public JsonModel getTopNByItObjects(String indicators, Long domainId, String neIds,
                                        String baseNeClass, String neClass, String field,
                                        String number, String windows, String order,
                                        HttpSession session, Boolean bar) throws Exception{
        bar = existJudgment(bar);
        if (Strings.isNullOrEmpty(indicators)) {
            return new JsonModel(true, "未选择指标！", empObj());
        }
        List<NetworkEntity> nes = Lists.newArrayList();
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        criteria = rpcProcessService.setCriteriaDomainIds(criteria, session, domainId);
        if (Strings.isNullOrEmpty(neIds)) {
            // 如果子类型为空，查询父类型，如果父类型也为空，则直接判断资源
            if (!Strings.isNullOrEmpty(neClass)) {
                criteria.setNeClass(NeClass.valueOf(neClass));
            } else {
                return new JsonModel(true, "子类型与资源均未选择！", empObj());
            }
            criteria.setMonitoring(true);
            nes = rpcProcessService.getNeList(criteria);
            neIds = nes.stream().map(NetworkEntity::getId).collect(Collectors.joining(","));
        }else {
            criteria.setIds(Lists.newArrayList(neIds.split(",")));
            criteria.setMonitoring(true);
            nes = rpcProcessService.getNeList(criteria);
        }
        if (CollectionUtils.isEmpty(nes)) {
            return new JsonModel(true, empObj());
        }
        JSONObject json = new JSONObject();
        // 健康度特殊化处理
        if ("healthy".equals(indicators)) {
            List<NeHealth> healthList = rpcProcessService.findNeHealthOrderByHealthy(Lists.newArrayList(neIds.split(",")), order);
            JSONArray columns = new JSONArray();
            columns.add("资源名称");
            columns.add("健康度");
            JSONArray rows = new JSONArray();
            int i = 0;
            int num = Integer.parseInt(number);
            for (NeHealth health : healthList) {
                if (i < num) {
                    JSONObject row = new JSONObject();
                    NetworkEntity ne = rpcProcessService.findNetworkEntityById(health.getId());
                    if (ObjectUtils.isEmpty(ne)) {
                        continue;
                    }
                    row.put("资源名称", ne.getName() + "(" + ne.getIp() + ")");
                    row.put("健康度", health.getHealth());
                    rows.add(row);
                    i++;
                }
            }
            json.put("columns", columns);
            json.put("rows", rows);
            return new JsonModel(true, json);
        } else {
            JSONArray window = JSONArray.parseArray(windows);
            if (!ObjectUtils.isEmpty(windows)) {
                window = windowBreakUp(window);
            }
            IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicators);
            if (Objects.equals("LIST", ind.getIndicatorType())) {
                for (int i = 0; i < window.size(); i++) {
                    JSONObject itm = window.getJSONObject(i);
                    if (StringUtils.isBlank(itm.getJSONObject("ne").getString("component"))) {
                        return new JsonModel(false, "未选择部件，请在配置资源指标详细弹窗中重新配置!");
                    }
                }
            }
            if (ObjectUtils.isEmpty(field) && validHasFields(ind)) {
                return new JsonModel(true, empObj());
            }
            json = getTopNByItObjects(ind, nes, number, field, window, order);
        }
        if (Objects.isNull(json)) {
            return new JsonModel(true, empObj());
        }
        if (bar) {
            JSONArray rows = json.getJSONArray("rows");
            if (!ObjectUtils.isEmpty(rows)) {
                JSONArray tmpRows = new JSONArray();
                for (int i = rows.size() - 1; i >= 0; i--) {
                    JSONObject obj = rows.getJSONObject(i);
                    tmpRows.add(obj);
                }
                json.put("rows", tmpRows);
            }
        }
        return new JsonModel(true, json);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject getTopNByItObjects(IndicatorTable ind, List<NetworkEntity> neList, String topStr, String field,
                                         JSONArray window, String order) throws Exception{
        JSONObject json = new JSONObject();
        List<RunStatus> runStatuses = Lists.newArrayList(RunStatus.Loading, RunStatus.Good, RunStatus.Warning);
        neList = neList.stream().filter(ne -> runStatuses.contains(ne.getRunStatus())).collect(Collectors.toList());
        neList = neList.stream().filter(ne -> !ne.getManageStatus().equals(ManageStatus.Delected)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(neList)) {
            return null;
        }
        String itObjectIds = String.join(",",neList.stream().map(NetworkEntity::getId).toArray(String[]::new));
        List<String> availableIds = new ArrayList<>();
        availableIds.add(itObjectIds);
        JSONObject ipObj = new JSONObject();
        JSONObject nameObj = new JSONObject();
        for (NetworkEntity ne : neList) {
            ipObj.put(ne.getId(), ne.getIp());
            nameObj.put(ne.getId(), ne.getName());
            availableIds.add(ne.getId());
        }
        if (availableIds.size() == 0) {
            return null;
        }

        // 默认为-1，表明查询个数不限
        Integer topNumber = -1;
        if (StringUtils.isNoneBlank(topStr)) {
            try {
                // String类型转换成Integer的转换异常处理
                topNumber = Integer.valueOf(topStr);
            } catch (Exception e) {
                log.error("字符串:{}无法转换成Integer类型,错误:", topStr, e);
                return null;
            }
        }
//        IndicatorValueCriteria InvCriteria = new IndicatorValueCriteria();
//        JSONObject should = new JSONObject();
//        should.put("ne", new ArrayList<>(availableIds));
//        InvCriteria.setShould(should);
//        InvCriteria.setIndicatorName(indicators);
//        InvCriteria.setPagination(false);
//        InvCriteria = indicatorService.searchIndicatorValue(InvCriteria);
        com.uxsino.commons.db.criteria.IndicatorValueCriteria qo = new com.uxsino.commons.db.criteria.IndicatorValueCriteria();
        qo.setIndicatorName(Lists.newArrayList(ind.getName()));
        List<String> neIds = neList.stream().map(NetworkEntity::getId).collect(Collectors.toList());
        qo.setNeIds(neIds);
        List<IndValue> indValues = rpcProcessService.getCurIndValues(qo);
        if (ObjectUtils.isEmpty(indValues)) {
            return null;
        }
        // 如果是部件，则需要取属性值
        JSONArray allValue = new JSONArray();
        String unit = null;
        boolean multipleComp = false;

        FieldModel model = new FieldModel();
        model.setIndicator(ind);
        model.setField(field);
        JSONObject fieldLabel = model.getFieldLabel();
        unit = Optional.ofNullable(fieldLabel).flatMap(o -> Optional.ofNullable(o.getString("unit"))).orElse(unit);
        if ("LIST".equals(ind.getIndicatorType())) {
            if (StringUtils.isEmpty(field)) {
                return null;
            }
            IndicatorValueUtils valueUtils = new IndicatorValueUtils();
            JSONArray arr = new JSONArray();
            for (int i = 0; i < window.size(); i++) {
                JSONObject windowObj = window.getJSONObject(i);
                JSONObject obj = new JSONObject();
                obj.put("id", windowObj.getJSONObject("ne").getString("id"));
                obj.put("component", windowObj.getJSONObject("ne").getString("component"));
                arr.add(obj);
            }
            for (int i = 0; i < arr.size(); i++) {
                JSONObject object = arr.getJSONObject(i);
                String neId = object.getString("id");
                String component = object.getString("component");
                for (IndValue indValue: indValues) {
                    if (indValue.getNeId().equals(neId)){
                        JSONObject obj = new JSONObject();
                        JSONObject value = new JSONObject();
                        // 对cup核心利用率指标做特殊化处理
                        if ("cpu_usage_core".equals(ind.getName())){
                            value = getValueJSON(indValue.getIndicatorValue());
                        }else {
                            value = getValueJSON(indValue.getIndicatorValue(), component);
                        }
                        obj.put("num", value.getString(field));
                        valueUtils.transferItem(fieldLabel, value);
                        obj.put("id", neId);
                        obj.put("value", value.getString(field));
                        obj.put("unit", unit);

                        NeComponentQuery compQuery = new NeComponentQuery();
                        compQuery.setNeIds(Lists.newArrayList(neId));
                        compQuery.setIndicatorName(ind.getName());
                        List<Map<String, Object>> idAndComponent = rpcProcessService.findNeComps(compQuery);
                        Map<String, Object> compMap =
                                idAndComponent.stream().filter(map -> map.get("identifier").equals(component))
                                        .findFirst().orElse(Maps.newHashMap());
                        // 对cup核心利用率指标做特殊化处理
                        if ("cpu_usage_core".equals(ind.getName())){
                            obj.put("componentName", "cpu");
                        }else {
                            obj.put("componentName", compMap.get("componentName").toString());
                        }
                        allValue.add(obj);
                    }
                }

            }
        } else if ("COMPOUND".equals(ind.getIndicatorType())) {
            if (StringUtils.isEmpty(field)) {
                return null;
            }
            IndicatorValueUtils valueUtils = new IndicatorValueUtils();
            for (IndValue value : indValues) {
                JSONObject obj = new JSONObject();
                JSONObject valueJson = getValueJSON(value.getIndicatorValue());
                obj.put("num", valueJson.getString(field));
                valueUtils.transferItem(fieldLabel, valueJson);
                obj.put("id", value.getNeId());
                obj.put("value", valueJson.getString(field));
                obj.put("unit", unit);
                allValue.add(obj);
            }
        } else {
            // 如果不是部件，直接取值
            if ("PERCENT".equals(ind.getIndicatorType())) {
                unit = "%";
            }
            for (IndValue value : indValues) {
                JSONObject obj = new JSONObject();
                JSONObject valueJson = getValueJSON(value.getIndicatorValue());
                obj.put("num", valueJson.getString(value.getIndicatorName()));
                obj.put("id", value.getNeId());
                obj.put("value", valueJson.getString(value.getIndicatorName()) + unit);
                obj.put("unit", unit);
                allValue.add(obj);
            }
        }
        JSONArray results = new JSONArray();
        // 降序或升序排列(取决于order)取topNumber个
        for (int fI = 0; fI < allValue.size(); fI++) {
            int i = fI;
            JSONObject temp = new JSONObject();
            JSONObject jsonAvg = (JSONObject) allValue.get(i);
            for (int secondI = allValue.size() - 1; secondI > fI; secondI--) {
                JSONObject jsonAvgs = (JSONObject) allValue.get(secondI);
                if ((JSONUtils.getDoubleValue(jsonAvgs, "num") > JSONUtils.getDoubleValue(jsonAvg, "num")
                        && "desc".equals(order))
                        || (JSONUtils.getDoubleValue(jsonAvgs, "num") < JSONUtils.getDoubleValue(jsonAvg, "num")
                        && "asc".equals(order))) {
                    i = secondI;
                    jsonAvg = (JSONObject) allValue.get(i);
                }
            }
            results.add(allValue.get(i));
            allValue.remove(i);
            allValue.add(0, temp);
            if (results.size() == topNumber) {
                break;
            }
        }
        String name = "指标值";
        try {
            if (org.apache.commons.lang3.ObjectUtils.allNotNull(ind.getFields())) {
                for (int i = 0; i < ind.getFields().size(); i++) {
                    JSONObject obj = ind.getFields().getJSONObject(i);
                    if (field.equals(obj.getString("name"))) {
                        name = obj.getString("label");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject transfer = MonitorUtils.unitTransfer(results, unit, "num");
        unit = transfer.getString("unit");
        results = transfer.getJSONArray("result");
        JSONArray columns = new JSONArray();
        columns.add("资源");
        if (Strings.isNullOrEmpty(unit)) {
            columns.add(name);
        } else {
            columns.add(name + "(" + unit + ")");
        }
        json.put("columns", columns);
        JSONArray rows = new JSONArray();
        for (Object result1 : results) {
            JSONObject result = (JSONObject) result1;
            JSONObject row = new JSONObject();
            String neName = nameObj.getString(result.getString("id")) + "(" + ipObj.getString(result.getString("id")) + ")";
            if (multipleComp) {
                row.put("资源", neName + result.getString("componentName"));
            } else {
                row.put("资源", neName);
            }
            if (Strings.isNullOrEmpty(unit)) {
                row.put(name, result.getString("num"));
            } else {
                row.put(name + "(" + unit + ")", result.getString("num"));
            }
            rows.add(row);
        }
        json.put("rows", rows);
        return json;
    }



    private Long[] getDomainIds(Long domainId, HttpSession session) {
        if (domainId == null) {
            // 域控制
            if (!SessionUtils.isSuperAdmin(session)) {
                List<Long> domainIds = domainUtils.getUserDomainIds(session);
                return domainIds.toArray(new Long[domainIds.size()]);
            }else {
                return null;
            }
        }
        return new Long[] { domainId };
    }

    /**
     * 获取多资源多指标统计的展示数据
     * @param neIds 资源IDs
     * @param indicators 展示的指标类型
     * @param windows 弹窗返回值
     * @param session
     * @return
     */
    public JsonModel getMultipleIndicatorObject(String[] neIds, String[] indicators, String windows, HttpSession session) throws Exception{
        JSONArray window = JSONArray.parseArray(windows);
        if (ObjectUtils.isEmpty(window)) {
            return new JsonModel(true, empObj());
        }
        // 如果多个资源无公共指标，即指标选择为空时，应该展示无数据
        if (ObjectUtils.isEmpty(indicators)) {
            return new JsonModel(true, empObj());
        }
        // 如果没有选择资源时，也展示无数据
        if (ObjectUtils.isEmpty(neIds)) {
            return new JsonModel(true, empObj());
        }
        // 存放失效资源Id
        List<String> invalidId = Lists.newArrayList();
        window = windowBreakUp(window);
        // 返回的结果
        JSONObject result = new JSONObject();
        JSONArray columns = new JSONArray();
        JSONArray rows = new JSONArray();
        columns.add("资源");
        // 用于存放最终的结果数据，并且指标最大选择25个
        JSONArray[] resultArray = new JSONArray[26];
        // 对paramsArray遍历拆解
        for (int i = 0; i < window.size(); i++) {
            resultArray[i + 1] = new JSONArray();
            // 指标已经被确定，接下来就是确定资源名
            JSONObject params = (JSONObject) window.get(i);
            String indicatorName = params.getString("indicator");
            String fieldsName = params.getString("fields");
            // 得到指标
            IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicatorName);
            if (Objects.isNull(ind)) {
                return new JsonModel(true, empObj());
            }
            // 属性字段的JSON
            JSONObject fieldLabel = null;
            // 属性值的JSON
            JSONObject valueJSON = null;
            // 如果是NUMBER类型或者PERCENT类型的指标没有属性，直接取值
            if (validHasFields(ind)) {
                FieldModel model = new FieldModel();
                model.setIndicator(ind);
                model.setField(fieldsName);
                fieldLabel = Optional.ofNullable(model.getFieldLabel()).orElse(null);
                if (Objects.isNull(fieldLabel)) {
                    return new JsonModel(true, empObj());
                }
            }
            JSONArray neArray = (JSONArray) params.get("ne");
            List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(neIds);
            for (int j = 0; j < neArray.size(); j++) {
                // 资源名已经被确定
                JSONObject neObj = (JSONObject) neArray.get(j);
                String neId = neObj.get("id").toString();
                String componentName = neObj.getString("component");
                NetworkEntity ne = nes.stream().filter(n -> n.getId().equals(neId)).findFirst().orElse(null);
                if (ObjectUtils.isEmpty(ne)){
                    continue;
                }
                // 如果资源已被删除或者取消监控，将此资源的数据展示取消
                if (ne.getManageStatus().equals(ManageStatus.Delected) || !ne.isMonitoring()) {
                    invalidId.add(neId);
                    continue;
                }
                if (ObjectUtils.isEmpty(ne)) {
                    return new JsonModel(true, "该资源不存在", empObj());
                }
                // 获取指标监控策略
                Boolean strategyField = getStrategy(neId, indicatorName, fieldsName);
                IndValue indValue = null;
                // 若指标被监控
                if (strategyField) {
                    // 获取指标的值
                    indValue = indicatorService.findIndValue(neId, indicatorName, null);
                }
                // 如果该资源的该指标无值，将其赋空值，继续统计
                if (Objects.isNull(indValue) || ObjectUtils.isEmpty(indValue.getIndicatorValue())) {
                    JSONObject resultObj = new JSONObject();
                    if (!ObjectUtils.isEmpty(fieldLabel)) {
                        resultObj.put("name",
                                ind.getLabel() + " : " + fieldLabel.getString("label")
                                        + (ObjectUtils.isEmpty(fieldLabel.get("unit")) ? ""
                                        : "(" + fieldLabel.get("unit") + ")"));
                    } else {
                        if ("PERCENT".equals(ind.getIndicatorType())) {
                            resultObj.put("name", ind.getLabel() + "(%)");
                        } else {
                            resultObj.put("name", ind.getLabel());
                        }
                    }
                    resultObj.put("value", null);
                    resultArray[i + 1].add(resultObj);
                    continue;
                }
                JSONObject resultObj = new JSONObject();
                if ("cpu_usage_core".equals(ind.getName())){
                    //对cpu_usage_core指标进行特殊处理
                    valueJSON = getValueJSON(indValue.getIndicatorValue());
                }else {
                    valueJSON = getValueJSON(indValue.getIndicatorValue(), componentName);
                }
                // 进行属性有无的判断
                if (!validHasFields(ind)) {
                    // 该指标若本身就无部件无属性就直接取值
                    String value = MonitorUtils.getValueStr(valueJSON.getString(indValue.getIndicatorName()));
                    String unit = "PERCENT".equals(ind.getIndicatorType())? "%" : "";
                    resultObj.put("name", ind.getLabel() + (ObjectUtils.isEmpty(unit) ? "" : "(" + unit + ")"));
                    resultObj.put("value", value);
                    resultObj.put("unit", unit);
                    resultArray[i + 1].add(resultObj);
                } else {
                    String unit = Optional.ofNullable(fieldLabel).flatMap(o -> Optional.ofNullable(o.getString("unit"))).orElse(null);
                    String label = Optional.ofNullable(fieldLabel).flatMap(o -> Optional.ofNullable(o.getString("label"))).orElse(null);
                    String value = MonitorUtils.getValueStr(valueJSON.getString(fieldsName));
                    if (!Strings.isNullOrEmpty(unit) && !StringUtils.isEmpty(value)) {
                        int index = value.lastIndexOf(" ");
                        String subValue = value;
                        value = index > 0 ? subValue.substring(0, index) : value;
                        unit = index > 0 ? subValue.substring(index + 1) : unit;
                    }
                    resultObj.put("name", ind.getLabel() + " : " + label + (ObjectUtils.isEmpty(unit) ? "" : "(" + unit + ")"));
                    resultObj.put("value", value);
                    resultObj.put("unit", unit);
                    resultArray[i + 1].add(resultObj);
                }
            }
        }
        for (int i = 0; i < resultArray.length; i++) {
            if (resultArray[i] != null){
                JSONObject obj = MonitorUtils.unitTransfer(resultArray[i], resultArray[i].getJSONObject(0).getString("unit"), "value");
                String unit = obj.getString("unit");
                String name = new String();
                for (int j = 0; j < resultArray[i].size(); j++) {
                    JSONObject o = resultArray[i].getJSONObject(j);
                    name = o.getString("name");
                    if (name.contains("(")){
                        name = name.substring(0, name.indexOf("(") + 1) + unit + ")";
                        o.put("name", name);
                    }
                }
                columns.add(name);
            }
        }
        resultArray[0] = new JSONArray();
        JSONObject params = (JSONObject) window.get(0);
        JSONArray neArray = (JSONArray) params.get("ne");
        List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(neIds);
        for (int j = 0; j < neArray.size(); j++) {
            JSONObject neObj = (JSONObject) neArray.get(j);
            String neId = neObj.get("id").toString();
            if (invalidId.contains(neId)) {
                continue;
            }
            NetworkEntity ne = nes.stream().filter(n -> n.getId().equals(neId)).findFirst().orElse(null);
            if (ObjectUtils.isEmpty(ne)){
                continue;
            }
            JSONObject obj = new JSONObject();
            obj.put("name", "资源");
            obj.put("value", ne.getIp());
            resultArray[0].add(obj);
        }
        for (int i = 0; i < resultArray[0].size(); i++) {
            JSONObject row = new JSONObject();
            for (int j = 0; j < resultArray.length; j++) {
                if (ObjectUtils.isEmpty(resultArray[j])) {
                    break;
                }
                String name = resultArray[j].getJSONObject(i).getString("name");
                String value = ObjectUtils.isEmpty(resultArray[j].getJSONObject(i).getString("value")) ? ""
                        : resultArray[j].getJSONObject(i).getString("value");
                row.put(name, value);
            }
            rows.add(row);
        }
        result.put("columns", columns);
        result.put("rows", rows);
        return new JsonModel(true, result);
    }

    @SuppressWarnings("unchecked")
    public JsonModel countNeLink(Boolean abnormal, HttpSession session) throws Exception{
        Long[] domains;
        if (ObjectUtils.isEmpty(abnormal)) {
            abnormal = false;
        }
        List<Long> domainList = getDomainList(null, session);
        domains = domainList.toArray(new Long[domainList.size()]);
        Set<String> networkLinkIds = Sets.newHashSet();
        NetworkLinkModel networkLinkModel = new NetworkLinkModel();
        networkLinkModel.setNetworkLinkIds(new ArrayList<>(networkLinkIds));
        networkLinkModel.setNeIds(rpcProcessService.getNeIdsByDomainIds(domains, session));
        PageModel temPage = new PageModel();
        temPage.setCurrentNo(1);
        temPage.setPageSize(10000);
        List<NetworkLinkModel> list = rpcProcessService.findNeLinks(temPage, networkLinkModel);
        JSONObject result = new JSONObject();
        if (abnormal) {
            int count = 0;
            // 异常链路的状态为：告警、断开、未知
            List<NetworkLinkStatus> statuses = Lists.newArrayList(NetworkLinkStatus.Alert,
                    NetworkLinkStatus.Unconnection, NetworkLinkStatus.Unkown);
            for (NetworkLinkModel linkModel : list) {
                if (statuses.contains(linkModel.getLinkStatus())) {
                    count++;
                }
            }
            result.put("name", "异常链路数");
            result.put("value", count);
        } else {
            result.put("name", "链路条数");
            result.put("value", list.size());
        }
        result.put("unit", "");
        return new JsonModel(true, result);
    }

    public JsonModel valueNetwork(HttpSession session, String sourceId, String sourceIfName, String targetId, String targetIfName, String field) {
        try {
            List<Long> domainList = getDomainList(null, session);
            Long[] domains = domainList.toArray(new Long[domainList.size()]);
            Set<String> networkLinkIds = Sets.newHashSet();
            NetworkLinkModel networkLinkModel = new NetworkLinkModel();
            networkLinkModel.setNetworkLinkIds(new ArrayList<>(networkLinkIds));
            networkLinkModel.setNeIds(rpcProcessService.getNeIdsByDomainIds(domains, session));
            networkLinkModel.setSourceId(sourceId);
            networkLinkModel.setSourceIfName(sourceIfName);
            networkLinkModel.setTargetId(targetId);
            networkLinkModel.setTargetIfName(targetIfName);
            PageModel temPage = new PageModel();
            temPage.setCurrentNo(1);
            temPage.setPageSize(10000);
            List<NetworkLinkModel> list = rpcProcessService.findNeLinks(temPage, networkLinkModel);
            Map<String, String> nameMap = Maps.newHashMap();
            nameMap.put("speed", "链路带宽");
            nameMap.put("speedUsage", "带宽利用率");
            nameMap.put("upBps", "上行流量");
            nameMap.put("downBps", "下行流量");
            JSONObject result = new JSONObject();
            result.put("name", nameMap.get(field));
            if (ObjectUtils.isEmpty(list)) {
                result.put("value", "");
                result.put("unit", "");
                return new JsonModel(false, "参数有误", result);
            }
            NetworkLinkModel ne = new NetworkLinkModel();
            for (NetworkLinkModel tmpNe : list) {
                if (sourceIfName.equals(tmpNe.getSourceIfName()) && targetIfName.equals(tmpNe.getTargetIfName())
                        && sourceId.equals(tmpNe.getSourceId()) && targetId.equals(tmpNe.getTargetId())) {
                    ne = tmpNe;
                    break;
                }
            }
            if ("speed".equals(field)) {
                result.put("value", ne.getSpeed());
                result.put("unit", "Mbps");
            } else if ("speedUsage".equals(field)) {
                result.put("value",
                        Objects.isNull(ne.getSpeedUsage()) ? "" : String.format("%.2f", ne.getSpeedUsage()));
                result.put("unit", "%");
            } else {
                if ("upBps".equals(field)) {
                    Map<String, Object> map = UnitTransfer(ne.getUpBps(), "bps");
                    result.put("value", String.format("%.2f", (Double) map.get("value")));
                    result.put("unit", map.get("unit"));
                } else if ("downBps".equals(field)) {
                    Map<String, Object> map = UnitTransfer(ne.getDownBps(), "bps");
                    result.put("value", String.format("%.2f", (Double) map.get("value")));
                    result.put("unit", map.get("unit"));
                }
            }
            return new JsonModel(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage(), empObj());
        }
    }

    /**
     * 单位转换类
     * @param value
     * @param unit
     * @return
     */
    private Map<String, Object> UnitTransfer(Double value, String unit) {
        Map<String, Object> result = Maps.newHashMap();
        if ("bps".equals(unit)) {
            if (value / (1024L * 1024L) >= 1) {
                result.put("value", value / (1024L * 1024L));
                result.put("unit", "Mbps");
            } else if (value / 1024L >= 1) {
                result.put("value", value / 1024L);
                result.put("unit", "Kbps");
            } else {
                result.put("value", value);
                result.put("unit", "bps");
            }
        }
        return result;
    }

    public JsonModel networkTable(HttpSession session, String network, Long number, String[] field) {
        try {
            if (ObjectUtils.isEmpty(field)) {
                field = new String[] { "speed", "speedUsage", "upBps", "downBps", "sourceIfName", "targetIfName" };
            }
            List<Long> domainList = getDomainList(null, session);
            Long[] domains = domainList.toArray(new Long[domainList.size()]);
            NetworkLinkModel networkLinkModel = new NetworkLinkModel();
            networkLinkModel.setNeIds(rpcProcessService.getNeIdsByDomainIds(domains, session));
            PageModel temPage = new PageModel();
            temPage.setCurrentNo(1);
            temPage.setPageSize(10000);
            List<NetworkLinkModel> list = rpcProcessService.findNeLinks(temPage, networkLinkModel);
            if (!Strings.isNullOrEmpty(network)){
                List<String> sourceIds = Lists.newArrayList(network.split(","));
                list = list.stream().filter(net -> sourceIds.contains(net.getSourceId())).collect(Collectors.toList());
            }
            Map<String, String> nameMap = Maps.newLinkedHashMap();
            nameMap.put("speed", "链路带宽");
            nameMap.put("speedUsage", "带宽利用率");
            nameMap.put("upBps", "上行流量");
            nameMap.put("downBps", "下行流量");
            nameMap.put("sourceIfName", "源接口");
            nameMap.put("targetIfName", "目的接口");
            JSONObject result = new JSONObject();
            List<String> fields = Lists.newArrayList(field);
            JSONArray rows = new JSONArray();
            JSONArray columns = new JSONArray();
            columns.add("源IP");
            columns.add("目的IP");
            for (Map.Entry<String, String> entry : nameMap.entrySet()) {
                if (fields.contains(entry.getKey().toString())) {
                    columns.add(entry.getValue().toString());
                }
            }
            int num = 0;
            for (NetworkLinkModel ne : list) {
                JSONObject row = new JSONObject(true);
                row.put("源IP", ne.getSourceIp());
                row.put("目的IP", ne.getTargetIp());
                if (fields.contains("speed")) {
                    row.put(nameMap.get("speed"), ne.getSpeed() + "Mbps");
                }
                if (fields.contains("speedUsage")) {
                    row.put(nameMap.get("speedUsage"), String.format("%.2f", ne.getSpeedUsage()) + "%");
                }
                if (fields.contains("upBps")) {
                    Map<String, Object> map = UnitTransfer(ne.getUpBps(), "bps");
                    row.put(nameMap.get("upBps"), String.format("%.2f", (Double) map.get("value")) + map.get("unit"));
                }
                if (fields.contains("downBps")) {
                    Map<String, Object> map = UnitTransfer(ne.getDownBps(), "bps");
                    row.put(nameMap.get("downBps"), String.format("%.2f", (Double) map.get("value")) + map.get("unit"));
                }
                if (fields.contains("sourceIfName")) {
                    row.put(nameMap.get("sourceIfName"), ne.getSourceIfName());
                }
                if (fields.contains("targetIfName")) {
                    row.put(nameMap.get("targetIfName"), ne.getTargetIfName());
                }
                if (++num > number) {
                    break;
                }
                rows.add(row);
            }
            result.put("columns", columns);
            result.put("rows", rows);
            return new JsonModel(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonModel(false, e.getMessage(), empObj());
        }
    }

    /**
     * 获取资源分布数据
     * @param status
     * @param range
     * @param names
     * @param areaName
     * @param period
     * @return
     */
    public JsonModel neDivision(HttpSession session, String[] status, String range, String[] names, String areaName, String period) throws Exception{
        List<RunStatus> statusList = Lists.newArrayList();
        if (ObjectUtils.isEmpty(status)) {
            statusList = Lists.newArrayList(RunStatus.values());
        } else {
            for (String str : status) {
                statusList.add(RunStatus.valueOf(str));
            }
        }
        JsonModel model = mcService.getDomainList(session.getId());
        ArrayList<HashMap> allDomains = (ArrayList<HashMap>) model.getObj();
        Map<String, Integer> result = Maps.newHashMap();
        for (String name : names) {
            result.put(name, 0);
        }
        // 遍历所有的域，取他们的地区属性
        for (HashMap map : allDomains) {
            String region = map.get("region").toString();
            if (!region.contains(areaName)) {
                continue;
            }
            for (String name : names) {
                if (region.contains(name)) {
                    List<NetworkEntity> neList = Lists.newArrayList();
                    NetworkEntityCriteria criteria = new NetworkEntityCriteria();
                    criteria.setDomainId(Long.parseLong(map.get("id").toString()));
                    criteria.setMonitoring(true);
                    for (RunStatus runStatus : statusList) {
                        criteria.setRunStatus(runStatus);
                        neList.addAll(rpcProcessService.getNeList(criteria));
                    }
                    result.put(name, result.get(name) + neList.size());
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

    public JsonModel countNe(String domainId, String[] baseNeClass, String[] neClass, String status, HttpSession session) throws Exception {
        NetworkEntityCriteria criteria = new NetworkEntityCriteria();
        if (Strings.isNullOrEmpty(domainId)) {
            List<Long> domainList = getDomainList(null, session);
            criteria.setDomainIds(domainList);
        } else {
            criteria.setDomainId(Long.parseLong(domainId));
        }
        if (ObjectUtils.isEmpty(neClass)) {
            List<NeClass> neClasses = Lists.newArrayList();
            Arrays.stream(baseNeClass).forEach(base -> {
                neClasses.addAll(BaseNeClass.valueOf(base).getNeClass());
            });
            criteria.setNeClasses(ObjectUtils.isEmpty(baseNeClass) ? null : neClasses);
        } else {
            List<NeClass> neClasses = Lists.newArrayList();
            Arrays.stream(neClass).forEach(ne -> neClasses.add(NeClass.valueOf(ne)));
            criteria.setNeClasses(neClasses);
        }
        List<NetworkEntity> neList = Lists.newArrayList();
        if ("abnormal".equals(status)) {
            criteria.setRunStatusIn(Lists.newArrayList(RunStatus.Warning, RunStatus.Unconnection, RunStatus.Unknown));
        } else {
            criteria.setRunStatus(Strings.isNullOrEmpty(status) ? null : RunStatus.valueOf(status));
        }
        neList = rpcProcessService.getAllNeList(criteria);
        JSONObject result = new JSONObject();
        if (ObjectUtils.isEmpty(neList)) {
            result.put("name", Strings.isNullOrEmpty(status) ? "总设备数" : "abnormal".equals(status) ? "故障设备数" : "资源个数");
            result.put("value", 0);
            result.put("unit", "");
        }
        result.put("name", Strings.isNullOrEmpty(status) ? "总设备数" : "abnormal".equals(status) ? "故障设备数" : "资源个数");
        result.put("value", neList.size());
        result.put("unit", "");
        return new JsonModel(true, result);
    }


    /**
     * 双轴-历史值统计图
     * @param period
     * @return
     */
    public JsonModel multipleIndicatorHistory(String neId, String indicatorsLeft, String componentNameLeft,
                                              String fieldLeft, String indicatorsRight, String componentNameRight,
                                              String fieldRight, IndPeriod period) throws Exception{
        // 由于双轴曲线没有可选的时间间隔,故统计时段为天、周、月时的时间间隔分别为5分钟、20分钟、60分钟

        //由于会出现indicatorName和fieldName相同但ComponentName不同的情况，如果只将indicatorName+fieldName
        //作为key传给前端，当出现上述情况时就会导致同样的key有不同的值，前端无法区分，因此需要将中间的component
        //Name也加入，所以必须要依据给出的componentName代码来查到其名字。
        JsonModel leftJsonModel = monitorDataParamsService.getComponentName(new String[]{neId}, indicatorsLeft);
        JSONArray leftComponentNameKVs = (JSONArray) leftJsonModel.getObj();
        JsonModel rightJsonModel = monitorDataParamsService.getComponentName(new String[]{neId}, indicatorsRight);
        JSONArray rightComponentNameKVs = (JSONArray) rightJsonModel.getObj();
        IntervalType intervalType = IntervalType.minute;
        Integer interval = 5;
        if (Objects.equals("_1week", period.name())) {
            interval = 20;
        } else if (Objects.equals("_1month", period.name())) {
            interval = 60;
        }
        JSONArray columns = new JSONArray();
        columns.add("采集时间");
        JSONArray rows = new JSONArray();
        JSONObject leftValues = new JSONObject();
        JSONObject rightValues = new JSONObject();
        JSONArray unit = new JSONArray();

        Map<String, String > filedLabelMap = Maps.newHashMap();
        List<String> filedList = Lists.newArrayList();

        NetworkEntity ne = rpcProcessService.findNetworkEntityById(neId);
        if (ObjectUtils.isEmpty(ne)){
            return new JsonModel(true, "资源已被取消监控", empObj() );
        }

        JSONArray values = new JSONArray();
        //当前时间作为聚合的末尾时间，左右侧指标该事件需要为一致
        Date curDate = new Date();
        if (!Objects.equals((indicatorsLeft + componentNameLeft + fieldLeft),
                (indicatorsRight + componentNameRight + fieldRight))) {
            IndicatorTable leftInd = rpcProcessService.getIndicatorInfoByName(indicatorsLeft);
            String[] leftTemp = getLabel(leftInd, fieldLeft).split(":");
            String leftComponentNameString = null;
            for(Object obj: leftComponentNameKVs){
                JSONObject map = (JSONObject) obj;
                if(componentNameLeft.equals(map.get("value"))) {
                    leftComponentNameString = (String)map.get("name");
                    break;
                }
            }
            String label = leftTemp[0] + (leftComponentNameString==null? "": (":" + leftComponentNameString)) + ((leftTemp.length==1)? "": (":" + leftTemp[1]));
            columns.add(label);
            if ("healthy".equals(indicatorsLeft)){
                filedList.add(indicatorsLeft);
            }else {
                if (!MonitorUtils.validHasFields(leftInd)){
                    fieldLeft = leftInd.getName();
                }
                filedList.add(fieldLeft);
            }
            //利用代码来区分名字，一定是将indicator + componentName + fieldName组合在一起来区分左右指标，
            //因为可能会出现indicator相同另外两个不同、indicator和componentName都相同但fieldName不同这
            //两种极端情况，如果map只使用indicatorName来区分左右指标，则有可能出现区分不出来的情况
            filedLabelMap.put(indicatorsLeft + (componentNameLeft==null?"":componentNameLeft) + (fieldLeft==null?"":fieldLeft), label);
            leftValues = getHistoryValues(neId, indicatorsLeft, componentNameLeft, fieldLeft, intervalType, interval, period, new Date());
            if (ObjectUtils.isEmpty(leftValues)){
                unit.add("");
                values.addAll(new JSONArray());
            }else {
                unit.add(leftValues.getString("unit"));
                values.addAll(leftValues.getJSONArray("result"));
            }
        }
        IndicatorTable rightInd = rpcProcessService.getIndicatorInfoByName(indicatorsRight);
        String[] rightTemp = getLabel(rightInd, fieldRight).split(":");
        String rightComponentNameString = null;
        for(Object obj: rightComponentNameKVs){
            JSONObject map = (JSONObject) obj;
            if(componentNameRight.equals(map.get("value"))) {
                rightComponentNameString = (String)map.get("name");
                break;
            }
        }
        String label = rightTemp[0] + (rightComponentNameString==null? "": (":" + rightComponentNameString)) + ((rightTemp.length==1)? "": (":" + rightTemp[1]));
        columns.add(label);
        if ("healthy".equals(indicatorsRight)){
            filedList.add(indicatorsRight);
        }else {
            if (!MonitorUtils.validHasFields(rightInd)){
                fieldRight = "result";
            }
            filedList.add(fieldRight);
        }
        //利用代码来区分名字，一定是将indicator + componentName + fieldName组合在一起来区分左右指标，
        //因为可能会出现indicator相同另外两个不同、indicator和componentName都相同但fieldName不同这
        //两种极端情况，如果map只使用indicatorName来区分左右指标，则有可能出现区分不出来的情况
        filedLabelMap.put(indicatorsRight + (componentNameRight==null?"":componentNameRight) + (fieldRight==null?"":fieldRight), label);
        rightValues = getHistoryValues(neId, indicatorsRight, componentNameRight, fieldRight, intervalType, interval, period, new Date());
        if (ObjectUtils.isEmpty(rightValues)){
            unit.add("");
            values.addAll(new JSONArray());
        }else {
            unit.add(rightValues.getString("unit"));
            values.addAll(rightValues.getJSONArray("result"));
        }
        List<String> cacheTime = Lists.newArrayList();
        //日期升序处理
        values = sortByDate(values);
        for (int i = 0; i < values.size(); i++) {
            JSONObject obj = values.getJSONObject(i);
            JSONObject row = new JSONObject();
            String fetchDate = obj.getString("fetchDate");
            if (!cacheTime.contains(fetchDate)){
                JSONArray arr = values;
                JSONArray filter = MonitorUtils.filter(arr, o -> o.getString("fetchDate").equals(fetchDate));
                row.put("采集时间", fetchDate);
                /*
                    如果依照时间作为划分标准，则每一次循环filter的size一定是<=2，因为这是双轴曲线，也就是最多
                    在同一时间只能由两个目标。这里会出现两种极端情况：
                    1.某一次循环只有一个指标有值，另一个指标没有值，也就是filter的size=1的情况，这种情况下如·
                      果直接将row返回，则前端在该时间点上另一个没有的指标会出现undefined；
                    2.filter的size=2，但是其中某一个指标没有fieldName作为键的值（因为返回的数据结构中，这个
                      指标的值对应的键实际上就是这个指标的fieldName，但是有些情况下可能会出现filter中有这个
                      指标，但是这个指标却没有对应的fieldName作为键的值，因此会导致组合出来的key对应不上
                      filedLabelMap中的任何键，就会导致在这个row中会装有一个null->null，在该方法返回前不会
                      有任何问题，但是返回到前端时会抛出异常表明key不能为null
                    为了处理上面的两种极端情况，我们需要用一个count对当前row的指标计数，即表示当前行不为null的
                    指标数量，如果=2，代表该时间点两个指标都有值，则不作任何处理；如果==1，则代表要么filter中
                    本来就只有一个指标，要么代表其中一个指标monitor那边不给值，我们就需要在循环中将有值的那个
                    指标的key用变量existDataKey保存起来，在下面对map进行遍历时，如果第一个key=existDataKey，
                    则证明是第二个key对应的指标没有值，则继续循环到第二个key然后把0值加入到row；如果是第二个key
                    =existDataKey，则证明第一个key没有值，则直接把第一个加入row后break，因为第二个指标肯定有
                    值；如果count=0，则两个指标都没有值，就遍历一遍filedLabelMap，全部加入0值。
                */
                int count = 0;
                String existDataKey = null;
                for(Object o: filter){
                    count++;
                    JSONObject object = (JSONObject)o;
                    String key = object.getString("indicator_name");
                    String identifier = object.getString("identifier");
                    key += identifier==null? "": identifier;
                    if(object.getString(fieldLeft) != null){
                        key += fieldLeft;
                    }else if(object.getString(fieldRight) != null){
                        key += fieldRight;
                    }
                    String tryNullKey = filedLabelMap.get(key);
                    if(tryNullKey == null) {
                        count--;
                        continue;
                    }
                    row.put( tryNullKey, object.getString(MonitorUtils.getValueKey(object, filedList)));
                    existDataKey = key;
                }
                if(count == 1){
                    for(Map.Entry<String, String> entry: filedLabelMap.entrySet()){
                        if(!entry.getKey().equals(existDataKey)){
                            row.put(entry.getValue(), "0");
                            break;
                        }
                    }
                }else if(count == 0){
                    for(Map.Entry<String, String> entry: filedLabelMap.entrySet()){
                        row.put(entry.getValue(), "0");
                    }
                }
                cacheTime.add(fetchDate);
                rows.add(row);
            }
        }
        JSONObject result = new JSONObject();
        result.put("unit", unit);
        result.put("columns", columns);
        result.put("rows", rows);
        return new JsonModel().success(result);
    }

    private JSONArray sortByDate(JSONArray values) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values = values.stream().sorted((o1, o2) -> {
            JSONObject obj1 = JSONObject.parseObject(JSON.toJSONString(o1));
            JSONObject obj2 = JSONObject.parseObject(JSON.toJSONString(o2));
            String date1 = obj1.getString("fetchDate");
            String date2 = obj2.getString("fetchDate");
            try {
                Date dt1 = sdf.parse(date1);
                Date dt2 = sdf.parse(date2);
                //升序
                return Long.compare(dt1.getTime(), dt2.getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }).collect(Collectors.toCollection(JSONArray::new));
        return values;
    }

    private JSONObject getHistoryValues(String neId, String indName, String component, String field, IntervalType intervalType, Integer interval, IndPeriod period,
            Date curDate) throws Exception{
        // 健康度历史值特殊处理
        if ("healthy".equals(indName)){
            String[] neIds = new String[]{neId};
            NetworkEntity ne = rpcProcessService.findNetworkEntityById(neId);
            if (ObjectUtils.isEmpty(ne)){
                return new JSONObject();
            }
            JSONArray healthValues = getHistoryHealthValues(neIds, period, interval);
            action(healthValues, o -> {
                o.put("fetchDate", o.get("采集时间"));
                o.put("indicator_name", "healthy");
                o.put("healthy", o.get(ne.getIp() + ne.getName()));
            });
            JSONObject result = new JSONObject();
            result.put("result", healthValues);
            result.put("unit", "");
            return result;
        }

        NetworkEntity ne = rpcProcessService.findNetworkEntityByIdIn(neId);
        IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indName);
        if (ObjectUtils.isEmpty(ne) || ObjectUtils.isEmpty(ind)){
            return null;
        }
        IndicatorValueQO qo = new IndicatorValueQO();
        qo.setNeIds(Lists.newArrayList(ne.getId()));
        qo.setIndicatorNames(Lists.newArrayList(ind.getName()));
        Map<String,List<String>> fieldMap = Maps.newHashMap();
//        if (!MonitorUtils.validHasFields(ind)){
//            field = "result";
//        }
        fieldMap.put(ind.getName(), Lists.newArrayList(field));
        qo.setFields(fieldMap);
        Map<String,String> indicatorTypeMap = Maps.newHashMap();
        indicatorTypeMap.put( ind.getName() ,ind.getIndicatorType());
        qo.setIndicatorTypes(indicatorTypeMap);
        qo.setIntervalType(intervalType);
        qo.setInterval(Long.valueOf(interval));
        qo.setDateTo(curDate);
        if (!ObjectUtils.isEmpty(component)){
            qo.setIdentifiers(Lists.newArrayList(component));
        }
        JSONArray values = rpcProcessService.getIndAggValues(qo);
        if (ObjectUtils.isEmpty(values)){
            return null;
        }
        FieldModel model = new FieldModel();
        model.setIndicator(ind);
        model.setField(field);
        return MonitorUtils.unitTransfer(values, getUnit(model), field);
    }

    /**
     * 通过给定时间间隔处理指标历史数据
     * @param indicatorList 指标集合对象（neId、indicator、component、field）
     * @param period 统计时段
     * @param interval 时间间隔（单位为分钟）
     * @param isUnitTransfer 是否转换单位
     * @return
     */
    public JSONObject indicatorHistoryHandleByInterval(List<JSONObject> indicatorList, IndPeriod period,
                                                       Integer interval, Boolean isUnitTransfer) throws Exception {
        Date now = new Date();
        JSONArray columns = new JSONArray();
        JSONArray units = new JSONArray();
        JSONArray rows = new JSONArray();
        columns.add("采集时间");

        // 固定时间间隔
        Date startDate = IndPeriod.getStartDate(period, now);
        startDate.setTime(startDate.getTime() - startDate.getTime() % 60000L);
        Date startTag = new Date(startDate.getTime());
        List<Date> dateList = Lists.newArrayList();
        while (startDate.before(now)) {
            Date itm = new Date(startDate.getTime());
            dateList.add(itm);
            startDate.setTime(startDate.getTime() + 1000 * 60 * interval);
        }

        // 该map用于缓存指标历史数据，避免过多的查询
        Map<String, List<IndicatorVal>> indicatorValueCache = new HashMap<>(indicatorList.size());
        // 该map用于缓存column和unit，避免过多的查询（需使用有序map）
        Map<String, JSONObject> columnUnitCache = new LinkedHashMap<>(indicatorList.size());
        dateList.forEach(intervalDate -> {
            JSONObject row = new JSONObject();
            indicatorList.forEach(indicatorObj -> {
                String neId = indicatorObj.getString("neId");
                String indicator = indicatorObj.getString("indicator");
                String component = indicatorObj.getString("component");
                String field = indicatorObj.getString("field");
                String cacheKey = neId + indicator + component + field;
                // 获取指标历史数据
                List<IndicatorVal> indicatorValueList = null;
                if (indicatorValueCache.containsKey(cacheKey)) {
                    indicatorValueList = indicatorValueCache.get(cacheKey);
                } else {
                    IndicatorValueCriteria criteria = new IndicatorValueCriteria();
                    JSONObject sort = new JSONObject();
                    JSONObject must = new JSONObject();
                    JSONObject scope = new JSONObject();
                    criteria.setPagination(false);
                    sort.put("tm", "asc");
                    criteria.setSort(sort);
                    scope.put("gte", startTag);
                    scope.put("lte", now);
                    must.put("tm", scope);
                    must.put("ne", neId);
                    List<String> indNameArr = Lists.newArrayList(Strings.nullToEmpty(indicator).split(","));
                    criteria.setIndicatorName(indNameArr);
                    criteria.setMust(must);
                    indicatorService.searchHistoryRecords(criteria);
                    Object IndValueObj = criteria.getObject();
                    JSONArray valueArray = IndValueObj == null ? new JSONArray()
                            : JSON.parseArray(JSON.toJSONString(IndValueObj));
                    indicatorValueList = valueArray == null ? null
                            : JSONObject.parseArray(valueArray.toString(), IndicatorVal.class);
                    indicatorValueCache.put(cacheKey, indicatorValueList);
                }
                // 获取column和unit
                String column = null;
                String unit = null;
                String transferUnit = null;
                if (columnUnitCache.containsKey(cacheKey)) {
                    column = columnUnitCache.get(cacheKey).getString("column");
                    unit = columnUnitCache.get(cacheKey).getString("unit");
                    transferUnit = columnUnitCache.get(cacheKey).getString("transferUnit");
                } else {
                    IndicatorTable indicatorTable = new IndicatorTable();
                    JSONArray fieldLabel = new JSONArray();
                    try {
                        indicatorTable = rpcProcessService.getIndicatorInfoByName(indicator);
                        fieldLabel = rpcProcessService.getFieldLables(indicator);
                    }catch (Exception e){

                    }
                    if (ObjectUtils.isEmpty(fieldLabel)) {
                        column = indicatorTable.getLabel();
                        if (Objects.equals("PERCENT", indicatorTable.getIndicatorType())) {
                            unit = "%";
                            transferUnit = unit;
                        }
                    } else {
                        for (Object obj : fieldLabel) {
                            JSONObject itm = (JSONObject) obj;
                            if (Objects.equals(field, itm.getString("name"))) {
                                column = indicatorTable.getLabel() + ":" + itm.getString("label");
                                unit = itm.getString("unit");
                                // 取该指标所有被统计的数据，进行最大值筛选，按照最大值的最合适的单位对所有数据进行该单位的处理
                                Optional<Double> optional = indicatorValueList.stream().map(map -> {
                                    Object valueObj = map.getIndicatorValue();
                                    // valueObj = viewService.changeToJson(valueObj, indicator);
                                    JSON indicatorValue = (JSON) valueObj;
                                    return getIndicatorValue(indicatorValue, component, field);
                                }).filter(condition -> !ObjectUtils.isEmpty(condition)).reduce(Double::max);
                                if (optional.isPresent() && isUnitTransfer) {
                                    transferUnit = indicatorUnitTransfer(optional.get(), unit, null)
                                            .getString("transferUnit");
                                } else {
                                    transferUnit = unit;
                                }
                            }
                        }
                    }
                    JSONObject obj = new JSONObject();
                    obj.put("column", column);
                    obj.put("unit", unit);
                    obj.put("transferUnit", transferUnit);
                    columnUnitCache.put(cacheKey, obj);
                }

                /* 健康度指标特殊处理：如果时间间隔内没有健康度（没有发生告警），使用上一次的健康度填充。
                   非健康度指标处理：如果时间间隔内没有数据（采集器服务停止），不使用健康度指标的方式填充（使用上一次数据填充），用null填充。
                   所有指标公共处理：如果时间间隔内有多个数据（主要针对统计时段比较大的情况），使用间隔区间内的平均值（保留两位小数）。 */
                if (Objects.equals("healthy", indicator) && intervalDate.compareTo(startTag) == 0) {
                    // 统计时段起始时间的健康度特殊处理，取统计时段起始时间之前的的所有健康度中距离起始时间最近的健康度
                    Optional<IndicatorVal> firstOptional = indicatorValueList.stream()
                            .filter(itm -> itm.getTm().getTime() <= startTag.getTime())
                            .max(Comparator.comparing(IndicatorVal::getTm));
                    if (firstOptional.isPresent()) {
                        Object valueObj = firstOptional.get().getIndicatorValue();
                        // valueObj = viewService.changeToJson(valueObj, indicator);
                        JSON indicatorValue = (JSON) valueObj;
                        row.put(column, getIndicatorValue(indicatorValue, component, field));
                    } else {
                        row.put(column, null);
                    }
                    return;
                }
                OptionalDouble averageOptional = indicatorValueList.stream()
                        // 指标采集时间与间隔区间判断条件为左开右闭，不可使用after、before
                        .filter(itm -> itm.getTm().getTime() <= intervalDate.getTime() && itm.getTm()
                                .getTime() > (new Date(intervalDate.getTime() - 1000 * 60 * interval)).getTime())
                        // 该filter的情况较少，且条件中存在遍历，故没有放置在上个filter，减少无用的迭代，加快查询速度
                        .filter(itm -> {
                            Object valueObj = itm.getIndicatorValue();
                            // valueObj = viewService.changeToJson(valueObj, indicator);
                            JSON indicatorValue = (JSON) valueObj;
                            return !ObjectUtils.isEmpty(getIndicatorValue(indicatorValue, component, field));
                        }).mapToDouble(itm -> {
                            Object valueObj = itm.getIndicatorValue();
                            // valueObj = viewService.changeToJson(valueObj, indicator);
                            JSON indicatorValue = (JSON) valueObj;
                            return getIndicatorValue(indicatorValue, component, field);
                        }).average();
                if (averageOptional.isPresent()) {
                    Double value = averageOptional.getAsDouble();
                    value = isUnitTransfer ? indicatorUnitTransfer(value, unit, transferUnit).getDouble("value")
                            : value;
                    value = new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    row.put(column, value);
                } else if (Objects.equals("healthy", indicator)) {
                    Object lastHealth = rows.getJSONObject(rows.size() - 1).get(column);
                    row.put(column, lastHealth);
                } else {
                    row.put(column, null);
                }
            });
            row.put("采集时间", DateUtils.formatCommonDate(intervalDate));
            rows.add(row);
        });

        columnUnitCache.forEach((key, value) -> {
            columns.add(value.getString("column"));
            units.add(value.getString("transferUnit"));
        });
        JSONObject result = new JSONObject();
        result.put("columns", columns);
        result.put("rows", rows);
        result.put("unit", units);
        return result;
    }

    /**
     * 获取指标中给定部件和属性的值
     * @param indicatorValue 指标中的所有值
     * @param component 部件
     * @param field 属性
     * @return
     */
    public Double getIndicatorValue(JSON indicatorValue, String component, String field) {
        Double result = null;
        if (indicatorValue instanceof JSONArray) {
            JSONArray indicatorValues = (JSONArray) indicatorValue;
            for (Object obj : indicatorValues) {
                JSONObject itm = (JSONObject) obj;
                if (Objects.equals(component, itm.getString("identifier"))) {
                    result = itm.getDouble(field);
                }
            }
        }
        if (indicatorValue instanceof JSONObject) {
            JSONObject indicatorValues = (JSONObject) indicatorValue;
            if (StringUtils.isBlank(field)) {
                result = indicatorValues.getDouble("result");
            } else {
                result = indicatorValues.getDouble(field);
            }
        }
        return result;
    }

    /**
     * 大屏曲线图单位转换
     * @param value 值
     * @param unit 原单位
     * @param transferUnit 需要转换的单位
     * @return
     */
    public JSONObject indicatorUnitTransfer(Double value, String unit, String transferUnit) {
        JSONObject result = new JSONObject();
        result.put("unit", unit);
        if (ObjectUtils.isEmpty(unit) || ObjectUtils.isEmpty(value)) {
            result.put("value", value);
            result.put("transferUnit", transferUnit);
            return result;
        }
        if ("byte".equals(unit) || "B".equals(unit) || "KB".equals(unit) || "MB".equals(unit) || "GB".equals(unit)
                || "8KB".equals(unit) || "16MB".equals(unit) || "2KB".equals(unit) || "TB".equals(unit)) {
            if ("8KB".equals(unit)) {
                value *= 8;
                unit = "KB";
            }
            if ("16MB".equals(unit)) {
                value *= 16;
                unit = "MB";
            }
            if ("2KB".equals(unit)) {
                value *= 2;
                unit = "KB";
            }
            List<String> units = Arrays.asList("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB");
            if ("byte".equals(unit)) {
                units.set(0, "byte");
            }
            int index = units.indexOf(unit);
            if (StringUtils.isBlank(transferUnit)) {
                double transferIndex = value == 0 ? 0 : Math.floor(Math.log(value) / Math.log(1024));
                transferUnit = units.get(index + (int) transferIndex);
            } else {
                int transferIndex = units.indexOf(transferUnit);
                value = value / Math.pow(1024, transferIndex - index);
            }
        } else if ("second".equals(unit) || "ms".equals(unit) || "microsecond".equals(unit) || "ns".equals(unit)
                || "sec".equals(unit) || "day".equals(unit) || "cs".equals(unit)) {
            if ("day".equals(unit)) {
                unit = "天";
            }
            if ("second".equals(unit) || "sec".equals(unit)) {
                unit = "秒";
            }
            if ("cs".equals(unit)) {
                unit = "厘秒";
            }
            if ("ms".equals(unit)) {
                unit = "毫秒";
            }
            if ("microsecond".equals(unit)) {
                unit = "微秒";
            }
            if ("ns".equals(unit)) {
                unit = "纳秒";
            }
            List<String> units = Arrays.asList("天", "时", "分", "秒", "厘秒", "毫秒", "微秒", "纳秒");
            int[] gap = { 24, 60, 60, 100, 10, 1000, 1000 };
            int index = units.indexOf(unit);
            if (StringUtils.isBlank(transferUnit)) {
                transferUnit = units.get(index);
                value = value / gap[index - 1];
                while (Math.floor(value) >= 1 && index > 0) {
                    value = value / gap[index - 1];
                    index--;
                    transferUnit = units.get(index);
                }
            } else {
                int transferIndex = units.indexOf(transferUnit);
                while (transferIndex < index) {
                    value = value / gap[index - 1];
                    index--;
                }
            }
        } else {
            transferUnit = unit;
        }
        result.put("value", value);
        result.put("transferUnit", transferUnit);
        return result;
    }

    /**
     * 同资源多部件统计
     * @param neIds 资源ID
     * @param indicators 指标名称
     * @param componentName 部件名称
     * @param field 属性
     * @return
     */
    public JsonModel multipleComponent(String neIds, String indicators, String[] componentName, String field) throws Exception{
        IndicatorTable ind = null;
        try {
            ind = rpcProcessService.getIndicatorInfoByName(indicators);
        }catch (Exception e){
            return new JsonModel(true, e.getMessage(), empObj());
        }
        // 资源ID和指标名为必选项
        if (StringUtils.isEmpty(neIds) || StringUtils.isEmpty(indicators)) {
            return new JsonModel(true, empObj());
        }
        NetworkEntity ne = rpcProcessService.findNetworkEntityById(neIds);
        // 判断资源是否存在或者是否已被销毁或者未监控
        if (ObjectUtils.isEmpty(ne) || ne.getManageStatus().equals(ManageStatus.Delected) || !ne.isMonitoring()) {
            return new JsonModel(true, empObj());
        }
        // IPMI数据进行指标单位特殊处理
        List<String> ipmiIndicator = Lists.newArrayList("ipmi_temp", "ipmi_current", "ipmi_fan", "ipmi_voltage");
        // 返回的结果
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        // 属性字段的JSON
        JSONObject fieldLabel = null;
        // 属性值的JSON
        // JSONObject valueJSON = new JSONObject();
        if (validHasFields(ind)) {
            JSONArray fields = ind.getFields();
            if (CollectionUtils.isEmpty(fields)) {
                return new JsonModel(true, empObj());
            }
            // 查找属性的label
            for (int i = 0; i < fields.size(); i++) {
                JSONObject fieldJson = fields.getJSONObject(i);
                if (field.equalsIgnoreCase(fieldJson.getString("name"))) {
                    fieldLabel = fieldJson;
                    break;
                }
            }
            if (Objects.isNull(fieldLabel)) {
                return new JsonModel(true, empObj());
            }
        }
        // 获取指标的值
        com.uxsino.commons.db.criteria.IndicatorValueCriteria qo = new com.uxsino.commons.db.criteria.IndicatorValueCriteria();
        qo.setNeId(neIds);
        qo.setIndicatorName(Lists.newArrayList(indicators));
        IndValue indValue = rpcProcessService.getIndValue(qo);
        Boolean strategyField = getStrategy(neIds, indicators, field);
        // 若指标未监控，取消其数值展示
        if (!strategyField) {
            indValue = new IndValue();
        }
        if (Objects.isNull(indValue) || ObjectUtils.isEmpty((JSON) indValue.getIndicatorValue())) {
            return new JsonModel(true, empObj());
        }
        // indicatorValues的类型可能是JSONArray,也可能是JSONObject
        JSON indicatorValues = (JSON) indValue.getIndicatorValue();
        // 根据指标ID取对应参数
        JSONArray fieldArray = JSON.parseArray(indicatorValues.toJSONString());
        Map<String, String> componentNameMap = Maps.newHashMap();
        NeComponentQuery compQuery = new NeComponentQuery();
        compQuery.setNeIds(Lists.newArrayList(neIds));
        compQuery.setIndicatorName(indicators);
        List<Map<String, Object>> idAndComponent = rpcProcessService.findNeComps(compQuery);
        for (Map<String, Object> map : idAndComponent) {
            if (map.get("identifier") == null || map.get("componentName") == null) {
                continue;
            }
            componentNameMap.put(map.get("identifier").toString(), map.get("componentName").toString());
        }
        String unit = fieldLabel.getString("unit");
        JSONObject row = new JSONObject();
        columns.add("资源名");
        row.put("资源名",
                ne.getName() + " : " + fieldLabel.getString("label") + (ObjectUtils.isEmpty(unit) ? "" : "(" + unit + ")"));
        for (String component : componentName) {
            for (int i = 0; i < fieldArray.size(); i++) {
                JSONObject fieldObj = fieldArray.getJSONObject(i);
                if (fieldObj.get("identifier").toString().equals(component)) {
                    String value = fieldObj.getString(field);
                    String name = new String();
                    Object value1 = new Object();
                    if (Strings.isNullOrEmpty(value)) {
                        name = componentNameMap.get(component);
                        value1 = "";
                    } else {
                        Matcher m = CHINESE_PATTERN.matcher(value);
                        while (m.find()) {
                            value = m.group(1);
                            break;
                        }
                        if (Strings.isNullOrEmpty(unit)) {
                            name = componentNameMap.get(component);
                            value1 = String.format("%.2f", Double.parseDouble(value));
                        } else {
                            int index = value.lastIndexOf(" ");
                            name = componentNameMap.get(component);
                            value1 = index > 0 ? String.format("%.2f", Double.parseDouble(value.substring(0, index)))
                                    : String.format("%.2f", Double.parseDouble(value));
                            unit = index > 0 ? value.substring(index + 1) : unit;
                            if (ipmiIndicator.contains(indicators)) {
                                unit = fieldObj.getString("unit");
                            }
                        }
                    }
                    if (Strings.isNullOrEmpty(unit)) {
                        row.put(name, value1);
                        columns.add(name);
                    } else {
                        row.put(name + "(" + unit + ")", value1);
                        columns.add(name + "(" + unit + ")");
                    }
                    break;
                }
            }
        }
        rows.add(row);
        if (row.size() == 1) {
            rows = new JSONArray();
            columns = new JSONArray();
        }
        result.put("rows", rows);
        result.put("columns", columns);
        result.put("unit", unit);
        return new JsonModel(true, result);
    }

    /**
     * 由接口 链路统计-按城市 调用获取数据
     *
     * @param locationCode 前端需要展示的地图区域的区域代码
     * @return
     * @throws Exception 进行服务调用时产生的异常
     */
    public JsonModel getNELinkByCity(String locationCode, String displayMode) throws Exception{
        //选择区域为不限时，默认返回全国地图的数据
        if(locationCode == null)
            locationCode = "100000";
        /*
            下面开始向monitor获取地图拓扑参数，分三步：
                1.直接调取/mapTopoDomain获取topoId，作为第二步的参数
                2.调取/getMapLocationTree，传入参数，获得当前帐号在IT基础监控下创建的所有拓扑地图
                3.将当前想要展示的地图的区域代码和第二步中获得的地图信息中的区域代码匹配，获取到对应的
                  mapLocationId，用于第四步传参
                4.再次调用/mapTopoDomain，传入参数，获得将要显示的地图对应在IT基础监控那边的结点和
                  链路信息
         */
        String topoId = rpcProcessService.getTopoId();
        ArrayList<LinkedHashMap> mapTree = rpcProcessService.getMapLocationTree(topoId);
        String mapLocationId= null;
        for(LinkedHashMap map: mapTree){
            if(locationCode.equals(map.get("locationCode"))){
                mapLocationId = (String)map.get("id");
                break;
            }
        }
        if(mapLocationId == null)
            return new JsonModel(true, "地图拓扑中没有这张图的数据！");
        LinkedHashMap<String, ArrayList> nodesAndLinks = rpcProcessService.getMapNodesAndLinkes(null, mapLocationId);
        ArrayList nodes = nodesAndLinks.get("nodes");
        ArrayList links = nodesAndLinks.get("links");

        //获取想要显示区域的所有子区域的经纬度以及对应的地名
        HashMap<String, ArrayList> coordinates = this.getCoordinateForArea(locationCode);
        if(coordinates == null)
            return new JsonModel(true, "该区域暂无详细地图！");

        /*
            下面开始将IT基础监控地图拓扑的数据和经纬度数据经过封装变成地图迁徙图可以识别的数据格式：
                1.首先分配几个链路对象的内存空间，这里的每个内存空间序号对应于links数组中几个链路信息的
                  序号，方便在二层循环中遍历links数组匹配到第i个link的起点ip或者终点ip与一层循环中当前
                  nodes中的结点ip时立刻将该link相关信息存到相对应的第i个内存空间。第一个for循环用于声
                  明这几个内存空间。
                2.origins和terminations分别封装地图拓扑上的所有点，其中origins会将地图拓扑上的所有点
                  全部装入，而terminations只会装入link中的所有终点信息对应的结点，一个结点可以同时在
                  origins和terminations中。
                3.第一层循环：将nodes遍历一遍，目的有两个。一是将所有结点全部加入到起点数组中，通过获取
                  其最底层的区域编码来获得这个子区域对应的经纬度，然后放到MigrationStation中；二是将
                  有用的信息（包括当前子区域的经纬度和所绑定在这个子区域的设备的ip）留在当前作用域，方便
                  第二层循环使用。
                4.第二层循环：将就当前这个结点信息，立刻遍历一次links，看有没有哪一条链路的起点或者终点
                  是当前结点，判断方法就是看他们俩所绑定的设备的ip是否一致，如果一致则把经纬度也传给链路
                  上的这个结点，同时如果这个结点是起点，还需要将这条链路的上下流量等属于链路本身的数据一
                  并绑定到起点上，终点则只指定经纬度即可。
            由于设有for循环，也就不用判断nodes和links的size是否为0了，为0自然不会进入循环，从而返回时也
            只会返回几个空的json。
         */
        ArrayList<MigrationStation> origins = new ArrayList<>();
        ArrayList<MigrationStation> terminations = new ArrayList<>();
        ArrayList<ArrayList<MigrationLink>> paths = new ArrayList<>();
        double maxValue = 1.0d;
        double minValue = 1.0d;
        int linkCount = links.size();
        for(int i=0; i<linkCount; i++){
            ArrayList<MigrationLink> path = new ArrayList<>();
            path.add(new MigrationLink());
            path.add(new MigrationLink());
            paths.add(path);
        }
        for(Object object: nodes){
            LinkedHashMap node = (LinkedHashMap)object;
            MigrationStation origin = new MigrationStation();
            String ip = (String)node.get("ip");
            //monitor那边返回的结点的locationCode是从省份开始往下依次填写的区域代码并用','分割，
            //但是实际上只用得到最后一个，因此要去取最后一个
            String[] allCodes = ((String)node.get("locationCode")).split(",");
            String subLocationCode = allCodes[allCodes.length-1];
            ArrayList values = coordinates.get(subLocationCode);
            /*
                这里的coord按顺序存放三个值：
                    coord.get(0): 结点的经度
                    coord.get(1): 结点的纬度
                    coord.get(2): 结点的value，在前端地图上将会影响该点的颜色样式
             */
            ArrayList<Float> coord = new ArrayList<>();
            if (!"name".equals(displayMode)) {
                coord.add((float)values.get(0));
                coord.add((float)values.get(1));
            }
            coord.add(1.0f);
            origin.setValue(coord);
            origin.setName((String)values.get(2));
            origins.add(origin);
            for(int i=0; i<linkCount; i++){
                LinkedHashMap link = (LinkedHashMap)links.get(i);
                MigrationLink temp;
                MigrationStation termination;
                ArrayList<String> obj;
                //这里虽然也是存的经纬度，但是这里只能有经纬度两个值，上面的经纬度值还有第三个值用于
                //前端渲染，必须但没有实际意义。不想要第三个变量又需要上面的经纬度，所以就只能新声明
                //一段内存空间来单独储存上面的经纬度。
                ArrayList<Float> copyCoord;
                if(ip.equals(link.get("sourceIp"))){
                    temp = paths.get(i).get(0);
                    copyCoord = new ArrayList<>();
                    //不会出现数组超界，因为这里取0、1的条件和那里加入0、1的条件相同，那边没加则这边也不会取
                    if (!"name".equals(displayMode)) {
                        copyCoord.add(coord.get(0));
                        copyCoord.add(coord.get(1));
                    }
                    temp.setCoord(copyCoord);
                    temp.setName((String)values.get(2));
                    temp.setValue((double)link.get("downBps"));
                    obj = new ArrayList<>();
                    double upBps = (double)link.get("upBps");
                    if(maxValue<upBps)
                        maxValue = upBps;
                    if(minValue>upBps)
                        minValue = upBps;
                    double downBps = (double)link.get("downBps");
                    if(maxValue<downBps)
                        maxValue = downBps;
                    if(minValue>downBps)
                        minValue = downBps;
                    obj.add("上行流量：" + upBps + "Bps");
                    obj.add("下行流量：" + downBps + "Bps");
                    temp.setObj(obj);
                }else if(ip.equals(link.get("targetIp"))){
                    temp = paths.get(i).get(1);
                    copyCoord = new ArrayList<>();
                    if (!"name".equals(displayMode)) {
                        copyCoord.add(coord.get(0));
                        copyCoord.add(coord.get(1));
                    }
                    temp.setCoord(copyCoord);
                    temp.setName((String)values.get(2));
                    //如果这个点也是终点，则我们也应顺便把他加入到终点数组中
                    termination = new MigrationStation();
                    termination.setName((String)values.get(2));
                    //上面进行了判断，如果是name模式则coord数组size=0，因此直接装入即可，不用再判断是否需要new ArrayList
                    termination.setValue(coord);
                    terminations.add(termination);
                }
            }
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("stationData", origins);
        result.put("lineData", paths);
        result.put("Statistical", terminations);
        result.put("maxValue", maxValue);
        result.put("minValue", minValue);

        return new JsonModel(true, result);
    }

    /**
     * 该方法用于获取一个指定区域地图上所有子区域中心位置的经纬度
     *
     * @param mapLocationCode 要显示在前端的区域地图的区域编码
     * @return 由该显示区域的各个子区域的区域编码为key，value是一个ArrayList，按以下顺序返回：
     *         arrayList.get(0): 该区域中心的经度-->float
     *         arraylist.get(1): 该区域中心的纬度-->float
     *         arrayList.get(2): 该区域的中文名称-->String
     *         返回null表示该区域不能支持划分为子区域
     */
    public HashMap<String, ArrayList> getCoordinateForArea(String mapLocationCode){
        //用于判断是不是显示的全国地图，因为全国地图的经纬度的key和其他地图的key不一样
        boolean isCountryMap = mapLocationCode.equals("100000");

        String jsonFilePath = "static/mapJson/";
        /*
            下面一部分代码完成geoJson文件相对路径的构造。
            第2、3为同时为0表示显示的地图是全国地图、一个省级地图或者一个直辖市地图，这些地图的geoJson文件不需要
            经过中间省级代码文件夹层，直接在mapJson文件夹下访问即可
            否则需要通过mapLocationcode获得其省级代码，并加到路径中
         */
        if(mapLocationCode.charAt(2)=='0' && mapLocationCode.charAt(3)=='0'){
            jsonFilePath += mapLocationCode;
        }else{
            String provinceCode = mapLocationCode.substring(0, 1) + "0000";
            jsonFilePath += provinceCode + "/" + mapLocationCode;
        }
        jsonFilePath += ".geoJson";

        JSONReader reader = new JSONReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(jsonFilePath)));
        JSONObject object = JSONObject.parseObject(reader.readString());
        JSONArray features = (JSONArray)object.get("features");
        /*
            四个直辖市geoJson文件与省级同层，所以直辖市的文件直接记载的是区县经纬度，所以其对应的省级文件夹层
            下的文件是空的，虽然会直接输入四个直辖市的省级代码不会访问到那四个空文件夹，但是还是判断一下。
            返回null就需要在上层直接返回提示信息不支持该区域划分。
         */
        if(features.size()==0)
            return null;
        /*
            下一部分代码开始遍历整个geoJson文件，把该区域下的所有子区域的经纬度连同其区域代码形成一个
            HashMap并返回
         */
        HashMap<String, ArrayList> coordinates = new HashMap<>();
        for(Object obj: features){
            JSONObject jsonObject = (JSONObject) obj;
            ArrayList coordinate = new ArrayList();
            JSONArray coord;
            String locationCode;
            if(isCountryMap)
                coord = (JSONArray)((JSONObject)jsonObject.get("properties")).get("cp");
            else
                coord = (JSONArray)((JSONObject)jsonObject.get("properties")).get("center");
            locationCode = String.valueOf((int)((JSONObject)jsonObject.get("properties")).get("adcode"));
            coordinate.add(((BigDecimal)coord.get(0)).floatValue());
            coordinate.add(((BigDecimal)coord.get(1)).floatValue());
            coordinate.add((String)((JSONObject)jsonObject.get("properties")).get("name"));
            coordinates.put(locationCode, coordinate);
        }

        return coordinates;
    }
}
