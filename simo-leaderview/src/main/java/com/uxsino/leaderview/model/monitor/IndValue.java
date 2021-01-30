package com.uxsino.leaderview.model.monitor;

import com.alibaba.fastjson.JSON;
import com.uxsino.commons.db.JsonObjectUserType;
import com.uxsino.commons.model.RunStatus;
import com.uxsino.commons.utils.DateUtils;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@MappedSuperclass
@TypeDef(name = "jsonb", typeClass = JsonObjectUserType.class)
public class IndValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255, message = "名称最大长度255")
    private String indicatorName;

    @Column(columnDefinition = "timestamp")
    private Date fetchDate;

    @Size(max = 255, message = "batch最大长度255")
    private String batch;

    @Size(max = 255, message = "资源ID最大长度255")
    private String neId;

    @Enumerated(EnumType.STRING)
    private RunStatus runStatus;

    @Type(type = "jsonb")
    private JSON indicatorValue;

    private Boolean indicatorStatus;

    private String msg;

    @Column(nullable = false, columnDefinition = "int default 0")
    public long cost;

    /**
     * 获取采集时间 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getFetchDateStr() {
        if (fetchDate != null) {
            return DateUtils.formatCommonDate(fetchDate);
        }
        return null;
    }

}
