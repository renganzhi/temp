package com.uxsino.leaderview.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.uxsino.commons.utils.ArithUtils;
import com.uxsino.commons.utils.JSONUtils;
import com.uxsino.commons.utils.TimeUtils;
import com.uxsino.leaderview.model.monitor.IndicatorVal;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IndicatorValueUtils {

    private static Logger logger = LoggerFactory.getLogger(IndicatorValueUtils.class);

    public Double getMeanValue(JSON indicatorValue, String field) {
        Double fieldMean = 0D;
        DecimalFormat df = new DecimalFormat("#.00");
        if (indicatorValue instanceof JSONObject) {
            fieldMean = Double.valueOf(df.format(JSONUtils.getDoubleValue(((JSONObject) indicatorValue), field)));
        } else if (indicatorValue instanceof JSONArray) {
            Double fieldSum = 0D;
            Double freeSum = 0D;
            JSONArray indicatorValueArray = (JSONArray) indicatorValue;
            if (indicatorValueArray != null && !indicatorValueArray.isEmpty() && indicatorValueArray.size() > 0) {
                if ("memory_usage".equalsIgnoreCase(field)) {
                    for (int i = 0; i < indicatorValueArray.size(); i++) {
                        JSONObject memory = indicatorValueArray.getJSONObject(i);
                        if (memory.containsKey("memory_type") && JSONUtils.getIntValue(memory, "memory_type") > 3) {
                            continue;
                        }
                        if (memory.containsKey("memory_name")
                                && (memory.getString("memory_name").equalsIgnoreCase("mem:")
                                        || memory.getString("memory_name").equalsIgnoreCase("Physical memory"))) {
                            double usage = JSONUtils.getDouble(memory, "memory_total") == null ? 0
                                    : (JSONUtils.getDoubleValue(memory, "memory_used")
                                            / JSONUtils.getDoubleValue(memory, "memory_total"));
                            fieldMean = Double.valueOf(df.format(JSONUtils.getDouble(memory, "memory_usage") != null
                                    ? memory.getDouble("memory_usage") : usage));
                            return fieldMean;
                        }
                        if (!(memory.get("memory_total") instanceof Number)
                                || !((memory.get("memory_free") instanceof Number))) {
                            continue;
                        }
                        fieldSum += JSONUtils.getDoubleValue(memory, "memory_total");
                        freeSum += JSONUtils.getDoubleValue(memory, "memory_free");
                    }
                    if (!Double.valueOf(0).equals(fieldSum)) {
                        fieldMean = (fieldSum - freeSum) / fieldSum;
                        fieldMean = Double.valueOf(df.format(fieldMean));
                    }
                } else {
                    for (int i = 0; i < indicatorValueArray.size(); i++) {
                        fieldSum += JSONUtils.getDoubleValue(indicatorValueArray.getJSONObject(i), field);
                    }
                    fieldMean = Double.valueOf(df.format(fieldSum / indicatorValueArray.size()));
                }
            }
        }
        return fieldMean;
    }

    public JSONObject getMemAvg(JSON value) {
        JSONObject memoryObj = new JSONObject();
        if (value instanceof JSONObject) {
            try {
                if (((JSONObject) value).containsKey("memory_usage")) {
                    memoryObj.put("memory_avg", ((JSONObject) value).getDouble("memory_usage"));
                } else {
                    memoryObj.put("memory_avg", ((JSONObject) value).getDouble("result"));
                }
            } catch (Exception e) {
                memoryObj.put("memory_avg", 0);
            }
        } else if (value instanceof JSONArray) {
            JSONArray memoryArray = (JSONArray) value;
            if (memoryArray != null && !memoryArray.isEmpty()) {
                for (int i = 0; i < memoryArray.size(); i++) {
                    JSONObject memory = memoryArray.getJSONObject(i);
                    if (StringUtils.isNoneBlank(memory.getString("memory_usage"))) {
                        try {
                            memoryObj.put("memory_avg", memory.getDouble("memory_usage"));
                        } catch (Exception e) {
                            memoryObj.put("memory_avg", 0);
                        }
                        break;
                    }
                }
            }
        }
        return memoryObj;
    }

    public boolean checkUptime(String uptime) {
        if (StringUtils.isEmpty(uptime)) {
            return false;
        }
        String pat = "([0-9]{14})\\.[0-9]{6}\\+\\d+";
        Pattern pattern = Pattern.compile(pat);
        Matcher m = pattern.matcher(uptime);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public String transferSysUptime(String uptime) {
        if (StringUtils.isEmpty(uptime)) {
            return null;
        }
        String pat = "([0-9]{14})\\.[0-9]{6}\\+\\d+";
        Pattern pattern = Pattern.compile(pat);
        Matcher m = pattern.matcher(uptime);

        if (m.find()) {
            String timestr = m.group(1);
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                Date startDate = format.parse(timestr);
                // logger.info("start date: {}", startDate);
                Date curDate = Calendar.getInstance().getTime();
                long diffSec = (curDate.getTime() - startDate.getTime()) / 1000L;
                int day = (int) (diffSec / (24 * 3600));
                long rest = diffSec % (24 * 3600);
                int hour = (int) (rest / 3600);
                rest = rest % 3600;
                int minute = (int) (rest / 60);
                int second = (int) (rest % 60);
                StringBuilder sb = new StringBuilder();
                if (day > 0) {
                    sb.append(day + " day");
                    if (day > 1) {
                        sb.append("s");
                    }
                    sb.append(" ");
                }
                sb.append(hour + ":" + minute + ":" + second);
                return sb.toString();
            } catch (ParseException e) {
                logger.error("error parse startTime to uptime", e);
            }

        }
        return null;
    }

    public String transferNumUptime1(long uptime) {
        String runtime = "";
        long day = uptime / (1000 * 60 * 60 * 24);
        long hour = uptime % (1000 * 60 * 60 * 24) / (1000 * 60 * 60);
        long minute = uptime % (1000 * 60 * 60 * 24) % (1000 * 60 * 60) / (1000 * 60);
        runtime = day + "天  " + hour + "小时  " + minute + " 分钟";
        System.out.println(runtime);
        return runtime;
    }

    /**
     *
     * @param start_time 开始运行的时间
     * @param end_time  当前时间
     * @return 返回部件和主机的运行时间
     */
    public String transferNumUptime(String start_time, String end_time) {
        String run_time_str = "";
        long run_time_long = 0;
        long day = 0;
        long hour = 0;
        long minute = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date begin_date = df.parse(start_time);
            Date end_date = df.parse(end_time);
            run_time_long = end_date.getTime() - begin_date.getTime();
            System.out.println(run_time_long);
            day = run_time_long / (1000 * 60 * 60 * 24);
            hour = run_time_long % (1000 * 60 * 60 * 24) / (1000 * 60 * 60);
            minute = run_time_long % (1000 * 60 * 60 * 24) % (1000 * 60 * 60) / (1000 * 60);
            run_time_str = day + "天  " + hour + "小时  " + minute + " 分钟";
        } catch (ParseException e) {
            logger.error("Error time format", e);
        }
        return run_time_str;
    }

    public void setAixDiskUsageIndValue(IndicatorVal indValue) {
        JSONArray ori = (JSONArray) indValue.getIndicatorValue();
        JSONArray changed = new JSONArray();
        for (Object obj : ori) {
            JSONObject oriObj = (JSONObject) obj;
            JSONObject curObj = new JSONObject();
            changed.add(curObj);
            curObj.put("disk_name", oriObj.getString("fs_name"));
            if (oriObj.containsKey("fs_usage") && (oriObj.get("fs_usage") instanceof Number)) {
                curObj.put("disk_usage", JSONUtils.getDoubleValue(oriObj, "fs_usage"));
            }
            if (oriObj.containsKey("fs_used_size") && (oriObj.get("fs_used_size") instanceof Number)) {
                curObj.put("disk_used", JSONUtils.getDoubleValue(oriObj, "fs_used_size") * 1024.0);
            }
            if (oriObj.containsKey("fs_size") && (oriObj.get("fs_size") instanceof Number)) {
                curObj.put("disk_total", JSONUtils.getDoubleValue(oriObj, "fs_size") * 1024.0);
            }
            if (oriObj.containsKey("fs_free_size") && (oriObj.get("fs_free_size") instanceof Number)) {
                curObj.put("disk_free", JSONUtils.getDoubleValue(oriObj, "fs_free_size") * 1024.0);
            }
        }
        indValue.setIndicatorValue(changed);

    }

    public JSONArray removeNullDiskItem(JSONArray jsonArr) {
        JSONArray changed = new JSONArray();
        for (Object obj : jsonArr) {
            JSONObject jobj = (JSONObject) obj;
            if (!jobj.containsKey("disk_name")) {
                continue;
            }
            if (!jobj.containsKey("disk_usage") || !(jobj.get("disk_usage") instanceof Number)) {
                continue;
            }
            if (!jobj.containsKey("disk_total") || !(jobj.get("disk_total") instanceof Number)) {
                continue;
            }
            if (!jobj.containsKey("disk_used") || !(jobj.get("disk_used") instanceof Number)) {
                continue;
            }
            if (!jobj.containsKey("disk_free") || !(jobj.get("disk_free") instanceof Number)) {
                continue;
            }
            changed.add(obj);
        }
        return changed;
    }

    /**
     * 单位转换，现包括百分数取小数点两位和容量转换
     */
    public JSONObject transferItem(JSONObject fieldLabel, JSONObject valueJSON) {
        if (valueJSON == null) {
            valueJSON = new JSONObject();
        }
        String fieldName = fieldLabel.getString("name");
        if (!fieldLabel.containsKey("unit")) {
            keepTwoDecimals(fieldLabel, valueJSON, "");
            return valueJSON;
        }
        String unit = fieldLabel.getString("unit");
        if ("byte".equals(unit) || "B".equals(unit) || "KB".equals(unit) || "MB".equals(unit) || "GB".equals(unit)
                || "8KB".equals(unit) || "16MB".equals(unit) || "2KB".equals(unit) || "TB".equals(unit)) {
            if (!StringUtils.isNotBlank(valueJSON.getString(fieldName))) {
                valueJSON.put(fieldName, null);
                return valueJSON;
            }
            double bytes = 0;
            try {
                bytes = valueJSON.getDoubleValue(fieldName);
            } catch (Exception e) {
                logger.error("JSONObject.getDoubleValue(\"" + fieldName + "\") -> " + valueJSON.get(fieldName));
                valueJSON.put(fieldName, null);
                return valueJSON;
            }
            // sqlServer size_8kb
            if ("8KB".equals(unit)) {
                bytes *= 8;
                fieldLabel.put("unit", "KB");
            }
            if ("16MB".equals(unit)) {
                bytes *= 16;
                fieldLabel.put("unit", "MB");
            }
            if ("2KB".equals(unit)) {
                bytes *= 2;
                fieldLabel.put("unit", "KB");
            }
            if (bytes >= 0 && valueJSON.containsKey(fieldName)) {
                double k = 1024;
                String[] sizes = { "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
                double d = bytes == 0 ? 0 : Math.floor(Math.log(bytes) / Math.log(k));
                unit = fieldLabel.getString("unit");
                if (d >= 0) {
                    unit = sizes[(int) d];
                    for (int i = 1; i < sizes.length; i++) {
                        if (fieldLabel.getString("unit").equals(sizes[i])) {
                            unit = sizes[(int) d + i];
                            break;
                        }
                    }
                }
                if (bytes == 0) {
                    valueJSON.put(fieldName, 0 + " " + unit);
                } else if (d < 0) {
                    valueJSON.put(fieldName, ArithUtils.div(bytes, 1, 2) + " " + unit);
                } else {
                    double valueD = ArithUtils.div(bytes, Math.pow(k, d), 2);
                    if ((long) valueD == valueD) {
                        // 若是整数，直接返回
                        valueJSON.put(fieldName, (long) valueD + " " + unit);
                    } else {
                        valueJSON.put(fieldName, valueD + " " + unit);
                    }
                }
            } else if (!valueJSON.containsKey(fieldName)) {
                valueJSON.put(fieldName, null);
            }
        }
        if ("MBPS".equalsIgnoreCase(unit) || "KBPS".equalsIgnoreCase(unit)) {
            if (StringUtils.isEmpty(valueJSON.getString(fieldName))) {
                valueJSON.put(fieldName, null);
            } else {
                valueJSON.put(fieldName, valueJSON.getString(fieldName) + " " + unit);
            }
        }
        if ("%".equals(unit) || "B/request".equals(unit) || "byte/s".equals(unit) || "KB/sec".equals(unit)
                || "KB/s".equals(unit) || "kbs".equals(unit) || "MHz".equals(unit) || "B/sec".equals(unit)) {
            return keepTwoDecimals(fieldLabel, valueJSON, unit);
        }
        // ibmmq的时间相关属性为“12.10.00”格式时，控制成“12:10:00”这种时间格式
        if ("time".equals(unit)) {
            if (StringUtils.isNotBlank(valueJSON.getString(fieldName))) {
                valueJSON.put(fieldName, valueJSON.getString(fieldName).replace(".", ":"));
            } else {
                valueJSON.put(fieldName, null);
            }
        }
        // 日期为“yyyy-MM-dd hh:mm:ss.ffffff”格式时，控制成“yyyy-MM-dd hh:mm:ss”这种时间格式
        if ("date".equals(unit)) {
            String valueStr = valueJSON.getString(fieldName);
            if (StringUtils.isNotBlank(valueStr)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                try {
                    Date valueDate = sdf.parse(valueStr);
                    valueJSON.put(fieldName, sdf.format(valueDate));
                } catch (ParseException e) {
                    logger.error("transferItem方法转换日期格式错误：待转换值：{}", valueStr);
                }
            } else {
                valueJSON.put(fieldName, null);
            }
        }
        // ibmmq的通道状态没有值时，控制成“INACTIVE”
        if ("channelStatus".equals(unit)) {
            if (StringUtils.isEmpty(valueJSON.getString(fieldName))) {
                valueJSON.put(fieldName, "INACTIVE");
            }
        }
        if ("页".equals(unit) || "page".equals(unit) || "page/s".equals(unit)) {
            if (JSONUtils.getInteger(valueJSON, fieldName) != null) {
                valueJSON.put(fieldName, JSONUtils.getIntValue(valueJSON, fieldName) + " " + unit);
            } else {
                valueJSON.put(fieldName, null);
            }
        }
        // Unix 10位时间戳（秒）日期时间转换
        if ("unixTime".equals(unit)) {
            if (JSONUtils.getLongValue(valueJSON, fieldName) >= 0) {
                valueJSON.put(fieldName,
                    TimeUtils.formatTime(new Date(JSONUtils.getLongValue(valueJSON, fieldName) * 1000)));
            } else {
                valueJSON.put(fieldName, null);
            }
        }
        // Unix 13位时间戳（毫秒）日期时间转换
        if ("unixTimeMilli".equals(unit)) {
            if (JSONUtils.getLongValue(valueJSON, fieldName) >= 0) {
                valueJSON.put(fieldName, TimeUtils.formatTime(new Date(JSONUtils.getLongValue(valueJSON, fieldName))));
            } else {
                valueJSON.put(fieldName, null);
            }
        }
        if ("second".equals(unit) || "ms".equals(unit) || "microsecond".equals(unit) || "ns".equals(unit)
                || "sec".equals(unit) || "day".equals(unit) || "cs".equals(unit)) {
            String value = valueJSON.getString(fieldName);
            if (value == null || "NaN".equals(value)) {
                valueJSON.put(fieldName, null);
            } else {
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
                //long t = JSONUtils.getLongValue(valueJSON, fieldName);
                /*
                 * 原方法使用JSONUtils取值时若指标数据包含小数点后两位则转换时会报错，
                 * 现手动处理数据取值
                 * */
                String string = valueJSON.getString(fieldName);
                long t;
                if (string.contains(".")) {
                    t = Long.valueOf(string.substring(0, string.indexOf(".")));
                } else {
                    t = Long.valueOf(string);
                }
                //**************************************************************
                List<String> arr = Arrays.asList("天", "时", "分", "秒", "厘秒", "毫秒", "微秒", "纳秒");
                int[] gap = { 24, 60, 60, 100, 10, 1000, 1000 };
                String[] time = { null, null, null, null, null, null, null, null };
                int index = arr.indexOf(unit);
                if (t == 0) {
                    valueJSON.put(fieldName, t + " " + unit);
                } else {
                    for (int i = index; i >= 0; i--) {
                        if (t <= 0) {
                            break;
                        }
                        if (i == 0) {
                            time[0] = t + " " + arr.get(i);
                            break;
                        }
                        time[i] = t % gap[i - 1] + " " + arr.get(i);
                        t = (long) Math.floor(t / gap[i - 1]);
                    }
                    String timeStr = "";
                    for (String str : time) {
                        if (str == null) {
                            continue;
                        }
                        timeStr += str;
                    }
                    valueJSON.put(fieldName, StringUtils.isEmpty(timeStr) ? null : timeStr);
                }
            }
        }
        return valueJSON;
    }

    /**
     * 根据指标调用单位换算
     * @param indicatorID
     * @param indicatorValueJSON
     * @return
     */
    public JSON transferIndValue(String indicatorID, JSON indicatorValueJSON, JSONArray fieldLabelArray) {
        if (fieldLabelArray != null && fieldLabelArray.size() > 0) {
            for (int i = 0; i < fieldLabelArray.size(); i++) {
                JSONObject fieldLabel = fieldLabelArray.getJSONObject(i);
                transferValue(indicatorID, fieldLabel, indicatorValueJSON);
            }
            fieldLabelArray = JSON.parseArray(JSON.toJSONString(
                fieldLabelArray.stream().filter(fieldLabel -> !((JSONObject) fieldLabel).containsKey("withoutrule"))
                    .collect(Collectors.toList())));
        }
        return indicatorValueJSON;
    }

    /**
     * 单位转换
     * @param fieldLabel
     * @param valueJSON
     * @return
     */
    public JSON transferValue(String indicatorID, JSONObject fieldLabel, JSON valueJSON) {
        String fieldName = fieldLabel.getString("name");
        // 枚举原指标的desc
        if (valueJSON != null) {
            if (valueJSON instanceof JSONArray) {
                if (!((JSONArray) valueJSON).isEmpty()) {
                    JSONArray valueArray = (JSONArray) valueJSON;
                    for (int i = 0; i < valueArray.size(); i++) {
                        JSONObject valueJson = valueArray.getJSONObject(i);
                        valueJson = transferItem(fieldLabel, valueJson);
                        if (fieldLabel.containsKey("desc")) {
                            value2Text(indicatorID, fieldLabel.getJSONObject("desc"), fieldName, valueJson);
                        }
                    }
                }
            } else {
                JSONObject valueJson = (JSONObject) valueJSON;
                valueJson = transferItem(fieldLabel, valueJson);
                if (fieldLabel.containsKey("desc")) {
                    value2Text(indicatorID, fieldLabel.getJSONObject("desc"), fieldName, valueJson);
                }
            }
        }
        return valueJSON;
    }

    /**
     * 采集到的数据中的特殊值转成对应的含义
     * @param desc 取值映射jsonObject
     * @param fieldName 取值filed
     * @param valueJSON 待转换数据源
     * @return
     */
    public void value2Text(String indicatorID, JSONObject desc, String fieldName, JSONObject valueJSON) {
        if (!desc.isEmpty()) {
            String fieldValue = valueJSON.getString(fieldName);
            if ("sybase_device".equals(indicatorID) && "status".equals(fieldName)) {
                Set<String> contentKeys = new HashSet<String>();
                Set<String> content = new HashSet<String>();
                try {
                    contentKeys = sumForContent(JSONUtils.getIntValue(valueJSON, fieldName), contentKeys);
                    contentKeys.forEach(contentKey -> {
                        content.add(desc.getString(contentKey));
                    });
                    valueJSON.put(fieldName, StringUtils.join(content.toArray(), ","));
                } catch (Exception e) {
                    logger.error("transferValue2Content方法转换sybase_device指标的status属性错误,status{}：", fieldValue, e);
                }
            } else {
                if (desc.containsKey(fieldValue)) {
                    valueJSON.put(fieldName, desc.get(fieldValue));
                }
            }
        }
    }

    /*
     * 求value是由哪些2的次方之和所得
     */
    public Set<String> sumForContent(int value, Set<String> result) {
        int product = 1;
        while (true) {
            product *= 2;
            if (product > value) {
                product = product / 2;
                result.add(String.valueOf(product));
                break;
            }
        }
        if ((value - product) > 0) {
            sumForContent((value - product), result);
        }
        return result;
    }

    /**
     * 排序处理
     * @param indicatorValue
     * @param sortFieldJson
     * @param sortAsc
     */
    public void sortValue(JSONArray indicatorValue, JSONObject sortFieldJson, boolean sortAsc) {
        if (sortFieldJson == null || sortFieldJson.isEmpty()) {
            return;
        }
        String sortField = sortFieldJson.getString("name");
        indicatorValue.sort((a, b) -> {
            JSONObject itma = JSON.parseObject(JSON.toJSONString(a));
            JSONObject itmb = JSON.parseObject(JSON.toJSONString(b));
            String aStr = itma.getString(sortField);
            String bStr = itmb.getString(sortField);
            if (StringUtils.isEmpty(aStr) && StringUtils.isEmpty(bStr)) {
                return 0;
            }
            if (StringUtils.isEmpty(aStr)) {
                return sortAsc ? 1 : -1;
            }
            if (StringUtils.isEmpty(bStr)) {
                return sortAsc ? -1 : 1;
            }
            if ("NUMBER".equals(sortFieldJson.getString("type"))) {
                Float va = Float.valueOf(aStr);
                Float vb = Float.valueOf(bStr);
                return sortAsc ? va.compareTo(vb) : vb.compareTo(va);
            } else {
                return sortAsc ? aStr.compareTo(bStr) : bStr.compareTo(aStr);
            }
        });
    }

    public JSONObject getFieldJsonByFieldName(JSONArray fieldLabelArray, String fieldName) {
        JSONObject fieldJson = new JSONObject();
        if (StringUtils.isNotBlank(fieldName) && fieldLabelArray != null && fieldLabelArray.size() > 0) {
            for (int i = 0; i < fieldLabelArray.size(); i++) {
                JSONObject fieldLabel = fieldLabelArray.getJSONObject(i);
                if (fieldName.equals(fieldLabel.getString("name"))) {
                    fieldJson = fieldLabel;
                    break;
                }
            }
        }
        return fieldJson;
    }

    /**
     * 检索
     * @param indicatorValue 待检索数据
     * @param key 检索字段
     * @param search 值
     */
    public JSONArray searchValue(JSONArray indicatorValue, String key, String search) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < indicatorValue.size(); i++) {
            JSONObject value = indicatorValue.getJSONObject(i);
            if (value.getString(key) != null && value.getString(key).contains(search)) {
                result.add(value);
            }
        }
        return result;
    }

    public JSONObject changeInfoToJSONArrayPage(String vmId, JSONArray indicatorValue, String fetchDate, int pageNo,
        int pageSize) {
        JSONArray result = new JSONArray();
        JSONObject json = new JSONObject();
        if (indicatorValue == null || indicatorValue.size() <= 0) {
            return null;
        }
        indicatorValue.removeIf(ele -> (JSONObject) ele == null || ((JSONObject) ele).isEmpty());
        for (int i = (pageNo - 1) * pageSize; i < pageNo * pageSize; i++) {
            if (i < indicatorValue.size()) {
                JSONObject oneRecord = new JSONObject();
                oneRecord = indicatorValue.getJSONObject(i);
                oneRecord.put("fetchDate", fetchDate);
                result.add(oneRecord);
            }
        }
        json.put("result", result);
        json.put("size", indicatorValue.size());
        return json;
    }

    public JSONObject keepTwoDecimals(JSONObject fieldLabel, JSONObject valueJSON, String unit) {
        String fieldName = fieldLabel.getString("name");
        String fieldType = fieldLabel.containsKey("type") ? fieldLabel.getString("type")
                : fieldLabel.getString("fieldType");
        if (!valueJSON.containsKey(fieldName)) {
            valueJSON.put(fieldName, valueJSON.get("result"));
            if (StringUtils.isEmpty(valueJSON.getString(fieldName))) {
                return valueJSON;
            }
        }
        if ("STRING".equalsIgnoreCase(fieldType) || "BOOLEAN".equalsIgnoreCase(fieldType)) {
            return valueJSON;
        }
        try {
            double valueD = Double
                .valueOf(valueJSON.getString(fieldName))/*JSONUtils.getDoubleValue(valueJSON, fieldName)*/;
            if ((long) valueD == valueD || 0 == valueD) {
                valueJSON.put(fieldName, (long) valueD + (StringUtils.isEmpty(unit) ? "" : " ") + unit);
            } else {
                valueJSON.put(fieldName, ArithUtils.div(valueD, 1, 2) + (StringUtils.isEmpty(unit) ? "" : " ") + unit);
            }
            return valueJSON;
        } catch (Exception e) {
            if ("NUMBER".equalsIgnoreCase(fieldType) || "PERCENT".equalsIgnoreCase(fieldType)) {// 字符串的Number也要能转换
                valueJSON.put(fieldName, null);
            }
            return valueJSON;
        }
    }
    public Map<String, List<Double>> getIndValGroup(JSONObject vv, String key,
            Map<String, List<Double>> map,String feild) {
        if(vv.containsKey(feild)&& StringUtils.isNotBlank(vv.getString(feild))){
            Double value =0.00;
            try {
                value=new Double(Double.parseDouble(vv.getString(feild)));
            }catch (Exception e){
                logger.error("字符串转Double失败：{}",value);
            }
            if(map.containsKey(key)){
                List<Double> values = map.get(key);
                values.add(value);
                map.put(key,values);
            }else {
                map.put(key, Lists.newArrayList(value));
            }
        }
        return map;
    }

    public String  calcuateIndValAvg(List<Double> vals) {
        if(vals.isEmpty()||vals==null){
            return null;
        }
        Double sum=0.0;
        for(Double num:vals){
            sum+=num;
        }
        Double avg=sum/vals.size();
        String format = String.format("%.2f", avg);
        return format;
    }
}
