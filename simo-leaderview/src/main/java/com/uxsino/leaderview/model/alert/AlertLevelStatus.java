package com.uxsino.leaderview.model.alert;

/**
 * @description 告警等级状态枚举对象
 * 
 * @date 2017年4月13日
 */
public enum AlertLevelStatus {
                              ACTIVATED(1,"启用"),
                              DEACTIVATED(0,"停用");
    private int value;

    private String text;

    AlertLevelStatus(int value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 将传入的告警等级状态枚举值转化为枚举对象.
     * @param value 传入的告警等级状态枚举值
     * @return 告警等级状态枚举对象
     */
    public static AlertLevelStatus fromValue(int value) {
        for (AlertLevelStatus item : AlertLevelStatus.values()) {
            if (item.value == value) {
                return item;
            }
        }
        throw new IllegalArgumentException("0-1 is a range of parameter('value')");
    }

    /**
     * 将传入的告警等级状态中文显示转化为枚举对象.
     * @param value 传入的告警等级状态中文显示
     * @return 告警等级状态枚举对象
     */
    public static AlertLevelStatus fromText(String text) {
        for (AlertLevelStatus item : AlertLevelStatus.values()) {
            if (item.text.equals(text)) {
                return item;
            }
        }
        throw new IllegalArgumentException();
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return text;
    }

}
