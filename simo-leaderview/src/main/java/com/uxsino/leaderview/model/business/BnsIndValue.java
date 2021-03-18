package com.uxsino.leaderview.model.business;

import com.alibaba.fastjson.JSON;
import com.uxsino.commons.db.JsonObjectUserType;
import com.uxsino.commons.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

/**
 * @description 业务指标值
 * @date 2019年1月4日
 */
@Data
@MappedSuperclass
@TypeDef(name = "jsonb", typeClass = JsonObjectUserType.class)
@AllArgsConstructor
@NoArgsConstructor
public class BnsIndValue {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 业务系统ID
     */
    private String businessId;

    /**
     * 采集时间
     */
    private Date fetchDate;

    /**
     * 指标
     */
    @Enumerated(EnumType.STRING)
    private Indicator indicatorName;

    /**
     * 指标值
     */
    private Double indicatorValue;

    /**
     * 指标详情
     */
    @Type(type = "jsonb")
    private JSON indicatorDetail;

    /**
     * 指标状态
     */
    @Enumerated(EnumType.STRING)
    private RunStatus runStatus;

    public BnsIndValue(Date fetchDate, Indicator indicatorName, Double indicatorValue) {
        this.fetchDate = fetchDate;
        this.indicatorName = indicatorName;
        this.indicatorValue = indicatorValue;
    }

    /**
     * 获取采集时间字符串 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getFetchDateStr() {
        if (fetchDate != null) {
            return TimeUtils.formatTime(fetchDate);
        }
        return null;
    }

}
