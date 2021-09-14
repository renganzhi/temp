package com.uxsino.leaderview.model.datacenter;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class IndValueQuery {

    private String neId;//资源Id

    private String indicatorId;//指标

    private JSONObject fieldMustFilters;//属性筛选条件，格式：{"power_state":"poweredOn"}

    private JSONObject fieldShouldFilters;//属性筛选条件，格式：{"power_state":"poweredOn"}

    private List<String> fieldResFilter;//属性返回结果筛选，格式：["name","power_state","cpu_usage"]

    private JSONObject fieldSort;//排序,格式：{"cpu_usage": "desc"}

    private Integer fieldSize = 100;//指定返回属性条目，不同于分页参数

    private static final String fieldPrefix = "v.";

    public JSONObject generalFieldMustFilters() {
        JSONObject newFieldFilters = new JSONObject();
        if (null != fieldMustFilters && !fieldMustFilters.isEmpty()) {
            for (String key : fieldMustFilters.keySet()) {
                newFieldFilters.put(fieldPrefix + key, fieldMustFilters.get(key));
            }
        }
        return newFieldFilters;
    }

    public JSONObject generalFieldShouldFilters() {
        JSONObject newFieldFilters = new JSONObject();
        if (null != fieldShouldFilters && !fieldShouldFilters.isEmpty()) {
            for (String key : fieldShouldFilters.keySet()) {
                newFieldFilters.put(fieldPrefix + key, fieldShouldFilters.get(key));
            }
        }
        return newFieldFilters;
    }

    public List<String> generalFieldResFilter() {
        List<String> newFieldResFilters = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fieldResFilter)) {
            for (String key : fieldResFilter) {
                newFieldResFilters.add(fieldPrefix + key);
            }
        }
        return newFieldResFilters;
    }

    public JSONObject generalFieldSort() {
        JSONObject newFieldSort = new JSONObject();
        if (null != fieldSort && !fieldSort.isEmpty()) {
            for (String key : fieldSort.keySet()) {
                newFieldSort.put(fieldPrefix + key, fieldSort.get(key));
            }
        }
        return newFieldSort;
    }
}
