package com.uxsino.leaderview.model.business;

import java.util.Calendar;
import java.util.Date;

/**
 * @description 统计时段
 * @date 2019年1月24日
 */
public enum Period {
                    _7days(Calendar.DATE,-7,"最近7天"),
                    _2weeks(Calendar.WEEK_OF_MONTH,-2,"最近2周"),
                    _1month(Calendar.MONTH,-1,"最近1个月");
    private int calendarField;

    private int amount;

    private String text;

    Period(int calendarField, int amount, String text) {
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
     * @return
     */
    public static Date getStartDate(Period period, Date endDate) {
        if (period == null || endDate == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(period.getCalendarField(), period.getAmount());
        return calendar.getTime();
    }

}
