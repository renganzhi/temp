package com.uxsino.leaderview.model.monitor;

import com.alibaba.fastjson.annotation.JSONField;
import com.uxsino.commons.db.JsonObjectUserType;
import com.uxsino.commons.model.RunStatus;
import com.uxsino.commons.utils.DateUtils;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@MappedSuperclass
@TypeDef(name = "jsonb", typeClass = JsonObjectUserType.class)
public class IndicatorVal {
    
    private Long id;

    @Size(max = 255, message = "指标名称最大长度255")
    private String ind;

    private Date tm;

    private String seq;

    @JSONField(name = "ne")
    private String neId;

    private RunStatus runStatus;

    @JSONField(name = "v")
    private Object indicatorValue;

    public long cost;

    /**
     * 获取采集时间 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String getFetchDateStr() {
        if (tm != null) {
            return DateUtils.formatCommonDate(tm);
        }
        return null;
    }

}
