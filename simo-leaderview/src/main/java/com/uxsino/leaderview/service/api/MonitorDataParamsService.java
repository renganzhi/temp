package com.uxsino.leaderview.service.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.uxsino.authority.lib.model.DomainInfo;
import com.uxsino.authority.lib.util.DomainUtils;
import com.uxsino.commons.db.model.PageModel;
import com.uxsino.commons.model.BaseNeClass;
import com.uxsino.commons.model.JsonModel;
import com.uxsino.commons.model.NeClass;
import com.uxsino.commons.model.RunStatus;
import com.uxsino.leaderview.model.monitor.IndicatorTable;
import com.uxsino.leaderview.model.monitor.NetworkEntity;
import com.uxsino.leaderview.model.monitor.NetworkEntityQO;
import com.uxsino.leaderview.model.monitor.NetworkLinkModel;
import com.uxsino.leaderview.rpc.MonitorService;
import com.uxsino.reactorq.model.FieldType;
import com.uxsino.reactorq.model.INDICATOR_TYPE;
import com.uxsino.leaderview.utils.MonitorUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Component
public class MonitorDataParamsService {

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
        return new JsonModel(true, list);
    }

    public List<Long> getDomainByUserSession(HttpSession session){
        return domainUtils.getUserDomainIds(session);
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
                    fields = filter(fields, o -> Objects.equals(o.getString("fieldType"), FieldType.NUMBER.toString()));
                    action(fields, o ->{
                        String unit = o.getString("unit");
                        unit = Strings.isNullOrEmpty(unit) ? "NUMBER" : unit;
                        map.put(unit, ObjectUtils.isEmpty(map.get(unit)) ? 1 : map.get(unit) + 1);
                        if (!unitList.contains(unit)) {
                            unitList.add(unit);
                        }
                    });
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
                                fields = filter(fields, o -> Objects.equals(o.getString("unit"), "%"));
                                fields = filter(fields, o -> !o.containsKey("withoutrule"));
                                hasNumberType = !CollectionUtils.isEmpty(fields);
                            } else {
                                fields = filter(fields, o -> !o.containsKey("withoutrule"));
                                fields = filter(fields, o -> Objects.equals(o.getString("fieldType"), FieldType.NUMBER.toString()));
                                if (unitExist) {
                                    fields = filter(fields, o -> Objects.equals(o.getString("unit"), unitString));
                                }
                                hasNumberType = !CollectionUtils.isEmpty(fields);
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
                        arr.add(newResultObj(ind.getLabel(), ind.getName()));
                    });
                    // 如果选择展示健康度指标，这里对健康度指标进行特殊增添
                    if (healthy) {
                        arr.add(newResultObj("健康度", "healthy"));
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
                        fields = filter(fields, o -> Objects.equals(o.getString("fieldType"), FieldType.NUMBER.toString()));
                        boolean hasNumberType = !CollectionUtils.isEmpty(fields);
                        // 过滤掉属性中没有NUMBER类型的指标
                        if (!hasNumberType) {
                            return;
                        }
                    }
                    arr.add(newResultObj(ind.getLabel(), indName));
                });
                // 如果选择展示健康度指标，这里对健康度指标进行特殊增添
                arr.add(newResultObj("健康度", "healthy"));

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
                            fields = filter(fields, o -> Objects.equals(o.getString("fieldType"), FieldType.NUMBER.toString()));
                            fields = filter(fields, o -> !o.containsKey("withoutrule"));
                            boolean hasNumberType = !CollectionUtils.isEmpty(fields);
                            // 过滤掉属性中没有NUMBER类型的指标
                            if (!hasNumberType) {
                                return;
                            }
                        }
                        arr.add(newResultObj(ind.getLabel(), indName));
                    });
                    // 如果选择展示健康度指标，这里对健康度指标进行特殊增添
                    arr.add(newResultObj("健康度", "healthy"));

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

    /**
     * 查询资源已配置的指标，或者用于TOPN的指标，用于下拉框
     * @param neIds 资源ID
     * @param neClass 资源子类型
     */
    public JsonModel getIndicatorStr(String[] neIds, NeClass neClass) {
        // new一个新的arrs用于存放最后便利结果得到的arr，之后再拿arr进行比较
        List<JSONArray> arrs = new ArrayList<JSONArray>();
        if ((neIds == null || neIds.length == 0) && ObjectUtils.isEmpty(neClass)) {
            return new JsonModel(false, "未选择资源");
        }
        if (!(neIds == null || neIds.length == 0)) {
            // 当取多个neId时，需要将查询出来的ne放在一个List当中，之后查询每一个ne对应的指标，取其并集显示于下拉框
            List<NetworkEntity> neList = new ArrayList<NetworkEntity>();
            try {
                neList = rpcProcessService.findNetworkEntityByIdIn(neIds);

                // 指标类型集合设置
                List<String> indTypes = Lists.newArrayList(INDICATOR_TYPE.STRING.toString(),
                        INDICATOR_TYPE.COMPOUND.toString(), INDICATOR_TYPE.LIST.toString(),
                        INDICATOR_TYPE.NUMBER.toString(), INDICATOR_TYPE.PERCENT.toString());

                for (NetworkEntity ne : neList) {
                    if (ObjectUtils.isEmpty(ne)) {
                        return new JsonModel(false, "资源不存在");
                    }
                    // 资源所有可配置的指标
                    List<IndicatorTable> indicatorTables = rpcProcessService.findUsableIndForNe(Lists.newArrayList(ne), Lists.newArrayList(ne.getNeClass()));
                    JSONArray arr = new JSONArray();
                    if (CollectionUtils.isNotEmpty(indicatorTables)) {
                        // 查看资源已经配置的指标
                        JSONArray neInds = ne.getIndicators();
                        List<String> indList = neInds == null ? null : neInds.toJavaList(String.class);
                        // 如果该指标未包含在资源配置的指标列表中，则跳过
                        indicatorTables = filterToList(indicatorTables, ind -> !(indList != null && !indList.contains(ind.getName())));
                        // 过滤掉指标类型非String的指标
                        indicatorTables = filterToList(indicatorTables, ind -> indTypes.contains(ind.getIndicatorType()));
                        indicatorTables = filterToList(indicatorTables, ind -> "performance".equals(ind.getTag()));

                        indicatorTables.forEach(ind -> {
                            String indName = ind.getName();
                            if ("NUMBER".equals(ind.getIndicatorType()) || "PERCENT".equals(ind.getIndicatorType())
                                    || "STRING".equals(ind.getIndicatorType())) {
                                arr.add(newResultObj(ind.getLabel(), indName));
                            } else {
                                JSONArray fields = ind.getFields();
                                boolean hasStringType;
                                if (!ObjectUtils.isEmpty(fields)){
                                    fields = filter(fields, o -> Objects.equals(o.getString("fieldType"), FieldType.NUMBER.toString())
                                            || Objects.equals(o.getString("fieldType"), FieldType.STRING.toString()));
                                    fields = filter(fields, o -> !o.containsKey("withoutrule"));
                                    hasStringType = !CollectionUtils.isEmpty(fields);
                                }else {
                                    hasStringType = true;
                                }
                                // 过滤掉属性中没有String类型的指标
                                if (!hasStringType) {
                                    return;
                                }
                                arr.add(newResultObj(ind.getLabel(), indName));
                            }
                        });
                        arrs.add(arr);
                    }
                }
            } catch (Exception e) {
                return new JsonModel(false, e.getMessage());
            }
        }
        // 遍历取指标交集
        return getIntersection(arrs);
    }

    /**
     * 根据资源ID和指标名获取部件ID与名称的键值对:用于下拉框
     * @param neIds 资源IDs
     * @param indicators 指标名称
     * @return
     */
    public JsonModel getComponentName(String[] neIds, String indicators) throws Exception{
        if (neIds.length == 0 || StringUtils.isEmpty(neIds)) {
            return new JsonModel(false, "未选择资源");
        }
        if (Strings.isNullOrEmpty(neIds[0]) || Strings.isNullOrEmpty(indicators)) {
            return new JsonModel(false, "参数错误");
        }
        IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicators);
        if (ObjectUtils.isEmpty(ind)) {
            return new JsonModel(false, "未查询到指标");
        }
        // 如果是数值或者百分比类型的指标，则无属性
        if (!validHasFields(ind)) {
            return new JsonModel(true, new JSONArray());
        }
        if ("COMPOUND".equals(ind.getIndicatorType())) {
            return new JsonModel(true, new JSONArray());
        }
        JSONArray result = new JSONArray();
        List<Map<String, Object>> idAndComponent = rpcProcessService.findNeComps(Lists.newArrayList(neIds),
                indicators, null, null, null);
        // 如果有部件，应该对资源进行遍历查询部件
        if (ObjectUtils.isEmpty(idAndComponent)) {
            result.add(newResultObj("值", null));
        }
        else {
            idAndComponent.forEach(map -> {
                result.add(newResultObj(map.get("componentName"),  map.get("identifier")));
            });
        }
        return new JsonModel(true, result);
    }

    /**
     * 获取某指标的数值属性-用于下拉框
     * @param indicators 指标名称
     */
    public JsonModel getIndField(String indicators) throws Exception{
        if (Strings.isNullOrEmpty(indicators)) {
            return new JsonModel(false, "指标名称为空");
        }
        IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicators);
        if (ObjectUtils.isEmpty(ind)) {
            return new JsonModel(false, "未查询到任何指标");
        }
        JSONArray result = new JSONArray();
        // 如果是NUMBER或者PERCENT的指标，是没有属性的
        if (!validHasFields(ind)) {
            result.add(newResultObj("值", null));
        }
        else {
            JSONArray fields = ind.getFields();
            if (CollectionUtils.isEmpty(fields)) {
                return new JsonModel(false, "该指标的属性为空");
            }
            fields = filter(fields, o -> !o.containsKey("withoutrule"));
            fields = filter(fields, o -> FieldType.NUMBER.toString().equals(o.getString("fieldType")));
            action(fields, o -> result.add(newResultObj(o.getString("label"), o.getString("name"))));
        }
        return new JsonModel(true, result);
    }


    /**
     * 根据指标类型获取指标的某类型属性-用于下拉框
     * @param indicators 指标名称
     * @param type 属性类型 ("ALL"取所有类型，"PERCENT"取百分比类型，"NUMBER"取数值类型，"STRING"取字符串类型)
     * @return
     */
    public JsonModel getIndField(String indicators, String type) throws Exception{
        if (Strings.isNullOrEmpty(indicators)) {
            return new JsonModel(false, "指标名称为空");
        }
        JSONArray result = new JSONArray();
        // 对健康度指标进行特殊化处理
        if ("healthy".equals(indicators)) {
            result.add(newResultObj("值", null));
            return new JsonModel(true, result);
        }
        IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicators);
        if (ObjectUtils.isEmpty(ind)) {
            return new JsonModel(false, "未查询到任何指标");
        }
        // 如果是NUMBER或者PERCENT的指标，是没有属性的
        if (!validHasFields(ind)) {
            result.add(newResultObj("值", null));
        } else {
            JSONArray fields = ind.getFields();
            if (CollectionUtils.isEmpty(fields)) {
                return new JsonModel(false, "该指标的属性为空");
            }
            fields = filter(fields, o -> !o.containsKey("withoutrule"));
            if ("ALL".equals(type)) {
                // 只展示数值类和需要展示的指标
                fields = filter(fields, o -> FieldType.NUMBER.toString().equals(o.getString("fieldType")));
                fields = filter(fields, o -> Objects.equals(o.getString("unit"), "%"));
                fields = filter(fields, o -> Objects.equals("performance", o.getString("tag")));
            } else {
                switch (type){
                    case "PERCENT":
                        fields = filter(fields, o -> FieldType.NUMBER.toString().equals(o.getString("fieldType")));
                        fields = filter(fields, o -> Objects.equals(o.getString("unit"), "%"));
                        break;
                    case "NUMBER":
                        fields = filter(fields, o -> FieldType.NUMBER.toString().equals(o.getString("fieldType")));
                        fields = filter(fields, o -> !Objects.equals(o.getString("unit"), "%"));
                        break;
                    case "STRING":
                        fields = filter(fields, o -> FieldType.NUMBER.toString().equals(o.getString("fieldType"))
                                || FieldType.NUMBER.toString().equals(o.getString("fieldType")));
                        break;
                }
            }
            action(fields, o -> result.add(newResultObj(o.getString("label"), o.getString("name"))));
        }
        return new JsonModel(true, result);
    }

    /**
     * 根据已选资源和指标查询可选部件与可选属性
     * @param indicators
     * @param neIds
     * @param type
     * @return
     */
    public JsonModel getComponentNameAndIndFieldByType(String[] indicators, String[] neIds, String type, String unit) throws Exception{
        boolean unitExist = !Strings.isNullOrEmpty(unit);
        if (unitExist) {
            if (unit.equals("%")) {
                type = "PERCENT";
            } else {
                if (unit.equals("NUMBER")) {
                    unit = null;
                }
                type = "NUMBER";
            }
        }
        JSONArray result = new JSONArray();
        // 对每个指标进行遍历
        for (String indicatorName : indicators) {
            JSONObject indicatorResult = new JSONObject();
            // 根据指标名称查询指标
            IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicatorName);
            if (ObjectUtils.isEmpty(ind)) {
                // 该指标名称无对应指标
                return new JsonModel(false, "windows");
            }
            /**
             * 将指标Obj传入指标Array
             */
            // 往指标Array中增加指标名称和对应值
            JSONObject indicatorObj = new JSONObject();
            indicatorObj.put("name", ind.getLabel());
            indicatorObj.put("value", ind.getName());
            // 属性多选
            indicatorObj.put("multipleField", true);
            indicatorResult.put("indicator", indicatorObj);
            /**
             * 将属性Array传入指标Array
             */
            JSONArray fieldsResult = new JSONArray();
            // 根据指标查询属性
            JSONArray fields = ind.getFields();
            if (ObjectUtils.isEmpty(fields)) {
                // 该指标无可选属性
                // 判断是否为NUMBER或者PERCENT类型的指标，若都不是，且指标没有属性，直接返回空值
                if (validHasFields(ind)) {
                    return new JsonModel(false, "windows");
                }
                fieldsResult.add(newResultObj("值",null));
            } else {
                fields = filter(fields, o -> !o.containsKey("withoutrule"));
                fields = filter(fields, o -> FieldType.NUMBER.toString().equals(o.getString("fieldType")));
                if ("PERCENT".equals(type)) {
                    fields = filter(fields, o -> Objects.equals(o.getString("unit"), "%"));
                }
                if ("NUMBER".equals(type) && !unitExist) {
                    fields = filter(fields, o -> !Objects.equals(o.getString("unit"), "%"));
                }
                if ("NUMBER".equals(type) && unitExist) {
                    fields = filter(fields, o -> Objects.equals(o.getString("unit"), "%"));
                }
                action(fields, o -> fieldsResult.add(newResultObj(o.getString("label"), o.getString("name"))));
            }
            indicatorResult.put("fields", fieldsResult);

            /**
             * 将资源Array传入指标Array
             */
            JSONArray neParentResult = new JSONArray();

            List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(neIds);
            List<Map<String, Object>> idAndComponent = rpcProcessService.findNeComps(Lists.newArrayList(neIds),
                    indicatorName, null, null, null);
            // 对每个资源进行遍历
            for (NetworkEntity ne : nes) {
                JSONObject neResult = new JSONObject();
                // 根据id查找对应资源
                neResult.put("name", ne.getName());
                neResult.put("neClass", ne.getNeClass());
                neResult.put("ip", ne.getIp());
                neResult.put("id", ne.getId());
                // 部件单选
                neResult.put("multipleComponent", false);
                // 根据指标查询部件
                /**
                 * 将部件Array传入资源Array
                 */
                JSONArray componentResult = new JSONArray();
                // 如果该指标类型为LIST类型才存在部件，COMPOUND类型也是没有部件的
                if (!validHasFields(ind) || "COMPOUND".equals(ind.getIndicatorType())) {
                    neResult.put("component", componentResult);
                    neParentResult.add(neResult);
                    continue;
                }
                // 根据资源id和指标名查询部件
                List<Map<String, Object>> list = Lists.newArrayList();
                idAndComponent.forEach(map -> {
                    if (ne.getId().equals((String) map.get("neId"))){
                        list.add(map);
                    }
                });
                if (ObjectUtils.isEmpty(list)) {
                    componentResult.add(newResultObj("无部件数据",null));
                }
                list.forEach(map -> componentResult.add(newResultObj(map.get("componentName"),map.get("identifier"))));
                neResult.put("component", componentResult);
                neParentResult.add(neResult);
            }
            indicatorResult.put("ne", neParentResult);
            result.add(indicatorResult);
        }
        return new JsonModel(true, "windows", result);
    }

    /**
     * 弹窗内容的数据获取
     */
    public JsonModel getWindows(String[] indicators, String[] neIds, boolean multipleComponent,
                                 boolean multipleField) throws Exception{
        // 弹窗数据返回值的msg字段无论成功与否均要返回windows
        JSONArray result = new JSONArray();
        if (ObjectUtils.isEmpty(neIds)) {
            return new JsonModel(false, "windows");
        }
        // 对每个指标进行遍历
        for (String indicatorName : indicators) {
            JSONObject indicatorResult = new JSONObject();
            // 根据指标名称查询指标
            IndicatorTable ind = rpcProcessService.getIndicatorInfoByName(indicatorName);
            if (ObjectUtils.isEmpty(ind)) {
                // 该指标名称无对应指标
                return new JsonModel(false, "windows");
            }
            /**
             * 将指标Obj传入指标Array
             */
            // 往指标Array中增加指标名称和对应值
            JSONObject indicatorObj = new JSONObject();
            indicatorObj.put("name", ind.getLabel());
            indicatorObj.put("value", ind.getName());
            // 属性是否多选
            indicatorObj.put("multipleField", multipleField);
            indicatorResult.put("indicator", indicatorObj);
            /**
             * 将属性Array传入指标Array
             */
            JSONArray fieldsResult = new JSONArray();
            // 根据指标查询属性
            JSONArray fields = ind.getFields();
            if (ObjectUtils.isEmpty(fields)) {
                // 该指标无可选属性
                // 判断是否为NUMBER或者PERCENT类型的指标，若都不是，且指标没有属性，直接返回空值
                if (validHasFields(ind)) {
                    return new JsonModel(false, "windows");
                }
                fieldsResult.add(newResultObj("值", null));
            } else {
                // LIST指标存在属性，则获取属性列表
                fields = filter(fields, o -> FieldType.NUMBER.toString().equals(o.getString("fieldType")));
                fields = filter(fields, o -> !o.containsKey("withoutrule"));
                action(fields, o -> fieldsResult.add(newResultObj(o.getString("label"), o.getString("name"))));
            }
            indicatorResult.put("fields", fieldsResult);

            /**
             * 将资源Array传入指标Array
             */
            JSONArray neParentResult = new JSONArray();

            List<NetworkEntity> nes = rpcProcessService.findNetworkEntityByIdIn(neIds);
            List<Map<String, Object>> idAndComponent = rpcProcessService.findNeComps(Lists.newArrayList(neIds),
                    indicatorName, null, null, null);
            // 对每个资源进行遍历
            for (NetworkEntity ne : nes) {
                JSONObject neResult = new JSONObject();
                // 根据id查找对应资源
                neResult.put("name", ne.getName());
                neResult.put("neClass", ne.getNeClass());
                neResult.put("ip", ne.getIp());
                neResult.put("id", ne.getId());
                // 部件单选
                neResult.put("multipleComponent", multipleComponent);
                // 根据指标查询部件
                /**
                 * 将部件Array传入资源Array
                 */
                JSONArray componentResult = new JSONArray();
                // 如果该指标类型为LIST类型才存在部件，COMPOUND类型也是没有部件的
                if (!validHasFields(ind) || "COMPOUND".equals(ind.getIndicatorType())) {
                    neResult.put("component", componentResult);
                    neParentResult.add(neResult);
                    continue;
                }
                // 根据资源id和指标名查询部件
                List<Map<String, Object>> list = Lists.newArrayList();
                idAndComponent.forEach(map -> {
                    if (ne.getId().equals((String) map.get("neId"))){
                        list.add(map);
                    }
                });
                if (ObjectUtils.isEmpty(list)) {
                    componentResult.add(newResultObj("无部件数据",null));
                }
                list.forEach(map -> componentResult.add(newResultObj(map.get("componentName"),map.get("identifier"))));
                neResult.put("component", componentResult);
                neParentResult.add(neResult);
            }
            indicatorResult.put("ne", neParentResult);
            result.add(indicatorResult);
        }
        return new JsonModel(true, "windows", result);
    }

    @SuppressWarnings("unchecked")
    public JsonModel getNetworkSourceId(HttpSession session) throws Exception{
        List<Long> domainList = getDomainByUserSession(session);
        NetworkLinkModel networkLinkModel = new NetworkLinkModel();
        networkLinkModel.setNetworkLinkIds(Lists.newArrayList());
        networkLinkModel.setNeIds(rpcProcessService.getNeIdsByDomainIds(domainList.toArray(new Long[domainList.size()]), session));
        PageModel temPage = new PageModel();
        temPage.setCurrentNo(1);
        temPage.setPageSize(10000);
        PageModel pageModel = rpcProcessService.findPage(temPage, networkLinkModel);
        List<NetworkLinkModel> list = (List<NetworkLinkModel>) pageModel.getObject();
        List<Map<String, String>> result = Lists.newArrayList();
        for (NetworkLinkModel ne : list) {
            Map<String, String> map = Maps.newHashMap();
            map.put("name", ne.getSourceIp());
            map.put("value", ne.getSourceId());
            if (result.contains(map)) {
                continue;
            }
            result.add(map);
        }
        return new JsonModel(true, result);
    }

    @SuppressWarnings("unchecked")
    public JsonModel getNetworkSourceIfName(HttpSession session, String sourceId) throws Exception{
        List<Long> domainList = getDomainByUserSession(session);
        Long[] domains = domainList.toArray(new Long[domainList.size()]);
        NetworkLinkModel networkLinkModel = new NetworkLinkModel();
        networkLinkModel.setNetworkLinkIds(Lists.newArrayList());
        networkLinkModel.setNeIds(rpcProcessService.getNeIdsByDomainIds(domains, session));
        networkLinkModel.setSourceId(sourceId);
        PageModel temPage = new PageModel();
        temPage.setCurrentNo(1);
        temPage.setPageSize(10000);
        PageModel pageModel = rpcProcessService.findPage(temPage, networkLinkModel);
        List<NetworkLinkModel> list = (List<NetworkLinkModel>) pageModel.getObject();
        List<Map<String, String>> result = Lists.newArrayList();
        for (NetworkLinkModel ne : list) {
            Map<String, String> map = Maps.newHashMap();
            map.put("name", ne.getSourceIfName());
            map.put("value", ne.getSourceIfName());
            if (result.contains(map)) {
                continue;
            }
            if (sourceId.equals(ne.getSourceId())) {
                result.add(map);
            }
        }
        return new JsonModel(true, result);
    }

    @SuppressWarnings("unchecked")
    public JsonModel getNetworkTargetId(HttpSession session, String sourceId, String sourceIfName) throws Exception{
        List<Long> domainList = getDomainByUserSession(session);
        Long[] domains = domainList.toArray(new Long[domainList.size()]);
        NetworkLinkModel networkLinkModel = new NetworkLinkModel();
        networkLinkModel.setNetworkLinkIds(Lists.newArrayList());
        networkLinkModel.setNeIds(rpcProcessService.getNeIdsByDomainIds(domains, session));
        networkLinkModel.setSourceId(sourceId);
        networkLinkModel.setSourceIfName(sourceIfName);
        PageModel temPage = new PageModel();
        temPage.setCurrentNo(1);
        temPage.setPageSize(10000);
        PageModel pageModel = rpcProcessService.findPage(temPage, networkLinkModel);
        List<NetworkLinkModel> list = (List<NetworkLinkModel>) pageModel.getObject();
        List<Map<String, String>> result = Lists.newArrayList();
        for (NetworkLinkModel ne : list) {
            Map<String, String> map = Maps.newHashMap();
            map.put("name", ne.getTargetIp());
            map.put("value", ne.getTargetId());
            if (result.contains(map)) {
                continue;
            }
            if (sourceIfName.equals(ne.getSourceIfName()) && sourceId.equals(ne.getSourceId())) {
                result.add(map);
            }
        }
        return new JsonModel(true, result);
    }

    public JsonModel getNetworkTargetIfName(HttpSession session, String sourceId, String sourceIfName, String targetId) throws Exception{
        List<Long> domainList = getDomainByUserSession(session);
        Long[] domains = domainList.toArray(new Long[domainList.size()]);
        Set<String> networkLinkIds = Sets.newHashSet();
        NetworkLinkModel networkLinkModel = new NetworkLinkModel();
        networkLinkModel.setNetworkLinkIds(new ArrayList<>(networkLinkIds));
        networkLinkModel.setNeIds(rpcProcessService.getNeIdsByDomainIds(domains, session));
        networkLinkModel.setSourceId(sourceId);
        networkLinkModel.setSourceIfName(sourceIfName);
        networkLinkModel.setTargetId(targetId);
        PageModel temPage = new PageModel();
        temPage.setCurrentNo(1);
        temPage.setPageSize(10000);
        PageModel pageModel = rpcProcessService.findPage(temPage, networkLinkModel);
        List<NetworkLinkModel> list = (List<NetworkLinkModel>) pageModel.getObject();
        List<Map<String, String>> result = Lists.newArrayList();
        for (NetworkLinkModel ne : list) {
            Map<String, String> map = Maps.newHashMap();
            map.put("name", ne.getTargetIfName());
            map.put("value", ne.getTargetIfName());
            if (result.contains(map)) {
                continue;
            }
            if (sourceIfName.equals(ne.getSourceIfName()) && sourceId.equals(ne.getSourceId())
                    && targetId.equals(ne.getTargetId())) {
                result.add(map);
            }
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

    /** 根据指标名称以及资源获取对应部件列表 */
    public JSONObject newResultObj(Object name, Object value){
        return MonitorUtils.newResultObj(name, value);
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