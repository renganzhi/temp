package com.uxsino.leaderview.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.leaderview.model.monitor.IndicatorTable;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MonitorUtils {
    /** 对JSONArray进行遍历，根据predicate过滤 */
    public static JSONArray filter(JSONArray array, Predicate<? super JSONObject> predicate){
        List list = StreamSupport.stream(array.spliterator(),false).map(o -> (JSONObject) o).filter(predicate).collect(Collectors.toList());
        return JSON.parseArray(JSON.toJSONString(list));
    }

    /** 对JSONArray进行遍历，执行操作action */
    public static void action(JSONArray array,Consumer<? super JSONObject> action){
        StreamSupport.stream(array.spliterator(), false).map(o -> (JSONObject) o).forEach(action);
    }

    public static <T> List<T> filterToList(List<T> list, Predicate<? super T> predicate){
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    public static JSONObject newResultObj(Object name, Object value){
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("value", value);
        return obj;
    }

    public static JSONObject newResultObj(String nameStr, Object name, String valueStr ,Object value){
        JSONObject obj = new JSONObject();
        obj.put(nameStr, name);
        obj.put(valueStr, value);
        return obj;
    }


    /**
     *  判断某一指标是否应该存在部件
     */
    public static boolean validHasFields(IndicatorTable ind) {
        // 只要是NUMBER类型、PERCENT类型或者STRING类型的指标，都没有属性
        return !"NUMBER".equals(ind.getIndicatorType()) && !"PERCENT".equals(ind.getIndicatorType())
                && !"STRING".equals(ind.getIndicatorType());
    }

    /**
     * 构造vCharts的空对象
     */
    public static JSONObject empObj() {
        JSONObject obj = new JSONObject();
        JSONArray emp = new JSONArray();
        obj.put("columns", emp);
        obj.put("rows", emp);
        return obj;
    }

    /** Boolean类型参数存在性判断，默认为false */
    public static Boolean existJudgment(Boolean value){
        return existJudgment(value, false);
    }

    public static Boolean existJudgment(Boolean value ,Boolean defaultValue){
        return ObjectUtils.isEmpty(value) ? defaultValue : value;
    }
}
