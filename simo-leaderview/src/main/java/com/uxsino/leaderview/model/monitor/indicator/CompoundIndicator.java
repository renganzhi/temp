package com.uxsino.leaderview.model.monitor.indicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CompoundIndicator {
    CPU("CPU利用率", "network_cpu,cpu_usage_avg", "used_cpu_usage"),
    Memory("内存利用率", "network_memory,physical_memory", "memory_usage");

    private String text;

    private String indicators;//多个，没有做部件相关逻辑处理，所以不支持list指标

    private String field;//单个，多个指标的交集属性

    CompoundIndicator(String text, String indicators, String field) {
        this.text = text;
        this.indicators = indicators;
        this.field = field;
    }

    public String getText() {
        return text;
    }

    public List<String> getIndicators() {
        return Arrays.asList(indicators.split(","));
    }

    public String getField() {
        return field;
    }

    public static List<String> valueList(){
        List<String> values = new ArrayList<>();
        for (CompoundIndicator value : values()) {
            values.add(value.toString());
        }
        return values;
    }
}
