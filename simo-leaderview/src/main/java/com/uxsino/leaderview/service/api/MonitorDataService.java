package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uxsino.authority.lib.util.DomainUtils;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.model.RunStatus;
import com.uxsino.commons.utils.DateUtils;
import com.uxsino.commons.utils.SessionUtils;
import com.uxsino.commons.utils.TimeUtils;
import com.uxsino.leaderview.model.FieldModel;
import com.uxsino.leaderview.model.datacenter.IndicatorValueCriteria;
import com.uxsino.leaderview.model.monitor.*;
import com.uxsino.leaderview.utils.IndicatorValueUtils;

import com.uxsino.leaderview.utils.MonitorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        list.forEach(v -> map.put(RunStatus.valueOf(v.get(0).toString()).getName(), v.get(1)));

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            rows.add(newResultObj("状态", entry.getKey(), "数量", entry.getValue()));
        }
        json.put("rows", rows);
        return new JsonModel(true, null, json);
    }

    /**
     * 资源状态列表
     * @param domainId 域ID
     * @param neIds 资源IDs
     * @param baseNeClass 资源父类型
     * @param session
     * @return
     */
    public JsonModel neList(Long domainId, String neIds, BaseNeClass baseNeClass, HttpSession session) throws Exception{
        JSONArray columns = newColumns("资源名称","IP地址","资源类型","运行状态","更新时间");
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
        List<Long> domainList = getDomainList(domainId, session);
        List<Map<String, Object>> list = rpcProcessService.findByIdsAndBaseNe(domainList, neIds, baseNeClass);
        if (CollectionUtils.isEmpty(list)) {
            return new JsonModel(true, newResultObj("columns",columns,"rows",new JSONArray()));
        }
        json.put("columns", columns);
        JSONArray rows = new JSONArray();
        for (Map<String, Object> ne : list) {
            if (!(Boolean) (ne.get("monitoring"))) {
                continue;
            }
            JSONObject row = new JSONObject(true);
            row.put("资源名称", ne.get("name"));
            row.put("IP地址", ne.get("ip"));
            row.put("资源类型", NeClass.valueOf(ne.get("neClass").toString()).getText());
            row.put("运行状态", RunStatus.valueOf(ne.get("runStatus").toString()).getName());
            row.put("更新时间", DateUtils.formatCommonDate(new Date((Long) ne.get("patrolTime"))));
            rows.add(row);
        }
        json.put("rows", rows);
        return new JsonModel(true, json);
    }

    public JsonModel findIndicatorsStatus(String neId) throws Exception {
        List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(new String[]{neId});
        NetworkEntity ne = new NetworkEntity();
        if (!nes.isEmpty()){
            ne = nes.get(0);
        }
        List<String> indicators = null;
        if (ne.getIndicators() != null) {
            indicators = JSON.parseArray(ne.getIndicators().toString()).toJavaList(String.class);
        }
        List<JSONObject> list = indicatorService.findIndicatorStatus(neId);
        JSONObject json = new JSONObject();
        json.put("columns", newColumns("指标名称","运行状态","更新时间"));
        JSONArray rows = new JSONArray();
        for (JSONObject indicator : list) {
            if (indicator.getString("neids") != null && !Arrays.asList(indicator.getString("neids").split(",")).contains(neId)) {
                continue;
            }
            if (indicators == null || indicators.contains(indicator.getString("name"))) {
                JSONObject row = newResultObj("指标名称", indicator.getString("label"),
                        "运行状态", indicator.getString("run_status"));
                try {
                    row.put("fetch_date", TimeUtils.formatTime(sdf.parse(indicator.getString("fetch_date"))));
                } catch (ParseException e) {
                    row.put("fetch_date", indicator.getString("fetch_date"));
                }
                rows.add(row);
            }
        }
        json.put("rows", rows);
        return new JsonModel(true, json);
    }

    /**
     * 获取指标属性的单值
     * @param neIds 资源ID
     * @param indicators 指标名称
     * @param componentName 部件名称
     * @param field 属性
     * @return
     */
    public JsonModel getIndicatorValue(String neIds, String indicators, String componentName, String field) throws Exception {
        IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicators);
        // 单值元件中，错误数据也需要展示正确图例
        JSONObject empObj = newResultObj("name", Objects.isNull(ind) ? "" : ind.getLabel(), "unit","");
        // 资源ID和指标名为必选项
        if (StringUtils.isEmpty(neIds) || StringUtils.isEmpty(indicators)) {
            return new JsonModel(true, empObj);
        }
        NetworkEntity ne = rpcProcessService.findNetworkEntityByIdIn(neIds);
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
        IndicatorVal indValue = null;
        // 若指标被监控
        if (strategyField) {
            // 获取指标的值
            indValue = indicatorService.findValueByNeIdAndIndicator1(neIds, indicators, null);
        }
        if (Objects.isNull(indValue) || ObjectUtils.isEmpty((JSON) indValue.getIndicatorValue())) {
            return new JsonModel(true, empObj);
        }

        // 属性值的JSON
        JSONObject valueJSON = getValueJSON((JSON) indValue.getIndicatorValue());

        // 如果是类型为NUMBER、PERCENT的指标，直接取值返回。就不用做之后的部件判断了
        if (!validHasFields(ind)) {
            String value = valueJSON.getString("result");
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
            List<Map<String, Object>> idAndComponent = rpcProcessService.findNeComps(Lists.newArrayList(neIds),
                    indicators, null, null, null);
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

        IndicatorVal indValue = null;
        // 若指标被监控
        if (strategyField) {
            // 获取指标的值
            indValue = indicatorService.findValueByNeIdAndIndicator1(neIds, indicators, null);
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
            valueJSON = getValueJSON(indicatorValues);
            String value = valueJSON.getString("result");
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
        JSONObject empObj = new JSONObject();
        empObj.put("info", "数据获取发生错误");
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
        IndicatorVal indValue = null;
        // 若指标被监控
        if (strategyField) {
            // 获取指标的值
            indValue = indicatorService.findValueByNeIdAndIndicator1(neIds, indicators, null);
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
            String value = valueJSON.getString("result");
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
            List<Map<String, Object>> idAndComponent = rpcProcessService.findNeComps(Lists.newArrayList(neIds),
                    indicators, null, null, null);
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
        IndicatorVal indValue = null;
        // 若指标被监控
        if (strategyField) {
            // 获取指标的值
            indValue = indicatorService.findValueByNeIdAndIndicator1(neIds, ind.getName(), null);
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


    private JSONObject getValueJSON(JSON indicatorValues) {
        JSONObject valueJSON = new JSONObject();
        // indicatorValues的类型可能是JSONArray,也可能是JSONObject
        if (indicatorValues instanceof JSONArray) {
            valueJSON = ((JSONArray) indicatorValues).getJSONObject(0);
        } else if (indicatorValues instanceof JSONObject) {
            valueJSON = (JSONObject) indicatorValues;
        }
        return valueJSON;
    }

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
        return strategyField;
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

    public JSONArray newColumns(String... columns){
        JSONArray jsonArray = new JSONArray();
        for (String column : columns) {
            jsonArray.add(column);
        }
        return jsonArray;
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

        JSONArray rows = new JSONArray();
        for (Date date: allDate) {
            JSONObject row = new JSONObject();
            List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(neIds);
            nes.forEach(ne -> {
                try {
                    List<NeHealthHistory> healthList = rpcProcessService.findHealthByNeIdIn(Lists.newArrayList(ne.getId()));
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
                        return;
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
                }catch (Exception e){
                    return;
                }
            });

            row.put("采集时间", DateUtils.formatCommonDate(date));
            rows.add(row);
        }

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

    public JsonModel getHistoryValue(String[] neIds, String indicators, String windows, String field, IndPeriod period,
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
        for (int i = 0; i < componentArray.size(); i++) {
            componentNames[i] = componentArray.getJSONObject(i).getString("component");
            neIds[i] = componentArray.getJSONObject(i).getString("id");
            componentMap.put(neIds[i], componentNames[i]);
        }


        // 如果资源选择为空，无数据展示
        if (ObjectUtils.isEmpty(neIds)) {
            return new JsonModel(true, "未选择资源", empObj());
        }
        // 排除无效、被删除、未监控资源
        List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(neIds);
        nes = nes.stream().filter(ne -> !ne.getManageStatus().equals(ManageStatus.Delected) && ne.isMonitoring()).collect(Collectors.toList());
        IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicators);
        if (Objects.isNull(ind)) {
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
        nes = nes.stream().filter(ne -> {
            columns.add(ne.getIp() + ne.getName());
            Boolean strategyField = getStrategy(ne.getId(), indicators, field);
            if (strategyField) {
                neMap.put(ne.getId(), ne);
            }
            // 只要被监控的指标
            return strategyField;
        }).collect(Collectors.toList());


        FieldModel model = new FieldModel();

        List<IndicatorVal> records = Lists.newArrayList();
        for (NetworkEntity ne: nes) {
            JSONObject neComponent = filter(componentArray, o -> o.getString("id").equals(ne.getId())).getJSONObject(0);
            model.setNe(ne);
            model.setIndicator(ind);
            model.setField(field);
            model.setComponent(neComponent.getString("component"));

            JSONArray neIndVal = getHistoryValByNe(model, period, "asc");
            if (neIndVal != null){
                records.addAll(JSONObject.parseArray(neIndVal.toJSONString(), IndicatorVal.class));
            }
        }

        model.setIndicator(ind);
        model.setField(field);
        if (ObjectUtils.isEmpty(records)) {
            return new JsonModel(true, empObj());
        }

        JSONArray resultArray = getHistoryArray(records, model, componentMap, neMap, period, interval);
        JSONObject unitTransfer = MonitorUtils.HistoryUnitTransfer(resultArray, getUnit(model));
        resultArray = unitTransfer.getJSONArray("result");
        result.put("unit", unitTransfer.getString("unit"));

        rows = unifyHistory(resultArray);

        result.put("rows", rows);
        result.put("columns", columns);
        return new JsonModel(true, result);
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
                    && !StringUtils.isEmpty(componentMap.get(historyValue.getNeId()))) {
                // 如果是部件，则indicatorValues肯定为JSONArray类型
                JSONArray fieldArray = (JSONArray) indicatorValues;
                for (int j = 0; j < fieldArray.size(); j++) {
                    JSONObject obj = fieldArray.getJSONObject(j);
                    // 跳过错误的采集数据
                    if (Strings.isNullOrEmpty(obj.getString("identifier"))) {
                        continue;
                    }
                    if (obj.getString("identifier").equals(componentMap.get(historyValue.getNeId()))) {
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
            NetworkEntity ne = neMap.get(historyValue.getNeId());
            // 如果采集间隔时间不存在，则展示所有存在的数据
            if (ObjectUtils.isEmpty(interval)) {
                resultObj.put("采集时间", DateUtils.formatCommonDate(
                        new Date(historyValue.getTm().getTime() - historyValue.getTm().getTime() % 60000L)));
                resultObj.put("name", ne.getIp() + ne.getName());
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
                        resultObj.put("name", ne.getIp() + ne.getName());
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
            String field = Strings.isNullOrEmpty(model.getField()) ? "result" : model.getField();
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

    public JsonModel getMultipleIndHistoryValue(String[] neIds, String[] indicators, String windows, IndPeriod period) throws Exception{
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
        // 根据统计时段的选择，将采集时间轴给固定好
        if (ObjectUtils.isEmpty(period)) {
            return new JsonModel(true, "未选择统计时段", empObj());
        }
        // 用一个JSONArray保存所有的展示时间
        JSONArray dateArray = new JSONArray();
        Date startDate = IndPeriod.getStartDate(period, new Date());
        // 先将startDate取整
        startDate.setTime(startDate.getTime() - startDate.getTime() % 60000L);
        Integer interval;
        if ("_1month".equals(period.name())) {
            // 如果选择的是展示一个月，则展示时间点数为每30分钟展示一个数据
            interval = 30;
        } else if ("_1week".equals(period.name())) {
            // 如果选择的是展示一周，则展示时间点数为每5分钟展示一个数据
            interval = 5;
        } else {
            // 如果选择的是展示一天，则根据存在数据进行展示
            interval = null;
        }
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
        JSONArray resultArray = new JSONArray();
        // 如果资源选择为空，无数据展示
        if (ObjectUtils.isEmpty(neIds)) {
            return new JsonModel(true, "未选择资源", empObj());
        }
        NetworkEntity ne = rpcProcessService.findNetworkEntityById(neIds[0]);
        // 排除无效、被删除、未监控资源
        if (ObjectUtils.isEmpty(ne) || ne.getManageStatus().equals(ManageStatus.Delected) || !ne.isMonitoring()) {
            return new JsonModel(true, "资源无效", empObj());
        }
        // 返回的结果
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONArray columns = new JSONArray();
        columns.add("采集时间");

        // 获取数据中心的指标数据
        for (int i = 0; i < indicatorArr.size(); i++) {
            String indicatorId = indicatorArr.getString(i);
            IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicatorId);
            String field = windowsJsonArray.getJSONObject(i).getString("fields");
            if (Objects.isNull(ind)) {
                return new JsonModel(true, empObj());
            }
            // 属性字段的JSON
            JSONObject fieldLabel = null;
            // 属性值的JSON
            JSONObject valueJSON = null;
            // 判断该指标是否有属性
            if (validHasFields(ind)) {
                JSONArray fields = ind.getFields();
                if (CollectionUtils.isEmpty(fields)) {
                    return new JsonModel(true, empObj());
                }
                // 查找属性的label
                for (int j = 0; j < fields.size(); j++) {
                    JSONObject fieldJson = fields.getJSONObject(j);
                    if (field.equalsIgnoreCase(fieldJson.getString("name"))) {
                        fieldLabel = fieldJson;
                        break;
                    }
                }
                if (null == fieldLabel) {
                    return new JsonModel(true, empObj());
                }
            }
            IndicatorValueCriteria criteria = new IndicatorValueCriteria();
            JSONObject sort = new JSONObject();
            JSONObject must = new JSONObject();
            JSONObject scope = new JSONObject();
            criteria.setPagination(false);
            sort.put("tm", "asc");
            criteria.setSort(sort);
            scope.put("gte", IndPeriod.getStartDate(period, new Date()));
            scope.put("lte", new Date());
            must.put("tm", scope);
            criteria.setMust(must);
            if (!indicatorId.isEmpty() && indicatorId.length() != 0) {
                List<String> idcNames = Lists.newArrayList(Strings.nullToEmpty(indicatorId).split(","));
                criteria.setIndicatorName(idcNames);
            }
            indicatorService.searchHistoryRecords(criteria);
            if (criteria.getObject() == null) {
                return new JsonModel(true, empObj());
            }
            String label = null;
            if (!validHasFields(ind)) {
                label = ind.getLabel();
                columns.add(label);
            } else {
                JSONArray fieldArr = ind.getFields();
                for (int j = 0; j < fieldArr.size(); j++) {
                    if (fieldArr.getJSONObject(j).getString("name").equals(field)) {
                        label = ind.getLabel() + ":" + fieldArr.getJSONObject(j).getString("label");
                        columns.add(label);
                    }
                }
            }
            Object IndValueObj = criteria.getObject();
            JSONArray valueArray = IndValueObj == null ? new JSONArray()
                    : JSON.parseArray(JSON.toJSONString(IndValueObj));
            List<IndicatorVal> records = valueArray == null ? null
                    : JSONObject.parseArray(valueArray.toString(), IndicatorVal.class);
            // 采集时间缓存判断：避免产生同一资源相同采集时间的数据
            String cacheTime = new String();
            // 创建一个采集时间判断数，用于判断采集时间与显示时间的关系
            Integer timeValid = 0;
            // 创建一个缓存数据保存值
            Double cacheValue = null;
            // 获取指标监控策略列表
            Boolean strategyField = getStrategy(neIds[0], indicatorId, field);
            // 若指标未监控，取消其数值展示
            if (!strategyField) {
                records = new ArrayList<IndicatorVal>();
            }
            for (IndicatorVal historyValue : records) {
                Object valueObj = historyValue.getIndicatorValue();
                if (ObjectUtils.isEmpty(valueObj)) {
                    continue;
                }
                JSON indicatorValues = (JSON) valueObj;
                // 若存在属性
                String unit;
                if (validHasFields(ind)) {
                    unit = fieldLabel.getString("unit");
                } else if ("PERCENT".equals(ind.getIndicatorType())) {
                    unit = "%";
                } else {
                    unit = null;
                }
                result.put("unit", unit);
                BigDecimal indValue;
                // 判断该指标是否有部件,COMPOUND、NUMBER、PERCENT类型的指标无部件，并且LIST类型中也有部件为空的(CPU核心利用率)
                if ((!"COMPOUND".equals(ind.getIndicatorType()) && validHasFields(ind))
                        && !StringUtils.isEmpty(componentNames[i])) {
                    // 如果是部件，则indicatorValues肯定为JSONArray类型
                    JSONArray fieldArray = JSON.parseArray(indicatorValues.toJSONString());
                    for (int j = 0; j < fieldArray.size(); j++) {
                        JSONObject obj = fieldArray.getJSONObject(j);
                        // 跳过错误的采集数据
                        if (Strings.isNullOrEmpty(obj.getString("identifier"))) {
                            continue;
                        }
                        if (obj.getString("identifier").equals(componentNames[i])) {
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
                        return new JsonModel(true, empObj());
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
                if (cacheTime.equals(DateUtils.formatCommonDate(historyValue.getTm()))) {
                    // 如果相同则跳过该采集点
                    continue;
                }
                // 缓存时间更新
                cacheTime = DateUtils.formatCommonDate(historyValue.getTm());
                /**
                 * 采集时间存放
                 */
                JSONObject resultObj = new JSONObject();
                // 如果采集间隔时间不存在，则展示所有存在的数据
                if (ObjectUtils.isEmpty(interval)) {
                    resultObj.put("采集时间", DateUtils.formatCommonDate(
                            new Date(historyValue.getTm().getTime() - historyValue.getTm().getTime() % 60000L)));
                    resultObj.put("name", label);
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
                            resultObj.put("name", label);
                            resultObj.put("value", cacheValue);
                            resultArray.add(resultObj);
                            // 清除缓存
                            cacheValue = null;
                        }
                    }
                }
            }
        }
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
            if (StringUtils.isEmpty(value)) {
                valueAllNull = true;
                row.put(name, value);
            } else {
                if (value.contains(".") && !value.contains("E")) {
                    value = value.substring(0, value.indexOf(".") + 2);
                }
                row.put(name, Double.parseDouble(value));
                valueAllNull = false;
            }
            // 从下一个采集数据开始遍历，查询是否有相同时间、不同资源名的数据
            for (int j = i + 1; j < resultArray.size(); j++) {
                if (resultArray.getJSONObject(j).getString("采集时间").equals(time)
                        && !resultArray.getJSONObject(j).getString("name").equals(name)) {
                    String otherValue = resultArray.getJSONObject(j).getString("value");
                    if (StringUtils.isEmpty(otherValue)) {
                        row.put(resultArray.getJSONObject(j).getString("name"), otherValue);
                        valueAllNull = true;
                    } else {
                        if (otherValue.contains(".") && !otherValue.contains("E")) {
                            otherValue = otherValue.substring(0, otherValue.indexOf(".") + 2);
                        }
                        row.put(resultArray.getJSONObject(j).getString("name"), Double.parseDouble(otherValue));
                        valueAllNull = false;
                    }
                }
            }
            useTime += " " + time;
            // 连续两次为空，跳过此数据
            if (continueNull && valueAllNull) {
                continue;
            }
            rows.add(row);
        }
        result.put("rows", rows);
        result.put("columns", columns);
        return new JsonModel(true, result);
    }
}
