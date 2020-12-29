package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.uxsino.authority.lib.model.DomainInfo;
import com.uxsino.authority.lib.util.DomainUtils;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.model.RunStatus;
import com.uxsino.leaderview.model.monitor.IndicatorTable;
import com.uxsino.leaderview.model.monitor.NetworkEntity;
import com.uxsino.leaderview.model.monitor.NetworkEntityQO;
import com.uxsino.leaderview.rpc.MonitorService;
import com.uxsino.reactorq.model.FieldType;
import com.uxsino.reactorq.model.INDICATOR_TYPE;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class HomeDataParamsService {

    @Autowired
    private DomainUtils domainUtils;

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private RpcProcessService rpcProcessService;

    /**
     * 根据当前用户获取所拥有权限的下拉框
     */
    public JsonModel getDomainByUser(HttpSession session) {
        return new JsonModel(true, getDomainByUser(session,List.class));
    }

    private List<Map<String, String>> getDomainByUser(HttpSession session, Class clazz){
        List<Map<String, String>> list = new ArrayList<>();
        List<Long> domainIds = domainUtils.getUserDomainIds(session);
        List<DomainInfo> infos = domainUtils.getAllDomainInfos();
        domainIds.forEach(id -> {
            Map<String, String> map1 = Maps.newHashMap();
            map1.put("name", infos.stream().filter(info -> info.getId().equals(id))
                    .findFirst().orElse(new DomainInfo()).getName());
            map1.put("value", id.toString());
            list.add(map1);
        });
        return list;
    }

    /**
     * 获取所有BaseNeClass-用于下拉列表
     * @param except 排除的父类型
     */
    public JsonModel getManageObjectEnum(BaseNeClass except){
        List<Map<String, String>> list = new ArrayList<>();
        try {
            for (BaseNeClass baseClass : BaseNeClass.values()) {
                if (baseClass.equals(except)) {
                    continue;
                }
                Map<String, String> map = new HashMap<>(2);
                map.put("name", baseClass.getText());
                map.put("value", baseClass.toString());
                list.add(map);
            }
        } catch (Exception e) {
            return new JsonModel(false, "网元类型配置错误，请检查配置文件！");
        }
        return new JsonModel(true, list);
    }

    public JsonModel getManageObjectEnum(){
        return getManageObjectEnum(null);
    }

    /**
     * 根据BaseNeClass获取所有子类型-用于下拉列表
     * @param baseNeClass 资源父类型
     */
    public JsonModel getNeClassByBase(BaseNeClass baseNeClass) {
        return new JsonModel(true, neClassByBase(baseNeClass));
    }

    private JsonModel getMultiNeClassByBase(Set<BaseNeClass> baseNeClasses){
        List<Map<String, String>> ls = new ArrayList<>();
        for (BaseNeClass baseNeClass:baseNeClasses) {
            ls.addAll(neClassByBase(baseNeClass));
        }
        return new JsonModel(true, ls);
    }

    /**
     * 根据BaseNeClass获取所有子类型-用于下拉列表
     * @param baseNeClass 资源父类型
     */
    public JsonModel getMultiNeClassByBase(String[] baseNeClass){
        Set<BaseNeClass> set = Sets.newHashSet();
        for (String str:baseNeClass) {
            set.add(BaseNeClass.valueOf(str));
        }
        return new JsonModel(true, getMultiNeClassByBase(set));
    }

    private List<Map<String, String>> neClassByBase(BaseNeClass baseNeClass){
        List<Map<String, String>> ls = new ArrayList<>();
        List<NeClass> classes;
        if (ObjectUtils.isEmpty(baseNeClass)) {
            classes = Arrays.asList(NeClass.values());
        } else {
            classes = baseNeClass.getNeClass();
        }
        for (NeClass neClass : classes) {
            Map<String, String> map = new HashMap<>(1);
            map.put("name", neClass.getText());
            map.put("value", neClass.toString());
            ls.add(map);
        }
        return ls;
    }

    /**
     * 获取所有资源状态-用于下拉框
     * @param abnormal 是否统计异常
     * @return
     */

    public JsonModel getNeStatus(Boolean abnormal) {
        List<Map<String, String>> result = getNeStatus();
        if (!existJudgment(abnormal,true)) {
            return new JsonModel(true, result);
        }
        // 异常 = 未知 + 告警 + 失联
        Map<String, String> map = Maps.newHashMap();
        map.put("name", "异常");
        map.put("value", "abnormal");
        result.add(map);
        return new JsonModel(true, result);
    }

    private List<Map<String, String>> getNeStatus(){
        List<RunStatus> list = Lists.newArrayList(RunStatus.values());
        List<Map<String, String>> result = Lists.newArrayList();
        for (RunStatus status : list) {
            Map<String, String> map = Maps.newHashMap();
            map.put("name", status.getName());
            map.put("value", status.toString());
            result.add(map);
        }
        return result;
    }

    /**
     * 获取指标类型的资源
     */
    @SuppressWarnings("unchecked")
    public JsonModel findNes(NetworkEntityQO entityQO,Boolean notUnknown){
        List<Map<String, String>> list = new ArrayList<>();
        List<NetworkEntity> nes;
        try {
            nes = rpcProcessService.findAllNetworkEntity(entityQO);
        }catch (Exception e){
            return new JsonModel(false, e.getMessage());
        }
        if (!CollectionUtils.isEmpty(nes)) {
            for (NetworkEntity ne : nes) {
                if ((this.existJudgment(notUnknown, false) && BaseNeClass.unknow.equals(ne.getBaseNeClass()))) {
                    continue;
                }
                if (!Strings.isNullOrEmpty(ne.getParentId()) && !ne.getParentId().equals(ne.getId())) {
                    continue;
                }
                Map<String, String> map = new HashMap<>(2);
                map.put("name", ne.getName() + "-" + ne.getIp());
                map.put("value", ne.getId());
                list.add(map);
            }
        }
        return new JsonModel(true, list);
    }

    /** Boolean类型参数存在性判断，默认为false */
    private Boolean existJudgment(Boolean value){
        return existJudgment(value, false);
    }

    private Boolean existJudgment(Boolean value ,Boolean defaultValue){
        return ObjectUtils.isEmpty(value) ? defaultValue : value;
    }

    /**
     * 设置资源查询类QO
     */
    public NetworkEntityQO setNeQO(HttpSession session,Long domainId ,BaseNeClass baseNeClass,NeClass neClass){
        NetworkEntityQOModel entityQO = new NetworkEntityQOModel();
        return entityQO.setNeQO(session, domainId).setNeQO(baseNeClass, neClass);
    }

    public JsonModel getUnit(String[] neIds) {
        List<String> unitList = Lists.newArrayList();
        Map<String, Integer> map = Maps.newHashMap();
        if ((neIds == null || neIds.length == 0)) {
            return new JsonModel(false, "未选择资源");
        }
        List<NetworkEntity> neList = Lists.newArrayList();
        try {
            neList = rpcProcessService.findNetworkEntityByIdIn(neIds);
        }catch (Exception e){
            return new JsonModel(false, e.getMessage());
        }
        if (ObjectUtils.isEmpty(neList)) {
            return new JsonModel(false, "资源不存在");
        }
        List<NeClass> neClasses = Lists.newArrayList();
        neList.forEach(ne -> neClasses.add(ne.getNeClass()));
        // 资源所有可配置的指标
        List<IndicatorTable> indicatorTables = Lists.newArrayList();
        try {
            indicatorTables = rpcProcessService.findUsableIndForNe(neList, neClasses);
        }catch (Exception e){
            return new JsonModel(false, e.getMessage());
        }
        if (CollectionUtils.isNotEmpty(indicatorTables)) {
            indicatorTables.forEach(ind -> {
                // 指标类型集合设置
                List<String> indTypes = Lists.newArrayList();
                indTypes = Lists.newArrayList(INDICATOR_TYPE.COMPOUND.toString(), INDICATOR_TYPE.LIST.toString(),
                        INDICATOR_TYPE.NUMBER.toString(), INDICATOR_TYPE.PERCENT.toString(),
                        INDICATOR_TYPE.COMPOUND.toString() + "_" + INDICATOR_TYPE.LIST.toString());
                // 过滤掉指标类型非LIST,COMPOUND、NUMBER和PERCENT的指标
                if (!indTypes.contains(ind.getIndicatorType())) {
                    return;
                }
                if (validHasFields(ind)) {
                    JSONArray fields = ind.getFields();
                    for (int i = 0; i < fields.size(); i++) {
                        JSONObject field = fields.getJSONObject(i);
                        if (!Objects.equals(field.getString("fieldType"), FieldType.NUMBER.toString())) {
                            continue;
                        }
                        String unit = field.getString("unit");
                        unit = Strings.isNullOrEmpty(unit) ? "NUMBER" : unit;
                        map.put(unit, ObjectUtils.isEmpty(map.get(unit)) ? 1 : map.get(unit) + 1);
                        if (!unitList.contains(unit)) {
                            unitList.add(unit);
                        }
                    }
                }
                // 类型为NUMBER和PERCENT的指标是无属性的，直接存放对应单位
                else if ("NUMBER".equals(ind.getIndicatorType())) {
                    String unit = "NUMBER";
                    map.put(unit, ObjectUtils.isEmpty(map.get(unit)) ? 1 : map.get(unit) + 1);
                    if (!unitList.contains(unit)) {
                        unitList.add(unit);
                    }
                } else if ("PERCENT".equals(ind.getIndicatorType())) {
                    String unit = "%";
                    map.put(unit, ObjectUtils.isEmpty(map.get(unit)) ? 1 : map.get(unit) + 1);
                    if (!unitList.contains(unit)) {
                        unitList.add(unit);
                    }
                }
            });
        }

        JSONArray result = new JSONArray();
        for (String unit : unitList) {
            JSONObject obj = new JSONObject();
            switch (unit){
                case "NUMBER":{
                    obj.put("name", "数值");
                    break;
                }
                case "%":{
                    obj.put("name", "百分比");
                    break;
                }
                default:{
                    obj.put("name", unit);
                    break;
                }
            }
            obj.put("value", unit);
            result.add(obj);
        }
        return new JsonModel(true, result);
    }

    private <T> List<T> filterToList(List<T> list, Predicate<? super T> predicate){
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 查询资源已配置的指标，或者用于TOPN的指标，用于下拉框
     * @param neIds 资源ID
     * @param type 指标类型
     * @param neClass 资源子类型
     * @param healthy 是否展示健康度
     */
    public JsonModel getIndicator(String[] neIds, String type, String unit, NeClass neClass, Boolean healthy) {

        // new一个新的arrs用于存放最后便利结果得到的arr，之后再拿arr进行比较
        List<JSONArray> arrs = new ArrayList<JSONArray>();
        healthy = existJudgment(healthy);

        if ((neIds == null || neIds.length == 0)) {
            return new JsonModel(false, "未选择资源");
        }

        boolean unitExist = !Strings.isNullOrEmpty(unit);
        boolean typeExist = !Strings.isNullOrEmpty(type) || unitExist;
        if (unitExist) {
            if ("NUMBER".equals(unit)) {
                unit = null;
            }
            if ("%".equals(unit)) {
                type = "PERCENT";
            } else {
                type = "NUMBER";
            }
        }
        String typeString = type;
        String unitString = unit;

            // 当取多个neId时，需要将查询出来的ne放在一个List当中，之后查询每一个ne对应的指标，取其并集显示于下拉框
            List<NetworkEntity> neList = Lists.newArrayList();
            try {
                neList = rpcProcessService.findNetworkEntityByIdIn(neIds);
            } catch (Exception e) {
                return new JsonModel(false, e.getMessage());
            }

            List<String> indTypes;
            if (!typeExist) {
                indTypes = Lists.newArrayList(INDICATOR_TYPE.COMPOUND.toString(),
                        INDICATOR_TYPE.LIST.toString(), INDICATOR_TYPE.NUMBER.toString(),
                        INDICATOR_TYPE.PERCENT.toString(),
                        INDICATOR_TYPE.COMPOUND.toString() + "_" + INDICATOR_TYPE.LIST.toString());
            } else if ("LIST".equals(typeString)) {
                indTypes = Lists.newArrayList(INDICATOR_TYPE.LIST.toString(),
                        INDICATOR_TYPE.COMPOUND.toString() + "_" + INDICATOR_TYPE.LIST.toString());
            } else {
                indTypes = Lists.newArrayList(INDICATOR_TYPE.COMPOUND.toString(),
                        INDICATOR_TYPE.LIST.toString(), typeString,
                        INDICATOR_TYPE.COMPOUND.toString() + "_" + INDICATOR_TYPE.LIST.toString());
            }
            for (NetworkEntity ne : neList) {
                if (ObjectUtils.isEmpty(ne)) {
                    return new JsonModel(false, "资源不存在");
                }
                List<IndicatorTable> indicatorTables = Lists.newArrayList();
                try {
                    indicatorTables = rpcProcessService.findUsableIndForNe(Lists.newArrayList(ne), Lists.newArrayList(ne.getNeClass()));
                } catch (Exception e) {
                    return new JsonModel(false, e.getMessage());
                }
                // 资源所有可配置的指标
                JSONArray arr = new JSONArray();
                if (CollectionUtils.isNotEmpty(indicatorTables)) {
                    // 查看资源已经配置的指标
                    JSONArray neInds = ne.getIndicators();
                    List<String> indList = neInds == null ? null : neInds.toJavaList(String.class);
                    indicatorTables = filterToList(indicatorTables, ind -> !(indList != null && !indList.contains(ind.getName())));
                    indicatorTables = filterToList(indicatorTables, ind -> indTypes.contains(ind.getIndicatorType()));
                    indicatorTables = filterToList(indicatorTables, ind -> "performance".equals(ind.getTag()));
                    indicatorTables.forEach(ind -> {
                        // 类型为NUMBER和PERCENT的指标是无属性的
                        if (validHasFields(ind)) {
                            JSONArray fields = ind.getFields();
                            boolean hasNumberType = false;
                            if (typeExist && "PERCENT".equals(typeString)) {
                                hasNumberType = fields.stream().anyMatch(e -> {
                                    JSONObject field = JSON.parseObject(JSON.toJSONString(e));
                                    return Objects.equals(field.getString("unit"), "%")
                                            && !field.containsKey("withoutrule");
                                });
                            } else {
                                hasNumberType = fields.stream().anyMatch(e -> {
                                    JSONObject field = JSON.parseObject(JSON.toJSONString(e));
                                    if (!unitExist) {
                                        return Objects.equals(field.getString("fieldType"), FieldType.NUMBER.toString())
                                                // && !Objects.equals(field.getString("unit"), "%")
                                                && !field.containsKey("withoutrule");
                                    } else {
                                        return Objects.equals(field.getString("fieldType"), FieldType.NUMBER.toString())
                                                && Objects.equals(field.getString("unit"), unitString)
                                                && !field.containsKey("withoutrule");
                                    }
                                });
                            }
                            // 过滤掉属性中没有NUMBER类型的指标
                            if (!hasNumberType) {
                                return;
                            }
                        } else if (unitExist
                                && !(Strings.isNullOrEmpty(unitString) && "NUMBER".equals(ind.getIndicatorType()))
                                && !("%".equals(unitString) && "PERCENT".equals(ind.getIndicatorType()))) {
                            return;
                        }
                        JSONObject obj = new JSONObject();
                        obj.put("name", ind.getLabel());
                        obj.put("value", ind.getName());
                        arr.add(obj);
                    });
                    // 如果选择展示健康度指标，这里对健康度指标进行特殊增添
                    if (healthy) {
                        JSONObject obj = new JSONObject();
                        obj.put("name", "健康度");
                        obj.put("value", "healthy");
                        arr.add(obj);
                    }
                    arrs.add(arr);
                }
            }

        // 遍历取指标交集
        return getIntersection(arrs);
    }

    public JsonModel getIndicatorTopn(String[] neIds,NeClass neClass) {

        // new一个新的arrs用于存放最后便利结果得到的arr，之后再拿arr进行比较
        List<JSONArray> arrs = new ArrayList<JSONArray>();

        if ((neIds == null || neIds.length == 0) && ObjectUtils.isEmpty(neClass)) {
            return new JsonModel(false, "未选择资源");
        }

        // 如果未选择资源，但是是topn的类型，要将已选子类型的指标展示出来
        if ((neIds == null || neIds.length == 0) && !ObjectUtils.isEmpty(neClass)) {
            List<IndicatorTable> indicatorTables = Lists.newArrayList();
            try {
                indicatorTables.addAll(rpcProcessService.findIndByNeClass(Lists.newArrayList(neClass.getBaseNeClass().toString())));
                indicatorTables.addAll(rpcProcessService.findIndByNeClass(Lists.newArrayList(neClass.toString())));
            }catch (Exception e){
                return new JsonModel(false, e.getMessage());
            }
            JSONArray arr = new JSONArray();
            if (CollectionUtils.isNotEmpty(indicatorTables)) {

                List<String> indTypes = Lists.newArrayList(INDICATOR_TYPE.NUMBER.toString(),
                        INDICATOR_TYPE.PERCENT.toString(), INDICATOR_TYPE.COMPOUND.toString());
                // 过滤掉指标类型非NUMBER、PERCENT、COMPOUND的指标
                indicatorTables = filterToList(indicatorTables, ind -> indTypes.contains(ind.getIndicatorType()));

                indicatorTables.forEach(ind -> {
                    String indName = ind.getName();
                    // 类型为NUMBER和PERCENT的指标是无属性的
                    if (validHasFields(ind)) {
                        JSONArray fields = ind.getFields();
                        boolean hasNumberType = fields.stream().anyMatch(e -> {
                            JSONObject field = JSON.parseObject(JSON.toJSONString(e));
                            return Objects.equals(field.getString("fieldType"), FieldType.NUMBER.toString());
                        });
                        // 过滤掉属性中没有NUMBER类型的指标
                        if (!hasNumberType) {
                            return;
                        }
                    }
                    JSONObject obj = new JSONObject();
                    obj.put("name", ind.getLabel());
                    obj.put("value", indName);
                    arr.add(obj);
                });
                // 如果选择展示健康度指标，这里对健康度指标进行特殊增添
                JSONObject obj = new JSONObject();
                obj.put("name", "健康度");
                obj.put("value", "healthy");
                arr.add(obj);
                arrs.add(arr);
            }
        } else if (!(neIds == null || neIds.length == 0)) {
            // 当取多个neId时，需要将查询出来的ne放在一个List当中，之后查询每一个ne对应的指标，取其并集显示于下拉框
            List<NetworkEntity> neList = Lists.newArrayList();
            try {
                neList = rpcProcessService.findNetworkEntityByIdIn(neIds);
            } catch (Exception e) {
                return new JsonModel(false, e.getMessage());
            }

            // 指标类型集合设置
            List<String> indTypes;
            if (ObjectUtils.isEmpty(neIds)) {
                indTypes = Lists.newArrayList(INDICATOR_TYPE.NUMBER.toString(),
                        INDICATOR_TYPE.PERCENT.toString(), INDICATOR_TYPE.COMPOUND.toString());
            } else {
                indTypes = Lists.newArrayList(INDICATOR_TYPE.COMPOUND.toString(),
                        INDICATOR_TYPE.LIST.toString(), INDICATOR_TYPE.NUMBER.toString(),
                        INDICATOR_TYPE.PERCENT.toString(),
                        INDICATOR_TYPE.COMPOUND.toString() + "_" + INDICATOR_TYPE.LIST.toString());
            }

            for (NetworkEntity ne : neList) {
                if (ObjectUtils.isEmpty(ne)) {
                    return new JsonModel(false, "资源不存在");
                }
                List<IndicatorTable> indicatorTables = Lists.newArrayList();
                try {
                    indicatorTables = rpcProcessService.findUsableIndForNe(Lists.newArrayList(ne), Lists.newArrayList(ne.getNeClass()));
                } catch (Exception e) {
                    return new JsonModel(false, e.getMessage());
                }
                // 资源所有可配置的指标
                JSONArray arr = new JSONArray();
                if (CollectionUtils.isNotEmpty(indicatorTables)) {
                    // 查看资源已经配置的指标
                    JSONArray neInds = ne.getIndicators();
                    List<String> indList = neInds == null ? null : neInds.toJavaList(String.class);

                    indicatorTables = filterToList(indicatorTables, ind -> !(indList != null && !indList.contains(ind.getName())));
                    indicatorTables = filterToList(indicatorTables, ind -> indTypes.contains(ind.getIndicatorType()));
                    indicatorTables = filterToList(indicatorTables, ind -> "performance".equals(ind.getTag()));


                    indicatorTables.forEach(ind -> {
                        String indName = ind.getName();
                        // 类型为NUMBER和PERCENT的指标是无属性的
                        if (validHasFields(ind)) {
                            JSONArray fields = ind.getFields();
                            boolean hasNumberType = fields.stream().anyMatch(e -> {
                                JSONObject field = JSON.parseObject(JSON.toJSONString(e));
                                return Objects.equals(field.getString("fieldType"), FieldType.NUMBER.toString())
                                        && !field.containsKey("withoutrule");
                            });
                            // 过滤掉属性中没有NUMBER类型的指标
                            if (!hasNumberType) {
                                return;
                            }
                        }
                        JSONObject obj = new JSONObject();
                        obj.put("name", ind.getLabel());
                        obj.put("value", indName);
                        arr.add(obj);
                    });
                    // 如果选择展示健康度指标，这里对健康度指标进行特殊增添

                    JSONObject obj = new JSONObject();
                    obj.put("name", "健康度");
                    obj.put("value", "healthy");
                    arr.add(obj);

                    arrs.add(arr);
                }
            }
        }
        // 遍历取指标交集
        return getIntersection(arrs);
    }

    /**
     * 多资源指标遍历取交集指标
     * @param arrs
     * @return
     */
    private JsonModel getIntersection(List<JSONArray> arrs) {
        if (CollectionUtils.isEmpty(arrs)) {
            return new JsonModel(true, empObj());
        }
        // 将所有的指标列表遍历取交集
        JSONArray result = arrs.get(0);
        JSONArray intersection_arr = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.size(); i++) {
                if (result.indexOf(arr.getJSONObject(i)) == -1) {
                    intersection_arr = new JSONArray();
                    break;
                } else {
                    intersection_arr.add(result.getJSONObject(i));
                }
            }
            result = intersection_arr;
            intersection_arr = new JSONArray();
        }
        if (result.isEmpty()) {
            return new JsonModel(false, "没有相同属性部件");
        }
        return new JsonModel(true, result);
    }

    public class NetworkEntityQOModel extends NetworkEntityQO{
        NetworkEntityQOModel setNeQO(HttpSession session, Long domainId){
            if (domainId != null) {
                this.setDomainIds(new Long[] { domainId });
            } else {
                List<Long> domainIds = domainUtils.getUserDomainIds(session);
                if (domainIds.isEmpty()) {
                    //TODO 该用户权限下无资源
                    return null;
                }
                Long[] ids = new Long[domainIds.size()];
                for (int i = 0; i < domainIds.size(); i++) {
                    ids[i] = domainIds.get(i);
                }
                this.setDomainIds(ids);
            }
            return this;
        }

        NetworkEntityQOModel setNeQO(BaseNeClass baseNeClass,NeClass neClass){
            // 过滤掉不被监控的资源
            this.setMonitoring(true);
            if (!ObjectUtils.isEmpty(neClass)) {
                this.setNeClasses(neClass.toString());
                // 虚拟化资源的sourceManage为false
                if (neClass.getBaseNeClass().equals(BaseNeClass.virtualization)) {
                    this.setSourceManage(false);
                }
            } else {
                this.setBaseNeClass(baseNeClass);
                // 虚拟化资源的sourceManage为false
                if (!ObjectUtils.isEmpty(baseNeClass) && baseNeClass.equals(BaseNeClass.virtualization)) {
                    this.setSourceManage(false);
                }
            }
            return this;
        }
    }

    /**
     *  判断某一指标是否应该存在部件
     */
    private boolean validHasFields(IndicatorTable ind) {
        // 只要是NUMBER类型、PERCENT类型或者STRING类型的指标，都没有属性
        return !"NUMBER".equals(ind.getIndicatorType()) && !"PERCENT".equals(ind.getIndicatorType())
                && !"STRING".equals(ind.getIndicatorType());
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

}
