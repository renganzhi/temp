package com.uxsino.leaderview.model.alert;

/**
 * @description 告警处理方式的枚举对象
 * 
 * @date 2017年4月13日
 */
public enum AlertHandleWay {
                            AUTOMATIC(0,"自动"),   // 如，动态变化的内存占用率，告警发生后过段时间又检测到正常值
                            SCRIPT(1,"脚本"),      // 通过自动运行脚本处理告警
                            MANUAL(2,"人工");      // 运维人员手动处理告警
    private int value;

    private String text;

    AlertHandleWay(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    /**
     * 将传入的告警处理方式枚举值转化为枚举对象
     * @param value 传入的告警处理方式枚举值
     * @return 告警处理方式枚举对象
     */
    public static AlertHandleWay fromValue(int value) {
        for (AlertHandleWay item : AlertHandleWay.values()) {
            if (item.value == value) {
                return item;
            }
        }
        throw new IllegalArgumentException("0-3 is a range of parameter('value')");
    }

    /**
     * 将传入的告警处理方式中文显示转化为枚举对象
     * @param value 传入的告警处理方式中文显示
     * @return 告警处理方式枚举对象
     */
    public static AlertHandleWay fromText(String text) {
        for (AlertHandleWay item : AlertHandleWay.values()) {
            if (item.text.equals(text)) {
                return item;
            }
        }
        throw new IllegalArgumentException();
    }

}
