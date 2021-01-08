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
        // 属性值的JSON
        JSONObject valueJSON = new JSONObject();
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

        // 获取指标监控策略列表
        JSONObject strategyObj = rpcProcessService.getStrategy(neIds, indicators);
        Boolean strategyField = true;
        Object fieldStraObj = strategyObj.get(indicators);
        if (fieldStraObj instanceof JSONObject) {
            strategyField = ((JSONObject) fieldStraObj).getBoolean(field);
        } else if (fieldStraObj instanceof Boolean) {
            strategyField = (Boolean) fieldStraObj;
        }

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


        // 如果是类型为NUMBER、PERCENT的指标，直接取值返回。就不用做之后的部件判断了
        if (!validHasFields(ind)) {
            result.put("name", ind.getLabel());
            if (indicatorValues instanceof JSONArray) {
                valueJSON = ((JSONArray) indicatorValues).getJSONObject(0);
            } else if (indicatorValues instanceof JSONObject) {
                valueJSON = (JSONObject) indicatorValues;
            }
            // 如果是百分比类型的指标，单位为%
            if ("PERCENT".equals(ind.getIndicatorType())) {
                result.put("unit", "%");
            } else {
                result.put("unit", "");
            }
            String value = valueJSON.getString("result");
            Matcher m = NUMBER_PATTERN.matcher(value);
            while (m.find()) {
                value = m.group(1);
                break;
            }
            if (Objects.equals(value, "false")) {
                value = "0";
            }
            result.put("value", value);
            // 直接返回值显示
            return new JsonModel(true, result);
        }

        IndicatorValueUtils valueUtils = new IndicatorValueUtils();
        String unit = fieldLabel.getString("unit");
        if (StringUtils.isEmpty(componentName)) {

            if (indicatorValues instanceof JSONArray) {
                valueJSON = ((JSONArray) indicatorValues).getJSONObject(0);
            } else if (indicatorValues instanceof JSONObject) {
                valueJSON = (JSONObject) indicatorValues;
            }

            valueUtils.transferItem(fieldLabel, valueJSON);
            String value = valueJSON.getString(field);
            if (Strings.isNullOrEmpty(value)) {
                return new JsonModel(true, empObj);
            }
            Matcher m = CHINESE_PATTERN.matcher(value);
            while (m.find()) {
                value = m.group(1);
                break;
            }
            if (Strings.isNullOrEmpty(unit)) {
                result.put("value", String.format("%.2f", Double.parseDouble(value)));
                result.put("unit", "");
            } else {
                int index = value.lastIndexOf(" ");
                result.put("value", index > 0 ? String.format("%.2f", Double.parseDouble(value.substring(0, index)))
                        : String.format("%.2f", Double.parseDouble(value)));
                result.put("unit", index > 0 ? value.substring(index + 1) : unit);
            }
            result.put("name", fieldLabel.getString("label"));
            if (ipmiIndicator.contains(indicators)) {
                result.put("name", ind.getLabel());
                result.put("unit", valueJSON.getString("unit"));
            }
        } else {
            // 根据指标ID取对应参数
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
            for (int i = 0; i < fieldArray.size(); i++) {
                JSONObject fieldObj = fieldArray.getJSONObject(i);
                if (fieldObj.get("identifier").toString().equals(componentName)) {
                    result.put("name", componentNameMap.get(componentName) + ":" + fieldLabel.getString("label"));
                    // if ("sw_run_entry".equals(indicators)){
                    // result.put("name", componentName + ":" + fieldLabel.getString("label"));
                    // }
                    // valueJSON.put(field, fieldObj.getString(field));
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
                        result.put("value", String.format("%.2f", Double.parseDouble(value)));
                        result.put("unit", "");
                    } else {
                        int index = value.lastIndexOf(" ");
                        result.put("value",
                                index > 0 ? String.format("%.2f", Double.parseDouble(value.substring(0, index)))
                                        : String.format("%.2f", Double.parseDouble(value)));
                        result.put("unit", index > 0 ? value.substring(index + 1) : unit);
                    }
                    if (ipmiIndicator.contains(indicators)) {
                        result.put("name", componentNameMap.get(componentName) + ":" + ind.getLabel());
                        result.put("unit", fieldObj.get("unit"));
                    }
                    break;
                }
            }
        }
        return new JsonModel(true, result);
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


}
