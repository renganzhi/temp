package com.uxsino.leaderview.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.uxsino.leaderview.model.monitor.IndicatorTable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.text.DecimalFormat;
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
     *  判断某一指标是否有多个属性值
     */
    public static boolean validHasFields(IndicatorTable ind) {
        // 只要是NUMBER类型、PERCENT类型或者STRING类型的指标，都没有属性，为单值指标
        return !"NUMBER".equals(ind.getIndicatorType()) && !"PERCENT".equals(ind.getIndicatorType())
                && !"STRING".equals(ind.getIndicatorType());
    }

    /**
     *  判断某一指标是否为部件指标
     */
    public static boolean validHasComponent(IndicatorTable ind) {
        return "List".equals(ind.getIndicatorType()) && StringUtils.isNotBlank(ind.getComponentNameFormula());
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

    public static JSONObject HistoryUnitTransfer(JSONArray array, String unit){
        return unitTransfer(array, unit, "value");
    }


    public static JSONObject unitTransfer(JSONArray array, String unit, String valueKey) {
        return unitTransfer(array, unit, Lists.newArrayList(valueKey));
    }

    public static JSONObject unitTransfer(JSONArray array, String unit, List<String> valueKey) {
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
            String valueStr = obj.getString(getValueKey(obj, valueKey));
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
                    String valueStr = obj.getString(getValueKey(obj, valueKey));
                    if (ObjectUtils.isEmpty(valueStr)) {
                        result.add(obj);
                    } else {
                        Double objValue = getValueDob(valueStr);
                        if ("秒".equals(unit)) {
                            objValue = objValue / 100D;
                        } else {

                        }
                        obj.put(getValueKey(obj, valueKey), getValueStr(objValue));
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
                    String valueStr = obj.getString(getValueKey(obj, valueKey));
                    if (ObjectUtils.isEmpty(valueStr)) {
                        result.add(obj);
                    } else {
                        Double objValue = getValueDob(valueStr);
                        if ("TB".equals(unit)) {
                            objValue = objValue / (1024 * 1024 * 1024 * 1024D);
                        } else if ("GB".equals(unit)) {
                            objValue = objValue / (1024 * 1024 * 1024D);
                        } else if ("MB".equals(unit)) {
                            objValue = objValue / (1024 * 1024D);
                        } else {
                            objValue = objValue / (1024D);
                        }
                        obj.put(getValueKey(obj, valueKey), getValueStr(objValue));
                        result.add(obj);
                    }
                }
                break;
            }
            default:{
                break;
            }
        }
        re.put("result", ObjectUtils.isEmpty(result) ? array : result);
        re.put("unit", unit);
        return re;
    }

    public static String getValueKey(JSONObject obj, List<String> fields){
        String result = null;
        for (String field: fields) {
            if (!ObjectUtils.isEmpty(obj.getString(field))){
                result =  field;
            }
        }
        return result;
    }

    public static String getValueStr(String valueStr){
        if (ObjectUtils.isEmpty(valueStr)) {
            return valueStr;
        }else if (valueStr.contains(".") && !valueStr.contains("E")) {
            valueStr = valueStr.substring(0, valueStr.indexOf(".") + 2);
        }else if (valueStr.contains("E")){
            valueStr = new DecimalFormat("0.0").format(Double.parseDouble(valueStr));
        }
        return valueStr;
    }

    public static String getValueStr(Double value){
        return new DecimalFormat("0.0").format(value);
    }

    public static Double getValueDob(String valueStr){
        valueStr = getValueStr(valueStr);
        Double objValue = null;
        if (!Strings.isNullOrEmpty(valueStr)){
            objValue = Double.parseDouble(valueStr);
        }
        return objValue;
    }


    public static JSONObject splitUnitValue(String valueStr){
        return splitUnitValue(valueStr, "");
    }

    public static JSONObject splitUnitValue(String valueStr, String nullValue){
        String[] str = valueStr.split(" ");
        String value = null;
        String unit = "";
        if (str.length == 2){
            value = Strings.isNullOrEmpty(str[0]) ? nullValue : str[0];
            unit = str[1];
        }else if (str.length == 1){
            value = Strings.isNullOrEmpty(str[0]) ? nullValue : str[0];
        }
        JSONObject result = new JSONObject();
        result.put("value", value);
        result.put("unit", unit);
        return result;
    }

    public static JSONObject getValueJSON(JSON indicatorValues){
        return getValueJSON(indicatorValues, null);
    }

    public static JSONObject getValueJSON(JSON indicatorValues, String componentName) {
        //TODO 这里可以根据indicatorValues中的值是否包含cpu_id字段来对cpu_usage_core指标进行特殊处理

        JSONObject valueJSON = new JSONObject();
        // indicatorValues的类型可能是JSONArray,也可能是JSONObject
        if (indicatorValues instanceof JSONArray) {
            if (!StringUtils.isEmpty(componentName)) {
                JSONArray indicatorValuesArray = (JSONArray) indicatorValues;
                for (int k = 0; k < indicatorValuesArray.size(); k++) {
                    if (indicatorValuesArray.getJSONObject(k).getString("identifier").equals(componentName)) {
                        valueJSON = indicatorValuesArray.getJSONObject(k);
                        break;
                    }
                }
                valueJSON = ObjectUtils.isEmpty(valueJSON) ? indicatorValuesArray.getJSONObject(0) : valueJSON;
            }else {
                valueJSON = ((JSONArray) indicatorValues).getJSONObject(0);
            }
        } else if (indicatorValues instanceof JSONObject) {
            valueJSON = (JSONObject) indicatorValues;
        }
        return valueJSON;
    }
}
