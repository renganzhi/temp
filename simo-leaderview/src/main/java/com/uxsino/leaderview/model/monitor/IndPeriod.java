package com.uxsino.leaderview.model.monitor;

import java.util.Calendar;
import java.util.Date;

/**
 * 指标历史性能的统计时段
 * @author Wang Sihao
 * @create 2019/7/26
 */
public enum IndPeriod {
    /**
     *最近1天
     */
    _1day(Calendar.DATE,-1,"最近1天"),

    /**
     *最近1周
     */
    _1week(Calendar.WEEK_OF_MONTH,-1,"最近1周"),

    /**
     * 最近1月
     */
    _1month(Calendar.MONTH,-1,"最近1月");

    private int calendarField;

    private int amount;

    private String text;

    IndPeriod(int calendarField, int amount, String text) {
        this.calendarField = calendarField;
        this.amount = amount;
        this.text = text;
    }

    public int getCalendarField() {
        return this.calendarField;
    }

    public int getAmount() {
        return this.amount;
    }

    public String getText() {
        return this.text;
    }


    /**
     * 查询起始时间
     * @param period 统计时段
     * @param endDate 截止时间
     */
    @SuppressWarnings("Duplicates")
    public static Date getStartDate(IndPeriod period, Date endDate) {
        if (period == null || endDate == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(period.getCalendarField(), period.getAmount());
        return calendar.getTime();
    }
}
