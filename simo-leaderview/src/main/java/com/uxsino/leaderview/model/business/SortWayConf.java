package com.uxsino.leaderview.model.business;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @description 业务排序设置
 * @date 2019年2月11日
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortWayConf {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 排序方式
     */
    @Enumerated(EnumType.STRING)
    private SortWay sortWay;

    /**
     * 自定义排序的业务系统ID用,分隔
     */
    @Column(columnDefinition = "TEXT")
    private String customSortIds;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    public SortWayConf(Long createUserId, Date createDate) {
        this.createUserId = createUserId;
        this.createDate = createDate;
    }

    public SortWayConf(SortWay sortWay) {
        this.sortWay = sortWay;
    }

}
