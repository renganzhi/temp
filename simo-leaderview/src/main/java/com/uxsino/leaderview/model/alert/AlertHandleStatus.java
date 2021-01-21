package com.uxsino.leaderview.model.alert;

/**
 * @description 告警处理状态的枚举对象
 * 
 * @date 2017年4月13日
 */
public enum AlertHandleStatus {
                               UNTREATED(0,"未处理",true),// 新告警、或恢复后又重新告警
                               CONFIRMED(1,"已确认",false),// 页面处理-已确认（只有“未处理”的告警才能被确认）
                               INHAND(2,"处理中",false),// 页面处理-处理中、或创建告警工单后-处理中
                               FINISHED(3,"已处理",false),// 页面处理-已处理、或告警工单处理完成-已处理
                               UNRESTORED(4,"未恢复",true),// 已处理后，触发恢复检查，若仍符合告警条件，则未恢复
                               RESTORED(5,"已恢复",false),// 已处理后，触发恢复检查，若不符合告警条件，则已恢复；或巡检自动恢复
                               INVALID(6,"失效",false);// 资源停止监控、或告警策略变更、或页面处理-忽略、或告警升级后，原告警失效
    private int value;

    private String text;

    private boolean isNotified; // 是否推送告警消息

    AlertHandleStatus(int value, String text, boolean isNotified) {
        this.value = value;
        this.text = text;
        this.isNotified = isNotified;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    public boolean isNotified() {
        return isNotified;
    }

    /**
     * 将传入的告警处理状态枚举值转化为枚举对象
     * @param value 传入的告警处理状态枚举值
     * @return 告警处理状态枚举对象
     */
    public static AlertHandleStatus fromValue(int value) {
        for (AlertHandleStatus item : AlertHandleStatus.values()) {
            if (item.value == value) {
                return item;
            }
        }
        throw new IllegalArgumentException("0-6 is a range of parameter('value')");
    }

    /**
     * 将传入的告警处理状态中文显示转化为枚举对象
     * @param value 传入的告警处理状态中文显示
     * @return 告警处理状态枚举对象
     */
    public static AlertHandleStatus fromText(String text) {
        for (AlertHandleStatus item : AlertHandleStatus.values()) {
            if (item.text.equals(text)) {
                return item;
            }
        }
        throw new IllegalArgumentException();
    }

}
