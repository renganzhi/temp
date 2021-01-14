package com.uxsino.leaderview.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.uxsino.leaderview.model.monitor.IndicatorTable;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
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

    public static JSONObject HistoryUnitTransfer(JSONArray array, String unit) {
        JSONObject re = newResultObj("result", array, "unit", unit);
        if (ObjectUtils.isEmpty(array) || Strings.isNullOrEmpty(unit)) {
            return re;
        }
        String name = array.getJSONObject(0).getString("name");
        Double value = 0D;
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
//            if (!unit.equals(obj.getString("unit"))) {
//                continue;
//            }
            String valueStr = obj.getString("value");
            if (Strings.isNullOrEmpty(valueStr)) {
                continue;
            }
            if (valueStr.contains(".") && !valueStr.contains("E")) {
                valueStr = valueStr.substring(0, valueStr.indexOf(".") + 2);
            }
            Double objValue = Double.parseDouble(valueStr);
            value = (objValue > value) ? objValue : value;
        }
        if (value == 0D) {
            return re;
        }
        JSONArray result = new JSONArray();
        switch (unit){
            case "cs": {
                unit = "厘秒";
                if (value / 100D > 1) {
                    unit = "秒";
                }
                re.put("unit",unit);
                List<String> arr = Arrays.asList("天", "时", "分", "秒", "厘秒", "毫秒", "微秒", "纳秒");
                int[] gap = { 24, 60, 60, 100, 10, 1000, 1000 };
                String[] time = { null, null, null, null, null, null, null, null };
                int index = arr.indexOf(unit);
                // TODO 暂时只有厘秒和秒做转换
                for (int i = 0; i < array.size(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String valueStr = obj.getString("value");
                    if (ObjectUtils.isEmpty(valueStr)) {
                        result.add(obj);
                    } else {
                        Double objValue = getValueDob(valueStr);
                        if ("秒".equals(unit)) {
                            obj.put("value", objValue / 100D);
                        } else {
                            obj.put("value", objValue);
                        }
                    }
                }
                break;
            }
            case "byte":{
                if (value / (1024 * 1024 * 1024 * 1024D) >= 1) {
                    unit = "TB";
                } else if (value / (1024 * 1024 * 1024D) >= 1) {
                    unit = "GB";
                } else if (value / (1024 * 1024D) >= 1) {
                    unit = "MB";
                } else if (value / 1024D >= 1) {
                    unit = "KB";
                } else {
                    return re;
                }
                re.put("unit",unit);
                for (int i = 0; i < array.size(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String valueStr = obj.getString("value");
                    if (ObjectUtils.isEmpty(valueStr)) {
                        result.add(obj);
                    } else {
                        Double objValue = getValueDob(valueStr);
                        if ("TB".equals(unit)) {
                            obj.put("value", objValue / (1024 * 1024 * 1024 * 1024D));
                            result.add(obj);
                        } else if ("GB".equals(unit)) {
                            obj.put("value", objValue / (1024 * 1024 * 1024D));
                            result.add(obj);
                        } else if ("MB".equals(unit)) {
                            obj.put("value", objValue / (1024 * 1024D));
                            result.add(obj);
                        } else {
                            obj.put("value", objValue / (1024D));
                            result.add(obj);
                        }
                    }
                }
                break;
            }
            default:{
                break;
            }
        }
        re.put("result", ObjectUtils.isEmpty(result) ? array : result);
        return re;
    }

    public static String getValueStr(String valueStr){
        if (ObjectUtils.isEmpty(valueStr)) {
            return valueStr;
        }else if (valueStr.contains(".") && !valueStr.contains("E")) {
            valueStr = valueStr.substring(0, valueStr.indexOf(".") + 2);
        }
        return valueStr;
    }

    public static Double getValueDob(String valueStr){
        valueStr = getValueStr(valueStr);
        Double objValue = null;
        if (!Strings.isNullOrEmpty(valueStr)){
            objValue = Double.parseDouble(valueStr);
        }
        return objValue;
    }
}
